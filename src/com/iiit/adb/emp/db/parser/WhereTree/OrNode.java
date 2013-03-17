package com.iiit.adb.emp.db.parser.WhereTree;

public class OrNode implements WhereNode{
	private WhereNode parent;
	private WhereNode leftChild;
	private WhereNode rightChild;
	private int nodeType;
	public OrNode(){
		nodeType = 1;
	}
	@Override
	public boolean IsLeaf() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void accept(WhereNodeVisitor visitor) {
		// TODO Auto-generated method stub
		visitor.visit(this);
	}

	@Override
	public WhereNode getLeftChild() {
		// TODO Auto-generated method stub
		return leftChild;
	}

	@Override
	public String getNodeName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNodeType() {
		// TODO Auto-generated method stub
		return nodeType;
	}

	@Override
	public WhereNode getParent() {
		// TODO Auto-generated method stub
		return parent;
	}

	@Override
	public WhereNode getRightChild() {
		// TODO Auto-generated method stub
		return rightChild;
	}

	@Override
	public boolean isRoot() {
		// TODO Auto-generated method stub
		return parent == null;
	}

	@Override
	public void setLeftChild(WhereNode leftChild) {
		// TODO Auto-generated method stub
		this.leftChild = leftChild;
	}

	@Override
	public void setNodeName(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setParent(WhereNode parent) {
		// TODO Auto-generated method stub
		this.parent = parent;
	}

	@Override
	public void setRightChild(WhereNode rightChild) {
		// TODO Auto-generated method stub
		this.rightChild = rightChild;
	}
	@Override
	public void display() {
		// TODO Auto-generated method stub
		System.out.println("OR");
	}
	
}