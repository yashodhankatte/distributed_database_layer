package com.iiit.adb.emp.db.parser.localization;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import com.iiit.adb.emp.db.DBConnection;
import com.iiit.adb.emp.dblayer.syscatalogue.*;

public class LocalizationDataFetch {
	//Done replaced getsite name as name is not required as here we are going to use number only as name if number is 1 site name is site1
	// as we have not given name to sites.
	public int getSiteNumberofFragmentation(String fragName){
		//for (Iterator i = pl.getTbllist().iterator();i.hasNext() && (flag== true);){
		String table = fragName.replaceAll("\\d*", "");
		int frag_no = Integer.parseInt(fragName.replaceAll("\\D",""));
		int id = 0 ;
		String type_of_frag = new String();
		for(int i=0;i<tbl.size();i++)
			if(tbl.get(i) != null && tbl.get(i).getTblName().equalsIgnoreCase(table)) {
				id = tbl.get(i).getTblId();
				type_of_frag = tbl.get(i).getTypeOfFrag();
			}
			if(type_of_frag.matches("n")) {
				for(int i=0;i<nofrag.size();i++)
					if(nofrag.get(i) != null && nofrag.get(i).getTblId() == id) {
						return nofrag.get(i).getSiteId();
					}
			}
			else if(type_of_frag.matches("h")) {
				for(int i=0;i<horizontal.size();i++)
					if(horizontal.get(i) != null && horizontal.get(i).getTblId() == id && 
							horizontal.get(i).getFragNo() == frag_no) {
						return horizontal.get(i).getSiteId();
					}
			}
			else if(type_of_frag.matches("v")) {
				for(int i=0;i<vertical.size();i++)
					if(vertical.get(i) != null && vertical.get(i).getTblId() == id && 
							vertical.get(i).getFragNo() == frag_no) {
						return vertical.get(i).getSiteId();
					}
			}
			else {
				for(int i=0;i<derived.size();i++)
					if(derived.get(i) != null && derived.get(i).getTblId() == id && 
							derived.get(i).getFragNo() == frag_no) {
						return derived.get(i).getSiteId();
					}
			}
		return 0;// or we can raise exception
	}
	
	// should return table fragment list for a particular table
	public ArrayList<FragInfo> getTableFragList(String tableName){
		ArrayList<FragInfo> fraglist = new ArrayList<FragInfo>();
		FragInfo f = null; //new FragInfo();
		int tblId = 0 ;
		String type_of_frag = new String();
		for(int i=0;i<tbl.size();i++)
			if(tbl.get(i) != null && tbl.get(i).getTblName().equalsIgnoreCase(tableName)) {
				tblId = tbl.get(i).getTblId();
				type_of_frag = tbl.get(i).getTypeOfFrag();
			}
		//---------------------------------------
		//no frag
		if(f.getType_of_frag().equalsIgnoreCase("n")) {
			for(int i=0;i<nofrag.size();i++) {
				if(nofrag.get(i).getTblId()== tblId) {
					f.setTblName(tableName);
					f.setTblId(tblId);
					f.setType_of_frag(type_of_frag);
					f.setSiteId(nofrag.get(i).getSiteId());
				}
				fraglist.add(f);
			}
			return fraglist;
		}//end of no frag (returns only one entry in arraylist
		//horizontal
		else if (f.getType_of_frag().equalsIgnoreCase("h")){
			for(int i=0;i<horizontal.size();i++) {
				if(horizontal.get(i).getTblId()== tblId) {
					f.setTblName(tableName);
					f.setTblId(tblId);
					f.setType_of_frag(type_of_frag);
					f.setSiteId(horizontal.get(i).getSiteId());
					f.setFragCondition(horizontal.get(i).getFragCondition());
					fraglist.add(f);
				}
			}
			return fraglist;
		}
		//vertical
		else if (f.getType_of_frag().equalsIgnoreCase("v")) {
			for(int i=0;i<vertical.size();i++) {
				if(vertical.get(i).getTblId()== tblId) {
					f.setTblName(tableName);
					f.setTblId(tblId);
					f.setType_of_frag(type_of_frag);
					f.setSiteId(vertical.get(i).getSiteId());
					f.setColNames(vertical.get(i).getColNames());
					fraglist.add(f);
				}
			}
			return fraglist;
		}
		//derived
		else if (f.getType_of_frag().equalsIgnoreCase("d")) {
			for(int i=0;i<derived.size();i++) {
				if(derived.get(i).getTblId()== tblId) {
					f.setTblName(tableName);
					f.setTblId(tblId);
					f.setType_of_frag(type_of_frag);
					f.setSiteId(derived.get(i).getSiteId());
					f.setDtblId(derived.get(i).getDtblId());
					f.setDfragNo(derived.get(i).getDfragNo());
					fraglist.add(f);
				}
			}
			return fraglist;
		}
		
		return fraglist;
	}
	
