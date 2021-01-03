package com.kunfury.blepFishing.Signs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;

import Miscellaneous.Formatting;
import Miscellaneous.Variables;
import Objects.FishObject;

public class RefreshSign {
	public static void Refresh(SignObject signObj) {
		Sign sign = signObj.GetSign();
		String fishName = signObj.FishName;
		
		List<FishObject> savedFishList = new ArrayList<>();
    	savedFishList = Variables.GetFishList(fishName);
    	if(savedFishList != null) {
    		int index = signObj.Level;
    		
    		if(savedFishList.size() > index) {
    			FishObject fish = savedFishList.get(signObj.Level);
    			sign.setLine(0, fish.Name + " #" + (signObj.Level + 1));
    			sign.setLine(1, fish.PlayerName);
    			sign.setLine(2, Formatting.DoubleFormat(fish.RealSize) + "in");
    			sign.setLine(3, ChatColor.translateAlternateColorCodes('&',fish.Rarity));
    		}else {
    			sign.setLine(0, fishName + " #" + (signObj.Level + 1));
    			sign.setLine(1, ChatColor.translateAlternateColorCodes('&',"&4Not Available"));
    			sign.setLine(2, "");
    			sign.setLine(3, "");
    		}
    		sign.update();
    	}else {
    		sign.setLine(0, "");
			sign.setLine(1, ChatColor.translateAlternateColorCodes('&',"&Fish Doesn't Exist"));
			sign.setLine(2, "");
			sign.setLine(3, "");
    	}
    	
    		
	}

}
