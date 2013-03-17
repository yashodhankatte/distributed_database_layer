package com.iiit.adb.emp.db.gdd.GDD;

import java.util.Vector;

/**
 * class for executing result. not used now
 * 
 * @author Jyothi
 *
 */
public class SiteInfo{
	private int siteID;
	private String siteName;
	private String siteIP;
	private int sitePort;
	private int fragNum;
	private Vector<String> fragmentationsNames;	
	
	public SiteInfo(){
		this.siteID = 0;
		this.siteName = null;
		this.siteIP = null;
		this.sitePort = 0;
		this.fragNum = 0;
		this.fragmentationsNames = new Vector();
	}
	
	public SiteInfo(String siteName,String ip,int port){
		this.siteName = siteName;
		this.siteIP = ip;
		this.sitePort = port;
		this.fragNum = 0;
		this.fragmentationsNames = new Vector();
	}
	
	public SiteInfo(int id,String ip,int port,int num,Vector<String> fragNames){
		this.siteID = id;
		this.siteIP = ip;
		this.sitePort = port;
		this.fragNum = num;
		this.fragmentationsNames = fragNames;
	}
	
	public SiteInfo(String name,String ip,int port,int num,Vector<String> fragNames){
		this.siteName = name;
		this.siteID = Integer.parseInt(name.substring(4));
		this.siteIP = ip;
		this.sitePort = port;
		this.fragNum = num;
		this.fragmentationsNames = fragNames;
	}
	
	public int getSiteID(){
		return this.siteID;
	}
	
	public String getSiteName(){
		return this.siteName;
	}
	
	public String getSiteIP(){
		return this.siteIP;
	}
	
	public int getSitePort(){
		return this.sitePort;
	}
	
	public int getSiteFragNum(){
		return this.fragNum;
	}
	
	public Vector<String> getSiteFragNames(){
		return this.fragmentationsNames;
	}
	
	public void addSiteFragNum(){
		this.fragNum++;
	}
	public void printSite(){
		System.out.println("siteName="+this.siteName+", siteIP="+this.siteIP+",sitePort="+this.sitePort);
		System.out.print(this.fragNum+":");
		for(int i = 0 ; i < this.fragmentationsNames.size() ; i++)
			System.out.print(this.fragmentationsNames.elementAt(i)+",");
		System.out.println();
	}
	
}