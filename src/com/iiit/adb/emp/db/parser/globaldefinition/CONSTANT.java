package com.iiit.adb.emp.db.parser.globaldefinition;


/**
 * all the constants of the package are declared in this class
 * @author Jyothi
 *
 */
public class CONSTANT {
	public static final int SQL_INSERT = 1;
	public static final int SQL_SELECT = 2;
	public static final int SQL_DELETE = 3;
	public static final int SQL_CREATE = 4;
	public static final int SQL_IMPORTDATA = 5;
	public static final int SQL_V_FRAGMENT = 6;
	public static final int SQL_H_FRAGMENT = 7;
	public static final int SQL_ALLOCATE = 8;
	public static final int SQL_SETSITE = 9;
	public static final int SQL_PARSER_ERROR = 10;
	public static final int SQL_CREATEDATABASE = 11;
	public static final int SQL_USEDATABASE = 12;
	public static final int SQL_INIT = 13;
	
	public static final int VALUE_INT = 1;
	public static final int VALUE_STRING = 2;
	public static final int VALUE_DOUBLE = 3;
	
	
	public static final int TREE_SELECT = 1;
	public static final int TREE_INSERT = 2;
	public static final int TREE_DELETE = 3;
	
	public static final int FRAG_HORIZONTAL = 1;
	public static final int FRAG_VERTICAL = 2;
	public static final int FRAG_HYBIRD = 3;
	/**
	 * =
	 */
	public static final int OP_EQUAL = 1; 
	
	/**
	 * >
	 */
	public static final int OP_GREATERTHAN = 2; 
	
	/**
	 * >=
	 */
	public static final int OP_GREATERTHANEQUAL = 3; 
	
	/**
	 * <
	 */
	public static final int OP_MINORTHAN = 4;
	
	/**
	 * <=
	 */
	public static final int OP_MINORTHANEQUAL = 5;
	
	/*
	 * <> OR !=
	 */
	public static final int OP_NOTEQUAL = 6;
	
	public static final String[] DATATYPE = {"","integer","varchar",""};
	
}