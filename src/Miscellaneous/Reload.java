package Miscellaneous;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import com.kunfury.blepFishing.Setup;

import Objects.AreaObject;
import Objects.FishObject;
import Objects.RarityObject;

public class Reload {

	public static boolean ReloadPlugin(CommandSender sender) {
		Setup.setup.reloadConfig();
    	Setup.config = Setup.setup.getConfig();
    	
    	Set<String> existCheck = Setup.config.getConfigurationSection("").getKeys(false); //Gets all the config
		
    	if(existCheck.contains("fish") && existCheck.contains("rarities") ) {
    		//Reset all variables to reduce crashing
    		Variables.FishList.clear();
        	Variables.RarityList.clear();
        	Variables.AreaList.clear();
        	Variables.CaughtFish.clear();
        	Variables.RarityTotalWeight = 0;
        	Variables.FishTotalWeight = 0;
        	
        	//Reloading Fish
        	Map<String, Object> fishMap = Setup.config.getConfigurationSection("fish").getValues(false);
    		for(final String key : fishMap.keySet()) {
    			String name = key;
    			String lore = Setup.config.getString("fish." + key + ".Lore");
    			Double minSize = (Setup.config.getDouble("fish." + key + ".Min Size"));
    			Double maxSize = Setup.config.getDouble("fish." + key + ".Max Size");
    			int modelData = Setup.config.getInt("fish." + key + ".ModelData");
    			String area = Setup.config.getString("fish." + key + ".Area");
    			List<String> biomes = Setup.config.getStringList("areas." + area +".Biomes");
    			Boolean raining = Setup.config.getBoolean("fish." + key + ".Raining");
    			int weight = Setup.config.getInt("fish." + key + ".Weight");
    			double baseCost = Setup.config.getDouble("fish." + key + ".Base Price");
    			
    			FishObject fish = new FishObject(name, lore, minSize, maxSize, modelData, biomes, raining, baseCost, area)
    					.weight(weight);
    			Variables.FishList.add(fish);
    			
    		}
    		
    		//Reloading Rarities
        	Map<String, Object> rarityMap = Setup.config.getConfigurationSection("rarities").getValues(false);
    		for(final String key : rarityMap.keySet()) {
    			String name = key;
    			int weight = Setup.config.getInt("rarities." + key + ".Weight");
    			String prefix = Setup.config.getString("rarities." + key + ".Color Code");
    			double priceMod = Setup.config.getDouble("rarities." + key + ".Price Mod");
    			
    			RarityObject rarity = new RarityObject(name, weight, prefix, priceMod);
    			Variables.RarityList.add(rarity);
    		}
    		Collections.sort(Variables.RarityList);
        	
    		//Reloading Areas
    		Map<String, Object> areaMap = Setup.config.getConfigurationSection("areas").getValues(false);
    		for(final String key : areaMap.keySet()) {
    			String name = key;    			
    			List<String> biomes = Setup.config.getStringList("areas." + key + ".Biomes");
    			
    			AreaObject area = new AreaObject(name, biomes);
    			Variables.AreaList.add(area);
    		}  
    		
    		List<FishObject> fishList = new ArrayList<>();
    		
    	
    		//Getting all caught fish
    		for (File file : new File(Setup.dataFolder.getAbsolutePath() + File.separator + "fish data").listFiles()) {    		
        		try { //Finds the file        				
        		    ObjectInputStream input = new ObjectInputStream(new FileInputStream (file.getPath()));
        		    //Adds all fish to the caught fish list
        		    Variables.CaughtFish.addAll((List<FishObject>)input.readObject());
        		    //Adds all fish to the FishDict
        		    Variables.FishDict.clear();
        		    Variables.CaughtFish.forEach(f -> {
        		    	Variables.AddToFishDict(f);
        		    });
        		    input.close();
        		} catch (IOException | ClassNotFoundException ex) {
        			sender.sendMessage("Loading failed. Report this to your admin.");
        			ex.printStackTrace();
        		}  
        		
        	}
    		
    		
    		//Sets the total weight of rarities and fish
    		for(final RarityObject rarity : Variables.RarityList)
        		Variables.RarityTotalWeight += rarity.Weight;

        	for(final FishObject fish : Variables.FishList) 
        		Variables.FishTotalWeight += fish.Weight;
        	
        	
        	sender.sendMessage("reload complete");
        	FixOld();
        	
    	}else {
    		sender.sendMessage("Your config is incorrect");
    	}	
		
		return true;
	}
	
	private static void FixOld() {
		for(FishObject fish : Variables.CaughtFish) {
				for(AreaObject area: Variables.AreaList ) {
					if(area.Name.equals(fish.Area) && area.Biomes.equals(fish.Area)) {
						Bukkit.broadcastMessage("Area Found: " + area.Name);
						break;
				}
			}
		}
	}
	
	
	
}
