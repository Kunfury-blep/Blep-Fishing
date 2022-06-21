package com.kunfury.blepFishing.Miscellaneous;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicReferenceArray;

import com.kunfury.blepFishing.AllBlue.AllBlueVars;
import com.kunfury.blepFishing.DisplayFishInfo;
import com.kunfury.blepFishing.Objects.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import com.kunfury.blepFishing.Setup;

import com.kunfury.blepFishing.Tournament.Tournament;

public class Reload {

	CommandSender sender;

	public boolean ReloadPlugin(CommandSender s) {
		sender = s;
		Setup.setup.reloadConfig();
    	Setup.config = Setup.setup.getConfig();

    	Set<String> existCheck = Setup.config.getConfigurationSection("").getKeys(false); //Gets all the config-
		
    	if(existCheck.contains("fish") && existCheck.contains("rarities") ) {
    		//Reset all variables to reduce crashing
    		Variables.BaseFishList.clear();
        	Variables.RarityList.clear();
        	Variables.AreaList.clear();
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

    			int minHeight = Setup.config.getInt("fish." + key + ".Min Height");
    			int maxHeight = Setup.config.getInt("fish." + key + ".Max Height");


    			BaseFishObject base = new BaseFishObject(key, lore, minSize, maxSize, modelData, raining, baseCost, area, minHeight, maxHeight)
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
    			String compassHint = Setup.config.getString("areas." + key + ".Compass Hint");
    			boolean hasCompass = Setup.config.getBoolean("areas." + key + ".Has Compass");

    			AreaObject area = new AreaObject(key, biomes, compassHint, hasCompass);
    			Variables.AreaList.add(area);
    		}

    		//Sets the total weight of rarities and fish
    		for(final RarityObject rarity : Variables.RarityList)
        		Variables.RarityTotalWeight += rarity.Weight;

        	for(final BaseFishObject fish : Variables.BaseFishList) 
        		Variables.FishTotalWeight += fish.Weight;
        	
        	String t = Setup.config.getString("Currency Symbol");
        	if(t != null)
        		Variables.CurrSym = t;


        	Variables.HighPriority = Setup.config.getBoolean("High Priority");
        	Variables.TournamentOnly = Setup.config.getBoolean("Tournament Only");
        	Variables.RequireAreaPerm = Setup.config.getBoolean("Area Permissions");
        	Variables.WorldsWhitelist = Setup.config.getBoolean("World Whitelist");
        	Variables.AllowedWorlds = Setup.config.getStringList("Allowed Worlds");
        	Variables.LegendaryFishAnnounce = Setup.config.getBoolean("Announce Legendary");

			DisplayFishInfo.InfoScoreboard = Setup.config.getBoolean("Show ScoreBoard");
			DisplayFishInfo.InfoChat = Setup.config.getBoolean("Show Chat");

        	Variables.AllowWanderingTraders = Setup.config.getBoolean("Allow Wandering Traders");
        	if(Variables.AllowWanderingTraders) Variables.TraderMod = Setup.config.getDouble("Wandering Traders Modifier");

        	String chatPrefix = Setup.config.getString("Chat Prefix");
        	if(chatPrefix == null) chatPrefix = "&b[BF]&f ";
        	Variables.Prefix = ChatColor.translateAlternateColorCodes('&', chatPrefix);
        	String langSymbol = Setup.config.getString("Language Symbol");
        	Locale locale = Locale.ENGLISH;
        	if(langSymbol != null && !langSymbol.trim().isEmpty())
        		locale = new Locale(langSymbol);
        	Variables.Messages = ResourceBundle.getBundle("resource", locale);

        	sender.sendMessage(Variables.Prefix + Variables.Messages.getString("reloaded"));
        	//FixOld();

			AllBlueVars.TreasureEnabled = Setup.config.getBoolean("Enable Treasure");
			if(AllBlueVars.TreasureEnabled) AllBlueVars.TreasureChance = Setup.config.getInt("Treasure Chance");
			Variables.UseEconomy = Setup.config.getBoolean("Use Economy");
			Variables.EnableFishBags = Setup.config.getBoolean("Enable Fish Bags");

			//TODO: Add these config options to the website

			AllBlueVars.Enabled = Setup.config.getBoolean("Enable All Blue");
			AllBlueVars.Permanent = Setup.config.getBoolean("Permanent All Blue");
			AllBlueVars.AllBlueFishCount = Setup.config.getInt("All Blue Fish");
			AllBlueVars.AllBlueName = Setup.config.getString("All Blue Name");

			//Getting all caught fish
			LoadFishDict();

			//Get all tournaments
			LoadTournaments();

			//Get all active All Blue
			LoadAllBlue();
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
		new Tournament().Initialize();
	}

	@SuppressWarnings("unchecked")
	private void LoadFishDict(){
		try {
			String dictPath = Setup.dataFolder + "/fish.data";
			ObjectInputStream input = null;
			File tempFile = new File(dictPath);
			if(tempFile.exists()) {
				input = new ObjectInputStream(new FileInputStream (dictPath));
				Variables.FishDict = (HashMap<String, List<FishObject>>) input.readObject();
			}
			if(input != null)
				input.close();
		} catch (IOException | ClassNotFoundException ex) {
			sender.sendMessage("Loading of fish Failed");
			ex.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void LoadAllBlue(){
		AllBlueVars.AllBlueList = new ArrayList<>();
		try {
			String path = Setup.dataFolder + "/AllBlue.data";
			ObjectInputStream input = null;
			File tempFile = new File(path);
			if(tempFile.exists()) {
				input = new ObjectInputStream(new FileInputStream (path));
				AllBlueVars.AllBlueList = (List<AllBlueObject>) input.readObject();
			}
			if(input != null)
				input.close();
		} catch (IOException | ClassNotFoundException ex) {
			sender.sendMessage("Loading of all blue Failed");
			ex.printStackTrace();
		}
	}


}
