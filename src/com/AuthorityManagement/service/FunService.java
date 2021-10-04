package com.AuthorityManagement.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.AuthorityManagement.dao.FunDao;
import com.AuthorityManagement.domain.Fun;
import com.AuthorityManagement.exception.FnameRepeatException;
import com.AuthorityManagement.orm.SqlSession;

public class FunService {

	private FunDao dao = new SqlSession().getMapper(FunDao.class);
	
	public List<Fun> funAll(){
		List<Fun> funs = dao.funAll();

		return funs;
	}
	
	public void save(Fun fun) {
		if (fun.getFtype().equals(1) && fun.getPid().equals(-1)){//判断菜单名称是否重复
			long count = dao.fnameAssert(fun.getFname());
			if(count >0) {
				throw new FnameRepeatException(fun.getFname()+"菜单名称重复");
			}
		}
		Map<String,Object> map = new HashMap<>();
		map.put("pid",fun.getPid());
		map.put("fname",fun.getFname());
		long count =  dao.fnameAssert(map);
		if(count >0) {
			throw new FnameRepeatException(fun.getFname()+"功能重复");
		}
		dao.save(fun);
	}

	public String parent(int fid){
		Fun fun = dao.fun(fid);
		if (fun == null){
			return "根菜单";
		}else {
			return fun.getFname();
		}
	}

	public void update(Fun fun){
		dao.update(fun);
	}

	public void delete(int fid , int ftype){
		if (ftype == 1){ //是一个菜单 如果他下面有其他功能那么fid为父ID  删除 fid=fid 和 pid=fid
			Map<String,Integer> map = new HashMap<>();
			map.put("fid",fid);
			map.put("pid",fid);
			dao.delete(map);
		}else {//是一个按钮 直接删除
			dao.dbutton(fid);
		}

	}
}
