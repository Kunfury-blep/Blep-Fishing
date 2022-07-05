package com.kunfury.blepFishing.Signs;

import java.io.Serializable;

import com.kunfury.blepFishing.Miscellaneous.Variables;
import com.kunfury.blepFishing.Objects.FishObject;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Sign;

public class SignObject implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7158707181821449829L;
	public String FishName;
	public int Level;
	
	private double locX;
	private double locY;
	private double locZ;
	private String worldStr;


	public SignObject(Sign sign, String _fishName, int level, World world) {

		locX = sign.getLocation().getX();
		locY = sign.getLocation().getY();
		locZ = sign.getLocation().getZ();
		FishName = getFullName(_fishName);
		Level = level;
		worldStr = world.getName();
	}

	private String getFullName(String _fishName){
		_fishName = _fishName.toUpperCase();
		for(var f : Variables.BaseFishList){
			if(f.Name.equalsIgnoreCase(_fishName) || f.Name.toUpperCase().contains(_fishName)){
				return f.Name;
			}
		}
		return null;
	}
	
	public Sign GetSign(){
		Sign sign;
		
		try {
			Location loc = new Location(Bukkit.getServer().getWorld(worldStr), locX, locY, locZ);
			sign = (Sign)loc.getBlock().getState();	
			sign.getLines().toString(); //This is just for checking if it's a sign
			return sign;
		}catch(Exception e){
			return null;
		}

	}

}
