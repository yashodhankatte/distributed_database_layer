package com.iiit.adb.emp.parser;

import java.io.StringReader;

import com.iiit.adb.emp.db.parser.WhereTree.*;
import net.sf.jsqlparser.expression.AllComparisonExpression;
import net.sf.jsqlparser.expression.AnyComparisonExpression;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.DateValue;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.InverseExpression;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.TimeValue;
import net.sf.jsqlparser.expression.TimestampValue;
import net.sf.jsqlparser.expression.WhenClause;
import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseAnd;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseOr;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseXor;
import net.sf.jsqlparser.expression.operators.arithmetic.Concat;
import net.sf.jsqlparser.expression.operators.arithmetic.Division;
import net.sf.jsqlparser.expression.operators.arithmetic.Multiplication;
import net.sf.jsqlparser.expression.operators.arithmetic.Subtraction;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.Between;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.expression.operators.relational.Matches;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.select.Union;


public class WhereClauseDecomposition implements SelectVisitor, ExpressionVisitor{
	private WhereTree whereTree = new WhereTree();
	private WhereNode parentNode = null;
	
	public WhereClauseDecomposition(Select select){
		
		select.getSelectBody().accept(this);
		System.out.println("------where tree--------");
		whereTree.displayTree();
	}
	
   public WhereClauseDecomposition(String sql){
		
	   // for a string with open-close braces in the where loop
	   // do not do anything
	   String finalsql = null;
	   if (!( (sql.contains("(")) || (sql.contains(")")) )){
		   
	   
	   String temp = new String();
		 
	   // if sql do not have where, then no need to go inside this code
	   if (sql.contains("where")){
		  String wheresql = sql.substring(sql.indexOf("where") +5,sql.length());
		  
		 
			String[] temps = wheresql.split("and");
			for (int i=0;i<temps.length;i++){
				if (i != temps.length -1 ){
				temp = temp + "("+temps[i]+")" + " AND ";
				} else {
					temp = temp + "("+temps[i]+")" ;
				}
				
			}
		  
			 finalsql = sql.substring(0,sql.indexOf("where") -1)  + " where "+ temp;
	   } else {
		   finalsql = sql;
	   }
	   try{
			
				  CCJSqlParserManager pm = new CCJSqlParserManager();
				  net.sf.jsqlparser.statement.Statement statement2 = pm.parse
						  (new StringReader(finalsql));
					  	
				  if (statement2 instanceof Select){
						Select plainSelect = (Select) statement2;
				     WhereClauseDecomposition wc = new WhereClauseDecomposition(plainSelect);
				     this.whereTree = wc.whereTree;
				  }
				  
	   } catch(Exception e){
		   System.out.println("Incorrect SQL");
	   }
	   }	
	}
	
	public WhereTree getWhereClauseTree(){
		return whereTree;
	}
	
	@Override
	public void visit(PlainSelect plainSelect) {
		// TODO Auto-generated method stub
		//String temp = new String();
		if (plainSelect.getWhere()!=null){
			
			plainSelect.getWhere().accept(this);
		}
	}
	
	

	@Override
	public void visit(Union union) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(NullValue nullValue) {
		// TODO Auto-generated method stub
		ValueNode node = new ValueNode();
		node.setValue("NULL");
		node.setValueType("NULL");
		node.setParent(parentNode);
		if(parentNode!=null){
			if(parentNode.getLeftChild() == null)
				parentNode.setLeftChild(node);
			else
				parentNode.setRightChild(node);
		}
	}

	@Override
	public void visit(Function function) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(InverseExpression inverseExpression) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(JdbcParameter jdbcParameter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DoubleValue doubleValue) {
		// TODO Auto-generated method stub
		ValueNode node = new ValueNode();
		node.setValue(doubleValue.toString());
		node.setValueType("double");
		node.setParent(parentNode);
		if(parentNode!=null){
			if(parentNode.getLeftChild() == null)
				parentNode.setLeftChild(node);
			else
				parentNode.setRightChild(node);
		}
	}

	@Override
	public void visit(LongValue longValue) {
		// TODO Auto-generated method stub
		ValueNode node = new ValueNode();
		node.setValue(longValue.toString());
		node.setValueType("long");
		node.setParent(parentNode);
		if(parentNode!=null){
			if(parentNode.getLeftChild() == null)
				parentNode.setLeftChild(node);
			else
				parentNode.setRightChild(node);
		}
		
	}

	@Override
	public void visit(DateValue dateValue) {
		// TODO Auto-generated method stub
		ValueNode node = new ValueNode();
		node.setValue(dateValue.toString());
		node.setValueType("date");
		node.setParent(parentNode);
		if(parentNode!=null){
			if(parentNode.getLeftChild() == null)
				parentNode.setLeftChild(node);
			else
				parentNode.setRightChild(node);
		}
	}

