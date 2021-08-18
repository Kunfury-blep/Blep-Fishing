package com.kunfury.blepFishing.Objects;

import java.util.ArrayList;
import java.util.List;

public class AreaObject {

	public String Name;
	public List<String> Biomes = new ArrayList<>();
	
	public AreaObject(String _name, List<String> _biomes) {
		
		Name = _name;
		Biomes = _biomes;
		
	}
	
	
	
}
