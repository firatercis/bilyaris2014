package com.o6Systems.bilyarisUI.sqlite;

public class DatabaseConstants {
	
	public final static String USER_TABLE_NAME = "USER";
	public final static String QUESTION_TABLE_NAME = "QUESTION";
	public final static String QUESTION_ASKED_TABLE_NAME = "QUESTIONASKED";
	public final static String USERSTAT_TABLE_NAME = "USERSTAT";
	
	public final static String USER_CREATE = "CREATE TABLE " + USER_TABLE_NAME +  "("
			+ "uID integer primary key autoincrement, "
			+ "name varchar(100));"; 
	
	public final static String QUESTION_CREATE = "CREATE TABLE " + QUESTION_TABLE_NAME +  "("
			+ "qID integer primary key autoincrement, "
			+ "text varchar(255),"
			+ "difficulty integer, "
			+ "category varchar(30), "
			+ "choice1 varchar(100), choice2 varchar(100),choice3 varchar(100), choice4 varchar(100),"
			+ "correctanswer integer,"
			+ "author varchar(50),"
			+ "date datetime);";
	
	public final static String QUESTION_ASKED_CREATE = "CREATE TABLE " + QUESTION_ASKED_TABLE_NAME +  "("
			+ "uID integer,"
			+ "qID integer);"; 
	
	public final static String USERSTAT_CREATE = "CREATE TABLE " + USERSTAT_TABLE_NAME +  "("
			+ "uID integer primary key, "
			+ "category varchar(50),"
			+ "tag varchar(100),"
			+ "value integer);"; 
	
}
