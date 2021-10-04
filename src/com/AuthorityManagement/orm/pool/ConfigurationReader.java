package com.AuthorityManagement.orm.pool;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/***
 * 读取配置文件 获得 url name pass MYSQL驱动 最大连接数
 */
public class ConfigurationReader {
    //属性 缓存
    private static Map<String,String> map = new HashMap<>();

    static {
        try {
            Properties properties = new Properties();
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("/configuration.properties");
            properties.load(inputStream);//读取文件内容
            Enumeration en = properties.propertyNames();//获取全部的key
            while (en.hasMoreElements()){
                String key = (String) en.nextElement();
                String value = properties.getProperty(key);
                map.put(key,value);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getValue(String key){
        return map.get(key);
    }
}
