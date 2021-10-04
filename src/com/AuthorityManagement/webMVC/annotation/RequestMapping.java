package com.AuthorityManagement.webMVC.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
/**
  * 配置请求映射关系
  *指定请求对应那个controller的方法
 * 作用在那个方法上，就表示映射那个方法
 * value属性用来设置请求路径
  * */
    String value();
}
