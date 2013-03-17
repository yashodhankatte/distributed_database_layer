package com.iiit.adb.emp.db.parseresult;

import java.io.StringReader;
import java.util.ArrayList;

import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.parser.ParseException;
import net.sf.jsqlparser.statement.select.Select;

import com.iiit.adb.emp.db.parser.globaldefinition.CONSTANT;
import com.iiit.adb.emp.db.parser.globaldefinition.FunctionParam;
import com.iiit.adb.emp.db.parser.globaldefinition.JoinExpression;
import com.iiit.adb.emp.db.parser.globaldefinition.SimpleExpression;
import com.iiit.adb.emp.db.parser.utility.ParserLists;
import com.iiit.adb.emp.db.parser.utility.TblColumn;
import com.iiit.adb.emp.db.querytree.JoinNode;
import com.iiit.adb.emp.db.querytree.LeafNode;
import com.iiit.adb.emp.db.querytree.ProjectionNode;
import com.iiit.adb.emp.db.querytree.SelectionNode;

import com.iiit.adb.emp.parser.FunctionCallVisitor;
import com.iiit.adb.emp.parser.GroupByFinder;
import com.iiit.adb.emp.parser.HavingFunctionVisitor2;
import com.iiit.adb.emp.parser.SelectItemsFinder;
import com.iiit.adb.emp.parser.TableNamesFinder;
import com.iiit.adb.emp.parser.WhereItemsFinder;

public class SimpleParser{

	 ArrayList<SimpleExpression> hselectionList = null;
	 ArrayList<JoinExpression> hjelist = null;
	 ArrayList<FunctionParam> hfelist = null;
	 
	@SuppressWarnings("finally")
	public ArrayList<SimpleExpression> getWhereSimpleExpr(String sql)  throws Exception{
		ArrayList<SimpleExpression> selectionList = null;
		 try {
			 // if (DBConnection.conn1 != null){
			  //System.out.println("Connected to the database");
			  CCJSqlParserManager pm = new CCJSqlParserManager();
			 // String sql = "SELECT * FROM tbl,t1 where tbl.tbl_name=t1.name" ;
			  net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));
			  /* 
			  now you should use a class that implements StatementVisitor to decide what to do
			  based on the kind of the statement, that is SELECT or INSERT etc. but here we are only
			  interested in SELECTS
			  */
			  if (statement instanceof Select) {
			  	Select selectStatement = (Select) statement;
			  	/*------where clause----*/
				WhereItemsFinder finder = new WhereItemsFinder(selectStatement);
				int nodeID = 0;

				//ArrayList<SelectionNode> selections = new ArrayList<SelectionNode>();
				 selectionList = finder.getSelectionList();
				
				for(int i=0;i<selectionList.size();++i){
					//SelectionNode node3 = new SelectionNode();
					//node3.setNodeName("Selection");
					System.out.println("Conditon "+ selectionList.get(i));
					//node3.setNodeID(this.nodeID);
					System.out.println("TableName "+selectionList.get(i).tableName);
					System.out.println("ColumnName" + selectionList.get(i).columnName);
					System.out.println("Value" + selectionList.get(i).value);
					System.out.println("Value Type " + CONSTANT.DATATYPE[selectionList.get(i).valueType]);
					//selections.add(node3);
				}
			  }
		 }
		 catch (Exception e) {
			 throw new Exception(e);
		 } 
		 finally{
			return selectionList;
		 }
		
	}
	
	
	
	@SuppressWarnings("finally")
	public void getHavingLists(String sql)  throws Exception{
		ArrayList<SimpleExpression> selectionList = null;
		 try {
			 // if (DBConnection.conn1 != null){
			  //System.out.println("Connected to the database");
			  CCJSqlParserManager pm = new CCJSqlParserManager();
			 // String sql = "SELECT * FROM tbl,t1 where tbl.tbl_name=t1.name" ;
			  net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));
			  /* 
			  now you should use a class that implements StatementVisitor to decide what to do
			  based on the kind of the statement, that is SELECT or INSERT etc. but here we are only
			  interested in SELECTS
			  */
			  if (statement instanceof Select) {
			  	Select selectStatement = (Select) statement;
			  	/*------where clause----*/
				HavingFunctionVisitor2 finder = new HavingFunctionVisitor2(selectStatement);
				int nodeID = 0;

				//ArrayList<SelectionNode> selections = new ArrayList<SelectionNode>();
				 hselectionList = finder.getSelectionList();
				 hjelist = finder.getJionList();
				 hfelist = finder.getSelectionItemList();
				
				/*for(int i=0;i<selectionList.size();++i){
					//SelectionNode node3 = new SelectionNode();
					//node3.setNodeName("Selection");
					System.out.println("Conditon "+ selectionList.get(i));
					//node3.setNodeID(this.nodeID);
					System.out.println("TableName "+selectionList.get(i).tableName);
					System.out.println("ColumnName" + selectionList.get(i).columnName);
					System.out.println("Value" + selectionList.get(i).value);
					System.out.println("Value Type " + CONSTANT.DATATYPE[selectionList.get(i).valueType]);
					//selections.add(node3);
				}*/
			  }
		 }
		 catch (Exception e) {
			 throw new Exception(e);
		 } 
		
		
	}

	@SuppressWarnings("finally")
	public ArrayList<JoinExpression> getWhereJoinExpr(String sql){
		ArrayList<JoinExpression> joinList = null;
		 try {
			 // if (DBConnection.conn1 != null){
			  //System.out.println("Connected to the database");
			  CCJSqlParserManager pm = new CCJSqlParserManager();
			 // String sql = "SELECT * FROM tbl,t1 where tbl.tbl_name=t1.name" ;
			  net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));
			  /* 
			  now you should use a class that implements StatementVisitor to decide what to do
			  based on the kind of the statement, that is SELECT or INSERT etc. but here we are only
			  interested in SELECTS
			  */
			  if (statement instanceof Select) {
			  	Select selectStatement = (Select) statement;
			  	/*------where clause----*/
				WhereItemsFinder finder = new WhereItemsFinder(selectStatement);
				int nodeID = 0;

				//ArrayList<SelectionNode> selections = new ArrayList<SelectionNode>();
				joinList = finder.getJionList();
				
				for(int i=0;i<joinList.size();++i){
					SelectionNode node3 = new SelectionNode();
					//node3.setNodeName("Selection");
					System.out.println("Conditon "+ joinList.get(i));
					//node3.setNodeID(this.nodeID);
					System.out.println("LeftTableName "+joinList.get(i).leftTableName);
					System.out.println("RightTableName" + joinList.get(i).rightTableName);
					System.out.println("LeftColumn " + joinList.get(i).leftColumn);
					System.out.println("RightColumn " + joinList.get(i).rigthColumn);
					System.out.println("Operator " + joinList.get(i).op);
					
					//selections.add(node3);
				}
			  }
			 
			  } catch (Exception e) {
			  e.printStackTrace();
			  } finally{
					return joinList;
			  }
	}
	
