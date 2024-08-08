package com.kunfury.blepfishing.helpers;

import com.gmail.nossr50.runnables.skills.MasterAnglerTask;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.units.qual.A;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
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

	public static String asTime(long milli, ChatColor color) {

		long seconds = (milli / 1000) % 60;
		long minutes = (milli / (1000 * 60)) % 60;
		long hours = (milli / (1000 * 60 * 60)) % 24;
		long days = (milli / (1000 * 60 * 60 * 24));

		String result = "";

		if(days > 0) result += "&f" + days + color + "d ";
		if(hours > 0) result += "&f" + hours + color + "h ";
		if(minutes > 0) result += "&f" + minutes + color + "m ";
		if(seconds > 0) result += "&f" + seconds + color + "s ";

		return formatColor(result);
	}

	public static String asTime(double hours, ChatColor color){
		long milli = (long) (hours * 1000 * 60 * 60);
		return asTime(milli, color);
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

	private static String prefix;
	public static String getPrefix() {
		if(prefix == null)
			prefix = formatColor("&b[BF]&f ");
		return prefix;
	}

	public static String getFormattedMessage(String key){
		return getPrefix() + getMessage(key);
	}

	public static String getFormattedMessage(String key, ChatColor color){
		return getPrefix() + color + getMessage(key);
	}

	public static String getIdFromName(String name){
		return name.toLowerCase().replace(" ", "_");
	}

	public static String getCommaList(List<String> values, ChatColor textColor, ChatColor commaColor){
		int i = 1;
		StringBuilder sb = new StringBuilder();
		for(var value : values){
			sb.append(textColor).append(value);
			if(i < values.size())
				sb.append(commaColor).append(", ");
			i++;
		}
		return sb.toString();
	}

	public static String getCommaString(String value, ChatColor textColor, ChatColor commaColor){
		value = textColor + value;
		value = value.replace(" ", commaColor + ", " + textColor);

		return value;
	}

	public static List<String> toLoreList(String str){
		List<String> lore = new ArrayList<>();

		while(!str.isEmpty()){
			int pos = str.lastIndexOf(" ", 40);
			if (pos == -1) {
				pos = str.length();
			}
			String found = str.substring(0, pos);



			lore.add(found);

			if(str.length() > pos + 1)
				str = str.substring(pos+1);
			else
				str = "";
		}




		return lore;
	}

	public static List<String> toLoreList(List<String> textList){
		StringBuilder compactedLore = new StringBuilder();
		for(var t : textList){
			if(compactedLore.isEmpty()){
				compactedLore.append(t);
				continue;
			}
			compactedLore.append(" ").append(t);
		}

		return toLoreList(compactedLore.toString());
	}

	public static String getItemName(ItemStack item){
		ItemMeta meta = item.getItemMeta();
		if(meta != null && !meta.getDisplayName().isEmpty())
			return meta.getDisplayName();
		else
			return item.getType().toString();
	}

}
