package com.AuthorityManagement.service;

import com.AuthorityManagement.dao.FunDao;
import com.AuthorityManagement.dao.RoleDao;
import com.AuthorityManagement.domain.PageInfo;
import com.AuthorityManagement.domain.Role;
import com.AuthorityManagement.orm.SqlSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoleService {

    private RoleDao dao = new SqlSession().getMapper(RoleDao.class);
    private FunDao funDao = new SqlSession().getMapper(FunDao.class);
    public PageInfo findByPage(int page,int limit){
        page = Math.max(1,page);//确保page的下限
        long count = dao.total();
        int max = (int)Math.ceil(1.0*count/limit);
        max = Math.max(1,max);//确保max有效，至少有一页
        page = Math.min(page,max);//确保上限

        int start = (page-1)*limit;
        int length = limit;

        Map<String,Integer> map = new HashMap<>();
        map.put("start",start);
        map.put("length",length);
        List<Role> list = dao.findByPage(map);

        return  new PageInfo(list,count,"",0);
    }

    public void finAdd(Role role){
        dao.finAdd(role);
    }

    public void update(Role role){
        dao.update(role);
    }

    public void detele(int rid){
        dao.delete(rid);
    }

    public List<Integer> findFidByRole(int rid){
       return dao.findFidByRole(rid);
    }

    public void setFuns(int rid ,String fids){
        long count = dao.assertRoleFun(rid);
//        System.out.println(count);
//        if(count == 0){
//            //无效的账号
//            throw new RuntimeException("无效的操作") ;
//        }
        funDao.removeRelationship(rid);
        if(fids != null && !"".equals(fids)){
            //分配了角色
            String[] fidArray = fids.split(",");
            Map<String,Integer> param = new HashMap<String,Integer>();
            param.put("rid",rid);
            for(String fid : fidArray){
                param.put("fid",Integer.parseInt(fid)) ;
                funDao.addRelationship(param);
            }
        }
    }
}
