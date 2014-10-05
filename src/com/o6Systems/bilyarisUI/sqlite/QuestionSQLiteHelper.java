package com.o6Systems.bilyarisUI.sqlite;

import android.database.sqlite.SQLiteDatabase;

public class QuestionSQLiteHelper {
	
	public final static String QUESTION_TABLE_NAME = "QUESTIONS";
	public final static String QUESTION_TABLE_CREATE_SCRIPT = "CREATE TABLE " + QUESTION_TABLE_NAME +  "("
			+ "qID integer primary key, "
			+ "text varchar(255),"
			+ "difficulty integer, "
			+ "category varchar(30), "
			+ "choice1 varchar(100), choice2 varchar(100),choice3 varchar(100), choice4 varchar(100),"
			+ "correctanswer integer,"
			+ "author varchar(50),"
			+ "date datetime);";
	
	
	public static void onCreate(SQLiteDatabase db){
		db.execSQL(QUESTION_TABLE_CREATE_SCRIPT);
	}
	
	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		 db.execSQL("DROP TABLE IF EXISTS " + QUESTION_TABLE_NAME);
		    onCreate(db);
	}

	
}
