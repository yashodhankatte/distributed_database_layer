package com.iiit.adb.emp.db.parser.validate;

import com.iiit.adb.emp.db.DBConnection;
import com.iiit.adb.emp.db.parser.globaldefinition.JoinExpression;
import com.iiit.adb.emp.db.parser.globaldefinition.SimpleExpression;
import com.iiit.adb.emp.db.parser.utility.ParserLists;
import com.iiit.adb.emp.db.parser.utility.TblColumn;
import com.iiit.adb.emp.db.parseresult.SimpleParser;
import com.iiit.adb.emp.dblayer.syscatalogue.Tbl;

import java.sql.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.sf.jsqlparser.parser.ParseException;

public class SemanticValidator {

	// get db connection for one site
	// execute sql statement "select * from tbl;
	// store values in ArrayList<Tbl>
	// a method to call simpleparser methods on the sql statements and get where conditions,
	// ---- column names and table names
	// check the columnnames against the Tbl colnames, ]
	
	/**
	 * @param sql
	 * @throws Exception
	 */
	public void validateQuery(String sql) throws Exception{
		
		Connection conn = (new DBConnection()).getSite1Conn();
		ArrayList<Tbl> tbl = executeSql("Select * from tbl",conn);
		SimpleParser sp = new SimpleParser();
		ParserLists pl = sp.getParerLists(sql);
		
		// check table names
		String cstr = new String();
		String ctstr = new String();
		boolean flag = true;
		for (Iterator i = pl.getTbllist().iterator();i.hasNext() & (flag== true);){
			
		      String tname = (String)i.next();
		      flag = false;
		for (Iterator j = tbl.iterator();j.hasNext();){
			Tbl t = (Tbl)j.next();
			if (tname.equals(t.getTblName())) {
				
				flag = true;
				cstr += t.getColNames()+":";
				ctstr += t.getColTypes()+":";
				break;
				
			}
		}
		
		if (flag == false){
			throw (new Exception("Invalid Table Name"));
		}
		}
		
		
		// check for column names
		for (Iterator  i= pl.getCollist().iterator();i.hasNext() & (flag== true);){
			TblColumn tc = (TblColumn)i.next();
			if (tc.getTableName() == null){
			     if (cstr.contains(tc.getColumnName())) {
			    	  flag = true;
			     } else {
			    	 throw (new Exception("Invalid Column"));
			     }
			} else {
				for (Iterator j = tbl.iterator();j.hasNext();){
					   Tbl t = (Tbl)j.next();
						if (tc.getTableName().equals(t.getTblName())) {
							if (t.getColNames().contains(tc.getColumnName())){
								flag = true;
								break;
							}
							
						}
				   }
				if (flag == false){
					throw (new Exception("Invalid Column"));
				}
			}
		}
		
		
		// check for where condition tables,columns, datatype
		// simple expression
		for (Iterator  i= pl.getSelist().iterator();i.hasNext() & (flag== true);){
			SimpleExpression se = (SimpleExpression)i.next();
			
			// check if the table and column exist and column dataype
			for (Iterator j = tbl.iterator();j.hasNext();){
				Tbl t = (Tbl)j.next();
				if (se.tableName == "NULL"){
					// loop in all the tablenames
					//for (Iterator l = tbl.iterator();l.hasNext();){
						//   Tbl t = (Tbl)l.next();
							if (se.tableName.equals(t.getTblName())) {
								if (t.getColNames().contains(se.columnName)){
									String[] temp = t.getColNames().split(":");
									List<String> tas = Arrays.asList(temp);
									if (tas.contains(se.columnName)){
										int index = tas.indexOf(se.columnName);
										List<String> temp2 = Arrays.asList((t.getColTypes().split(":")));
										if (temp2.get(index).equals(se.valueType)){
											flag = true;
										}
										else {
											throw (new Exception("Invalid Column Type"));
										}
									} else {
										throw (new Exception("Invalid Column"));
									}
									flag = true;
									
									break;
								}
								
							}
					//   }
					if (flag == false){
						throw (new Exception("Invalid Column"));
					}
					// check for correct datatype
				}
				else if (se.tableName.equals(t.getTblName())) {
					String[] temp = t.getColNames().split(":");
					List<String> tas = Arrays.asList(temp);
					if (tas.contains(se.columnName)){
						int index = tas.indexOf(se.columnName);
						List<String> temp2 = Arrays.asList((t.getColTypes().split(":")));
						if (temp2.get(index).equals(se.valueType)){
							flag = true;
						}
						else {
							throw (new Exception("Invalid Column Type"));
						}
					} else {
						throw (new Exception("Invalid Column"));
					}
					flag = true;
					
					break;
					
				}
			}
		}
		
		
		// The below code takes care of the join expression where
		// simple expression
				for (Iterator  i= pl.getJelist().iterator();i.hasNext() & (flag== true);){
					JoinExpression je = (JoinExpression)i.next();
					String[] rightcolumns = null ;
					String[] rightcoltypes = null;
					String[] leftcolumns = null;
					String[] leftcoltypes = null;
					int leftindex= -1;
					int rightindex = -1;
					String type1 = null;
							String type2 = null;
					// check if the table and column exist and column dataype
					for (Iterator j = tbl.iterator();j.hasNext();){
						Tbl t = (Tbl)j.next();
					 if (je.leftTableName.equals(t.getTblName())) {
						 // collect all the column names
						 
							 leftcolumns = t.getColNames().split(":");
							 leftcoltypes = t.getColTypes().split(":");
							 List s1 = Arrays.asList(leftcolumns);
							 leftindex =  s1.indexOf(je.leftColumn);
							 List s2 = Arrays.asList(leftcoltypes);
							  type1 =  (String)s2.get(leftindex);
							 
							
						}
					 
					 if (je.rightTableName.equals(t.getTblName())) {
						 // collect all the column names
						 
							 rightcolumns = t.getColNames().split(":");
							 rightcoltypes = t.getColTypes().split(":");
							 List s1 = Arrays.asList(rightcolumns);
							 rightindex =  s1.indexOf(je.rigthColumn);
							 List s2 = Arrays.asList(rightcoltypes);
							  type2 =  (String)s2.get(rightindex);
							
						}
					 
					}
					
					if ((leftcolumns == null) || (rightcolumns == null)){
						throw new Exception("Invalid tables in where condition");
					} 
					
					if (type2 != type1) {
						throw new Exception("Invalid type in where Join condition");
					}
				}
		
				for (Iterator  i= pl.getGroupbylist().iterator();i.hasNext() & (flag== true);){
					
					TblColumn tc = (TblColumn)i.next();
					if (tc.getTableName() == null){
					     if (cstr.contains(tc.getColumnName())) {
					    	  flag = true;
					     } else {
					    	 throw (new Exception("Invalid Column"));
					     }
					} else {
						for (Iterator j = tbl.iterator();j.hasNext();){
							   Tbl t = (Tbl)j.next();
								if (tc.getTableName().equals(t.getTblName())) {
									if (t.getColNames().contains(tc.getColumnName())){
										flag = true;
										break;
									}
									
								}
						   }
						if (flag == false){
							throw (new Exception("Invalid Column"));
						}
					}
					
				}
				
				
				
		System.out.println("SQL statement is correct");
		
	}

	
	
