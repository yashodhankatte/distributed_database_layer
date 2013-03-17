package com.iiit.adb.emp.db.querytree;

import java.util.HashMap;
import java.util.Iterator;

public class JoinNode extends TreeNode{
	//join attributes, such as t1.a = t2.b, then jionAttrList[a] = b;
	private HashMap<String,String> attributeList = new HashMap<String,String>();
	private String leftTableName = null;
	private String rightTableName = null;
	
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
	public void addAttribute(String left_attr,String right_attr){
		attributeList.put(left_attr, right_attr);
	}
	
	public void removeAttribute(String left_attr){
		if(attributeList.containsKey(left_attr))
			attributeList.remove(left_attr);
	}
	public HashMap<String,String> getAttributeList(){
		return attributeList;
	}

	@Override
	public String getNodeType() {
		// TODO Auto-generated method stub
		return "Join";
	}
	
	public void setLeftTableName(String lName){
		leftTableName = lName;
	}
	public void setRightTableName(String rName){
		rightTableName = rName;
	}
	public String getLeftTableName(){
		return leftTableName;
	}
	public String getRightTableName(){
		return rightTableName;
	}

	@Override
	public String getContent() {
		// TODO Auto-generated method stub
		String cnt = new String();
		cnt +="JOIN:";
		for (Iterator<String> i = attributeList.keySet().iterator(); i.hasNext();) { 
			String key = i.next(); 
			String value = attributeList.get(key); 
			cnt+=leftTableName+"."+key+"="+rightTableName+"."+value;
			if(i.hasNext())
				cnt += ", ";
		}
		return cnt;
	}

}