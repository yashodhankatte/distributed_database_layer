package com.iiit.adb.emp.db.querytree;

import com.iiit.adb.emp.db.parser.globaldefinition.CONSTANT;
import com.iiit.adb.emp.db.parser.globaldefinition.SimpleExpression;

import java.util.ArrayList;
import java.util.List;

import net.sf.jsqlparser.expression.BinaryExpression;

public class SelectionNode extends TreeNode{
	private String tableName;
	private List<SimpleExpression> condList;
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
		return "Selection";
	}

	@Override
	public String getContent() {
		// TODO Auto-generated method stub
		String cnt = new String();
		cnt += "Selection: ";
		for(int i=0;i<condList.size();++i){
			cnt += condList.get(i).tableName+"."+condList.get(i).columnName+condList.get(i).op+condList.get(i).value;
			if(i<condList.size()-1)
				cnt +=" and ";
		}
		return cnt;
	}

	public void setCondList(List<SimpleExpression> condList) {
		this.condList = condList;
	}

	public List<SimpleExpression> getCondList() {
		return condList;
	}
	public void addConditon(SimpleExpression e){
		if(condList == null){
			condList = new ArrayList<SimpleExpression>();
		}
		condList.add(e);
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableName() {
		return tableName;
	}
}
