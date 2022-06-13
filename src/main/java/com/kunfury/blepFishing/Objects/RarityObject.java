package com.kunfury.blepFishing.Objects;

import com.kunfury.blepFishing.Miscellaneous.Variables;
import org.bukkit.ChatColor;

import java.util.List;

public class RarityObject implements Comparable<RarityObject>{
	public String Name;
	public Integer Weight;
	public String Prefix;
	public double PriceMod = 1;
	
	
	public RarityObject(String name, int weight, String prefix, double priceMod) {
		Prefix = prefix;
		Name = "&" + prefix + "&l&o" + name;
		Weight = weight;
		PriceMod = priceMod;
	}
	
	
    @Override
    public int compareTo(RarityObject r) {
        return this.Weight.compareTo(r.Weight);
    }

    public static RarityObject GetRarity(String name){
		List<RarityObject> baseList = Variables.RarityList;
		for(int i = 0; i < baseList.size(); i++){
			if(baseList.get(i).Name.equalsIgnoreCase(ChatColor.stripColor(name))){
				return(baseList.get(i));
			}
		}
		return null;
	}
	
}
