package com.iiit.adb.emp.dblayer.syscatalogue;

public class FragInfo {
	
	String tblName;
	int tblId;
	String type_of_frag;//deciders
	int fragNo;
	int siteId;
	String fragCondition;//horizontal
	String colNames;//vertical
	int dtblId;//derived
	int dfragNo;//derived
	
	public String getTblName() {
		return tblName;
	}
	public void setTblName(String tblName) {
		this.tblName = tblName;
	}
	public int getTblId() {
		return tblId;
	}
	public void setTblId(int tblId) {
		this.tblId = tblId;
	}
	public String getType_of_frag() {
		return type_of_frag;
	}
	public void setType_of_frag(String typeOfFrag) {
		type_of_frag = typeOfFrag;
	}
	public int getFragNo() {
		return fragNo;
	}
	public void setFragNo(int fragNo) {
		this.fragNo = fragNo;
	}
	public int getSiteId() {
		return siteId;
	}
	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}
	public String getFragCondition() {
		return fragCondition;
	}
	public void setFragCondition(String fragCondition) {
		this.fragCondition = fragCondition;
	}
	public String getColNames() {
		return colNames;
	}
	public void setColNames(String colNames) {
		this.colNames = colNames;
	}
	public int getDtblId() {
		return dtblId;
	}
	public void setDtblId(int dtblId) {
		this.dtblId = dtblId;
	}
	public int getDfragNo() {
		return dfragNo;
	}
	public void setDfragNo(int dfragNo) {
		this.dfragNo = dfragNo;
	}
}
