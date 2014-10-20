package com.o6Systems.bilyarisUI.sqlite;

import com.o6Systems.bilyarisAppFund.BYDatabaseInterface;
import com.o6Systems.bilyarisAppFund.Question;
import com.o6Systems.bilyarisAppFund.QuestionPack;
import com.o6Systems.bilyarisAppFund.QuestionQuery;
import com.o6Systems.bilyarisAppFund.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BilYarisSQLiteHelper extends SQLiteOpenHelper implements BYDatabaseInterface{
	
	final static String DATABASE_NAME = "bilyaris.db";
	final static int DATABASE_VERSION = 15;
	// Column Names;
	final static String QUESTION_ID_COLUMN = "qID";
	final static String COLUMN_TEXT = "text";
	final static String COLUMN_CATEGORY = "category";
	final static String COLUMN_DIFFICULTY = "difficulty";
	final static String COLUMN_CHOICE1 = "choice1";
	final static String COLUMN_CHOICE2 = "choice2";
	final static String COLUMN_CHOICE3 = "choice3";
	final static String COLUMN_CHOICE4 = "choice4";
	final static String COLUMN_ANSWER = "correctanswer";
	final static String COLUMN_AUTHOR = "author";
	final static String COLUMN_DATE = "date";
	final static String COLUMN_TAG = "tag";
	final static String COLUMN_VALUE = "value";
	
	// User column names
	final static String USER_ID_COLUMN = "uID";
	final static String USER_NAME_COLUMN = "name";
	
	public BilYarisSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		System.out.println("Creating database tables");
		db.execSQL(DatabaseConstants.QUESTION_CREATE);
		db.execSQL(DatabaseConstants.USER_CREATE);
		db.execSQL(DatabaseConstants.QUESTION_ASKED_CREATE);
		db.execSQL(DatabaseConstants.USERSTAT_CREATE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    db.execSQL("DROP TABLE IF EXISTS " + DatabaseConstants.QUESTION_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + DatabaseConstants.USER_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + DatabaseConstants.QUESTION_ASKED_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + DatabaseConstants.USERSTAT_TABLE_NAME);
		onCreate(db);
	}
	
	
	public void clearQuestions(){
		SQLiteDatabase db = getWritableDatabase();
	    db.execSQL("DELETE FROM " +DatabaseConstants.QUESTION_TABLE_NAME);
	}
	
	
	private void insertQuestion(SQLiteDatabase db, Question Q, String author,String date){
		
		ContentValues values = new ContentValues();
		values.put(QUESTION_ID_COLUMN, Q.questionID);
		values.put(COLUMN_TEXT, Q.text);
		values.put(COLUMN_CATEGORY, Q.category);
		values.put(COLUMN_CHOICE1, Q.getAlternative(0));
		values.put(COLUMN_CHOICE2, Q.getAlternative(1));
		values.put(COLUMN_CHOICE3, Q.getAlternative(2));
		values.put(COLUMN_CHOICE4, Q.getAlternative(3));
		values.put(COLUMN_ANSWER, Q.getAnswer());
		values.put(COLUMN_DIFFICULTY, Q.difficultyLevel);
		values.put(COLUMN_AUTHOR, author);
		values.put(COLUMN_DATE, date);
		
		long insertID = db.insert(DatabaseConstants.QUESTION_TABLE_NAME, null, values);
		System.out.println("Insert ID " + insertID);
		
	}
		
	private Question cursorToQuestion(Cursor c){
		
		//public final static String QUESTION_TABLE_CREATE_SCRIPT = "CREATE TABLE " + QUESTION_TABLE_NAME +  "("
			/*	+ "qID integer primary key, "
				+ "text varchar(255),"
				+ "difficulty integer, "
				+ "category varchar(30), "
				+ "choice1 varchar(100), choice2 varchar(100),choice3 varchar(100), choice4 varchar(100),"
				+ "author varchar(50));";
		*/
		
		Question Q = new Question();
		
		int index = c.getColumnIndex(QUESTION_ID_COLUMN);
		Q.questionID = c.getInt(index);
		
		index = c.getColumnIndex(COLUMN_TEXT);
		Q.text = c.getString(index);
		
		index = c.getColumnIndex(COLUMN_DIFFICULTY);
		Q.difficultyLevel = c.getInt(index);
		
		index = c.getColumnIndex(COLUMN_CATEGORY);
		Q.category = c.getString(index);
		
		index = c.getColumnIndex(QUESTION_ID_COLUMN);
		Q.questionID = c.getInt(index);
		
		index = c.getColumnIndex(COLUMN_CHOICE1);
		Q.addAlternative(c.getString(index));
		
		index = c.getColumnIndex(COLUMN_CHOICE2);
		Q.addAlternative(c.getString(index));
		
		index = c.getColumnIndex(COLUMN_CHOICE3);
		Q.addAlternative(c.getString(index));
		
		index = c.getColumnIndex(COLUMN_CHOICE4);
		Q.addAlternative(c.getString(index));
		
		index = c.getColumnIndex(COLUMN_ANSWER);
		Q.setAnswer(c.getInt(index));
	
		return Q;
		
	}
	
	

	@Override
	public void insertQuestionPack(QuestionPack qP) {
		SQLiteDatabase db = getWritableDatabase();
		for(Question Q:qP.getQuestions()){
			insertQuestion(db,Q,qP.getAuthor(),qP.getDate());	
		}
	}

	
	@Override
	public QuestionPack getQuestions(String category, int userID, int minDifficulty,
			int maxDifficulty, int nQuestions) {
		
		SQLiteDatabase db = getReadableDatabase();
		
		String queryText = "SELECT * FROM " +DatabaseConstants.QUESTION_TABLE_NAME+ " WHERE " 
		+ QUESTION_ID_COLUMN + 
		" NOT IN "
		+ "(SELECT " + QUESTION_ID_COLUMN + " FROM " + DatabaseConstants.QUESTION_ASKED_TABLE_NAME + " WHERE uID = "
				+ userID + ")"; 
			
		if(category!= null){
			queryText +=" AND category='" + category + "'";
		}
		
		if(minDifficulty != -1){
			queryText += " AND difficulty BETWEEN " + minDifficulty + " AND " + maxDifficulty; 
		}		
		
		queryText += " ORDER BY ABS(RANDOM()) LIMIT "  + nQuestions ; 
		
		System.out.println("Select Query!");
		System.out.println(queryText);
		
	
		Cursor cursor = db.rawQuery(queryText, null);
		System.out.println("Cursor count" + cursor.getCount());
		
		QuestionPack qp = new QuestionPack();
		cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      Question currentQuestion = cursorToQuestion(cursor);
	      qp.addQuestion(currentQuestion);
	      cursor.moveToNext();
	    }
	    cursor.close();
	    
	    System.out.println("Questions size!");
	    System.out.println(qp.getQuestions().size());
	    return qp;
	}
	
	@Override
	public void insertUser(User user) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(USER_NAME_COLUMN, user.getName());
		long insertID = db.insert(DatabaseConstants.USER_TABLE_NAME, null, values);
		System.out.println("User Insert ID " + insertID);
	}

	@Override
	public void setQuestionAsked(Question q, User u) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(USER_ID_COLUMN, u.getUserID());
		values.put(QUESTION_ID_COLUMN,q.getID());
		long insertID = db.insert(DatabaseConstants.QUESTION_ASKED_TABLE_NAME, null, values);
		System.out.println("Question asked insert ID " + insertID);
		printQuestionsAsked();	
	}

	private void printQuestionsAsked(){
		SQLiteDatabase db = getReadableDatabase();
		String queryText = "SELECT qID FROM " + DatabaseConstants.QUESTION_ASKED_TABLE_NAME ;
		Cursor cursor = db.rawQuery(queryText, null);
		int index = cursor.getColumnIndex("qID");
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			int currentQID = cursor.getInt(index);
			System.out.println("QID: " + currentQID);
			 cursor.moveToNext();
		}
	}
	
	@Override
	public void updateStatValue(int uID, String category, String tag) {
		SQLiteDatabase db = getReadableDatabase();
		String queryText = "SELECT value FROM USERSTAT WHERE uID = " + uID + 
				" AND category = "
				+ category
				+ " AND tag=" + tag;

		System.out.println("Select Query!");
		System.out.println(queryText);
		
		Cursor cursor = db.rawQuery(queryText, null);
		
		ContentValues contentValues = new ContentValues();
		contentValues.put(USER_ID_COLUMN,uID);
		contentValues.put(COLUMN_CATEGORY,category);
		contentValues.put(COLUMN_TAG,tag);
		
		db = getWritableDatabase();
		
		if(cursor.getCount()==0){
			// insert new 
			contentValues.put(COLUMN_VALUE,1);
			db.insert(DatabaseConstants.USERSTAT_TABLE_NAME, null, contentValues);
			
		}else{
			// update
			int index = cursor.getColumnIndex(COLUMN_VALUE);
			int value = cursor.getInt(index);
			contentValues.put(COLUMN_VALUE,value + 1);
			
			String whereClause = USER_ID_COLUMN  + "= ? AND " + COLUMN_CATEGORY + "= ? AND " + COLUMN_TAG  + "= ?";
			String[] whereArgs = {"" + uID,category,tag};
			db.update(DatabaseConstants.USERSTAT_TABLE_NAME, contentValues, whereClause , whereArgs);
		}
	}

	
	/*
	 * @Override
	public QuestionPack getQuestions(String category, int userID, int minDifficulty,
			int maxDifficulty, int nQuestions) {
		
		SQLiteDatabase db = getReadableDatabase();
		
		String queryText = "SELECT * FROM " +DatabaseConstants.QUESTION_TABLE_NAME+ " WHERE " 
		+ QUESTION_ID_COLUMN + 
		" NOT IN "
		+ "(SELECT " + QUESTION_ID_COLUMN + " FROM " + DatabaseConstants.QUESTION_ASKED_TABLE_NAME + " WHERE uID = "
				+ userID + ")"; 
			
		if(category!= null){
			queryText +=" AND category='" + category + "'";
		}
		
		if(minDifficulty != -1){
			queryText += " AND difficulty BETWEEN " + minDifficulty + " AND " + maxDifficulty; 
		}		
		
		queryText += " ORDER BY ABS(RANDOM()) LIMIT "  + nQuestions ; 
		
		System.out.println("Select Query!");
		System.out.println(queryText);
		
	
		Cursor cursor = db.rawQuery(queryText, null);
		System.out.println("Cursor count" + cursor.getCount());
		
		QuestionPack qp = new QuestionPack();
		cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      Question currentQuestion = cursorToQuestion(cursor);
	      qp.addQuestion(currentQuestion);
	      cursor.moveToNext();
	    }
	    cursor.close();
	    
	    System.out.println("Questions size!");
	    System.out.println(qp.getQuestions().size());
	    return qp;
	}(non-Javadoc)
	 * @see com.o6Systems.bilyarisAppFund.BYDatabaseInterface#getUsers()
	 */
	
	@Override
	public User[] getUsers() {
		User[] users;
		SQLiteDatabase db = getReadableDatabase();
		String queryText = "SELECT * FROM " +DatabaseConstants.USER_TABLE_NAME ;
		Cursor cursor = db.rawQuery(queryText, null);
		int nUsers = cursor.getCount();
		System.out.println("Number of users in system:" + nUsers);
		if(nUsers == 0){
			users = null;
		}else{
			users = new User[nUsers];
			cursor.moveToFirst();
			int currentUserIndex = 0;
		    while (!cursor.isAfterLast()) {
		      User currentUser = cursorToUser(cursor);
		      users[currentUserIndex] = currentUser;
		      currentUserIndex++;
		      cursor.moveToNext();
		    }
		    cursor.close();
		}
		return users;
	}
	
	private User cursorToUser(Cursor cursor){
		
		int index = cursor.getColumnIndex(USER_NAME_COLUMN);
		String userName = cursor.getString(index);
	
		User U = new User(userName);
		index = cursor.getColumnIndex(USER_ID_COLUMN);
		int userID = cursor.getInt(index);
		U.setUserID(userID);
		// TODO: Diger user bilgileri de alinacak.
		
		return U;
	}

	@Override
	public void deleteUser(int userID) {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("DELETE FROM " +DatabaseConstants.USER_TABLE_NAME+" WHERE " + USER_ID_COLUMN+ "=" + userID);
	}
	
	public void deleteAllUsers(){
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("DELETE FROM " +DatabaseConstants.USER_TABLE_NAME);
	}
	

}
