package com.iiit.adb.emp.dblayer.syscatalogue;

public class Derived {
  
	int tblId;
	int fragNo;
	int siteId;
	int dtblId;
	int dfragNo;
	//keep table on which it is dependant??? also condition
	public int getTblId() {
		return tblId;
	}
	public void setTblId(int tblId) {
		this.tblId = tblId;
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
	
	// Jy: why we are not using fragment condition
}
