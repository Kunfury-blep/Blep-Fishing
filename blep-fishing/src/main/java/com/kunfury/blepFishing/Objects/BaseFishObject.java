package com.kunfury.blepFishing.Objects;

import com.kunfury.blepFishing.Endgame.EndgameVars;
import com.kunfury.blepFishing.Config.Variables;
import org.bukkit.ChatColor;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class BaseFishObject implements Serializable{

	private static final long serialVersionUID = -2959331831404886148L;
	public String Name;
	public String Lore;
	public double MinSize;
	public double MaxSize;
	public double AvgSize;
	public int ModelData;
	public boolean RequiresRain;
	public int Weight = 100;
	public double BaseCost = 1;
	public List<String> Areas;

	public int MaxHeight = 1000;
	public int MinHeight = -1000;

	public BaseFishObject(String name, String lore, double minSize, double maxSize, 
			int modelData, boolean _requiresRain, double baseCost, List<String> areas, int _minHeight, int _maxHeight){
		Name = name;
		Lore = lore;
		MinSize = minSize;
		MaxSize = maxSize;
		AvgSize = (minSize + maxSize)/2;
		ModelData = modelData;		
		RequiresRain = _requiresRain;
		BaseCost = baseCost;
		Areas = areas;

		if(_minHeight != 0 && _maxHeight != 0){
			MinHeight = _minHeight;
			MaxHeight = _maxHeight;
		}

	}
	
	//Figure out what this dose
	public BaseFishObject weight(int _weight) {
		if(_weight > 0)
			Weight = _weight;
		else
			Weight = 100;
		return this;
	}

	public static BaseFishObject GetBase(String Name){
		List<BaseFishObject> baseList = Variables.BaseFishList;
		for(int i = 0; i < baseList.size(); i++){
			if(baseList.get(i).Name.equalsIgnoreCase(ChatColor.stripColor(Name))){
				return(baseList.get(i));
			}
		}
		return null;
	}

	public double getSize(boolean allBlue){
		double realMax = MaxSize;

		if(allBlue) realMax *= EndgameVars.FishSizeMod;
		//TODO: Increase fish stats based on config

		double size = ThreadLocalRandom.current().nextDouble(MinSize, realMax);

		return size;
	}
}
