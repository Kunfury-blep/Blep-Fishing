package com.kunfury.blepFishing.Miscellaneous;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Pattern;

public class Formatting {

	public static DecimalFormat df = new DecimalFormat("##.00");
	
	//Formats doubles to two decimal places and returns them
	//Parses through a locale formatter in order to ensure no incompatibilities
	public static String DoubleFormat(Double d) {
		if(d == null) d = 0.0;
		NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
		Number number = 0;
		try {
			number = format.parse(d.toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return(df.format(number));		
	}
	
	public static String asTime(long milli) {

		long seconds = (milli / 1000) % 60;
		long minutes = (milli / (1000 * 60)) % 60;
		long hours = (milli / (1000 * 60 * 60)) % 24;
		long days = (milli / (1000 * 60 * 60 * 24));

		String result = "";

		if(days > 0) result += days + "d ";
		if(hours > 0) result += hours + "h ";
		if(minutes > 0) result += minutes + "m ";
		if(seconds > 0) result += seconds + "s ";
		
		return result;
	}
	
	/********************************************************
    * Fix string spaces to align text in minecraft chat
    *
    * @author David Toledo ([EMAIL]david.oracle@gmail.com[/EMAIL])
    * @param s String to be resized
    * @param size to align
    * @return New aligned String
    */
    public static String FixFontSize (String s, int size) {
     
	    StringBuilder ret = new StringBuilder(s);
		for (int i=0; i < s.length(); i++) {
			if ( s.charAt(i) == 'I' || s.charAt(i) == ' ') {
				ret.append(" ");
			}
		}

		int availLength = size - s.length();

		ret.append(" ".repeat(Math.max(0, availLength)));
	     
	    return (ret.toString());
    }

	public static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();

		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	private static final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

	public static boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		return pattern.matcher(strNum).matches();
	}

	public static String formatColor(String msg){
		return ChatColor.translateAlternateColorCodes('&', msg);
	}


	public static FileConfiguration messages = new YamlConfiguration();
	public static String getMessage(String key){
		if(messages.contains(key)){
			return formatColor(messages.getString(key));
		}else{
			return ChatColor.RED + "Message Not Found - " + key;
		}
	}
}
