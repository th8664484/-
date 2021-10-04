package com.AuthorityManagement.orm;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Handler {
	//解析 返回查询结果
	Object handleResultSet(ResultSet rs, Class c) throws SQLException{
		Object obj = null;
		if(isCommonDataType(c) || isWrapClass(c) || c == String.class) {//判断返回值类型
			obj = rs.getObject(1);			
		}else {
			try {
				ResultSetMetaData rsmd = rs.getMetaData();
				int size = rsmd.getColumnCount();			
				if(c==Map.class) {
					Map<String,Object> map = new HashMap<>();
					for(int i=1;i<=size;i++) {
						map.put(rsmd.getColumnName(i), rs.getObject(rsmd.getColumnName(i)));
					}
					obj=map;
				}else {
					obj = c.newInstance();
					for(int i=1;i<=size;i++) {
						Field field = c.getDeclaredField(rsmd.getColumnName(i));
						field.setAccessible(true);
						field.set(obj, rs.getObject(rsmd.getColumnName(i)));
					}
					
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return obj;
	}
	
	//预编译的sql  把 ？ 替换成对应的值
	void handleParameter(PreparedStatement pstat, Object obj, ArrayList<String> list) throws SQLException {
		Class clazz = obj.getClass();
		if(isCommonDataType(clazz) || isWrapClass(clazz) || clazz == String.class) {
			pstat.setObject(1, obj);		
		}else {
			for(int i =0;i<list.size();i++) {
				if(obj instanceof Map) {// obj 为 Map 集合
					pstat.setObject(i+1, ((Map)obj).get(list.get(i)));
				}else {
					try {
						//获取对应的属性
						Field field = clazz.getDeclaredField(list.get(i));
						//获取属性对应的方法
						Method getMethod = obj.getClass().getMethod("get"+field.getName().substring(0,1).toUpperCase()+field.getName().substring(1));
					    pstat.setObject(i+1, getMethod.invoke(obj));
					}catch (Exception e) {
						e.printStackTrace();
					}	
				}
				
			}

		}
		
	}
	
	//解析SQL 语句  还原sql  取出 ？ 对应的value
	SQLAndKey analysisSQL(String sql) {
		ArrayList<String> list = new ArrayList<>();
		StringBuilder builder = new StringBuilder();
		while(true) {
			int left = sql.indexOf("*[");
			int right = sql.indexOf("]");
			if(left!=-1 && right!=-1 && left<right) {
				builder.append(sql.substring(0,left));
				builder.append("?");
				list.add(sql.substring(left+2,right));
			}else {
				builder.append(sql);
				break;
			}
			sql = sql.substring(right+1);
		}
		return new SQLAndKey(builder, list);
	}
	
	// 判断是否基本类型
	private  boolean isCommonDataType(Class clazz){
	    return clazz.isPrimitive();//确定指定 类对象表示一个基本类型。
	}
	//判断是否的基本类型的 包装类
	private boolean isWrapClass(Class clazz) {
	    try {
	        return ((Class) clazz.getField("TYPE").get(null)).isPrimitive();
	    } catch (Exception e) {
	        return false;
	    }
	}
}
