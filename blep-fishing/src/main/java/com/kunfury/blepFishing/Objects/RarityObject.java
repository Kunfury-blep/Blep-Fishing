package com.kunfury.blepFishing.Objects;

import com.kunfury.blepFishing.Config.Variables;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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
		for (RarityObject rarityObject : baseList) {
			if (rarityObject.Name.equalsIgnoreCase(ChatColor.stripColor(name))) {
				return rarityObject;
			}
		}
		return null;
	}

	public static RarityObject GetRandom(){
		//Rarity Selection
		int randR = ThreadLocalRandom.current().nextInt(0, Variables.RarityTotalWeight);
		RarityObject chosenRarity = Variables.RarityList.get(0);
		for(final RarityObject rarity : Variables.RarityList) {
			if(randR <= rarity.Weight) {
				chosenRarity = rarity;
				break;
			}else
				randR -= rarity.Weight;
		}

		return chosenRarity;
	}
	
}
