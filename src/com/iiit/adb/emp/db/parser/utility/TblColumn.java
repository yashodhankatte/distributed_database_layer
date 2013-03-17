package com.iiit.adb.emp.db.parser.utility;

/**
 * Class that holds info for table and column name 
 * when parsing column names from from clause and groub by clause
 * @author Jyothi
 *
 */
public class TblColumn {
    String tableName;
   	String columnName;
   	boolean isAllColumns = false;
   	
    public TblColumn(String tableName, String columnName) {
		super();
		this.tableName = tableName;
		this.columnName = columnName;
		if (this.columnName.trim().equals("*")){
			isAllColumns = true;
		}
	}

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

	public boolean isAllColumns() {
		// TODO Auto-generated method stub
		return isAllColumns;
	}

    
    
}
