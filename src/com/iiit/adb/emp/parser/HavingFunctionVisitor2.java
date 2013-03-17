package com.iiit.adb.emp.parser;

import com.iiit.adb.emp.db.parser.globaldefinition.*;
import com.iiit.adb.emp.db.parser.utility.TblColumn;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.iiit.adb.emp.db.parser.WhereTree.*;

import net.sf.jsqlparser.expression.AllComparisonExpression;
import net.sf.jsqlparser.expression.AnyComparisonExpression;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.DateValue;
import net.sf.jsqlparser.expression.DoubleValue;
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
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.select.Union;


class HExpressionItem{
	int expressionType;//0 as column; 1 as operator; 2 as value ; 3 as function
	String tableName;
	String columnName;
	ArrayList<TblColumn> tblcols;
	String op;
	String valueType;
	String value;
	String fName; // only difference between where and Having
}

public class HavingFunctionVisitor2 implements SelectVisitor, ExpressionVisitor{
	private ArrayList<JoinExpression> joinList;
	private ArrayList<SimpleExpression> selectionList;
	private ArrayList<HExpressionItem> itemList;
	private ArrayList<FunctionParam> hitemList;
	//private WhereTree whereTree = new WhereTree();
	public HavingFunctionVisitor2(Select select){
		joinList = new ArrayList<JoinExpression>();
		selectionList = new ArrayList<SimpleExpression>();
		hitemList = new ArrayList<FunctionParam>();
		itemList = new ArrayList<HExpressionItem>();
		select.getSelectBody().accept(this);
		seperateList();
	}
	public HavingFunctionVisitor2(Delete delete){
		joinList = new ArrayList<JoinExpression>();
		selectionList = new ArrayList<SimpleExpression>();
		itemList = new ArrayList<HExpressionItem>();
		delete.getWhere().accept(this);
		seperateList();
	}
	public ArrayList<JoinExpression> getJionList(){
		return joinList;
	}
	public ArrayList<SimpleExpression> getSelectionList(){
		return selectionList;
	}
	public ArrayList<FunctionParam> getSelectionItemList(){
		return hitemList;
	}
	private void seperateList(){
		for(int i=0;i<itemList.size();i+=3){
			HExpressionItem leftItem = itemList.get(i);
			HExpressionItem operator = null;
			HExpressionItem rightItem = null;
			if (leftItem.expressionType != 3){
				 operator = itemList.get(i+1);
				 rightItem = itemList.get(i+2);
			}
			
			if(leftItem.expressionType == 0 && rightItem.expressionType == 0){
				JoinExpression joinItem = new JoinExpression();
				joinItem.leftTableName = leftItem.tableName;
				joinItem.leftColumn = leftItem.columnName;
				joinItem.op = operator.op;
				joinItem.rightTableName = rightItem.tableName;
				joinItem.rigthColumn = rightItem.columnName;
				joinList.add(joinItem);
			} else if (leftItem.expressionType == 3){
				// collect all the function parameters
				FunctionParam he = new FunctionParam();
				//TblColumn tc = new TblColumn(leftItem.tableName,leftItem.columnName);
				he.setTblcolumn(leftItem.tblcols);
				he.setFunctionname(leftItem.fName);
				
				hitemList.add(he);
				
			}
			else{
				SimpleExpression selectionItem = new SimpleExpression();
				selectionItem.tableName = leftItem.tableName;
				selectionItem.columnName = leftItem.columnName;
				selectionItem.op = operator.op;
				if(rightItem.valueType.equalsIgnoreCase("int")||rightItem.valueType.equalsIgnoreCase("long"))
					selectionItem.valueType = CONSTANT.VALUE_INT;
				else if(rightItem.valueType.equalsIgnoreCase("double"))
					selectionItem.valueType = CONSTANT.VALUE_DOUBLE;
				else if(rightItem.valueType.equalsIgnoreCase("string"))
					selectionItem.valueType = CONSTANT.VALUE_STRING;
				selectionItem.value = rightItem.value;
				selectionList.add(selectionItem);
			}
		}
	}
	
	@Override
	public void visit(PlainSelect plainSelect) {
		// TODO Auto-generated method stub
		if(plainSelect.getWhere()!=null)
			plainSelect.getWhere().accept(this);
	}

	@Override
	public void visit(Union union) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(NullValue nullValue) {
		// TODO Auto-generated method stub
		HExpressionItem item = new HExpressionItem();
		item.expressionType = 2;
		item.valueType = "NULL";
		item.value = nullValue.toString();
		itemList.add(item);
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
		HExpressionItem item = new HExpressionItem();
		item.expressionType = 2;
		item.valueType = "double";
		item.value = doubleValue.toString();
		itemList.add(item);
	}

	@Override
	public void visit(LongValue longValue) {
		// TODO Auto-generated method stub
		//System.out.println(longValue.getValue());
		HExpressionItem item = new HExpressionItem();
		item.expressionType = 2;
		item.valueType = "long";
		item.value = longValue.getStringValue();
		itemList.add(item);
	}

	@Override
	public void visit(DateValue dateValue) {
		// TODO Auto-generated method stub
		HExpressionItem item = new HExpressionItem();
		item.expressionType = 2;
		item.valueType = "date";
		item.value = dateValue.toString();
		itemList.add(item);
	}

	@Override
	public void visit(TimeValue timeValue) {
		// TODO Auto-generated method stub
		HExpressionItem item = new HExpressionItem();
		item.expressionType = 2;
		item.valueType = "time";
		item.value = timeValue.toString();
		itemList.add(item);
	}

	@Override
	public void visit(TimestampValue timestampValue) {
		// TODO Auto-generated method stub
		HExpressionItem item = new HExpressionItem();
		item.expressionType = 2;
		item.valueType = "timestamp";
		item.value = timestampValue.toString();
		itemList.add(item);
	}

