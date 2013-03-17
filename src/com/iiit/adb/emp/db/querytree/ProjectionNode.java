package com.iiit.adb.emp.db.querytree;

import java.util.ArrayList;

public class ProjectionNode extends TreeNode{
	private ArrayList<String> attributeList = new ArrayList<String>();
	private ArrayList<String> tableNameList = new ArrayList<String>();
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
	
	public void addAttribute(String attr){
		attributeList.add(attr);
	}
	
	public void addTableName(String name){
		tableNameList.add(name);
	}
	public int getAttributeNum(){
		return attributeList.size();
	}
	
	public ArrayList<String> getAttributeList(){
		return attributeList;
	}
	@Override
	public String getNodeType() {
		// TODO Auto-generated method stub
		return "Projection";
	}

	@Override
	public String getContent() {
		// TODO Auto-generated method stub
		String cnt = new String();
		cnt += "Projection: ";
		for(int i=0;i<attributeList.size();++i){
			cnt += tableNameList.get(i)+"."+attributeList.get(i);
			if(i!= attributeList.size()-1)
				cnt+=", ";
		}
		return cnt;
	}

	public void setTableNameList(ArrayList<String> tableNameList) {
		this.tableNameList = tableNameList;
	}

	public ArrayList<String> getTableNameList() {
		return tableNameList;
	}

}