package com.o6Systems.utils.database;


import java.util.HashMap;

public interface DatabaseInterface {
	public long insert(String tableName, HashMap<String,String> keyValueMap);
	//public  HashMap<String,String>[] select(DatabaseQuery query);
	public HashMap<String,String> select(String queryString,String[] additionalParams);
	public boolean createTable(HashMap<String,String> tableTupples);
	
	public void setCreateScript(String createScript);
	
}
