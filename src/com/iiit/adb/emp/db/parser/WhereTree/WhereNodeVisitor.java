package com.iiit.adb.emp.db.parser.WhereTree;

public interface WhereNodeVisitor {
	public void visit(AndNode andNode);
	public void visit(OrNode orNode);
	public void visit(AttrNode attrNode);
	public void visit(ValueNode valueNode);
	public void visit(OpNode opNode);
}
