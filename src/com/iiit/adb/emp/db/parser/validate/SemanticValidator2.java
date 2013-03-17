package com.iiit.adb.emp.db.parser.validate;

import com.iiit.adb.emp.db.DBConnection;
import com.iiit.adb.emp.db.parser.WhereTree.WhereTree;
import com.iiit.adb.emp.db.parser.globaldefinition.FunctionParam;
import com.iiit.adb.emp.db.parser.globaldefinition.JoinExpression;
import com.iiit.adb.emp.db.parser.globaldefinition.SimpleExpression;
import com.iiit.adb.emp.db.parser.utility.ParserLists;
import com.iiit.adb.emp.db.parser.utility.TblColumn;
import com.iiit.adb.emp.db.parseresult.SimpleParser;
import com.iiit.adb.emp.dblayer.syscatalogue.Tbl;
import com.iiit.adb.emp.parser.WhereClauseDecomposition;

import java.sql.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.sf.jsqlparser.parser.ParseException;

public class SemanticValidator2 {

	// get db connection for one site
	// execute sql statement "select * from tbl;
	// store values in ArrayList<Tbl>
	// a method to call simpleparser methods on the sql statements and get where conditions,
	// ---- column names and table names
	// check the columnnames against the Tbl colnames, ]
	
	ArrayList<TblColumn> groupByList = new ArrayList<TblColumn>();
	
	/**
	 * @param sql
	 * @throws Exception
	 */
	public void validateQuery(String sql) throws Exception{
		
		Connection conn = (new DBConnection()).getSite1Conn();
		ArrayList<Tbl> tbl = executeSql("Select * from tbl",conn);
		SimpleParser sp = new SimpleParser();
		ParserLists pl = null;
		
		
		try{
		 pl = sp.getParerLists(sql);
		} catch (Exception pe){
			throw (new Exception("JSQL parser Exception"));
		}
		
		
		// check table names
		String cstr = new String();
		String ctstr = new String();
		boolean flag = true;
		if (pl.getTbllist() != null){
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
		}} else {
			throw new Exception("Null Table Names");
		}
		
		
		// check for column names
		if (pl.getCollist() != null){
		for (Iterator  i= pl.getCollist().iterator();i.hasNext() & (flag== true);){
			TblColumn tc = (TblColumn)i.next();
			flag = validateColumn(tc,cstr,tbl);
		}
		}
		// get groupby lists
		for (Iterator  i= pl.getGroupbylist().iterator();i.hasNext() & (flag== true);){
			TblColumn tc = (TblColumn)i.next();
			
			flag = validateColumn(tc,cstr,tbl);
			groupByList.add(tc);
		}
		
		// get Having SE List
		for (Iterator  i= pl.getHseList().iterator();i.hasNext() & (flag== true);){
			SimpleExpression se = (SimpleExpression) i.next();
			flag =this.checkHavingSimple(pl, tbl);
			
		}
		
		// get having JE list
		for (Iterator  i= pl.getHjeList().iterator();i.hasNext() & (flag== true);){
			JoinExpression se = (JoinExpression) i.next();
			flag =this.checkHavingJoin(pl, tbl,cstr,ctstr);
			
		}
		
		// get having function lists
		for (Iterator  i= pl.getHefList().iterator();i.hasNext() & (flag== true);){
			FunctionParam fp = (FunctionParam) i.next();
			String fname = fp.getFunctionname();
			// how to check the function name and valid datatype
			ArrayList<TblColumn> tclist = fp.getTblcolumn();
			for (Iterator j=tclist.iterator();j.hasNext() & (flag = true);){
				flag = validateColumn(((TblColumn)j.next()),cstr,tbl);
			}
			
			
		}
		
		
		// get function lists
		for (Iterator  i= pl.getFunctionList().iterator();i.hasNext() & (flag== true);){
			FunctionParam fp = (FunctionParam)i.next();
			String fname = fp.getFunctionname();
			// how to check the function name
			ArrayList<TblColumn> tclist = fp.getTblcolumn();
			for (Iterator j=tclist.iterator();j.hasNext() & (flag = true);){
				flag = validateColumn(((TblColumn)j.next()),cstr,tbl);
			}
			
		}
			// get the where tree
		
		WhereClauseDecomposition wd = new WhereClauseDecomposition(sql);
		WhereTree wt = wd.getWhereClauseTree();
		// you need to get the validate tree
		wt.validateWhere(tbl,cstr);
	
		
				
