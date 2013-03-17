package com.iiit.adb.emp.db.querytree;

public class LeafNode extends TreeNode{
	private String tableName;
	private String colname;
	private boolean b_Segment = false;
	
	@Override
	public void accept(TreeNodeVisitor visitor) {
		// TODO Auto-generated method stub
		visitor.visit(this);
	}

	@Override
	public boolean isLeaf() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public void setTableName(String name){
		tableName = name;
	}
	public String getTableName(){
		return tableName;
	}
	public boolean hasSegmented(){
		return b_Segment;
	}
	public void setSegment(boolean b){
		b_Segment = b;
	}

	@Override
	public String getNodeType() {
		// TODO Auto-generated method stub
		return "Leaf";
	}

	@Override
	public String getContent() {
		// TODO Auto-generated method stub
		return tableName;
	}

	public String getColname() {
		return colname;
	}

	public void setColname(String colname) {
		this.colname = colname;
	}

}