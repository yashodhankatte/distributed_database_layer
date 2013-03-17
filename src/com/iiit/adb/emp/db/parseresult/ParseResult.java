package com.iiit.adb.emp.db.parseresult;



import com.iiit.adb.emp.db.parser.WhereTree.WhereNodeVisitor;

public abstract class ParseResult {
	private int sqlType;
	public abstract void accept(ParseResultVisitor visitor);
	public abstract void displayResult();
	public int getSqlType() {
		return sqlType;
	}

	public void setSqlType(int sqlType) {
		this.sqlType = sqlType;
	}
	
}