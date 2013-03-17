package com.iiit.adb.emp.db.parser.WhereTree;

public interface WhereNode {
	//visitor parttern
	public void accept(WhereNodeVisitor visitor);
	//return the parent node
	public WhereNode getParent();
	//set the parent node
	public void setParent(WhereNode parent);
	
	//get the left child 
	public WhereNode getLeftChild();
	
	//set the left child
	public void setLeftChild(WhereNode leftChild);
	
	//get the right child
	public WhereNode getRightChild();
	
	//set the right child
	public void setRightChild(WhereNode rightChild);

	public boolean isRoot();
	
	//if leaf,return true;else false
	public boolean IsLeaf();
	
	public String getNodeName();
	
	public void setNodeName(String name);
	
	/**
	 * Node type: 0 as AND, 1 as OR ,2 as op(=,>,<,>=,<>,<=), 3 as table attribute,4 as value
	 * @param type
	 */
	public int getNodeType();
	
	public void display();
}