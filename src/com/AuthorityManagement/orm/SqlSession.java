package com.AuthorityManagement.orm;


import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import com.AuthorityManagement.orm.annotation.Delete;
import com.AuthorityManagement.orm.annotation.Insert;
import com.AuthorityManagement.orm.annotation.Select;
import com.AuthorityManagement.orm.annotation.Update;
import com.AuthorityManagement.orm.pool.ConnectionPool;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

public class SqlSession {
	private Handler handler = new Handler();
	//private JDBC jdbc = new JDBC();
	private ConnectionPool pool = ConnectionPool.getConnectionPool();
	private Connection conn=null;
	private PreparedStatement pstat=null;
	private ResultSet rs=null;
	
	public <T> T select(String sql, Class clazz) {
		return this.select(sql, null, clazz);
	}
	
	public <T> T select(String sql, Object obj, Class clazz) {
		return this.selectAll(sql, obj, clazz).size()>0?(T)selectAll(sql, obj, clazz).get(0):null;
	}
	
	public <T> List<T> selectAll(String sql, Class clazz){
		return this.selectAll(sql, null, clazz);
	}
	public <T>List<T> selectAll(String sql,Object obj,Class clazz) {
		List<T> list = new ArrayList<>();		
		try {
			SQLAndKey sak = handler.analysisSQL(sql);
			conn = pool.getConnection();//conn = jdbc.conn();
			pstat = conn.prepareStatement(sak.getNewSQL());
			if(obj != null) {
				handler.handleParameter(pstat, obj, sak.getKeyList());
			}
			rs = pstat.executeQuery();
			while(rs.next()) {
				list.add((T) handler.handleResultSet(rs, clazz));
			}
//			jdbc.closejdbc(rs, pstat, conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if(pstat != null) {
					pstat.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return list;
	}
	public int update(String sql, Object obj) {
		int count=0;
		try {
			SQLAndKey sak = handler.analysisSQL(sql);
			conn = pool.getConnection();//conn = jdbc.conn();
			pstat = conn.prepareStatement(sak.getNewSQL());
			if(obj != null) {
				handler.handleParameter(pstat, obj, sak.getKeyList());
			}
			count = pstat.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(pstat != null) {
					pstat.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if(conn != null) {
					conn.close();
				}			
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return count;
	}

	public void delete(String sql,Object obj) {
		this.update(sql, obj);
	}
	
	public int insert(String sql, Object obj) {
		return this.update(sql, obj);
	}



	//代理  动态
	//参数 被代理的类 DAO  必须是一个接口
	//返回值 对象-代理对象-代理DAO做事
	public <T> T getMapper(Class clazz){
		//创建一个代理对象
		//1.类加载器 ClassLoader
		ClassLoader loader = clazz.getClassLoader();
		//2.Class[] 加载的类 通常就一个
		Class[] interfaces = new Class[]{clazz};
		//3.具体该怎么做事 InvocationHandler 接口 实现接口
		InvocationHandler handler = new InvocationHandler() {
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				//用来描述代理对象 具体如何做事
				//invoke 参数的具体作用
				//proxy 代理对象   method 被代理的那个方法  args[] 被代理方法的参数

				//获取方法上面的注解
				Annotation an = method.getAnnotations()[0];
				//获取注解类class
				Class type = an.annotationType();
				//找到注解中的方法
				Method valueMethod = type.getDeclaredMethod("value");
				//获取value方法的值
				String sql = (String) valueMethod.invoke(an);
				//判断参数的情况
				Object param = args == null?null:args[0];
				if (type == Insert.class){
					return SqlSession.this.insert(sql,param);
				}else if (type == Delete.class){
					SqlSession.this.delete(sql,param);
				}else if (type == Update.class){
					return SqlSession.this.update(sql,param);
				}else if (type == Select.class){
					Class methodReturnTypeClass = method.getReturnType();

					if (methodReturnTypeClass == List.class){
						//解析 泛型
//						Type returnType = method.getGenericReturnType();//返回值的具体类型（java.util.List<domain.*>）
						//method的返回值类型是一个正常的Class，这个Class无法操作泛型
						//Type的一个接口，多态
						//将type 还原成可以操作泛型的类型
//						ParameterizedTypeImpl parameterizedType = (ParameterizedTypeImpl) returnType;
						//操作返回值类型中的泛型
//						Type[] patternTypes = parameterizedType.getActualTypeArguments();
//						Type patternType = patternTypes[0];
//						Class resultType = (Class) patternType;

						ParameterizedTypeImpl parameterizedType = (ParameterizedTypeImpl)method.getGenericReturnType();
						Class resultType = (Class) parameterizedType.getActualTypeArguments()[0];
						return  SqlSession.this.selectAll(sql,param,resultType);
					}else {
						return  SqlSession.this.select(sql,param,methodReturnTypeClass);
					}
				}
				return null;
			}
		};
		return (T)Proxy.newProxyInstance(loader,interfaces,handler);
//		return  (T)Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new InvocationHandler() {
//			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//				Annotation an = method.getAnnotations()[0];
//				Class type = an.annotationType();
//				Method valueMethod = type.getDeclaredMethod("value");
//				String sql = (String) valueMethod.invoke(an);
//				Object param = args == null?null:args[0];
//				if (type == Insert.class){
//					return SqlSession.this.insert(sql,param);
//				}else if (type == Delete.class){
//					SqlSession.this.delete(sql,param);
//				}else if (type == Update.class){
//					return SqlSession.this.update(sql,param);
//				}else if (type == Select.class){
//					Class methodReturnTypeClass = method.getReturnType();
//					if (methodReturnTypeClass == List.class){
//						ParameterizedTypeImpl parameterizedType = (ParameterizedTypeImpl)method.getGenericReturnType();
//						Class resultType = (Class) parameterizedType.getActualTypeArguments()[0];
//						return  SqlSession.this.selectAll(sql,param,resultType);
//					}else {
//						return  SqlSession.this.select(sql,param,methodReturnTypeClass);
//					}
//				}
//				return null;
//			}
//		});
	}
}
