package application.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

	public static String wordReducer(String s , int size) {
		if (s==null || s.length()<=size) return s;
		
		Pattern p = Pattern.compile("\\w+");
		Matcher m = p.matcher(s);
		int l = 0;
		while (m.find()) {
			if (m.end()>size) break;			
			l= m.end();
			
		}
		if (l>0) return s.substring(0, l) + "...";
		else return s.substring(0,size) + "...";
	}
	
}