@SuppressWarnings("finally")
public ArrayList<TblColumn> getColumnNames(String sql){
		
		ArrayList<TblColumn> tblcollist = null;
		
		 try {
			 // if (DBConnection.conn1 != null){
			  //System.out.println("Connected to the database");
			  CCJSqlParserManager pm = new CCJSqlParserManager();
			 // String sql = "SELECT * FROM tbl,t1 where tbl.tbl_name=t1.name" ;
			  net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));
			  /* 
			  now you should use a class that implements StatementVisitor to decide what to do
			  based on the kind of the statement, that is SELECT or INSERT etc. but here we are only
			  interested in SELECTS
			  */
			  if (statement instanceof Select) {
			  	Select selectStatement = (Select) statement;
			  	SelectItemsFinder finder = new SelectItemsFinder();
				ArrayList<String> selectItemsList = finder.getSelectItemsList(selectStatement);
				String attributes = new String();
			  tblcollist = new ArrayList<TblColumn>();
				
				for (int i=0;i<selectItemsList.size();i++){
					attributes += selectItemsList.get(i)+((i<selectItemsList.size())?",":"");
					int pos = selectItemsList.get(i).indexOf(".");
					
					if(pos == -1){
						tblcollist.add(new TblColumn("null",selectItemsList.get(i)));
						
					}
					else{
						String tableName = selectItemsList.get(i).substring(0,pos);
						String attrName = selectItemsList.get(i).substring(pos+1);
						tblcollist.add(new TblColumn(tableName,attrName));
					}
					
				}
			  } }catch (Exception e) {
				//throw new Exception
				  } finally{
						return tblcollist;
				  }
		
	
	}
	
	
	@SuppressWarnings("finally")
	public ArrayList<String> getTableNames(String sql){
		
		ArrayList<String> tableList = null;
		
		 try {
			 // if (DBConnection.conn1 != null){
			  //System.out.println("Connected to the database");
			  CCJSqlParserManager pm = new CCJSqlParserManager();
			 // String sql = "SELECT * FROM tbl,t1 where tbl.tbl_name=t1.name" ;
			  net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));
			  /* 
			  now you should use a class that implements StatementVisitor to decide what to do
			  based on the kind of the statement, that is SELECT or INSERT etc. but here we are only
			  interested in SELECTS
			  */
			  if (statement instanceof Select) {
			  	Select selectStatement = (Select) statement;
			  	ArrayList<LeafNode> leafs = new ArrayList<LeafNode>();
			  	TableNamesFinder finder = new TableNamesFinder();
			   tableList = (ArrayList<String>) finder.getTableList(selectStatement);
		
			  } }catch (Exception e) {
				  e.printStackTrace();
				  } finally{
						return tableList;
				  }
		
	
	}
	
	