	@Override
	public void visit(TimeValue timeValue) {
		// TODO Auto-generated method stub
		ValueNode node = new ValueNode();
		node.setValue(timeValue.toString());
		node.setValueType("time");
		node.setParent(parentNode);
		if(parentNode!=null){
			if(parentNode.getLeftChild() == null)
				parentNode.setLeftChild(node);
			else
				parentNode.setRightChild(node);
		}
	}

	@Override
	public void visit(TimestampValue timestampValue) {
		// TODO Auto-generated method stub
		ValueNode node = new ValueNode();
		node.setValue(timestampValue.toString());
		node.setValueType("timestamp");
		node.setParent(parentNode);
		if(parentNode!=null){
			if(parentNode.getLeftChild() == null)
				parentNode.setLeftChild(node);
			else
				parentNode.setRightChild(node);
		}
	}

	@Override
	public void visit(Parenthesis parenthesis) {
		// TODO Auto-generated method stub
		parenthesis.getExpression().accept(this);
	}

	@Override
	public void visit(StringValue stringValue) {
		// TODO Auto-generated method stub
		ValueNode node = new ValueNode();
		node.setValue(stringValue.toString());
		node.setValueType("string");
		node.setParent(parentNode);
		if(parentNode!=null){
			if(parentNode.getLeftChild() == null)
				parentNode.setLeftChild(node);
			else
				parentNode.setRightChild(node);
		}
	}

