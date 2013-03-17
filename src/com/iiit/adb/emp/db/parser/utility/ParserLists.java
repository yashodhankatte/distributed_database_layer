package com.iiit.adb.emp.db.parser.utility;

import java.util.ArrayList;
import java.util.Iterator;

import com.iiit.adb.emp.db.parser.globaldefinition.FunctionParam;
import com.iiit.adb.emp.db.parser.globaldefinition.JoinExpression;
import com.iiit.adb.emp.db.parser.globaldefinition.SimpleExpression;

/**
 * this class colects all the parser output list for a query
 * @author Jyothi Gudavalli
 *
 */
public class ParserLists {
	
	ArrayList<SimpleExpression> selist= null;
	ArrayList<JoinExpression> jelist = null;
	ArrayList<TblColumn> collist= null;
	ArrayList<String> tbllist= null;
	ArrayList<TblColumn> groupbylist = null;
	ArrayList<FunctionParam> functionList = null;
	ArrayList<FunctionParam> hefList = null;
	ArrayList<SimpleExpression> hseList = null;
	ArrayList<JoinExpression> hjeList = null;
			
			
			public ArrayList<SimpleExpression> getSelist() {
				return selist;
			}
			public void setSelist(ArrayList<SimpleExpression> selist) {
				this.selist = selist;
			}
			public ArrayList<JoinExpression> getJelist() {
				return jelist;
			}
			public void setJelist(ArrayList<JoinExpression> jelist) {
				this.jelist = jelist;
			}
			public ArrayList<TblColumn> getCollist() {
				return collist;
			}
			public void setCollist(ArrayList<TblColumn> collist) {
				this.collist = collist;
			}
			public ArrayList<String> getTbllist() {
				return tbllist;
			}
			public void setTbllist(ArrayList<String> tbllist) {
				this.tbllist = tbllist;
			}
			public ParserLists(ArrayList<SimpleExpression> selist,
					ArrayList<JoinExpression> jelist,
					ArrayList<TblColumn> collist, ArrayList<String> tbllist,ArrayList<TblColumn> gblist,
					ArrayList<FunctionParam> flist,ArrayList<FunctionParam> heflist,ArrayList<SimpleExpression> hselist,ArrayList<JoinExpression> hjelist) {
				super();
				this.selist = selist;
				this.jelist = jelist;
				this.collist = collist;
				this.tbllist = tbllist;
				this.groupbylist = gblist;
				this.functionList = flist;
				this.hefList = heflist;
				this.hseList = hselist;
				this.hjeList = hjelist;
			}
			public ArrayList<TblColumn> getGroupbylist() {
				return groupbylist;
			}
			
			public void setGroupbylist(ArrayList<TblColumn> groupbylist) {
				this.groupbylist = groupbylist;
			}
			public ArrayList<FunctionParam> getHavingList() {
				return functionList;
			}
			public void setHavingList(ArrayList<FunctionParam> fList) {
				functionList = fList;
			}
			public ArrayList<FunctionParam> getFunctionList() {
				return functionList;
			}
			public void setFunctionList(ArrayList<FunctionParam> functionList) {
				this.functionList = functionList;
			}
			public ArrayList<FunctionParam> getHefList() {
				return hefList;
			}
			public void setHefList(ArrayList<FunctionParam> hefList) {
				this.hefList = hefList;
			}
			public ArrayList<SimpleExpression> getHseList() {
				return hseList;
			}
			public void setHseList(ArrayList<SimpleExpression> hseList) {
				this.hseList = hseList;
			}
			public ArrayList<JoinExpression> getHjeList() {
				return hjeList;
			}
			public void setHjeList(ArrayList<JoinExpression> hjeList) {
				this.hjeList = hjeList;
			}
			
}
