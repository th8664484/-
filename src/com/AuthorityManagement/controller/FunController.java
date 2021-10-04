package com.AuthorityManagement.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.AuthorityManagement.domain.Fun;
import com.AuthorityManagement.service.FunService;
import com.AuthorityManagement.util.MySpring;
import com.AuthorityManagement.webMVC.annotation.*;

@Controller
public class FunController {
	private FunService service = MySpring.getBean("com.AuthorityManagement.service.FunService");
	
	@RequestMapping("/funAll")
	@ResponseBody
//	@Auth("com.qxgl.auth.fun")
	public Map<String,Object> funAll(){
		List<Fun> funs = service.funAll();
		
		Map<String,Object> map = new HashMap<>();
		map.put("code", 0);
		map.put("msg", "数据错误");
		map.put("data", funs);
		return map;
	}
	@RequestMapping("/funAdd")
	@Auth("com.qxgl.auth.fun.add")
	public String funAdd(Fun fun) {
		service.save(fun);
		return "fun.jsp";
	}
	@RequestMapping("/parent")
	@ResponseBody
	@Auth("com.qxgl.auth.fun.funUpdate")
	public String parent(@RequestParam("fid") int fid){
		return service.parent(fid);
	}

	@RequestMapping("/funUpdate")
	@Auth("com.qxgl.auth.fun.funUpdate")
	public String funUpdate(Fun fun) {
		service.update(fun);
		return "fun.jsp";
	}
	@RequestMapping("/dfun")
	@Auth("com.qxgl.auth.fun.dfun")
	public String delete(@RequestParam("fid") int fid, @RequestParam("ftype")int ftype){
		service.delete(fid,ftype);
		return "fun.jsp";
	}
}
