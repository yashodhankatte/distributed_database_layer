package com.iiit.adb.emp.db.parser.globaldefinition;

/**
 * Used for HavingExpression
 * @author Jyothi
 *
 */
public class HavingExpression {

	public String tableName;
	public String columnName;
	public String op;
	public int valueType;
	public String value;
	public String functionCall;
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getFunctionCall() {
		return functionCall;
	}
	public void setFunctionCall(String functionCall) {
		this.functionCall = functionCall;
	}
}
