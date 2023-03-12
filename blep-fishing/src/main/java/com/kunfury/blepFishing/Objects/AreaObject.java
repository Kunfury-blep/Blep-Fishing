package com.kunfury.blepFishing.Objects;

import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Miscellaneous.BiomeHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class AreaObject {

	public String Name;
	public List<String> Biomes;
	public String CompassHint;
	public boolean HasCompass;

	
	public AreaObject(String _name, List<String> _biomes, String _compassHint, boolean _hasCompass) {
		Name = _name;
		Biomes = _biomes;
		CompassHint = _compassHint;
		HasCompass = _hasCompass;
	}

	public static AreaObject FromString(String areaName){
		for(var a : Variables.AreaList){
			if(a.Name.equalsIgnoreCase(areaName)){
				return a;
			}
		}
		return null;
	}

	public static List<AreaObject> GetAreas(Location loc){
		String biomeName = new BiomeHandler().getBiomeName(loc);

		List<AreaObject> areas = new ArrayList<>();

		Variables.AreaList.forEach(a -> {
			if (a.Biomes.contains(biomeName.toUpperCase())) {
				areas.add(a);
			}
		});
		return areas;
	}
	
}
