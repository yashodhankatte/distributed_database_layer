package com.iiit.adb.emp.db.querytree;

import com.iiit.adb.emp.db.gdd.GDD.GDD;
import com.iiit.adb.emp.db.parser.WhereTree.WhereNode;
import com.iiit.adb.emp.db.parser.WhereTree.WhereTree;
import com.iiit.adb.emp.db.parser.globaldefinition.*;


import java.util.ArrayList;
import java.util.List;

import com.iiit.adb.emp.parser.SelectItemsFinder;
import com.iiit.adb.emp.parser.TableNamesFinder;
import com.iiit.adb.emp.parser.WhereClauseDecomposition;
import com.iiit.adb.emp.parser.WhereItemsFinder;

import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;

public class QueryTree {
	public class FormattedTreeNode{
		public String content;
		public int nodeID;
		public int parentNodeID;
		public int siteID;
		public FormattedTreeNode(){
			content = new String();
			nodeID = -1;
			parentNodeID = -1;
			siteID = -1;
		}
	}
	private TreeNode root = null;
	private List<FormattedTreeNode> nodeList = null;
	private List<LeafNode> leafNodeList = null;
	private int nodeID;
	private int treeType;
	private String sql;
	public QueryTree(){
		setNodeID(0);
		setTreeType(0);
	}
	public TreeNode getRoot(){
		return root;
	}
	public void setRoot(TreeNode r){
		root = r;
	}
	public boolean isValidTree(){
		return !(root == null);
	}
	public void displayTree(){
		recDisplayTree(root,0,0);
	}
	private void recDisplayTree(TreeNode thisNode, int level, int childNumber){
		System.out.print("level="+level+" child="+childNumber+": ");
		System.out.print(thisNode.getContent());
		System.out.println(" siteID="+thisNode.getSiteID()+" nodeID="+thisNode.getNodeID());
		int childNum = thisNode.getChildCount();
		for(int i=0; i<childNum; ++i){
			TreeNode nextNode = thisNode.getChildList().get(i);
			if(nextNode != null){
				recDisplayTree(nextNode,level+1,i);
			}
			else
				return;
		}
	}
	public void setNodeList(List<FormattedTreeNode> nodeList) {
		this.nodeList = nodeList;
	}
	public List<FormattedTreeNode> getNodeList() {
		return nodeList;
	}
	public void genTreeList(){
		if (root == null) return;
		nodeList = new ArrayList<FormattedTreeNode>();
		genTreeListByNode(root);
	}
	private void genTreeListByNode(TreeNode node){
		FormattedTreeNode n = new FormattedTreeNode();
		n.content = node.getContent();
		n.nodeID = node.getNodeID();
		n.parentNodeID = ((node.getParent() == null)?-1:node.getParent().getNodeID());
		n.siteID = node.getSiteID();
		nodeList.add(n);
		if (node.isLeaf())
			return;
		for(int i=0;i<node.getChildCount();++i){
			genTreeListByNode(node.getChild(i));
		}
	}
	public void setNodeID(int nodeID) {
		this.nodeID = nodeID;
	}
	public int getNodeID() {
		return nodeID;
	}
	public void setTreeType(int treeType) {
		this.treeType = treeType;
	}
	public int getTreeType() {
		return treeType;
	}
	public void genSelectTree(Select select){
		this.treeType = CONSTANT.TREE_SELECT;
		
		/*------select clause----*/
		SelectItemsFinder finder2 = new SelectItemsFinder();
		ArrayList<String> selectItemsList = finder2.getSelectItemsList(select);
		String attributes = new String();
		ProjectionNode node = new ProjectionNode();
		node.setNodeName("Projection");
		node.setNodeID(-1);
		for (int i=0;i<selectItemsList.size();i++){
			attributes += selectItemsList.get(i)+((i<selectItemsList.size())?",":"");
			int pos = selectItemsList.get(i).indexOf(".");
			if(pos == -1){
				node.addTableName("null");
				node.addAttribute(selectItemsList.get(i));
			}
			else{
				String tableName = selectItemsList.get(i).substring(0,pos);
				String attrName = selectItemsList.get(i).substring(pos+1);
				node.addTableName(tableName);
				node.addAttribute(attrName);
			}
			
		}
		node.setRoot();
		node.setParent(null);
		node.setNodeID(this.nodeID);
		this.nodeID++;
		root = node;
		
		/*------from clause----*/
		ArrayList<LeafNode> leafs = new ArrayList<LeafNode>();
		TableNamesFinder finder = new TableNamesFinder();
		ArrayList<String> tableList = (ArrayList<String>) finder.getTableList(select);
		for (int i=0;i<tableList.size();++i){
			LeafNode node1 = new LeafNode();
			node1.setNodeName(tableList.get(i));
			node1.setTableName(tableList.get(i));
			node1.setSegment(false);
			node1.setNodeID(this.nodeID);
			this.nodeID++;
			leafs.add(node1);
		}
		
		// create wheretree
		// convert to cnf
		// get the simple and join expression and create joinnodes and selection nodes
		
		 WhereClauseDecomposition wc = new WhereClauseDecomposition(select);
	     WhereNode wn = wc.getWhereTree().toCNF(wc.getWhereTree().getRoot());
	     WhereTree wt = new WhereTree();
	     wt.setRoot(wn);
	     wt.collectJoins(wt.getRoot());
	     ArrayList<JoinExpression> joinList = wt.getJelist();
	     ArrayList<SimpleExpression> selectionList = wt.getSelist();
	     
		/*------where clause----*/
		
		WhereItemsFinder finder3 = new WhereItemsFinder(select);
		/*------join clause----*/
		ArrayList<JoinNode> joins = new ArrayList<JoinNode>();
		//ArrayList<JoinExpression> joinList = finder3.getJionList();
		for(int i=0;i<joinList.size();++i){
			JoinNode node2 = new JoinNode();
			node2.setLeftTableName(joinList.get(i).leftTableName);
			node2.setRightTableName(joinList.get(i).rightTableName);
			node2.addAttribute(joinList.get(i).leftColumn, joinList.get(i).rigthColumn);
			node2.setNodeName("Join");
			node2.setNodeID(this.nodeID);
			this.nodeID++;
			joins.add(node2);
			
		}
		
		/*-------selection clause-------*/	

		ArrayList<SelectionNode> selections = new ArrayList<SelectionNode>();
		//ArrayList<SimpleExpression> selectionList = finder3.getSelectionList();
		for(int i=0;i<selectionList.size();++i){
			SelectionNode node3 = new SelectionNode();
			node3.setNodeName("Selection");
			node3.addConditon(selectionList.get(i));
			node3.setNodeID(this.nodeID);
			node3.setTableName(selectionList.get(i).tableName);
			this.nodeID++;
			selections.add(node3);
		}
		
		//WhereClauseDecomposition p = new WhereClauseDecomposition(select);
		for(int i=0;i<joins.size();++i){
			JoinNode jnode = joins.get(i);
			TreeNode leftChild = findLeafNode(jnode.getLeftTableName(),leafs);
			TreeNode rightChild = findLeafNode(jnode.getRightTableName(),leafs);
			while(leftChild.getParent()!= null) leftChild = leftChild.getParent();
			while(rightChild.getParent()!=null) rightChild = rightChild.getParent();
			leftChild.setParent(jnode);
			rightChild.setParent(jnode);
		}
		
		for(int i=0;i<selections.size();++i){
			SelectionNode snode = selections.get(i);
			TreeNode child = findLeafNode(snode.getTableName(), leafs);
			while (child.getParent()!=null) child = child.getParent();
			child.setParent(snode);
		}
		
		TreeNode leaf1 = leafs.get(0);
		while(leaf1.getParent()!= null) leaf1 = leaf1.getParent();
		leaf1.setParent(root);
		
		
		/*
		for(int i = 0;i<leafs.size();++i)
		{
			localization(leafs.get(i));
		}
		
		setSiteIDOnNodes();
		*/
	}
	
	
	
	
	private LeafNode findLeafNode(String tableName,ArrayList<LeafNode> leafs){
		if (leafs.size() == 0) return null;
		for(int i=0;i<leafs.size();++i){
			if(leafs.get(i).getTableName().equalsIgnoreCase(tableName))
				return leafs.get(i);
		}
		return null;
	}
	private void localization(LeafNode leafNode){
		if(leafNode.hasSegmented()) return;
		UnionNode unionNode = new UnionNode();
		
		//setting the parent node
		unionNode.setParent(leafNode.getParent());
		leafNode.getParent().removeChildNode(leafNode);
		leafNode.setParent(null);
		unionNode.setNodeID(leafNode.getNodeID());
		String tableName = leafNode.getTableName();
		/*GDD gdd = GDD.getInstance();
		List<String> subTableList = (List<String>)gdd.getTableFragList(tableName);
		for(int i=0;i<subTableList.size();++i){
			int siteID = gdd.getSiteNumberofFragmentation(subTableList.get(i));
			LeafNode node = new LeafNode();
			node.setTableName(subTableList.get(i));
			node.setNodeName(subTableList.get(i));
			node.setSegment(true);
			node.setNodeID(this.nodeID);
			node.setSiteID(siteID);
			this.nodeID++;
			node.setParent(unionNode);
		}
			*/
	}
	private void setSiteIDOnNodes(){
		if( this.root == null) return;
		setSiteIDOnNodesByChild(root);
	}
	private int setSiteIDOnNodesByChild(TreeNode node){
		if(node.isLeaf()) return node.getSiteID();
		List<TreeNode> childList = node.getChildList();
		for(int i=0;i<childList.size();++i)
		{
			setSiteIDOnNodesByChild(childList.get(i));
		}
		node.setSiteID(setSiteIDOnNodesByChild(node.getChild(0)));
		return node.getSiteID();
	}
	public void setLeafNodeList(List<LeafNode> leafNodeList) {
		this.leafNodeList = leafNodeList;
	}
	public List<LeafNode> getLeafNodeList() {
		if (this.root == null) return null;
		leafNodeList = new ArrayList<LeafNode>();
		getLeafNodeList(root);
		return leafNodeList;
	}
	private void getLeafNodeList(TreeNode node){
		if(node.isLeaf()){ 
			leafNodeList.add((LeafNode)node);
			return;
		}
		for(int i=0;i<node.getChildCount();++i){
			getLeafNodeList(node.getChild(i));
		}
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public String getSql() {
		return sql;
	}
}