package com.iiit.adb.emp.db.gdd.GDD;

import java.util.Vector;

/**
 * class for executing result. not used now
 * 
 * @author Jyothi
 *
 */
public class TableInfo{
	private String dbName;
	private String tableName;
	private int tableID;
	private int colNum;
	private int fragmentationType;
	private int fragmentationNum;
	//private Vector<String> conditionStrings;
	private Vector<ColumnInfo> columns;
	private Vector<FragmentationInfo> fragmentations;
	
	public TableInfo(){
		this.tableName = null;
		this.tableID = -1;
		this.colNum = 0;
		this.fragmentationType = -1;
		this.fragmentationNum = -1;
		//this.conditionStrings =  new Vector();
		this.columns = new Vector();
		this.fragmentations = new Vector();
	}
	
	public void setDBName(String db){
		this.dbName = db;
	}
	
	public void setTableName(String tableName){
		this.tableName = tableName;
	}
	
	public void setTableID(int tableid){
		this.tableID = tableid;
	}
	
	public void setColNum(int colnum){
		this.colNum = colnum;
	}
	
	public void setFragmentationType(int type){
		this.fragmentationType = type;
	}
	
	public void setFragmentationNum(int num){
		this.fragmentationNum = num;
	}
	
	public String getDBName(){
		return this.dbName;
	}
	
	public String getTableName(){
		return this.tableName;
	}
	
	public int getTabID(){
		return this.tableID;
	}
	
	public int getColNum(){
		return this.colNum;
	}
	
	public int getFragType(){
		return this.fragmentationType;
	}
	
	public int getFragNum(){
		return this.fragmentationNum;
	}
	
	public Vector<ColumnInfo> getColumnInfo(){
		return this.columns;
	}
	
	public Vector<FragmentationInfo> getFragmentationInfo(){
		return this.fragmentations;
	}
	
	public void printTableInfo(){
		System.out.println("tableName="+tableName+",DBName="+this.dbName);
		System.out.println("columnSize="+this.colNum);
		for(int i=0;i<columns.size();++i){
			columns.elementAt(i).printColumnInfo();
		}
		
		System.out.println("fragNum="+this.fragmentationNum+", fragType="+this.fragmentationType);
		for(int i = 0 ; i < this.fragmentations.size() ; i++)
			this.fragmentations.elementAt(i).printFragmentation();
	}
}