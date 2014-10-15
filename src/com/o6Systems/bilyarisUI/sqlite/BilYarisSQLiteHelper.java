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
	final static int DATABASE_VERSION = 11;
	// Column Names;
	final static String COLUMN_ID = "qID";
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
	
	public final static String USER_TABLE_NAME = "USERS";
	
	public final static String USER_TABLE_CREATE_SCRIPT = "CREATE TABLE " + USER_TABLE_NAME +  "("
			+ "uid integer primary key, "
			+ "name varchar(100));";
	// TODO: User'a yeni field'lar illa eklenecek.
	
	public BilYarisSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
	}
	
	
	public void printQuestionDatabase(SQLiteDatabase db){
		
		QuestionPack questions = getQuestions("Spor",0,0,100);
		questions.print();
	
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
		
		db.execSQL(DatabaseConstants.QUESTION_CREATE);
		db.execSQL(DatabaseConstants.USER_CREATE);
		db.execSQL(DatabaseConstants.QUESTION_ASKED_CREATE);
		db.execSQL(DatabaseConstants.USERSTAT_CREATE);
		
		
		
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
		values.put(COLUMN_ID, Q.questionID);
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
		
	public Question cursorToQuestion(Cursor c){
		
		//public final static String QUESTION_TABLE_CREATE_SCRIPT = "CREATE TABLE " + QUESTION_TABLE_NAME +  "("
			/*	+ "qID integer primary key, "
				+ "text varchar(255),"
				+ "difficulty integer, "
				+ "category varchar(30), "
				+ "choice1 varchar(100), choice2 varchar(100),choice3 varchar(100), choice4 varchar(100),"
				+ "author varchar(50));";
		*/
		
		Question Q = new Question();
		
		int index = c.getColumnIndex(COLUMN_ID);
		Q.questionID = c.getInt(index);
		
		index = c.getColumnIndex(COLUMN_TEXT);
		Q.text = c.getString(index);
		
		index = c.getColumnIndex(COLUMN_DIFFICULTY);
		Q.difficultyLevel = c.getInt(index);
		
		index = c.getColumnIndex(COLUMN_CATEGORY);
		Q.category = c.getString(index);
		
		index = c.getColumnIndex(COLUMN_ID);
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
			int maxDifficulty) {
		
		SQLiteDatabase db = getReadableDatabase();
		String[] params = { category,"" + minDifficulty, "" + maxDifficulty};
		//String queryText = "SELECT * FROM " + QuestionSQLiteHelper.QUESTION_TABLE_NAME +" WHERE category = ? "
		//		+ "AND difficulty BETWEEN ? AND ?";
		
		
		
		/*String categoryPredicate = "";
		if(category != null){
			categoryPredicat
		}*/
		
		String queryText = "SELECT * FROM " + DatabaseConstants.QUESTION_TABLE_NAME;
		
		System.out.println("Select Query!");
		System.out.println(queryText);
		
		System.out.println("Params:");
		for(int i=0; i<params.length;i++){
			System.out.println(params[i]);
		}
		
		Cursor cursor = db.rawQuery(queryText, null);
		
		QuestionPack qp = new QuestionPack();
		cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      Question currentQuestion = cursorToQuestion(cursor);
	      qp.addQuestion(currentQuestion);
	      cursor.moveToNext();
	    }
	    cursor.close();
	    
	    return qp;
		
	}

	@Override
	public void insertUser(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setQuestionsAsked(QuestionPack qp, User u) {
		// TODO Auto-generated method stub
		
	}


}
