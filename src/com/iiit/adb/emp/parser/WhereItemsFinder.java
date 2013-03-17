package com.iiit.adb.emp.parser;

import com.iiit.adb.emp.db.parser.globaldefinition.*;


import java.util.ArrayList;

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


class ExpressionItem{
	int expressionType;//0 as column; 1 as operator; 2 as value
	String tableName;
	String columnName;
	String op;
	String valueType;
	String value;
}

public class WhereItemsFinder implements SelectVisitor, ExpressionVisitor{
	private ArrayList<JoinExpression> joinList;
	private ArrayList<SimpleExpression> selectionList;
	private ArrayList<ExpressionItem> itemList;
	private WhereTree whereTree = new WhereTree();
	public WhereItemsFinder(Select select){
		joinList = new ArrayList<JoinExpression>();
		selectionList = new ArrayList<SimpleExpression>();
		itemList = new ArrayList<ExpressionItem>();
		select.getSelectBody().accept(this);
		seperateList();
	}
	
	/*public WhereItemsFinder(Delete delete){
		joinList = new ArrayList<JoinExpression>();
		selectionList = new ArrayList<SimpleExpression>();
		itemList = new ArrayList<ExpressionItem>();
		delete.getWhere().accept(this);
		seperateList();
	}*/
	
	public ArrayList<JoinExpression> getJionList(){
		return joinList;
	}
	public ArrayList<SimpleExpression> getSelectionList(){
		return selectionList;
	}
	public ArrayList<ExpressionItem> getSelectionItemList(){
		return itemList;
	}
	private void seperateList(){
		for(int i=0;i<itemList.size();i+=3){
			ExpressionItem leftItem = itemList.get(i);
			ExpressionItem operator = itemList.get(i+1);
			ExpressionItem rightItem = itemList.get(i+2);
			if(leftItem.expressionType == 0 && rightItem.expressionType == 0){
				JoinExpression joinItem = new JoinExpression();
				joinItem.leftTableName = leftItem.tableName;
				joinItem.leftColumn = leftItem.columnName;
				joinItem.op = operator.op;
				joinItem.rightTableName = rightItem.tableName;
				joinItem.rigthColumn = rightItem.columnName;
				joinList.add(joinItem);
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
		ExpressionItem item = new ExpressionItem();
		item.expressionType = 2;
		item.valueType = "NULL";
		item.value = nullValue.toString();
		itemList.add(item);
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
		ExpressionItem item = new ExpressionItem();
		item.expressionType = 2;
		item.valueType = "double";
		item.value = doubleValue.toString();
		itemList.add(item);
	}

	@Override
	public void visit(LongValue longValue) {
		// TODO Auto-generated method stub
		//System.out.println(longValue.getValue());
		ExpressionItem item = new ExpressionItem();
		item.expressionType = 2;
		item.valueType = "long";
		item.value = longValue.getStringValue();
		itemList.add(item);
	}

	@Override
	public void visit(DateValue dateValue) {
		// TODO Auto-generated method stub
		ExpressionItem item = new ExpressionItem();
		item.expressionType = 2;
		item.valueType = "date";
		item.value = dateValue.toString();
		itemList.add(item);
	}

	@Override
	public void visit(TimeValue timeValue) {
		// TODO Auto-generated method stub
		ExpressionItem item = new ExpressionItem();
		item.expressionType = 2;
		item.valueType = "time";
		item.value = timeValue.toString();
		itemList.add(item);
	}

	@Override
	public void visit(TimestampValue timestampValue) {
		// TODO Auto-generated method stub
		ExpressionItem item = new ExpressionItem();
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
		ExpressionItem item = new ExpressionItem();
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
		ExpressionItem item = new ExpressionItem();
		item.expressionType = 1;
		item.op = "=";
		itemList.add(item);
		equalsTo.getRightExpression().accept(this);
	}

	@Override
	public void visit(GreaterThan greaterThan) {
		// TODO Auto-generated method stub
		greaterThan.getLeftExpression().accept(this);
		ExpressionItem item = new ExpressionItem();
		item.expressionType = 1;
		item.op = ">";
		itemList.add(item);
		greaterThan.getRightExpression().accept(this);
	}

	@Override
	public void visit(GreaterThanEquals greaterThanEquals) {
		// TODO Auto-generated method stub
		greaterThanEquals.getLeftExpression().accept(this);
		ExpressionItem item = new ExpressionItem();
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
		ExpressionItem item = new ExpressionItem();
		item.expressionType = 1;
		item.op = (isNullExpression.isNot()?"!=":"=");
		itemList.add(item);
		ExpressionItem item2 = new ExpressionItem();
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
		ExpressionItem item = new ExpressionItem();
		item.expressionType = 1;
		item.op = "<";
		itemList.add(item);
		minorThan.getRightExpression().accept(this);
	}

	@Override
	public void visit(MinorThanEquals minorThanEquals) {
		// TODO Auto-generated method stub
		minorThanEquals.getLeftExpression().accept(this);
		ExpressionItem item = new ExpressionItem();
		item.expressionType = 1;
		item.op = "<=";
		itemList.add(item);
		minorThanEquals.getRightExpression().accept(this);
	}

	@Override
	public void visit(NotEqualsTo notEqualsTo) {
		// TODO Auto-generated method stub
		notEqualsTo.getLeftExpression().accept(this);
		ExpressionItem item = new ExpressionItem();
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
		ExpressionItem item = new ExpressionItem();
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


}