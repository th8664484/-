package com.AuthorityManagement.util;

import java.util.HashMap;

public class MySpring {

	private static HashMap<String,Object> beanMap = new HashMap<>();
	public static <T> T getBean(String className) {
		T obj = (T) beanMap.get(className);
		try {
			if(obj == null) {
				obj = (T) Class.forName(className).newInstance();
				beanMap.put(className, obj);
			}
			
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return obj;
	}
}
