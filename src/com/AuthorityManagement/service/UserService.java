package com.AuthorityManagement.service;

import java.util.*;

import com.AuthorityManagement.dao.FunDao;
import com.AuthorityManagement.dao.RoleDao;
import com.AuthorityManagement.dao.UserDao;
import com.AuthorityManagement.domain.Fun;
import com.AuthorityManagement.domain.PageInfo;
import com.AuthorityManagement.domain.User;
import com.AuthorityManagement.orm.SqlSession;
import com.AuthorityManagement.util.MySpring;
import com.AuthorityManagement.vo.FunVO;

public class UserService {
	
	private UserDao dao = MySpring.getBean("com.AuthorityManagement.dao.UserDao");	
	private RoleDao roleDao = new SqlSession().getMapper(RoleDao.class);
	private FunDao funDao = new SqlSession().getMapper(FunDao.class);
	public User checkLogin(String uname,String upass) {
		return dao.findUserByNameAndPass(uname, upass);
	}
	
	public PageInfo findUserByPage(int page ,int rows) {
		//下限
		if(page<1) {
			page = 1;
		}
		//上限
		int total = dao.total();
		int max = total%rows==0?(total/rows):(total/rows+1);
		max = max==0?1:max;
		if(page>max) {
			page = max;
		}
		//就可以查询 一页的数据  1 10   1  5
		//数据库的分页，使用limit关键字，需要的是start和length
		int start = (page-1)*rows;
		int length = rows;
		List<User> users = dao.findUserByPage(start, length);
		return new PageInfo(page,rows,total,max,start,(start+rows-1) , users);
	}
	
	public void save(User user) {
		dao.save(user);
	}
	public String delete(String uid) {
		if(dao.delete(uid)) {
			return "删除成功";
		}else {
			return "删除失败";
		}
	}
	
	public User select (String uid) {
		return dao.select(uid);
	}
	public String modify(User user) {
		if(dao.modify(user)) {
			return "修改成功";
		}else {
			return "修改失败";
		}
	}
	public List<Map> findRoleAllByUser(int uid){
		return roleDao.findAllByUser(uid);
	}

	public void setRolesForUser(int uid ,String rids){
		long count = dao.assertUser(uid);
		if(count == 0){
			//无效的账号
			throw new RuntimeException("无效的操作") ;
		}
		roleDao.removeRelationship(uid);
		if(rids != null && !"".equals(rids)){
			//分配了角色
			String[] ridArray = rids.split(",");
			Map<String,Integer> param = new HashMap<String,Integer>();
			param.put("uid",uid);
			for(String rid : ridArray){
				param.put("rid",Integer.parseInt(rid)) ;
				roleDao.addRelationship(param);
			}
		}
	}
	public Map<String,Object> findUserAuth(int uid){
		List<Fun> funs = funDao.findFunByUser(uid);

		//组装权限范围
		Set<String> auths = new HashSet<>();
		for (Fun fun:funs){
			auths.add(fun.getFauth());
		}

		//组装layui-tree所需要的数据结构
		List<FunVO> menus = reload(funs,-1);

		Map<String,Object> map = new HashMap<>();
		map.put("auths",auths);
		map.put("menus",menus);
		return map;
	}

	private List<FunVO> reload(List<Fun> funs, int level){
		List<FunVO> vos = new ArrayList<>();
		for (Fun fun: funs){
			if (fun.getFtype().equals(1) && fun.getPid().equals(level)){
				FunVO vo = new FunVO();
				if (fun.getFhref()!=null && !fun.getFhref().equals("")){
					vo.setTitle("<a href='"+fun.getFhref()+"' target='content'>"+fun.getFname()+"</a>");
				}else {
					vo.setTitle(fun.getFname());
				}

				vo.setId(fun.getFid());
				//找到当前fun的子级,当前这个父级菜单的子级菜单，就是父级pid = 当前菜单的fid
				List<FunVO> children = reload(funs,fun.getFid());
				vo.setChildren(children);

				vos.add(vo);
			}
		}
		return vos;
	}
}
