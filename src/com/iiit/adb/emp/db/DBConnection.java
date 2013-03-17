package com.iiit.adb.emp.db;

import java.io.StringReader;
import java.sql.*;
import java.util.Iterator;
import java.util.List;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.iiit.adb.emp.db.parser.globaldefinition.JoinExpression;
import com.iiit.adb.emp.db.querytree.JoinNode;
//import com.iiit.adb.emp.parser.ExpressionItem;
import com.iiit.adb.emp.parser.WhereItemsFinder;

import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.select.Select;

public class DBConnection {
	
   public static Connection conn1 ;
   public static Connection conn2 ;
   public static Connection conn3;
	   
	public  DBConnection(){
		
		  
	  if (conn1 == null){
	   conn1 =getSite1Conn();}
	   //conn2 = db.getSite2Conn();
	   //conn3 = db.getSite3Conn();
		   
	}
	
	public void closeDBConnection(){
		 try{
			  conn1.close();
			    System.out.println("Disconnected from database");
			  } catch(SQLException se){
				  se.printStackTrace();
			  }

	}
 
	/*
	 * return connection for site1
	 */
	public static Connection getSite1Conn(){
		
		  Connection conn = null;
		  //site 1 :)
		  String url = "jdbc:mysql://192.168.15.9:3306/";
		  String dbName = "dist";
		  String driver = "com.mysql.jdbc.Driver";
		  String userName = "root"; 
		  String password = "root123";
		  try {
		  Class.forName(driver).newInstance();
		  conn = DriverManager.getConnection(url+dbName,userName,password);
		  System.out.println("Connected to the database of Site1");
		  
		 
		  } catch (Exception e) {
			  e.printStackTrace();
		  }
		 
		  
		  return conn;
		
	}
	
	

	public static Connection getSite2Conn(){
		
		  Connection conn = null;
		  // change this for site2
		  String url = "jdbc:mysql://192.168.15.9:3306/";
		  String dbName = "dist";
		  String driver = "com.mysql.jdbc.Driver";
		  String userName = "root"; 
		  String password = "root123";
		  try {
		   Class.forName(driver).newInstance();
		   conn = DriverManager.getConnection(url+dbName,userName,password);
		   System.out.println("Connected to the database of Site2");
		  
		 
		  } catch (Exception e) {
			  e.printStackTrace();
		  }
		 /* finally{
			  try{
			  conn.close();
			  System.out.println("Disconnected from database of site2");
			  } catch(SQLException se){
				  
			  }
		  }*/
		  
		  return conn;
		
	}
	

	public static Connection getSite3Conn(){
		
		  Connection conn = null;
		  // change this for site2
		  String url = "jdbc:mysql://192.168.15.9:3306/";
		  String dbName = "dist";
		  String driver = "com.mysql.jdbc.Driver";
		  String userName = "root"; 
		  String password = "root123";
		  try {
		   Class.forName(driver).newInstance();
		   conn = DriverManager.getConnection(url+dbName,userName,password);
		   System.out.println("Connected to the database of Site3");
		  
		 
		  } catch (Exception e) {
			  e.printStackTrace();
		  }
		 /* finally{
			  try{
			  conn.close();
			  System.out.println("Disconnected from database of site2");
			  } catch(SQLException se){
				  
			  }
		  }*/
		  
		  return conn;
		
	}
  public static void main(String[] args) {
	  DBConnection db = new DBConnection();
	 // Connection conn = db.getSite1Conn();
  try {
 // if (DBConnection.conn1 != null){
  //System.out.println("Connected to the database");
  CCJSqlParserManager pm = new CCJSqlParserManager();
  String sql = "SELECT * FROM tbl,t1 where tbl.tbl_name=t1.name" ;
  net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));
  /* 
  now you should use a class that implements StatementVisitor to decide what to do
  based on the kind of the statement, that is SELECT or INSERT etc. but here we are only
  interested in SELECTS
  */
  if (statement instanceof Select) {
  	Select selectStatement = (Select) statement;
  	/*------where clause----*/
	WhereItemsFinder finder3 = new WhereItemsFinder(selectStatement);
	/*------join clause----*/
	ArrayList<JoinNode> joins = new ArrayList<JoinNode>();
	ArrayList<JoinExpression> joinList = finder3.getJionList();
	for(int i=0;i<joinList.size();++i){
		JoinNode node2 = new JoinNode();
		System.out.println(joinList.get(i).leftTableName);
		//node2.setRightTableName(joinList.get(i).rightTableName);
		//node2.addAttribute(joinList.get(i).leftColumn, joinList.get(i).rigthColumn);
		;
		
	}
	
  }
 // }
 
  } catch (Exception e) {
  e.printStackTrace();
  }
  finally{
	  try{
	   db.closeDBConnection();
	  System.out.println("Disconnected from database");
	  } catch(Exception se){
		  
	  }
  }
  }
}
