package com.iiit.adb.emp.dblayer.syscatalogue;

public class Tbl {
    String tblName;
    int tblId;
    String typeOfFrag;
    String colNames;
    String colTypes;
    String colConstraints;
    String primaryKeys;
    String fkeyConstraints;
    
    
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
	public String getTypeOfFrag() {
		return typeOfFrag;
	}
	public void setTypeOfFrag(String string) {
		this.typeOfFrag = string;
	}
	public String getColNames() {
		return colNames;
	}
	public void setColNames(String colNames) {
		this.colNames = colNames;
	}
	public String getColTypes() {
		return colTypes;
	}
	public void setColTypes(String colTypes) {
		this.colTypes = colTypes;
	}
	public String getColConstraints() {
		return colConstraints;
	}
	public void setColConstraints(String colConstraints) {
		this.colConstraints = colConstraints;
	}
	public String getPrimaryKeys() {
		return primaryKeys;
	}
	public void setPrimaryKeys(String primaryKeys) {
		this.primaryKeys = primaryKeys;
	}
	public String getFkeyConstraints() {
		return fkeyConstraints;
	}
	public void setFkeyConstraints(String fkeyConstraints) {
		this.fkeyConstraints = fkeyConstraints;
	}
	

    
}
