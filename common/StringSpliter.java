package battleships.common;

import java.util.ArrayList;

public class StringSpliter {
	public static String [] delimStr(String str, String del){
		ArrayList<String> result = new ArrayList<String>();
		StringBuilder s = new StringBuilder("");
		for(int i = 0 ; i < str.length(); i++){
			if(del.contains(""+str.charAt(i))) {
				if(!(s.toString().equals(""))){
					result.add(s.toString());
					s = new StringBuilder("");
				}
			}
			else s.append(str.charAt(i));
		}
		String [] res = new String[result.size()];
		result.toArray(res);
		return res;
	}
	private StringSpliter(){}
}