		System.out.println("SQL statement is correct");
		
	}

	
	
	private boolean validateColumn(TblColumn tc,String cstr,List<Tbl> tbl) throws Exception{
		boolean flag = false;
		if (tc.isAllColumns()){
			return true;
		}
		if (tc.getTableName().equals("null")){
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
		return flag;
		
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
	
	
	public boolean checkHavingSimple(ParserLists pl,ArrayList<Tbl> tbl) throws Exception{
		// check for where condition tables,columns, datatype
				// simple expression
		boolean flag = true;
				for (Iterator  i= pl.getSelist().iterator();i.hasNext() & (flag== true);){
					SimpleExpression se = (SimpleExpression)i.next();
					if (!(groupByExists(se.tableName,se.columnName))){
						throw new Exception("column doesn't exist in GroupBy");
					}
					// check if the table and column exist and column dataype
					
						if (se.tableName == "NULL"){
							// loop in all the tablenames
							for (Iterator l = tbl.iterator();l.hasNext();){
								  Tbl t = (Tbl)l.next();
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
							  }
							if (flag == false){
								throw (new Exception("Invalid Column"));
							}
							// check for correct datatype
						} else {
							for (Iterator j = tbl.iterator();j.hasNext();){
								Tbl t = (Tbl)j.next();
								
						     if (se.tableName.equals(t.getTblName())) {
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
					
				}
				
				return flag;
	}
	
	
	public boolean checkHavingJoin(ParserLists pl,ArrayList<Tbl> tbl,String cstr,String ctstr) throws Exception{
		boolean flag = true;
		
		for (Iterator  i= pl.getJelist().iterator();i.hasNext() & (flag== true);){
			JoinExpression je = (JoinExpression)i.next();
			//operator
			if (!(groupByExists(je.leftTableName,je.leftColumn))){
				throw new Exception("column doesn't exist in GroupBy");
			}
			
			if (!(groupByExists(je.rightTableName,je.rigthColumn))){
				throw new Exception("column doesn't exist in GroupBy");
			}
			je.op.matches("=|>|<|<=|>=");
			//table
			for (Iterator j = tbl.iterator();j.hasNext();){
				Tbl t = (Tbl)j.next();
				if (je.leftTableName.equals(t.getTblName())) {
					flag = true;
					break;
				}
				else {
			    	 throw (new Exception("Invalid table in where"));
			     }
			}
			for (Iterator j = tbl.iterator();j.hasNext();){
				Tbl t = (Tbl)j.next();
				if (je.rightTableName.equals(t.getTblName())) {
					flag = true;
					break;
				}
				else {
			    	 throw (new Exception("Invalid table in where"));
			     }
			}
			//column
			if (cstr.contains(je.leftColumn)) {
		    	  flag = true;
		     } 
		     else {
		    	 throw (new Exception("Invalid Column in where"));
		     }
			if (cstr.contains(je.rigthColumn)) {
		    	  flag = true;
		     } 
		     else {
		    	 throw (new Exception("Invalid Column in where"));
		     }
		}
		
		return flag;
	
	}
	private boolean groupByExists(String tableName, String columnName) {
		// TODO Auto-generated method stub
		
		for (Iterator i=this.groupByList.iterator();i.hasNext();){
			TblColumn tc = (TblColumn)i.next();
			if (tc.getTableName().equals(tableName)){
				if (tc.getColumnName().equals(columnName)){
					return true;
				}
			}
			
		}
		return false;
	}



	public static void main(String[] args){
		System.out.println("Testing sql");
		SemanticValidator2 sv = new SemanticValidator2();
		try {
			//Correct queries
			//sv.validateQuery("Select * from employee");
			//sv.validateQuery("Select * from employee,dependent where employee.emp_id = dependent.emp_id");
			//sv.validateQuery("Select * from employee group By emp_id");
			//sv.validateQuery("select * from employee where employee.emp_id = 1");
			//sv.validateQuery("Select SUM (salary) from employee");
			//sv.validateQuery("select * from employee where employee.sex='m'");
			//sv.validateQuery("select * from employee where employee.emp_id = 1 and employee.emp_id=2");
			//sv.validateQuery("select * from employee where employee.emp_id = 1 and employee.emp_id=2 or employee.emp_id=3");
			//sv.validateQuery("select * from employee where employee.emp_id = 1 and employee.emp_id=2 and employee.emp_id=3");
			//sv.validateQuery("select * from employee where emp_id = 1");
			//sv.validateQuery("select * from employee where employee.emp_id = 1 and employee.emp_id=2 and emp_id=3");
			
			//Incorrect queries
			//sv.validateQuery("select from employee");
			//sv.validateQuery("Select tname from employee");
			//sv.validateQuery("select * from employee where employee.tmp = 1");
			//sv.validateQuery("select tname from employee");
			//sv.validateQuery("select * from employee where employee.sex=1");
			//sv.validateQuery("Select SUM (slary) from employee");
			sv.validateQuery("select * from employee where employee.emp_id = 1 and employee.emp_id=2 and employee.tmp=3");
			//---
			
		} catch(ParseException pe){
			System.out.println("SQL statement is incorrect");
		}
			// TODO Auto-generated catch block
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	


	
}


