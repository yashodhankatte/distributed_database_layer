package com.iiit.adb.emp.db.gdd.GDD;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * A utility class for string token generation
 * 
 * @author Jyothi
 *
 */
public class Utility{
	public static String getToken(String inputStr,int index){
	
		String result = null;
		
		try{
			StringTokenizer token = new StringTokenizer(inputStr);
			int i = 0;
			while(i < index){
				if(token.hasMoreTokens()){
					token.nextToken();
					i++;
				}
				else
					break;
			}
			if(token.hasMoreTokens())
				result = token.nextToken();
			return result;
			
		}catch(NullPointerException e){
			System.out.println("NullPointerException here: "+inputStr);
			e.printStackTrace();
			return "-1";
		}
	}
	
	public static Vector<String> StringTokener(String s){
		Vector<String> result = new Vector();
		int pos;
		pos = s.indexOf(",");
		while(pos != -1){
			result.add(s.substring(0, pos));
			s = s.substring(pos+1);
			pos = s.indexOf(",");
		}
		result.add(s);
		//for(int i = 0 ; i < result.size() ; i++)
			//System.out.println(result.elementAt(i));
		
		return result;
	}
	
	public static String stringFromTokener(Vector<String> strs){
		String result = null;
		result = "(";
		int size = strs.size() ;
		int i;
		for(i = 0 ; i < size-1 ; i++)
			result += strs.elementAt(i)+",";
		result += strs.elementAt(size-1)+")";
		return result;
		
	}
	
	public static void formatBlackString(String str){
		//str.replace(oldChar, newChar)
	}
	
	public static Map<String,String> StringToMap(Vector<String> colNames,String str){
		Map m = new HashMap();
		Vector<String> strs = StringTokener(str);
		if(colNames.size() != strs.size()){
			System.out.println("Error: size is different!");
			return null;
		}
		for(int i = 0 ; i < strs.size() ; i++)
			m.put(colNames.elementAt(i), strs.elementAt(i));
		return m;
	}
	
	
	
}