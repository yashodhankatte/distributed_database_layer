package com.iiit.adb.emp.db.parser.globaldefinition;

/**
 * Used to handle simple expression in where tree generation and having
 * @author Jyothi
 *
 */

public class SimpleExpression{
	public String tableName;
	public String columnName;
	public String op;
	public String value; 
	public int valueType;
	public String valType;
	
	public SimpleExpression(){
		
	}
	
	/**
	 * @param tableName
	 * @param columnName
	 * @param op
	 * @param value
	 * @param valueType
	 */
	public SimpleExpression(String tableName,String columnName,String op,String value,int valueType){
		this.tableName = tableName;
		this.columnName = columnName;
		this.op = op;
		this.value = value;
		this.valueType = valueType;	
	}
	
	/**
	 * @param express
	 */
	public SimpleExpression(SimpleExpression express){
		if(express.tableName != null)
			this.tableName = express.tableName;
		else
			this.tableName = "NULL";
		
		if(express.columnName != null)
			this.columnName = express.columnName;
		else
			this.columnName = "NULL";
		this.op = express.op;
		this.value = express.value;
		this.valueType = express.valueType;
	}

	/**
	 * @return
	 */
	public String getValType() {
		return CONSTANT.DATATYPE[valueType];
	}

	/**
	 * @param valType
	 */
	public void setValType(String valType) {
		this.valType = valType;
	}
}