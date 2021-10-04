package com.AuthorityManagement.util;

import com.AuthorityManagement.webMVC.MvcInterceptor;
import com.AuthorityManagement.webMVC.annotation.Auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * 权限验证的切面对象
 * 在请求处理之前  调用controller.方法之前
 * 验证当前用户是否具有处理当前请求的权限
 */
public class AuthInterceptor implements MvcInterceptor {

    @Override
    public boolean prevHandle(HttpServletRequest request, HttpServletResponse response, Method targetMethod) {
       Auth auth = targetMethod.getAnnotation(Auth.class);
       if (auth != null){
           String authStr = auth.value();
           Map<String,Object> map = (Map<String, Object>) request.getSession().getAttribute("loginAuth");
           Set<String> authSet = (Set<String>) map.get("auths");
           if (authSet.contains(authStr)){
               //当前用户 包含了 执行此次请求应该具有的权限
               return true ;
           }else {
               response.setContentType("text/html;charset=utf-8");
               try {
                   response.getWriter().write("权限不足，请联系管理员。。。");
               } catch (IOException e) {
                   e.printStackTrace();
               }
               return false ;
           }
       }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response) {

    }
}
