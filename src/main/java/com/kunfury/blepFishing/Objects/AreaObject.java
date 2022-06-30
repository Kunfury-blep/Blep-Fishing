package com.kunfury.blepFishing.Objects;

import com.kunfury.blepFishing.Miscellaneous.Variables;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.awt.geom.Area;
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
		AreaObject area = null;
		for(var a : Variables.AreaList){
			if(a.Name.equalsIgnoreCase(areaName)) area = a;
		}
		return area;
	}
	
	public static List<AreaObject> GetAreas(Location loc){
		String biomeName = loc.getBlock().getBiome().name();

		List<AreaObject> areas = new ArrayList<>();

		Variables.AreaList.forEach(a -> {
			if (a.Biomes.contains(biomeName.toUpperCase())) {
				areas.add(a);
			}
		});
		return areas;
	}
	
}
