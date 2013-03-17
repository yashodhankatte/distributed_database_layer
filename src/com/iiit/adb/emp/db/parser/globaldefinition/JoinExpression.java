package com.iiit.adb.emp.db.parser.globaldefinition;

/**
 * Used for JoinExpression in where and having
 * @author Jyothi
 *
 */
public class JoinExpression{
	public String leftTableName;
	public String rightTableName;
	public String leftColumn;
	public String rigthColumn;
	public String op;
}