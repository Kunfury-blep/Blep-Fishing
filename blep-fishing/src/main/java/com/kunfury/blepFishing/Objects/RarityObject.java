package com.kunfury.blepFishing.Objects;

import com.kunfury.blepFishing.BlepFishing;
import com.kunfury.blepFishing.Config.Variables;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class RarityObject implements Comparable<RarityObject>{
	private String name;
	private String id;
	private Integer weight;
	private String prefix;
	private double priceMod = 1;


	public RarityObject(String _id, int _weight, String _prefix, double _priceMod) {
		id = _id;
		name = "&" + _prefix + "&l&o" + _id;
		prefix = _prefix;
		weight = _weight;
		priceMod = _priceMod;
	}


	@Override
    public int compareTo(RarityObject r) {
        return this.weight.compareTo(r.getWeight());
    }

    public static RarityObject GetRarity(String id){
		List<RarityObject> baseList = Variables.RarityList;
		for (RarityObject rarityObject : baseList) {
			if (rarityObject.getId().equalsIgnoreCase(id)) {
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
			if(randR <= rarity.getWeight()) {
				chosenRarity = rarity;
				break;
			}else
				randR -= rarity.getWeight();
		}

		return chosenRarity;
	}

	public String getId(){
		return id;
	}

	public String getName(){
		return name;
	}

	public int getWeight(){
		return weight;
	}

	public String getPrefix(){
		if(prefix == null)
			prefix = "";
		return prefix;
	}

	public double getPriceMod(){
		return priceMod;
	}

	public static RarityObject Update(RarityObject oldRarity, String _id, int _weight, String _prefix, double _priceMod){

		FileConfiguration config = BlepFishing.configBase.config;
		if (oldRarity != null) { //Deletes the old rarity
			config.set("rarities." + oldRarity.getId(), null);
			return oldRarity.Update(_id, _weight, _prefix, _priceMod);
		}

		config.set("rarities." + _id + ".Weight", _weight);
		config.set("rarities." + _id + ".Color Code", _prefix);
		config.set("rarities." + _id + ".Price Mod", _priceMod);

		BlepFishing.blepFishing.saveConfig();

		RarityObject newRarity = new RarityObject(_id, _weight, _prefix, _priceMod);

		Variables.RarityList.add(newRarity);
		return newRarity;
	}

	public RarityObject Update(String _id, int _weight, String _prefix, double _priceMod){
		id = _id;
		name = "&" + prefix + "&l&o" + _id;
		prefix = _prefix;
		weight = _weight;
		priceMod = _priceMod;

		return this;
	}
}
