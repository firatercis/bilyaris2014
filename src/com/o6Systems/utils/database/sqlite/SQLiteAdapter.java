package com.o6Systems.utils.database.sqlite;

import java.util.HashMap;

import com.o6Systems.utils.database.DatabaseInterface;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class SQLiteAdapter implements DatabaseInterface{


	//create table MY_DATABASE (ID integer primary key, Content text not null);
	
	private static final int MYDATABASE_VERSION = 1;
	private SQLiteHelper sqLiteHelper;
	private SQLiteDatabase sqLiteDatabase;

	private Context context;
	private String createDatabaseScript = "";

	
	public SQLiteAdapter(Context c){
		context = c;
	}
		
	private SQLiteAdapter openToRead(String tableName) throws android.database.SQLException {
		sqLiteHelper = new SQLiteHelper(context, tableName, null, MYDATABASE_VERSION);
		sqLiteDatabase = sqLiteHelper.getReadableDatabase();
		return this; 
	}

	private SQLiteAdapter openToWrite(String tableName) throws android.database.SQLException {
		sqLiteHelper = new SQLiteHelper(context, tableName, null, MYDATABASE_VERSION);
		sqLiteDatabase = sqLiteHelper.getWritableDatabase();
		return this; 
	}

	public void close(){
		sqLiteHelper.close();
	}

	public class SQLiteHelper extends SQLiteOpenHelper {

		public SQLiteHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			if(createDatabaseScript != null)
				db.execSQL(createDatabaseScript);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub

		}

	}

	@Override
	public long insert(String tableName, HashMap<String, String> keyValueMap) {
		openToWrite(tableName);
		ContentValues contentValues = new ContentValues();
		
		for(String currentKey:keyValueMap.keySet()){
			String value = keyValueMap.get(currentKey);
			contentValues.put(currentKey, value);
		}
		close();
		return sqLiteDatabase.insert(tableName, null, contentValues);
	}

	

	@Override
	public boolean createTable(HashMap<String, String> tableTupples) {
		// TODO Auto-generated method stub
		return true;
	}
	
//	@Override
//	public HashMap<String, String>[] select(DatabaseQuery dq) {
//		
//		openToRead(dq.getTableName());
//		
//		Cursor cursor = sqLiteDatabase.query(dq.getTableName(), dq.getColumns(),dq.getWhereClause(),null,dq.getGroupBy(),dq.getHaving(),dq.getOrderBy());
//		@SuppressWarnings("unchecked")
//		HashMap<String,String>[] outputEntries = (HashMap<String,String>[]) new HashMap[cursor.getCount()];
//		
//		int entryIndex = 0;
//		
//		for(cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()){
//			
//			HashMap<String,String> currentEntry = new HashMap<String,String>();
//			for(int currentColumnIndex=0; currentColumnIndex<cursor.getColumnCount();currentColumnIndex++ ){
//				String currentColumnName = cursor.getColumnName(currentColumnIndex);
//				currentEntry.put(currentColumnName, cursor.getString(currentColumnIndex));
//				
//			}
//			outputEntries[entryIndex] = currentEntry;
//			entryIndex++;
//		}
//		close();
//		return outputEntries;
//	}

	
	@Override
	public void setCreateScript(String createScript) {
		createDatabaseScript = createScript;
	}

@Override
public HashMap<String, String> select(String queryString,
		String[] additionalParams) {
	// TODO Auto-generated method stub
	return null;
}
	
	
	

}