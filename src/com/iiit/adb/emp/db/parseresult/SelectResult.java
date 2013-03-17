package com.iiit.adb.emp.db.parseresult;

import java.util.List;

import com.iiit.adb.emp.db.querytree.QueryTree;

import com.iiit.adb.emp.db.parser.globaldefinition.CONSTANT;

public class SelectResult extends ParseResult{
	private List tableNames;
	private QueryTree selectTree;
	public SelectResult(QueryTree tree){
		selectTree = tree;
	}
	public void setTableNames(List tableNames) {
		this.tableNames = tableNames;
	}
	public List getTableNames() {
		return tableNames;
	}
	@Override
	public void accept(ParseResultVisitor visitor) {
		// TODO Auto-generated method stub
		visitor.visit(this);
	}
	@Override
	public void displayResult() {
		// TODO Auto-generated method stub
		System.out.println("Select parse result:");
		selectTree.displayTree();
	}
	public void setSelectTree(QueryTree selectTree) {
		this.selectTree = selectTree;
	}
	public QueryTree getSelectTree() {
		return selectTree;
	}
	
}