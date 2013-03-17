package com.iiit.adb.emp.db.gdd.GDD;

import java.util.Vector;

import com.iiit.adb.emp.db.parser.globaldefinition.*;

/**
 * class for executing result. not used now
 * 
 * @author Jyothi
 *
 */
public class FragmentationInfo{
	private int fragmentationID;
	private String fragmentationName;
	private int fragmentationType;
	private int siteNumber;
	private String siteName;
	private int fragmentationSize;
	private String conditionString;
	private FragmentationCondition conditionExpression;
	
	public FragmentationInfo(){
		
	}
	
	public FragmentationInfo(int fragType,String fragName,String fragCondition,int siteNO,int size){
		this.fragmentationType = fragType;
		this.fragmentationName = fragName;
		this.conditionString = fragCondition;
		this.siteNumber = siteNO;
		this.siteName = null;
		this.fragmentationSize = size;
		this.conditionExpression = null;
		
		
	}
	
	public FragmentationInfo(int fragType,String fragName,String fragCondition,String siteName,int size){
		this.fragmentationType = fragType;
		this.fragmentationName = fragName;
		this.conditionString = fragCondition;
		this.siteName = siteName;
		
		if(!siteName.equals("null"))
			this.siteNumber = Integer.parseInt(siteName.substring(4));

		this.fragmentationSize = size;
		this.conditionExpression = null;
		
		
	}
	
	public FragmentationInfo(int fragType,int sitNO,int fragSize,String condition,FragmentationCondition express){
		this.fragmentationType = fragType;
		this.fragmentationSize = fragSize;
		this.siteNumber = sitNO;
		this.conditionString = condition;
		this.conditionExpression = express;
	}
	
	
	/**
	 * change a fragmentation sentence to a fragmentationInfo class
	 * @param		fragType int,
	 *              fragmentationType;
	 * @param       fraginfo String,
	 *              fragmentation info sentence,such as "1: condition siteNumber fragSize"
	 */
	public FragmentationInfo(int fragType,String fraginfo){
		
	}
	
	
	public void setFragmentationCondition(FragmentationCondition fragCondition){
		
		this.conditionExpression = fragCondition;
	}
	
	public int getFragID(){
		return this.fragmentationID;
	}
	
	public String getFragName(){
		return this.fragmentationName;
	}
	
	public int getFragType(){
		return this.fragmentationType;
	}
	
	public int getFragSiteNumber(){
		return this.siteNumber;
	}
	
	public String getFragSiteName(){
		return this.siteName;
	}
	
	public int getFragSize(){
		return this.fragmentationSize;
	}
	
	public String getFragCondition(){
		return this.conditionString;
	}
	
	public FragmentationCondition getFragConditionExpression(){
		return this.conditionExpression;
	}
	
	public void setSiteName(String sitename ){
		this.siteName = sitename;
	}
	
	public void printFragmentation(){
		System.out.print("fragName="+this.fragmentationName);
		System.out.print(",fragCondition="+this.conditionString);
		System.out.print(",fragSiteName="+this.siteName);
		System.out.println(",fragSize="+this.fragmentationSize);
		
		
		if(this.conditionExpression.fragmentationType == CONSTANT.FRAG_HORIZONTAL){
			System.out.print("fragType="+this.conditionExpression.fragmentationType);
			System.out.println(",expressSize="+this.conditionExpression.HorizontalFragmentationCondition.size());
			for(int i = 0 ; i < this.conditionExpression.HorizontalFragmentationCondition.size(); i++){
				SimpleExpression expression = this.conditionExpression.HorizontalFragmentationCondition.elementAt(i);
				System.out.print(expression.tableName);
				System.out.print(","+expression.columnName);
				System.out.print(","+expression.op);
				System.out.print(","+expression.value);
				System.out.print(","+expression.valueType);
				System.out.println();
			}
		}
		if(this.conditionExpression.fragmentationType == CONSTANT.FRAG_VERTICAL){
			System.out.print("fragType="+this.conditionExpression.fragmentationType);
			System.out.println(",columnSize="+this.conditionExpression.verticalFragmentationCondition.size());
			Vector<String> columns = this.conditionExpression.verticalFragmentationCondition;
			for(int i = 0 ; i < (columns.size()-1); i++){
				System.out.print(columns.elementAt(i)+",");
			}
			System.out.println(columns.elementAt(columns.size()-1));
		}
		//System.out.println();
	}
	
}