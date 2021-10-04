package com.AuthorityManagement.orm.pool;

import java.sql.Connection;
import java.util.ArrayList;

public class ConnectionPool {

    private static  int DEFAULT_CONNECTION = 10;
    private ArrayList<Connection> connectionList = new ArrayList<>();

    private static volatile ConnectionPool connectionPool;

    private ConnectionPool(){}

    {
        int count = 0;
        String str = ConfigurationReader.getValue("minCount");
        if(str!=null){
            count = Integer.parseInt(str);
        }else{
            count = DEFAULT_CONNECTION;
        }
        for (int i=0;i<count;i++){
            connectionList.add(new MyConnection());
        }
    }

    //创建连接池 避免重复创建
    public static ConnectionPool getConnectionPool(){//双重判定模型  单例
        if (connectionPool == null){
            synchronized (ConnectionPool.class){
                if (connectionPool == null){
                    connectionPool = new ConnectionPool();
                }
            }
        }
        return  connectionPool;
    }

    //线程锁 获取连接一次只有一个线程能获取连接 其他的等待
    private synchronized Connection getMC(){
        Connection result = null;
        for (int i=0;i<connectionList.size();i++){
            MyConnection mc = (MyConnection) connectionList.get(i);
            if(mc.isUsed() == false){
                mc.setUsed(true);
                result = mc;
                break;
            }
        }
        return result;
    }

    //获取数据库连接 设置等待时间
    public Connection getConnection(){
        Connection result = this.getMC();
        int count = 0;
        while (result == null && count<50){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            result = this.getMC();
            count++;
        }
        if (result == null){
            throw new ConnException("系统正忙");
        }
        return  result;
    }


}
