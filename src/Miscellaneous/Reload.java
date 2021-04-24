package Miscellaneous;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import org.bukkit.command.CommandSender;

import com.kunfury.blepFishing.Setup;

import Objects.AreaObject;
import Objects.BaseFishObject;
import Objects.FishObject;
import Objects.RarityObject;
import Objects.TournamentObject;
import Tournament.Tournament;

public class Reload {

	CommandSender sender;
	
	@SuppressWarnings("unchecked")
	public boolean ReloadPlugin(CommandSender s) {
		sender = s;
		Setup.setup.reloadConfig();
    	Setup.config = Setup.setup.getConfig();
    	
    	Set<String> existCheck = Setup.config.getConfigurationSection("").getKeys(false); //Gets all the config
		
    	if(existCheck.contains("fish") && existCheck.contains("rarities") ) {
    		//Reset all variables to reduce crashing
    		Variables.BaseFishList.clear();
        	Variables.RarityList.clear();
        	Variables.AreaList.clear();
        	Variables.CaughtFish.clear();
        	Variables.RarityTotalWeight = 0;
        	Variables.FishTotalWeight = 0;
        	
        	//Reloading Fish
        	Map<String, Object> fishMap = Setup.config.getConfigurationSection("fish").getValues(false);
    		for(final String key : fishMap.keySet()) {
				String lore = Setup.config.getString("fish." + key + ".Lore");
    			double minSize = (Setup.config.getDouble("fish." + key + ".Min Size"));
				double maxSize = Setup.config.getDouble("fish." + key + ".Max Size");
    			int modelData = Setup.config.getInt("fish." + key + ".ModelData");
    			String area = Setup.config.getString("fish." + key + ".Area");
    			boolean raining = Setup.config.getBoolean("fish." + key + ".Raining");
    			int weight = Setup.config.getInt("fish." + key + ".Weight");
    			double baseCost = Setup.config.getDouble("fish." + key + ".Base Price");
    			
    			BaseFishObject base = new BaseFishObject(key, lore, minSize, maxSize, modelData, raining, baseCost, area)
    					.weight(weight);
    			Variables.BaseFishList.add(base);
    			
    		}
    		
    		//Reloading Rarities
        	Map<String, Object> rarityMap = Setup.config.getConfigurationSection("rarities").getValues(false);
    		for(final String key : rarityMap.keySet()) {
				int weight = Setup.config.getInt("rarities." + key + ".Weight");
    			String prefix = Setup.config.getString("rarities." + key + ".Color Code");
    			double priceMod = Setup.config.getDouble("rarities." + key + ".Price Mod");
    			
    			RarityObject rarity = new RarityObject(key, weight, prefix, priceMod);
    			Variables.RarityList.add(rarity);
    		}
    		Collections.sort(Variables.RarityList);
        	
    		//Reloading Areas
    		Map<String, Object> areaMap = Setup.config.getConfigurationSection("areas").getValues(false);
    		for(final String key : areaMap.keySet()) {
				List<String> biomes = Setup.config.getStringList("areas." + key + ".Biomes");
    			
    			AreaObject area = new AreaObject(key, biomes);
    			Variables.AreaList.add(area);
    		}
    		
    	
    		//Getting all caught fish
    		try {
    			String dictPath = Setup.dataFolder + "/fish.data";   
            	ObjectInputStream input = null;
    		    File tempFile = new File(dictPath);
    		    if(tempFile.exists()) {
        		    input = new ObjectInputStream(new FileInputStream (dictPath));
        		    Variables.FishDict = (HashMap<String, List<FishObject>>) input.readObject();
    		    	//savedFishList.addAll((List<FishObject>)input.readObject());	
    		    }
    		    if(input != null)
    		    	input.close();
    		} catch (IOException | ClassNotFoundException ex) {
    			sender.sendMessage("Loading of fish Failed");
    			ex.printStackTrace();
    		}   
    		
    		//Get all tournaments
    		LoadTournaments();
    		

    		//Sets the total weight of rarities and fish
    		for(final RarityObject rarity : Variables.RarityList)
        		Variables.RarityTotalWeight += rarity.Weight;

        	for(final BaseFishObject fish : Variables.BaseFishList) 
        		Variables.FishTotalWeight += fish.Weight;
        	
        	String t = Setup.config.getString("Currency Symbol");
        	if(t != null)
        		Variables.CSym = t;        	
        	Variables.ShowScoreboard = Setup.config.getBoolean("Show ScoreBoard");
        	Variables.HighPriority = Setup.config.getBoolean("High Priority");
        	Variables.TournamentOnly = Setup.config.getBoolean("Tournament Only");


        	String langSymbol = Setup.config.getString("Language Symbol");
        	Locale locale = Locale.ENGLISH;
        	if(langSymbol != null && !langSymbol.trim().isEmpty())
        		locale = new Locale(langSymbol);
        	Variables.Messages = ResourceBundle.getBundle("main.resources.resource", locale);

        	sender.sendMessage(Variables.Prefix + Variables.Messages.getString("reloaded"));
        	//FixOld();
        	
    	}else {
    		sender.sendMessage("Your config is incorrect");
    	}	
		
		return true;
	}
	
	@SuppressWarnings("unchecked")
	private void LoadTournaments() {
		List<TournamentObject> tourneys = new ArrayList<>();
		
		//Gets Tournaments from file
		try {
			String tourneyPath = Setup.dataFolder + "/tournaments.data";   
        	ObjectInputStream input = null;
		    File tempFile = new File(tourneyPath);
		    if(tempFile.exists()) {
    		    input = new ObjectInputStream(new FileInputStream (tourneyPath));
    		    tourneys = (List<TournamentObject>) input.readObject();
		    }
		    if(input != null)
		    	input.close();
		} catch (IOException | ClassNotFoundException ex) {
			sender.sendMessage("Loading of tournaments Failed");
			ex.printStackTrace();
		}
		
		tourneys.forEach(t -> {
			long diff = ChronoUnit.MILLIS.between(LocalDateTime.now(), t.EndDate);
			if(!t.HasFinished) {
				if(diff <= 0)
					new Tournament().DelayedWinnings(t);
				else
					new Tournament().StartTimer(diff, t);
			}
		});

		Variables.Tournaments = tourneys;
		new Tournament().CheckActiveTournaments();
	}
	
	
}
