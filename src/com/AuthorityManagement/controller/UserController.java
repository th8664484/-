package com.AuthorityManagement.controller;

import com.AuthorityManagement.service.UserService;
import com.AuthorityManagement.util.MySpring;
import com.AuthorityManagement.vo.FunVO;
import com.AuthorityManagement.webMVC.ModelAndView;
import com.AuthorityManagement.webMVC.annotation.Controller;
import com.AuthorityManagement.webMVC.annotation.RequestMapping;
import com.AuthorityManagement.webMVC.annotation.RequestParam;
import com.AuthorityManagement.webMVC.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {

    private UserService service = MySpring.getBean("com.AuthorityManagement.service.UserService");
    @RequestMapping("/assignRoles")
    public ModelAndView finRole(@RequestParam("uid") int uid){
        List<Map> roles = service.findRoleAllByUser(uid);
//        System.out.println(roles.toString());
        ModelAndView mv = new ModelAndView();
        mv.addObject("roles",roles);
        mv.setViewName("setRole.jsp");
        return mv ;
    }

    @RequestMapping("/setRolesForUser")
    public void setRolesForUser(@RequestParam("uid") int uid,@RequestParam("rids") String rids){
        service.setRolesForUser(uid,rids);
    }

    @RequestMapping("/findUserMenus")
    @ResponseBody
    public List<FunVO> findUserMenus(HttpSession session){
        Map<String,Object> map = (Map<String, Object>) session.getAttribute("loginAuth");

        return (List<FunVO>) map.get("menus");
    }
}