@SuppressWarnings("finally")
public ArrayList<TblColumn> getGroubyColumnNames(String sql){
		
	ArrayList<TblColumn> tblcollist = null;
	
	 try {
		 // if (DBConnection.conn1 != null){
		  //System.out.println("Connected to the database");
		  CCJSqlParserManager pm = new CCJSqlParserManager();
		 // String sql = "SELECT * FROM tbl,t1 where tbl.tbl_name=t1.name" ;
		  net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));
		  /* 
		  now you should use a class that implements StatementVisitor to decide what to do
		  based on the kind of the statement, that is SELECT or INSERT etc. but here we are only
		  interested in SELECTS
		  */
		  if (statement instanceof Select) {
		  	Select selectStatement = (Select) statement;
		  	GroupByFinder finder = new GroupByFinder();
			ArrayList<String> selectItemsList = finder.getSelectItemsList(selectStatement);
			String attributes = new String();
		  tblcollist = new ArrayList<TblColumn>();
			
			for (int i=0;i<selectItemsList.size();i++){
				attributes += selectItemsList.get(i)+((i<selectItemsList.size())?",":"");
				int pos = selectItemsList.get(i).indexOf(".");
				
				if(pos == -1){
					tblcollist.add(new TblColumn("null",selectItemsList.get(i)));
					
				}
				else{
					String tableName = selectItemsList.get(i).substring(0,pos);
					String attrName = selectItemsList.get(i).substring(pos+1);
					tblcollist.add(new TblColumn(tableName,attrName));
				}
				
			}
		  } }catch (Exception e) {
			  e.printStackTrace();
			  } finally{
					return tblcollist;
			  }
	
	
	}
	
@SuppressWarnings("finally")
public ArrayList<FunctionParam> getFunctionExpr(String sql){
	ArrayList<FunctionParam> selectionList = null;
	 try {
			 // if (DBConnection.conn1 != null){
			  //System.out.println("Connected to the database");
			  CCJSqlParserManager pm = new CCJSqlParserManager();
			 // String sql = "SELECT * FROM tbl,t1 where tbl.tbl_name=t1.name" ;
			  net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));
			  /* 
			  now you should use a class that implements StatementVisitor to decide what to do
			  based on the kind of the statement, that is SELECT or INSERT etc. but here we are only
			  interested in SELECTS
			   */
			  if (statement instanceof Select) {
			  	Select selectStatement = (Select) statement;
			  	/*------where clause----*/
				FunctionCallVisitor finder = new FunctionCallVisitor();
				int nodeID = 0;
		
				//ArrayList<SelectionNode> selections = new ArrayList<SelectionNode>();
				 selectionList = finder.getFunctionParams(selectStatement);	
			  }
		  } 
	 	  catch (Exception e) {
		  // e.printStackTrace();
		  } 
	 	  finally{
				return selectionList;
		  }
}
	
	@SuppressWarnings("finally")
	public ParserLists getParerLists(String sql){

		ParserLists pl  = null;
	   try
		{
		   this.getHavingLists(sql);
		 pl = new ParserLists(this.getWhereSimpleExpr(sql),
					this.getWhereJoinExpr(sql),this.getColumnNames(sql),this.getTableNames(sql),
					this.getGroubyColumnNames(sql),this.getFunctionExpr(sql),this.hfelist,this.hselectionList,this.hjelist);
		
		} 
	   catch(ParseException e){
			throw new Exception(e);
		} 
	   finally {
			return pl;
		}
	}
	
	public static void main(String[] args){
		SimpleParser sp = new SimpleParser();
		//sp.getHavingSimpleExpr("SELECT Customer,SUM(OrderPrice) 
		//FROM Orders GROUP BY Customer HAVING SUM(OrderPrice)<2000");
	}
	
}