	static ArrayList<Tbl>  tbl = new ArrayList<Tbl>();
	static ArrayList<SiteInfo>  site = new ArrayList<SiteInfo>();
	static ArrayList<NoFrag>  nofrag = new ArrayList<NoFrag>();
	static ArrayList<Horizontal>  horizontal = new ArrayList<Horizontal>();
	static ArrayList<Vertical>  vertical = new ArrayList<Vertical>();
	static ArrayList<Derived>  derived = new ArrayList<Derived>();
	public static void main(String[] args)
	{
		Connection conn = (new DBConnection()).getSite1Conn();
	//tbl			
		String sql = "select * from tbl";
	//	ArrayList<Tbl>  tbl = new ArrayList<Tbl>();
		try {
		Statement st = conn.createStatement();
		ResultSet res = st.executeQuery(sql);
		  
		while (res.next()) {
				Tbl t= new Tbl();
				t. setTblName(res.getString("tbl_name"));
				t.setTblId(Integer.parseInt(res.getString("tbl_id")));
				t.setTypeOfFrag(res.getString("type_of_frag"));
				t.setColNames(res.getString("col_names"));
				t.setColTypes(res.getString("col_types"));
				t.setPrimaryKeys(res.getString("primary_keys"));
				t.setFkeyConstraints(res.getString("foreign_key_constraints"));
				// we can set other also in future
				tbl.add(t);
		    }
		}
		catch (SQLException s){
			System.out.println("SQL code does not execute.1");
		}  
	//site			
		sql = "select * from site_info";
	//	ArrayList<SiteInfo>  site = new ArrayList<SiteInfo>();
		try {
		Statement st = conn.createStatement();
		ResultSet res = st.executeQuery(sql);
			
			while (res.next()) {
					SiteInfo s= new SiteInfo();
					s.setSiteId(Integer.parseInt(res.getString("site_id")));
					s.setUser_id(res.getString("user_id"));
					s.setPassword(res.getString("password"));
					s.setIp(res.getString("ip"));
					s.setPort(res.getString("port"));
					s.setDb(res.getString("db"));
					// we can set other also in future
					site.add(s);
			    }
		}
		catch (SQLException s){
			System.out.println("SQL code does not execute.2");
		}  
		
		
	//fragment	
		//nofrar
		sql = "select * from nofrag";
	//	ArrayList<NoFrag>  nofrag = new ArrayList<NoFrag>();
		try {
		Statement st = conn.createStatement();
		ResultSet res = st.executeQuery(sql);
		  
		while (res.next()) {
			NoFrag n= new NoFrag();
				n.setTblId(res.getInt("tbl_id"));
				n.setSiteId(res.getInt("site_id"));
				// we can set other also in future
				nofrag.add(n);
		    }
		}
		catch (SQLException s){
			System.out.println("SQL code does not execute.");
		}  
		//horizontal
		sql = "select * from horizontal";
	//	ArrayList<Horizontal>  horizontal = new ArrayList<Horizontal>();
		try {
		Statement st = conn.createStatement();
		ResultSet res = st.executeQuery(sql);
		  
		while (res.next()) {
				Horizontal h= new Horizontal();
				h.setTblId(res.getInt("tbl_id"));
				h.setFragNo(res.getInt("frag_no"));
				h.setSiteId(res.getInt("site_id"));
				h.setFragCondition(res.getString("frag_cond"));
				// we can set other also in future
				horizontal.add(h);
		    }
		}
		catch (SQLException s){
			System.out.println("SQL code does not execute.");
		}  
		//vertical			
		sql = "select * from vertical";
	//	ArrayList<Vertical>  vertical = new ArrayList<Vertical>();
		try {
		Statement st = conn.createStatement();
		ResultSet res = st.executeQuery(sql);
		  
		while (res.next()) {
				Vertical v= new Vertical();
				v.setTblId(res.getInt("tbl_id"));
				v.setFragNo(res.getInt("frag_no"));
				v.setSiteId(res.getInt("site_id"));
				v.setColNames(res.getString("col_names"));
				// we can set other also in future
				vertical.add(v);
		    }
		}
		catch (SQLException s){
			System.out.println("SQL code does not execute.");
		}  
		//derived			
		sql = "select * from derived";
	//	ArrayList<Derived>  derived = new ArrayList<Derived>();
		try {
		Statement st = conn.createStatement();
		ResultSet res = st.executeQuery(sql);
		  
		while (res.next()) {
				Derived d= new Derived();
				d.setTblId(res.getInt("tbl_id"));
				d.setFragNo(res.getInt("frag_no"));
				d.setSiteId(res.getInt("site_id"));
				d.setSiteId(res.getInt("dtbl_id"));
				d.setSiteId(res.getInt("dfrag_no"));
				// we can set other also in future
				derived.add(d);
		    }
		}
		catch (SQLException s){
			System.out.println("SQL code does not execute.");
		}
		
	}//end of main
}
