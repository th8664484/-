package com.AuthorityManagement.orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBC {
	private static String url="jdbc:mysql://localhost:3306/examination?serverTimezone=UTC&characterEncoding=utf-8";
	private static String user="root";
	private static String password="123456";
	private static String className="com.mysql.cj.jdbc.Driver";
	public static Connection conn() {
		Connection conn = null;
		try {
			Class.forName(className);
			conn = DriverManager.getConnection(url, user, password);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public static void closejdbc(ResultSet rs,PreparedStatement pstat,Connection conn) {		
		rsClose(rs);
		pstatClose(pstat);
		connClose(conn);
	}

	private static void connClose(Connection conn) {
		try {
			if(conn != null) {
				conn.close();
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	private static void pstatClose(PreparedStatement pstat) {
		try {
			if(pstat != null) {
				pstat.close();
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	private static void rsClose(ResultSet rs) {
		try {
			if(rs != null) {
				rs.close();
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