	@Override
	public void visit(Parenthesis parenthesis) {
		// TODO Auto-generated method stub
		parenthesis.getExpression().accept(this);
	}

	@Override
	public void visit(StringValue stringValue) {
		// TODO Auto-generated method stub
		HExpressionItem item = new HExpressionItem();
		item.expressionType = 2;
		item.valueType = "string";
		item.value = stringValue.getValue();
		itemList.add(item);
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
		//System.out.println("TPYE:AND");
		//System.out.println(andExpression.getLeftExpression().toString());
		//System.out.println(andExpression.getRightExpression().toString());
		andExpression.getLeftExpression().accept(this);
		andExpression.getRightExpression().accept(this);
	}

	@Override
	public void visit(OrExpression orExpression) {
		// TODO Auto-generated method stub
		//System.out.println("TYPE:OR");
		orExpression.getLeftExpression().accept(this);
		orExpression.getRightExpression().accept(this);
	}

	@Override
	public void visit(Between between) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(EqualsTo equalsTo) {
		// TODO Auto-generated method stub
		//System.out.println("TPYE:=");
		equalsTo.getLeftExpression().accept(this);
		HExpressionItem item = new HExpressionItem();
		item.expressionType = 1;
		item.op = "=";
		itemList.add(item);
		equalsTo.getRightExpression().accept(this);
	}

	@Override
	public void visit(GreaterThan greaterThan) {
		// TODO Auto-generated method stub
		greaterThan.getLeftExpression().accept(this);
		HExpressionItem item = new HExpressionItem();
		item.expressionType = 1;
		item.op = ">";
		itemList.add(item);
		greaterThan.getRightExpression().accept(this);
	}

	@Override
	public void visit(GreaterThanEquals greaterThanEquals) {
		// TODO Auto-generated method stub
		greaterThanEquals.getLeftExpression().accept(this);
		HExpressionItem item = new HExpressionItem();
		item.expressionType = 1;
		item.op = ">=";
		itemList.add(item);
		greaterThanEquals.getRightExpression().accept(this);
	}

	@Override
	public void visit(InExpression inExpression) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(IsNullExpression isNullExpression) {
		// TODO Auto-generated method stub
		isNullExpression.getLeftExpression().accept(this);
		HExpressionItem item = new HExpressionItem();
		item.expressionType = 1;
		item.op = (isNullExpression.isNot()?"!=":"=");
		itemList.add(item);
		HExpressionItem item2 = new HExpressionItem();
		item2.expressionType = 2;
		item2.valueType = "NULL";
		item2.value = "NULL";
		itemList.add(item2);
		
	}

	@Override
	public void visit(LikeExpression likeExpression) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(MinorThan minorThan) {
		// TODO Auto-generated method stub
		//System.out.println("<");
		minorThan.getLeftExpression().accept(this);
		HExpressionItem item = new HExpressionItem();
		item.expressionType = 1;
		item.op = "<";
		itemList.add(item);
		minorThan.getRightExpression().accept(this);
	}

	@Override
	public void visit(MinorThanEquals minorThanEquals) {
		// TODO Auto-generated method stub
		minorThanEquals.getLeftExpression().accept(this);
		HExpressionItem item = new HExpressionItem();
		item.expressionType = 1;
		item.op = "<=";
		itemList.add(item);
		minorThanEquals.getRightExpression().accept(this);
	}

	@Override
	public void visit(NotEqualsTo notEqualsTo) {
		// TODO Auto-generated method stub
		notEqualsTo.getLeftExpression().accept(this);
		HExpressionItem item = new HExpressionItem();
		item.expressionType = 1;
		item.op = "<>";
		itemList.add(item);
		notEqualsTo.getRightExpression().accept(this);
	}

	@Override
	public void visit(Column tableColumn) {
		// TODO Auto-generated method stub
		//System.out.println("TYPE:table column");
		//System.out.println(tableColumn.getColumnName());
		//System.out.println(tableColumn.getTable().toString());
		HExpressionItem item = new HExpressionItem();
		item.expressionType = 0;
		item.tableName = tableColumn.getTable().toString();
		item.columnName = tableColumn.getColumnName();
		itemList.add(item);
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
	
	
	@Override
	public void visit(Function function) {
		// TODO Auto-generated method stub
		HExpressionItem item = new HExpressionItem();
		item.expressionType = 3;
	
		ArrayList<TblColumn> tblcollist = new ArrayList<TblColumn>();
		//FunctionParam fp = new FunctionParam();
		item.fName = function.getName();
		
		if (!function.isAllColumns()){
		List el = function.getParameters().getExpressions();
		ArrayList<String> al = new ArrayList();
		for (Iterator i= el.iterator();i.hasNext();){
		   al.add(i.next().toString());
		}
		String attributes = new String();
		  tblcollist = new ArrayList<TblColumn>();
			
			for (int i=0;i<al.size();i++){
				attributes += al.get(i)+((i<al.size())?",":"");
				int pos = al.get(i).indexOf(".");
				
				if(pos == -1){
					tblcollist.add(new TblColumn("null",al.get(i)));
					
				}
				else{
					String tableName = al.get(i).substring(0,pos);
					String attrName = al.get(i).substring(pos+1);
					tblcollist.add(new TblColumn(tableName,attrName));
				}
				
			}
		
		item.tblcols = tblcollist;
		} else {
			 tblcollist = new ArrayList<TblColumn>();
			 tblcollist.add(new TblColumn("null","*"));
				item.tblcols = tblcollist;
		}
		//List<Expression> elist = el.getExpressions();
		//flist.add(fp);
		
		itemList.add(item);
		//List<Expression> elist = el.getExpressions();
		
		
	}


}