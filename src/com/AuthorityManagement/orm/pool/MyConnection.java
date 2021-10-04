package com.AuthorityManagement.orm.pool;

import java.sql.*;

/**
 * 数据库连接对象
 */
public class MyConnection extends AbstractConn{

    private Connection connection;
    private boolean used = false;

    private static String url=ConfigurationReader.getValue("url");
    private static String user=ConfigurationReader.getValue("user");
    private static String password=ConfigurationReader.getValue("password");
    private static String className=ConfigurationReader.getValue("className");
    static {
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    {
        try {
            connection = DriverManager.getConnection(url,user,password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean isUsed() {
        return used;
    }
    public void setUsed(boolean used) {
        this.used = used;
    }

    @Override
    public Statement createStatement() throws SQLException {
        Statement stat = this.connection.createStatement();
        return stat;
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        PreparedStatement pstat =this.connection.prepareStatement(sql);
        return pstat;
    }

    @Override
    public void close() throws SQLException {
        this.used = false;
    }
}
