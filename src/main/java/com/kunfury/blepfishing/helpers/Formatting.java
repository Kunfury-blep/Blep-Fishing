package com.kunfury.blepfishing.helpers;

import com.kunfury.blepfishing.ui.objects.Panel;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	public static String dateToString(LocalDateTime dateTime){
		return dateTimeFormatter.format(dateTime);
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

		if(result.isEmpty())
			result = "&f" + 0 + color + "s";

		return formatColor(result);
	}

	public static String asTime(long milli){
		return asTime(milli, ChatColor.BLUE);
	}

	public static String asTime(double hours){
		return asTime(hours, ChatColor.BLUE);
	}

	public static String asTime(double hours, ChatColor color){
		long milli = (long) (hours * 1000 * 60 * 60);
		return asTime(milli, color);
	}

	public static String asTime(LocalDateTime dateTime){
		int hour = dateTime.getHour();
		int minute = dateTime.getMinute();
		String timeStr = "";

		if(hour < 10)
			timeStr += "0";

		timeStr += hour + ":";

		if(minute < 10)
			timeStr += "0";

		timeStr += minute;

		return timeStr;
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

	public static String formatColor(String msg){
		return ChatColor.translateAlternateColorCodes('&', msg);
	}


	public static YamlConfiguration languageYaml = new YamlConfiguration();

	/**
	 * @param key Key for language file
	 * @return Color formatted String from Language YAYamlConfiguration
	 */
	public static String GetLanguageString(String key){
		if(languageYaml.contains(key)){
			return formatColor(languageYaml.getString(key));
		}else{
			return ChatColor.RED + "Message Not Found - " + key;
		}
	}

	private static String prefix;
	public static String GetMessagePrefix() {
		if(prefix == null)
			prefix = formatColor("&b[BF]&f ");
		return prefix;
	}

	/**
	 * Used for sending Player Messages in Chat
	 * @param key Key for language file
	 * @return Prefix attached to getMessage(key)
	 */
	public static String GetFormattedMessage(String key){
		return GetMessagePrefix() + GetLanguageString(key);
	}

	public static String GetIdFromNames(String name){
		return name.toLowerCase().replace(" ", "_");
	}

	/**
	 * @param values The values to format to a String
	 * @param textColor The color of the values
	 * @param commaColor The color of the comma between values
	 * @return String containing a comma separated formatted list of all objects
	 */
	public static String ToCommaList(List<String> values, ChatColor textColor, ChatColor commaColor){
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


	/**
	 * Fits a String to in-game Item Lore window
	 * @param input The string to be converted
	 * @return List meant to be used with itemLore.addAll()
	 */
	public static List<String> ToLoreList(String input){
		List<String> lore = new ArrayList<>();

		if(input.length() <= 40){
			lore.add(input);
			return lore;
		}

		while(!input.isEmpty()){
			int pos = input.lastIndexOf(" ", 40);
			if (pos == -1) {
				pos = input.length();
			}
			String found = input.substring(0, pos);



			lore.add(ChatColor.WHITE + found);

			if(input.length() > pos + 1)
				input = input.substring(pos+1);
			else
				input = "";
		}
		return lore;
	}

	/**
	 * Quick combination of toCommaList and toLoreList
	 * @param values The values to format to a String
	 * @param textColor The color of the values
	 * @param commaColor The color of the comma between values
	 * @return Comma Separated List meant to be used with itemLore.addAll()
	 */
	public static List<String> ToCommaLoreList(List<String> values, ChatColor textColor, ChatColor commaColor){
		return ToLoreList(ToCommaList(values, textColor, commaColor));
	}

	public static String GetItemName(ItemStack item){
		ItemMeta meta = item.getItemMeta();
		if(meta != null && !meta.getDisplayName().isEmpty())
			return meta.getDisplayName();
		else
			return item.getType().toString();
	}

	private static final Pattern numericPattern = Pattern.compile("-?\\d+(\\.\\d+)?");
	public static boolean isNumeric(String arg) {
		if (arg == null) {
			return false;
		}
		return numericPattern.matcher(arg).matches();
	}
}
