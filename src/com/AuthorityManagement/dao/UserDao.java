package com.AuthorityManagement.dao;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.AuthorityManagement.domain.User;
import com.AuthorityManagement.orm.SqlSession;
import com.AuthorityManagement.util.MySpring;

public class UserDao {
	private SqlSession session = MySpring.getBean("com.AuthorityManagement.orm.SqlSession");
	public User findUserByNameAndPass(String uname, String upass) {
		String sql="select * from t_user where reserve1='1' and uname=*[uname] and upass=*[upass]";
		Map<String,String> map = new HashMap<>();
		map.put("uname",uname);
		map.put("upass", upass);
		return session.select(sql,map, User.class);
	}
	public int total() {
		String sql = "select count(*) from t_user where reserve1='1' ";	
		Number num = (Number)session.select(sql, int.class);
		return num.intValue();  
	}
	public List<User> findUserByPage(int start,int length){
		String sql = "select * from t_user where reserve1='1' limit *[start],*[length] ";
		Map<String,Integer> param = new HashMap<>();
		param.put("start", start);
		param.put("length", length);
		return session.selectAll(sql, param,User.class);
	}
	
	public void save(User user) {
		String sql ="insert into t_user values (null,*[uname],*[upass],*[reserve1],*[reserve2])";
		session.insert(sql, user);
	}
	public boolean delete(String uid) {
		String sql ="update t_user set reserve1 = \"0\" where uid =*[uid]";		
		if(session.update(sql, uid) > 0) {
			return true;
		}else {
			return false;
		}
	}
	public User select(String uid) {
		String sql = "select * from t_user where uid=*[uid]";	
		return session.select(sql, uid, User.class);
	}
	public boolean modify(User user) {
		String sql = "update t_user set uname=*[uname],upass=*[upass],reserve1=*[reserve1],reserve2=*[reserve2] where uid =*[uid]";
		if(session.update(sql, user) > 0) {
			return true;
		}else {
			return false;
		}
	}

	public long assertUser(int uid){
		String sql = "select count(*) from t_user where uid = *[uid]" ;
		return session.select(sql,uid,long.class);
	}
}
