package com.iiit.adb.emp.db.gdd.GDD;

import java.util.Vector;
import com.iiit.adb.emp.db.parser.globaldefinition.*;

/**
 * class for executing result. not used now
 * 
 * @author Jyothi
 *
 */
public class FragmentationCondition{
	public int fragmentationType;
	public Vector<String> verticalFragmentationCondition;
	public Vector<SimpleExpression> HorizontalFragmentationCondition;
	
	public FragmentationCondition(){
		this.fragmentationType = 0;
		this.verticalFragmentationCondition = null;
		this.verticalFragmentationCondition = null;
	}
	
	public FragmentationCondition(int type,Vector<String> verCondition,Vector<SimpleExpression>horCondition){
		this.fragmentationType = type;
		this.verticalFragmentationCondition = verCondition;
		this.HorizontalFragmentationCondition = horCondition;
	}
	
	
	/**
	 * change a condition sentence to a contion class
	 * @param	conditions	String
	 *              condition sentence ,such as "id > 1000 AND id < 3000" or "(id,name)"
	 *              or "(id,name) AND id < 1000" and so on
	 */
	public void StringToFragmentationCondition(String condition){
		
	}
	
	
	
	
	
}
