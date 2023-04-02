package com.kunfury.blepFishing.Objects;

import com.kunfury.blepFishing.Config.FishTime;
import com.kunfury.blepFishing.Endgame.EndgameVars;
import com.kunfury.blepFishing.Config.Variables;
import org.bukkit.ChatColor;

import java.io.Serializable;
import java.util.List;
import java.util.Random;
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

	private final FishTime time;

	public BaseFishObject(String name, String lore, double minSize, double maxSize, 
			int modelData, boolean _requiresRain, double baseCost, List<String> areas, int _minHeight, int _maxHeight, FishTime _time){
		Name = name;
		Lore = lore;
		MinSize = minSize;
		MaxSize = maxSize;
		AvgSize = (minSize + maxSize)/2;
		ModelData = modelData;		
		RequiresRain = _requiresRain;
		BaseCost = baseCost;
		Areas = areas;
		time = _time;

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

	public static BaseFishObject getBase(String Name){
		List<BaseFishObject> baseList = Variables.BaseFishList;
		for (BaseFishObject baseFishObject : baseList) {
			if (baseFishObject.Name.equalsIgnoreCase(ChatColor.stripColor(Name))) {
				return baseFishObject;
			}
		}
		return null;
	}

	public static BaseFishObject getRandom(){
		Random rand = new Random();
		return Variables.BaseFishList.get(rand.nextInt(Variables.BaseFishList.size()));
	}

	public double getSize(boolean allBlue){
		double realMax = MaxSize;

		if(allBlue) realMax *= EndgameVars.FishSizeMod;

		return ThreadLocalRandom.current().nextDouble(MinSize, realMax);
	}

	public boolean canCatch(boolean isRaining, int height, boolean isNight, List<AreaObject> areas){
		boolean rainCheck = !RequiresRain || isRaining;
		if(!rainCheck) return false;
		boolean heightCheck = MinHeight <= height && MaxHeight >= height;
		if(!heightCheck) return false;
		boolean timeCheck = time == FishTime.ALL || (time == FishTime.NIGHT && isNight) ||(time == FishTime.DAY && !isNight);
		if(!timeCheck) return false;

		boolean areaCheck = false;
		for (var area : areas) {
			if (Areas.contains(area.Name)) {
				areaCheck = true;
				break;
			}
		}

		return areaCheck;
	}
}