	@Override
	public void visit(Addition addition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Division division) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Multiplication multiplication) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Subtraction subtraction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AndExpression andExpression) {
		// TODO Auto-generated method stub
		AndNode node = new AndNode();
		if(whereTree.getRoot() == null){
			whereTree.setRoot(node);
		}
		node.setParent(parentNode);
		if(parentNode!=null){
			if(parentNode.getLeftChild() == null)
				parentNode.setLeftChild(node);
			else
				parentNode.setRightChild(node);
		}
		parentNode = node;
		andExpression.getLeftExpression().accept(this);
		parentNode = node;
		andExpression.getRightExpression().accept(this);
	}

	@Override
	public void visit(OrExpression orExpression) {
		// TODO Auto-generated method stub
		OrNode node = new OrNode();
		if(whereTree.getRoot() == null){
			whereTree.setRoot(node);
		}
		node.setParent(parentNode);
		if(parentNode!=null){
			if(parentNode.getLeftChild() == null)
				parentNode.setLeftChild(node);
			else
				parentNode.setRightChild(node);
		}
		parentNode = node;
		orExpression.getLeftExpression().accept(this);
		parentNode = node;
		orExpression.getRightExpression().accept(this);
	}

	@Override
	public void visit(Between between) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(EqualsTo equalsTo) {
		// TODO Auto-generated method stub
		OpNode node = new OpNode();
		node.setOp("=");
		if(whereTree.getRoot() == null){
			whereTree.setRoot(node);
		}
		node.setParent(parentNode);
		if(parentNode!=null){
			if(parentNode.getLeftChild() == null)
				parentNode.setLeftChild(node);
			else
				parentNode.setRightChild(node);
		}
		parentNode = node;
		equalsTo.getLeftExpression().accept(this);
		parentNode = node;
		equalsTo.getRightExpression().accept(this);
	}

	@Override
	public void visit(GreaterThan greaterThan) {
		// TODO Auto-generated method stub
		OpNode node = new OpNode();
		node.setOp(">");
		if(whereTree.getRoot() == null){
			whereTree.setRoot(node);
		}
		node.setParent(parentNode);
		if(parentNode!=null){
			if(parentNode.getLeftChild() == null)
				parentNode.setLeftChild(node);
			else
				parentNode.setRightChild(node);
		}
		parentNode = node;
		greaterThan.getLeftExpression().accept(this);
		parentNode = node;
		greaterThan.getRightExpression().accept(this);
	}

	@Override
	public void visit(GreaterThanEquals greaterThanEquals) {
		// TODO Auto-generated method stub
		OpNode node = new OpNode();
		node.setOp(">=");
		if(whereTree.getRoot() == null){
			whereTree.setRoot(node);
		}
		node.setParent(parentNode);
		if(parentNode!=null){
			if(parentNode.getLeftChild() == null)
				parentNode.setLeftChild(node);
			else
				parentNode.setRightChild(node);
		}
		parentNode = node;
		greaterThanEquals.getLeftExpression().accept(this);
		parentNode = node;
		greaterThanEquals.getRightExpression().accept(this);
	}

	@Override
	public void visit(InExpression inExpression) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(IsNullExpression isNullExpression) {
		// TODO Auto-generated method stub
		OpNode node = new OpNode();
		if (isNullExpression.isNot())
			node.setOp("<>");
		else
			node.setOp("=");
		if(whereTree.getRoot() == null){
			whereTree.setRoot(node);
		}
		node.setParent(parentNode);
		if(parentNode!=null){
				parentNode.setLeftChild(node);
		}
		parentNode = node;
		isNullExpression.getLeftExpression().accept(this);
		parentNode = node;
		ValueNode nullNode = new ValueNode();
		nullNode.setValue("NULL");
		nullNode.setValueType("NULL");
		parentNode.setRightChild(nullNode);
		
	}

	@Override
	public void visit(LikeExpression likeExpression) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(MinorThan minorThan) {
		// TODO Auto-generated method stub
		OpNode node = new OpNode();
		node.setOp("<");
		if(whereTree.getRoot() == null){
			whereTree.setRoot(node);
		}
		node.setParent(parentNode);
		if(parentNode!=null){
			if(parentNode.getLeftChild() == null)
				parentNode.setLeftChild(node);
			else
				parentNode.setRightChild(node);
		}
		parentNode = node;
		minorThan.getLeftExpression().accept(this);
		parentNode = node;
		minorThan.getRightExpression().accept(this);
	}

	@Override
	public void visit(MinorThanEquals minorThanEquals) {
		// TODO Auto-generated method stub
		OpNode node = new OpNode();
		node.setOp("<=");
		if(whereTree.getRoot() == null){
			whereTree.setRoot(node);
		}
		node.setParent(parentNode);
		if(parentNode!=null){
			if(parentNode.getLeftChild() == null)
				parentNode.setLeftChild(node);
			else
				parentNode.setRightChild(node);
		}
		parentNode = node;
		minorThanEquals.getLeftExpression().accept(this);
		parentNode = node;
		minorThanEquals.getRightExpression().accept(this);
	}

	@Override
	public void visit(NotEqualsTo notEqualsTo) {
		// TODO Auto-generated method stub
		OpNode node = new OpNode();
		node.setOp("<>");
		if(whereTree.getRoot() == null){
			whereTree.setRoot(node);
		}
		node.setParent(parentNode);
		if(parentNode!=null){
			if(parentNode.getLeftChild() == null)
				parentNode.setLeftChild(node);
			else
				parentNode.setRightChild(node);
		}
		parentNode = node;
		notEqualsTo.getLeftExpression().accept(this);
		parentNode = node;
		notEqualsTo.getRightExpression().accept(this);
		
	}

	@Override
	public void visit(Column tableColumn) {
		// TODO Auto-generated method stub
		AttrNode node = new AttrNode();
		node.setAttrName(tableColumn.getColumnName());
		node.setTableName(tableColumn.getTable().getName());
		node.setParent(parentNode);
		if(parentNode!=null){
			if(parentNode.getLeftChild() == null)
				parentNode.setLeftChild(node);
			else
				parentNode.setRightChild(node);
		}
	}

	@Override
	public void visit(SubSelect subSelect) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(CaseExpression caseExpression) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(WhenClause whenClause) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ExistsExpression existsExpression) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AllComparisonExpression allComparisonExpression) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AnyComparisonExpression anyComparisonExpression) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(Concat arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(Matches arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(BitwiseAnd arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(BitwiseOr arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(BitwiseXor arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	public static void main(String[] args){
		try{
			  //CCJSqlParserManager pm = new CCJSqlParserManager();
				 // String sql = "SELECT * FROM tbl,t1 where tbl.tbl_name=t1.name" ;
			  String sql = "select * from t1 where t1.a = t1.b or t1.c = t1.d and t1.c =\"dd\"";
				
			  try{
					
				  CCJSqlParserManager pm = new CCJSqlParserManager();
				  net.sf.jsqlparser.statement.Statement statement2 = pm.parse
						  (new StringReader(sql));
					  	
				  if (statement2 instanceof Select){
						Select plainSelect = (Select) statement2;
				     WhereClauseDecomposition wc = new WhereClauseDecomposition(plainSelect);
				     WhereNode wn = wc.whereTree.toCNF(wc.whereTree.getRoot());
				     WhereTree wt = new WhereTree();
				     wt.setRoot(wn);
				     
				     System.out.println("My new WhereTree");
				     wt.displayTree();
				  //   this.whereTree = wc.whereTree;
				  }
				  
	   } catch(Exception e){
		   System.out.println("Incorrect SQL");
	   }
				 
				  
				  
				  
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public WhereTree getWhereTree() {
		return whereTree;
	}

	public void setWhereTree(WhereTree whereTree) {
		this.whereTree = whereTree;
	}

}
