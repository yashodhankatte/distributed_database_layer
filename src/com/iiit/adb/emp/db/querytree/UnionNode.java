package com.iiit.adb.emp.db.querytree;

public class UnionNode extends TreeNode{

	@Override
	public void accept(TreeNodeVisitor visitor) {
		// TODO Auto-generated method stub
		visitor.visit(this);
	}

	@Override
	public boolean isLeaf() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getNodeType() {
		// TODO Auto-generated method stub
		return "Union";
	}

	@Override
	public String getContent() {
		// TODO Auto-generated method stub
		return "Union";
	}
	
}
