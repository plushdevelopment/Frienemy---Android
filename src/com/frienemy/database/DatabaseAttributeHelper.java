package com.frienemy.database;

public class DatabaseAttributeHelper {

	static String name = "databse_name";
	static int version = 1;

	public  void setDbName(String namex) {
		DatabaseAttributeHelper.name = namex;
	}
	public  static String getDbName(){
		return name;
	}
	public  void setDbVersion(int x) {
		DatabaseAttributeHelper.version = x;
	}
	public  static int getDbVersion(){
		return version;
	}

	static String status = "new";
	public  void setDbStatus(String statusy) {
		DatabaseAttributeHelper.status= statusy;
	}
	public  static String getDbStatus(){
		return status;
	}

	public static String tbl_Names[] = new String[10];
	public static int tnum = 4;

	public  void setNumTables(int numx) {
		DatabaseAttributeHelper.tnum = numx;
	}
	public static int getNumTables(){
		return tnum;
	}

	public void setTblNames(String Cnames[]) {
		for (int i=0; i< DatabaseAttributeHelper.tnum; i++ )
		{
			DatabaseAttributeHelper.tbl_Names[i] = Cnames[i];
		}
	}

	public static String[] getTblNames(){
		return tbl_Names;
	}

	public  static int []  Ncols = new int[10];

	public  void setNumCols(int x[]) {
		for (int i=0; i< DatabaseAttributeHelper.tnum; i++ ) {

			DatabaseAttributeHelper.Ncols[i] = x[i];
		}
	}
	public static  int [] getNumCols(){
		return Ncols;
	}

	public static  String[][] Col_Names = new String[10][10];

	public void setColNames(String Cnames[][]) {

		for (int j=0; j<DatabaseAttributeHelper.tnum; j++){

			for (int i=0; i< DatabaseAttributeHelper.Ncols[j]; i++ ) {

				DatabaseAttributeHelper.Col_Names[j][i] = Cnames[j][i];
			}
		}
	}

	public static String[][] getColNames(){
		return Col_Names;
	}


	public static String [][] Col_Types = new String[10][10];

	public void setColTypes(String Cnames[][]) {

		for (int j=0; j<DatabaseAttributeHelper.tnum; j++){

			for (int i=0; i< DatabaseAttributeHelper.Ncols[j]; i++ ) {

				DatabaseAttributeHelper.Col_Types[j][i] = Cnames[j][i];
			}
		}
	} 

	public static  String[][] getColTypes(){
		return Col_Types;
	}
}
