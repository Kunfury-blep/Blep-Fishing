package Miscellaneous;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class Formatting {

	public static DecimalFormat df = new DecimalFormat("##.00");
	
	//Formats doubles to two decimal places and returns them
	//Parses through a locale formatter in order to ensure no incompatibilities
	public static String DoubleFormat(Double d) {
		NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
		Number number = 0;
		try {
			number = format.parse(d.toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return(df.format(number));		
	}
	
	public static String TimeFormat(long milli) {
		
		long seconds = milli / 1000;
		long minutes = seconds / 60;
		long hours = minutes / 60;
		long days = hours / 24;
		String result = days + ":" + hours % 24 + ":" + minutes % 60 + ":" + seconds % 60; 
		
		return result;
	}
	
	/********************************************************
    * Fix string spaces to align text in minecraft chat
    *
    * @author David Toledo ([EMAIL]david.oracle@gmail.com[/EMAIL])
    * @param String to be resized
    * @param Size to align
    * @return New aligned String
    */
    public static String FixFontSize (String s, int size) {
     
	    String ret = s;
	     
	    if ( s != null ) {
	     
		    for (int i=0; i < s.length(); i++) {
			    if ( s.charAt(i) == 'I' || s.charAt(i) == ' ') {
			    	ret += " ";
			    }
		    }
		     
		    int availLength = size - s.length();
		     
		    for (int i=0; i < availLength; i++) {
		    	ret += " ";
		    	}
	    }
	     
	    return (ret);
    }
	     
	
}
