package com.iiit.adb.emp.db.querytree;

import java.util.ArrayList;



public abstract class TreeNode {
	private TreeNode parentNode = null;
	private ArrayList<TreeNode> childList = new ArrayList<TreeNode>();
	private int siteID = -1;
	private int nodeID = -1;
	private String nodeName = "";
	private boolean isRoot = false;
	public  TreeNode(){}
	abstract public void accept(TreeNodeVisitor visitor);
	//abstract public void accept(TreeNodeVisitor visitor);
	abstract public String getContent();
	public TreeNode(String name){
		nodeName = name;
	}
	public String getNodeName(){
		return nodeName;
	}
	public void setNodeName(String name){
		nodeName = name;
	}
	public int getSiteID(){
		return siteID;
	}
	public void setSiteID(int id){
		siteID = id;
	}
	public void addChild(TreeNode node){
		childList.add(node);
	}
	public TreeNode getChild(int index){
		return childList.get(index);
	}
	public int getChildCount(){
		return childList.size();
	}
	abstract public boolean isLeaf();
	public boolean isRoot(){
		return isRoot;
	}
	public void setRoot(){
		isRoot = true;
	}
	public TreeNode getParent(){
		return parentNode;
	}
	public void setParent(TreeNode parent){
		parentNode = parent;
		if(parentNode != null)
			parentNode.addChild(this);
	}
	public ArrayList<TreeNode> getChildList(){
		return childList;
	}
	public void removeChildAt(int index){
		TreeNode node = childList.get(index);
		childList.remove(index);
		node.setParent(null);
	}
	public int getChildIndex(TreeNode node){
		return childList.indexOf(node);
	}
	public void removeChildNode(TreeNode child){
		removeChildAt(getChildIndex(child));
	}
	public void displayNode(){
		System.out.println(nodeName);
	}
	abstract public String getNodeType();
	public void setNodeID(int nodeID) {
		this.nodeID = nodeID;
	}
	public int getNodeID() {
		return nodeID;
	}
}