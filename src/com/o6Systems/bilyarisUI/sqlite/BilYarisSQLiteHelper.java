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
	final static int DATABASE_VERSION = 2;
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
	
	public BilYarisSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
	}
	
	public void printQuestionDatabase(SQLiteDatabase db){
		
		QuestionPack questions = fetchQuestions(db,"Spor",0,100,"fercis");
		questions.print();
	
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		QuestionSQLiteHelper.onCreate(db);
		/*Question test = new Question();
		test.text = "aleleyo";
		test.difficultyLevel = 60;
		test.addAlternative("a1");
		test.addAlternative("a2");
		test.addAlternative("a3");
		test.addAlternative("a4");
		test.setAnswer(3);
		test.category = "Spor";
		QuestionPack qpDummy = new QuestionPack();
		qpDummy.addQuestion(test);
		
		insertQuestionPack(db, qpDummy);*/
	}
	
	public void insertQuestionPack(SQLiteDatabase db, QuestionPack qP){
		for(Question Q:qP.getQuestions()){
			insertQuestion(db,Q,qP.getAuthor(),qP.getDate());	
		}
	}
	
	public void insertQuestion(SQLiteDatabase db, Question Q, String author,String date){
		
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
		
		long insertID = db.insert(QuestionSQLiteHelper.QUESTION_TABLE_NAME, null, values);
		System.out.println("Insert ID " + insertID);
		
	}
	
	public QuestionPack fetchQuestions(SQLiteDatabase db, String category, int minDifficulty, int maxDifficulty, String userName){
		// TODO: userName ile kontrol konulacak.
		
		String[] params = { category,"" + minDifficulty, "" + maxDifficulty};
		//String queryText = "SELECT * FROM " + QuestionSQLiteHelper.QUESTION_TABLE_NAME +" WHERE category = ? "
		//		+ "AND difficulty BETWEEN ? AND ?";
		
		String queryText = "SELECT * FROM " + QuestionSQLiteHelper.QUESTION_TABLE_NAME;
		
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
		
		Q.print();
		
		return Q;
		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		QuestionSQLiteHelper.onUpgrade(db, oldVersion, newVersion);
	}

	@Override
	public void insertQuestionPack(QuestionPack qP) {
		SQLiteDatabase db = getWritableDatabase();
		for(Question Q:qP.getQuestions()){
			insertQuestion(db,Q,qP.getAuthor(),qP.getDate());	
		}
		
	}

	@Override
	public void updateQuestion(Question q) {
		// TODO Auto-generated method stub
		
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
		
		String queryText = "SELECT * FROM " + QuestionSQLiteHelper.QUESTION_TABLE_NAME;
		
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
	public QuestionPack getQuestions(QuestionQuery query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertUser(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setQuestionsAsked(QuestionPack qp, User u) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getQuestionsDate() {
		// TODO Auto-generated method stub
		return null;
	}

}
