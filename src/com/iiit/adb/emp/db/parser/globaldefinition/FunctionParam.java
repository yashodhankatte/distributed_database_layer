package com.iiit.adb.emp.db.parser.globaldefinition;

import java.util.ArrayList;

import com.iiit.adb.emp.db.parser.utility.TblColumn;

//
/**
 * Only for select function calls
 * this is incomplete in implementation
 * @author Jyothi
 *
 */
public class FunctionParam {
    ArrayList<TblColumn> tblcolumn;
   String functionname;
   

public String getFunctionname() {
	return functionname;
}
public void setFunctionname(String functionname) {
	this.functionname = functionname;
}
public ArrayList<TblColumn> getTblcolumn() {
	return tblcolumn;
}
public void setTblcolumn(ArrayList<TblColumn> tblcolumn) {
	this.tblcolumn = tblcolumn;
}

   
}
