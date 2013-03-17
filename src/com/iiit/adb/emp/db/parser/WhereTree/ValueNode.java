package com.iiit.adb.emp.db.parser.WhereTree;

public class ValueNode implements WhereNode{
	private WhereNode parent;
	private int nodeType;
	private String value;
	private String valueType;
	public ValueNode(){
		nodeType = 4;
	}
	public ValueNode(String v,String vType){
		value = v;
		valueType = vType;
		nodeType = 4;
	}
	@Override
	public boolean IsLeaf() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void accept(WhereNodeVisitor visitor) {
		// TODO Auto-generated method stub
		visitor.visit(this);
	}

	@Override
	public WhereNode getLeftChild() {
		// TODO Auto-generated method stub
		return null;
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
		return null;
	}

	@Override
	public boolean isRoot() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setLeftChild(WhereNode leftChild) {
		// TODO Auto-generated method stub
		
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
		
	}
	public String getValue(){
		return value;
	}
	public String getValueType(){
		return valueType;
	}
	public void setValue(String v){
		value = v;
	}
	public void setValueType(String vType){
		valueType = vType;
	}
	@Override
	public void display() {
		// TODO Auto-generated method stub
		System.out.println(valueType+": "+value);
	}
	
}
