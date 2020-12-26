package com.kunfury.blepFishing.Signs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;

import com.kunfury.blepFishing.Setup;

import Objects.FishObject;

public class RefreshSign {
	@SuppressWarnings("unchecked")
	public static void Refresh(SignObject signObj) {
		Sign sign = signObj.GetSign();
		String fishName = signObj.FishName;
		String filePath = Setup.dataFolder + "/fish data/" + fishName  + ".data"; 
		
		List<FishObject> savedFishList = new ArrayList<>();
    	File fishFile = new File(filePath);
    	
    	if(fishFile.exists()) {
    		//Saves the Fish
    		try {
    		    ObjectInputStream input = new ObjectInputStream(new FileInputStream (filePath));
    		    if(fishFile.exists())
    		    	savedFishList.addAll((List<FishObject>)input.readObject());
    		    input.close();
    		} catch (IOException | ClassNotFoundException ex) {
    			//player.sendMessage("Loading Failed");
    			ex.printStackTrace();
    			return;
    		}
    		
    		//Runs if a correct fish was found
    		Collections.sort(savedFishList, Collections.reverseOrder());
    		
    		int index = signObj.Level;
    		
    		if(savedFishList.size() > index) {
    			FishObject fish = savedFishList.get(signObj.Level);
    			sign.setLine(0, fish.Name + " #" + (signObj.Level + 1));
    			sign.setLine(1, fish.PlayerName);
    			sign.setLine(2, fish.RealSize + "in");
    			sign.setLine(3, ChatColor.translateAlternateColorCodes('&',fish.Rarity));
    		}else {
    			FishObject fish = savedFishList.get(savedFishList.size() - 1);
    			sign.setLine(0, fish.Name + " #" + (signObj.Level + 1));
    			sign.setLine(1, ChatColor.translateAlternateColorCodes('&',"&4Not Available"));
    			sign.setLine(2, "");
    			sign.setLine(3, "");
    		}
    		sign.update();
    	}else {
    		sign.setLine(0, "");
			sign.setLine(1, ChatColor.translateAlternateColorCodes('&',"&4Not Available"));
			sign.setLine(2, "");
			sign.setLine(3, "");
    	}
	}

}
