package com.AuthorityManagement.dao;

import java.util.List;
import java.util.Map;

import com.AuthorityManagement.domain.Fun;
import com.AuthorityManagement.orm.annotation.Delete;
import com.AuthorityManagement.orm.annotation.Insert;
import com.AuthorityManagement.orm.annotation.Select;

public interface FunDao {
	
	@Select("select * from t_fun ")
	public List<Fun> funAll();

	@Select("select * from t_fun where fid =*[fid]")
	public Fun fun(int fid);

	@Select("select count(*) from t_fun where pid=*[pid] and fname=*[fname]")
	public Long fnameAssert(Map<String,Object> map);

	@Select("select count(*) from t_fun where fname=*[fname]")
	public Long fnameAssert(String fname);

	@Insert("insert into t_fun values(null,*[fname],*[ftype],*[fhref],*[fauth],*[pid],*[reserve1],*[reserve2])")
	public void save(Fun fun);

	@Insert("update t_fun set fname=*[fname],ftype=*[ftype],fhref=*[fhref],fauth=*[fauth] where fid =*[fid]")
	public void update(Fun fun);

	@Delete("DELETE FROM t_fun WHERE fid=*[fid]")
	public void dbutton(int fid);

	@Delete("DELETE FROM t_fun WHERE fid=*[fid] or pid=*[pid]")
	public void delete(Map<String,Integer> map);

	@Delete("DELETE FROM t_role_fun WHERE rid=*[rid]")
	public void removeRelationship(int rid);

	@Insert("insert into t_role_fun values(*[rid],*[fid])")
	public  void addRelationship(Map<String,Integer> param);

	@Select("select * from t_fun where fid in(" +
			"select fid from t_role_fun where rid in(" +
			" select rid from t_user_role where uid =*[uid]))")
	public List<Fun> findFunByUser(int uid);
}
