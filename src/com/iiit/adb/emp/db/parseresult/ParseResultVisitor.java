package com.iiit.adb.emp.db.parseresult;

public interface ParseResultVisitor {
	
	public void visit(SelectResult selectResult);
	
}