package com.iiit.adb.emp.parser;

import java.util.ArrayList;
import java.util.List;

import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.Union;

public class GroupByFinder implements SelectVisitor{

	ArrayList<String> groupBycolumns;
	
	public ArrayList<String> getSelectItemsList(Select select){
		groupBycolumns = new ArrayList<String>();
		select.getSelectBody().accept(this);

		return groupBycolumns;
	}
	
	
	@Override
	public void visit(PlainSelect plainSelect) {
		// TODO Auto-generated method stub
		//selectItemsList.addAll((ArrayList<String>) plainSelect.getSelectItems());
		List list = plainSelect.getGroupByColumnReferences();
		if (list != null){
		for(int i=0;i<list.size();++i){
			String ans = "" + list.get(i);
			groupBycolumns.add(ans);
		}
		}
		//String aa = PlainSelect.getStringList(plainSelect.getSelectItems());
		//System.out.println(aa);
		//selectItemsList.add("aa");
		
	}

	@Override
	public void visit(Union arg0) {
		// TODO Auto-generated method stub
		
	}


}
