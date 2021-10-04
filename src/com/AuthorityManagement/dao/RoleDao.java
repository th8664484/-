package com.AuthorityManagement.dao;

import com.AuthorityManagement.domain.Role;
import com.AuthorityManagement.orm.annotation.Delete;
import com.AuthorityManagement.orm.annotation.Insert;
import com.AuthorityManagement.orm.annotation.Select;
import com.AuthorityManagement.orm.annotation.Update;

import java.util.List;
import java.util.Map;

public interface RoleDao {

    @Select("select count(*) from t_role")
    public Long total();

    @Select("select * from t_role limit *[start],*[length] ")
    public List<Role> findByPage(Map<String,Integer> map);

    @Insert("insert into t_role values(null,*[rname],*[rdescription],*[reserve1],*[reserve2])")
    public void finAdd(Role role);

    @Update("update t_role set rname=*[rname],rdescription=*[rdescription] where rid =*[rid]")
    public void update(Role role);

    @Delete("DELETE FROM t_role WHERE rid=*[rid]")
    public void delete(int rid);

    @Select("select r.*,if(ur.rid is null ,0,1) flag from t_role r left join (select rid from t_user_role where uid=*[uid]) ur on r.rid=ur.rid")
    public List<Map> findAllByUser(int uid);

    @Delete("delete from t_user_role where uid=*[uid]")
    public void removeRelationship(int uid);
    @Insert("insert into t_user_role values(*[uid],*[rid])")
    public void addRelationship(Map<String,Integer> param);

    @Select("select fid from t_role_fun where rid=*[rid]")
    public List<Integer> findFidByRole(int rid);

    @Select("select count(*) from t_role_fun where rid=*[rid]")
    public Long assertRoleFun(int rid);


}
