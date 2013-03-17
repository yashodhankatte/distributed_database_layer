package com.iiit.adb.emp.parser;

import com.iiit.adb.emp.db.parser.globaldefinition.*;
import com.iiit.adb.emp.db.parseresult.ParseErrorResult;
import com.iiit.adb.emp.db.parseresult.ParseResult;
import com.iiit.adb.emp.db.parseresult.SelectResult;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;



import com.iiit.adb.emp.db.querytree.*;


import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.*;

import net.sf.jsqlparser.statement.create.table.ColDataType;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;

import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.drop.Drop;

import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.replace.Replace;
import net.sf.jsqlparser.statement.select.*;

import net.sf.jsqlparser.statement.truncate.Truncate;
import net.sf.jsqlparser.statement.update.Update;


public class SqlParser implements StatementVisitor{
	private ParseResult parseResult;
	private String sql;
	public SqlParser(String sql){
		this.setSql(sql);
		CCJSqlParserManager pm = new  CCJSqlParserManager();
		try{
			Statement st = pm.parse(new StringReader(sql));
			st.accept(this);
		}catch(JSQLParserException e){
			parseResult = new ParseErrorResult("Syntax error");
			//System.out.println();
			e.printStackTrace();
		}
	}
	@Override
	public void visit(Select select) {
		// TODO Auto-generated method stub
		System.out.println(select.toString());
		QueryTree selectTree = new QueryTree();
		selectTree.setTreeType(CONSTANT.TREE_SELECT);
		selectTree.genSelectTree(select);
		selectTree.setSql(select.toString());
		selectTree.displayTree();
		parseResult = new SelectResult(selectTree);
		
		
		try{
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("d:\\treeInfo.xml")),true);   
			
		}catch(Exception e){}
	}

	@Override
	public void visit(Replace replace) {
		// TODO Auto-generated method stub
		System.out.println("command 'RELPACE' is not supported!");
	}

	@Override
	public void visit(Drop drop) {
		// TODO Auto-generated method stub
		System.out.println("command 'DROP' is not supported!");
	}

	@Override
	public void visit(Truncate truncate) {
		// TODO Auto-generated method stub
		System.out.println("command 'TRUNCATE' is not supported!");
	}

	@Override
	public void visit(CreateTable createTable) {
		// TODO Auto-generated method stub
		System.out.println("create table parse successfully");
	//	parseResult = new CreateTableResult(createTable.getTable().getName(),createTable.getColumnDefinitions(),createTable.getTableOptionsStrings(),createTable.getIndexes());
	}

	
	
	@Override
	public void visit(Insert init) {
		// TODO Auto-generated method stub
		System.out.println("init parse successfully!");
		//parseResult = new InitResult(init.getFileName());
	}
	public void setResult(ParseResult result) {
		this.parseResult = result;
	}
	public ParseResult getResult() {
		return parseResult;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public String getSql() {
		return sql;
	}
	@Override
	public void visit(Delete arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(Update arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	public static void main(String[] args){
		SqlParser sp = new SqlParser("Select * from employee where employee.fname = employee.lname and employee.salary = 50000");
		
		
	}

}