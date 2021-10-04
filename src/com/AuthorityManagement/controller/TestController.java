package com.AuthorityManagement.controller;


import com.AuthorityManagement.webMVC.annotation.Controller;
import com.AuthorityManagement.webMVC.annotation.RequestMapping;

@Controller
public class TestController {

    @RequestMapping("/test1")
    public void t1(){
        System.out.println("测试1");
    }


    @RequestMapping("/test2")
    public void t2(){
        System.out.println("测试2");
    }


    @RequestMapping("/test3")
    public void t3(){
        System.out.println("测试3");
    }
}
