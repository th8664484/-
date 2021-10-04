package com.AuthorityManagement.controller;

import com.AuthorityManagement.domain.PageInfo;
import com.AuthorityManagement.domain.Role;
import com.AuthorityManagement.service.RoleService;
import com.AuthorityManagement.util.MySpring;
import com.AuthorityManagement.webMVC.annotation.*;

import java.util.List;

@Controller
public class RoleController {
    private RoleService service = MySpring.getBean("com.AuthorityManagement.service.RoleService");
    @RequestMapping("/roleFindAll")
    @ResponseBody
    @Auth("com.qxgl.auth.role")
    public PageInfo finAll(@RequestParam("page") int page, @RequestParam("limit") int limit){
       return service.findByPage(page, limit);
    }
    @RequestMapping("/roleAdd")
    @Auth("com.qxgl.auth.role.add")
    public String finAdd(Role role){
        service.finAdd(role);
        return "role.jsp";
    }

    @RequestMapping("/roleUpdate")
    @Auth("com.qxgl.auth.role.edit")
    public String roleUpdate(Role role){
        service.update(role);
        return "role.jsp";
    }
    @RequestMapping("/detele")
    @Auth("com.qxgl.auth.role.delete")
    public String detele(@RequestParam("rid") int rid){
        service.detele(rid);
        return "role.jsp";
    }

    @RequestMapping("/findFidByRole")
    @ResponseBody
    @Auth("com.qxgl.auth.role.setFuns")
    public List<Integer> findFidByRole(@RequestParam("rid") int rid){
        return service.findFidByRole(rid);
    }

    @RequestMapping("/setFuns")
    @Auth("com.qxgl.auth.role.setFuns")
    public void  setFuns(@RequestParam("rid") int rid,@RequestParam("fids") String fids){
        service.setFuns(rid,fids);
    }

}
