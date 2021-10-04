package com.AuthorityManagement.orm;

import java.util.ArrayList;

public class SQLAndKey {

	private StringBuilder newSQL = new StringBuilder(); //解析后的sql语句
	private ArrayList<String> keyList = new ArrayList<>();//解析后的value
	
	public SQLAndKey(StringBuilder newSQL, ArrayList<String> keyList) {
		this.newSQL = newSQL;
		this.keyList = keyList;
	}
	
	public String getNewSQL() {
		return newSQL.toString();
	}
	public ArrayList<String> getKeyList() {
		return keyList;
	}

	public String toString() {
		return newSQL+"\n"+keyList.toString();
	}
}

