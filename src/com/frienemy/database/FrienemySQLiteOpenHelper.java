package com.frienemy.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FrienemySQLiteOpenHelper extends SQLiteOpenHelper {
	
	// Database values stored in DatabaseAttributeHelper class
    public  static final String DATABASE_NAME = DatabaseAttributeHelper.getDbName();
    public  static final int DATABASE_VERSION = DatabaseAttributeHelper.getDbVersion(); 
    public  static final String DATABASE_STATUS = DatabaseAttributeHelper.getDbStatus();
    public  static final String ID_FIELD = "_id";
   
	public   static int Num_Tbls = DatabaseAttributeHelper.getNumTables();
	public   static String [] Tbl_Names = DatabaseAttributeHelper.getTblNames();
	public   static int [] Num_Cols = DatabaseAttributeHelper.getNumCols();
	public   static String [][] Var_Names = DatabaseAttributeHelper.getColNames();
	public   static String [][] Var_Types = DatabaseAttributeHelper.getColTypes();

	public FrienemySQLiteOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (!db.isReadOnly())
		{
			// Enable foreign key constraints
			db.execSQL("PRAGMA foreign_keys=ON;");
		}
		if (DATABASE_STATUS == "new" )
		{

			String str1 ="";
			for ( int i= 0; i<Num_Tbls; i++)
			{
				String SuppString = " ";
				str1 = " ";
				for ( int j=0; j< Num_Cols[i]-1; j++)
				{

					SuppString =  SuppString + Var_Names[i][j] + " " + Var_Types[i][j] +" not null"+", ";

				}
				str1 = "create table " +
						Tbl_Names[i] + 
						" (" + 
						ID_FIELD + " integer primary key autoincrement, " + 
						SuppString + Var_Names[i][Num_Cols[i]-1] + " " + Var_Types[i][Num_Cols[i]-1] + " not null" +
						");";


				db.execSQL(str1); 
				Log.d(DATABASE_NAME, "onCreate: " + Tbl_Names[i]);

			}

		}	
		if (DATABASE_STATUS == "old" )
		{
			String myPath = "data/data/com.jay.intmulti/databases/" + DATABASE_NAME;
			db.openDatabase(myPath, null, db.OPEN_READWRITE);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion >= newVersion)
			return;

		String sql = null;
		if (oldVersion == 1) 
			sql = "alter table " + Tbl_Names[0] + " add note text;";
		if (oldVersion == 2)
			sql = "";

		Log.d("TableData", "onUpgrade	: " + sql);
		if (sql != null)
			db.execSQL(sql);
	}

}