	private ArrayList<Tbl> executeSql(String sql, Connection conn) {
		// TODO Auto-generated method stub
		ArrayList<Tbl>  tbltmp = new ArrayList<Tbl>();
		try{
			  Statement st = conn.createStatement();
			  ResultSet res = st.executeQuery(sql);
			  
			  while (res.next()) {
				  Tbl t= new Tbl();
				  t. setTblName(res.getString("tbl_name"));
				  t.setColNames(res.getString("col_names"));
				  t.setColTypes(res.getString("col_types"));
				  // we can set other also in future
				  tbltmp.add(t);
				  
			  		  
				  
				 
			      
			  }
			  
			  }
			  catch (SQLException s){
			  System.out.println("SQL code does not execute.");
			  }  
			  

		return tbltmp;
	}
	
	
	
	public static void main(String[] args){
		System.out.println("Testing sql");
		SemanticValidator sv = new SemanticValidator();
		try {
			//Correct queries
			//sv.validateQuery("Select * from employee");
			//sv.validateQuery("Select ename from employee");
			
			
			//Incorrect queries
			//sv.validateQuery("select from employee");
			sv.validateQuery("Select * from employee,dependent where employee.emp_id = dependent.emp_id");
		} catch(ParseException pe){
			System.out.println("SQL statement is incorrect");
		}
			// TODO Auto-generated catch block
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
