package com.kunfury.blepFishing.Config;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import com.kunfury.blepFishing.Endgame.EndgameVars;
import com.kunfury.blepFishing.Endgame.TreasureHandler;
import com.kunfury.blepFishing.DisplayFishInfo;
import com.kunfury.blepFishing.Miscellaneous.ItemSerializer;
import com.kunfury.blepFishing.Miscellaneous.Variables;
import com.kunfury.blepFishing.Objects.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.kunfury.blepFishing.Setup;

import com.kunfury.blepFishing.Tournament.Tournament;
import org.bukkit.inventory.ItemStack;

public class Reload {

	CommandSender sender;

	public boolean ReloadPlugin(CommandSender s) {
		sender = s;
		Setup.setup.reloadConfig();
    	Setup.config = Setup.setup.getConfig();

    	if(Setup.config.getKeys(false).size() == 0){
			Bukkit.getLogger().warning("No/Empty Config for Blep Fishing! Blep Fishing has been disabled.");
			Setup.getPlugin().getServer().getPluginManager().disablePlugin(Setup.getPlugin());
			return false;
		}

    	Set<String> existCheck = Setup.config.getConfigurationSection("").getKeys(false); //Gets all the config-
		
    	if(existCheck.contains("fish") && existCheck.contains("rarities") ) {
    		//Reset all variables to reduce crashing
    		Variables.BaseFishList.clear();
        	Variables.RarityList.clear();
        	Variables.AreaList.clear();
        	Variables.RarityTotalWeight = 0;
        	Variables.FishTotalWeight = 0;

        	LoadResourceBundle();
        	
        	LoadFish();

    		
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
				biomes.replaceAll(String::toUpperCase);
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


        	sender.sendMessage(Variables.Prefix + Variables.getMessage("reloaded"));
        	//FixOld();

			EndgameVars.TreasureEnabled = Setup.config.getBoolean("Enable Treasure");
			if(EndgameVars.TreasureEnabled) EndgameVars.TreasureChance = Setup.config.getInt("Treasure Chance");
			Setup.econEnabled = Setup.config.getBoolean("Use Economy");
			Variables.EnableFishBags = Setup.config.getBoolean("Enable Fish Bags");

			EndgameVars.Enabled = Setup.config.getBoolean("Enable All Blue");
			EndgameVars.Permanent = Setup.config.getBoolean("Permanent All Blue");
			EndgameVars.AvailableFish = Setup.config.getInt("All Blue Fish");
			EndgameVars.AreaName = Setup.config.getString("All Blue Name");


			EndgameVars.AreaRadius = Setup.config.getInt("Endgame Radius");
			String areaStr = Setup.config.getString("Endgame Area");
			EndgameVars.EndgameArea =  AreaObject.FromString(areaStr); //TODO: Add to web panel

			Variables.ParrotBonus = Setup.config.getDouble("Parrot Treasure Bonus");
			Variables.BoatBonus = Setup.config.getDouble("Boat Treasure Bonus");

			FixFileLocations();

			LoadTournaments();

			LoadAllBlue();

			LoadTreasure();

			LoadCollections();
    	}
		
		return true;
	}

	/**
	 * Used to update the previous data save locations
	 */
	private void FixFileLocations(){

		try {
			Files.createDirectories(Paths.get(Setup.dataFolder + "/Data"));

			if(new File(Setup.dataFolder + "/tournaments.data").exists()){
				Files.move(Paths.get(Setup.dataFolder + "/tournaments.data"),
						Paths.get(Setup.dataFolder + "/Data/" + "/tournaments.data"));
			}

			if(new File(Setup.dataFolder + "/fish.data").exists()){
				Files.move(Paths.get(Setup.dataFolder + "/fish.data"),
						Paths.get(Setup.dataFolder + "/Data/" + "/fish.data"));
			}

			if(new File(Setup.dataFolder + "/endgameArea.data").exists()){
				Files.move(Paths.get(Setup.dataFolder + "/endgameArea.data"),
						Paths.get(Setup.dataFolder + "/Data/" + "/endgameArea.data"));
			}

			//Needed due to changing of file name in v1.10.1
			if(new File(Setup.dataFolder + "/AllBlue.data").exists()){
				Files.move(Paths.get(Setup.dataFolder + "/AllBlue..data"),
						Paths.get(Setup.dataFolder + "/Data/" + "/endgameArea.data"));
			}

			if(new File(Setup.dataFolder + "/markets.data").exists()){
				Files.move(Paths.get(Setup.dataFolder + "/markets.data"),
						Paths.get(Setup.dataFolder + "/Data/" + "/markets.data"));
			}

			if(new File(Setup.dataFolder + "/signs.data").exists()){
				Files.move(Paths.get(Setup.dataFolder + "/signs.data"),
						Paths.get(Setup.dataFolder + "/Data/" + "/signs.data"));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void LoadTournaments() {
		List<TournamentObject> tourneys = new ArrayList<>();
		
		//Gets Tournaments from file
		try {
			Files.createDirectories(Paths.get(Setup.dataFolder + "/Data"));
			String tourneyPath = Setup.dataFolder + "/Data/" + "/tournaments.data";
        	ObjectInputStream input = null;
		    File tempFile = new File(tourneyPath);
		    if(tempFile.exists()) {
    		    input = new ObjectInputStream(new FileInputStream (tourneyPath));
    		    tourneys = (List<TournamentObject>) input.readObject();
		    }
		    if(input != null)
		    	input.close();
		} catch (IOException | ClassNotFoundException ex) {
			sender.sendMessage(Variables.Prefix + "Loading of tournaments Failed");
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
	private void LoadAllBlue(){
		EndgameVars.AllBlueList = new ArrayList<>();
		try {
			String path = Setup.dataFolder + "/Data/" + "/endgameArea.data";
			ObjectInputStream input = null;
			File tempFile = new File(path);
			if(tempFile.exists()) {
				input = new ObjectInputStream(new FileInputStream (path));
				EndgameVars.AllBlueList = (List<AllBlueObject>) input.readObject();
			}
			if(input != null)
				input.close();
		} catch (IOException | ClassNotFoundException ex) {
			sender.sendMessage(Variables.Prefix + "Loading of all blue Failed");
			ex.printStackTrace();
		}
	}

	private void LoadTreasure(){
		TreasureHandler.CasketList = new ArrayList<>();
		var configSection = Setup.config.getConfigurationSection("treasure");
		if(configSection != null){
			Map<String, Object> treasureMap = configSection.getValues(false); //Haha, treasure map. Classic.
			for(final String key : treasureMap.keySet()) {
				int weight = Setup.config.getInt("treasure." + key + ".Weight");
				String prefix = Setup.config.getString("treasure." + key + ".Prefix");
				int modelData = Setup.config.getInt("treasure." + key + ".ModelData");
				String path = "treasure." + key + ".Drop Table";
				List<String> dtList = Setup.config.getStringList(path);

				List<ItemStack> itemStacks = new ArrayList<>();
				try {
					for(var i : dtList){
						itemStacks.add(ItemSerializer.parseItem(i));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

				CasketObject treasure = new CasketObject(key, weight, prefix, modelData, itemStacks);
				TreasureHandler.CasketList.add(treasure);
			}
			Collections.sort(TreasureHandler.CasketList);
		}
		TreasureHandler.CasketTotalWeight = 0;
		for(final CasketObject casket : TreasureHandler.CasketList)
			TreasureHandler.CasketTotalWeight += casket.Weight;
	}

	private void LoadCollections(){
		Variables.CollectionLogs = new ArrayList<>();
		try {
			String path = Setup.dataFolder + "/Data/" + "/collections.data";
			ObjectInputStream input = null;
			File tempFile = new File(path);
			if(tempFile.exists()) {
				input = new ObjectInputStream(new FileInputStream (path));
				Variables.CollectionLogs = (List<CollectionLogObject>) input.readObject();

				for(var c : Variables.CollectionLogs){
					c.UpdateOldFormats();
				}

			}
			if(input != null)
				input.close();
		} catch (IOException | ClassNotFoundException ex) {
			sender.sendMessage(Variables.Prefix + "Loading of Collection Logs Failed");
			ex.printStackTrace();
		}
	}

	private void LoadResourceBundle(){
		String langSymbol = Setup.config.getString("Language Symbol");
		Locale locale = Locale.ENGLISH;
		if(langSymbol != null && !langSymbol.trim().isEmpty())
			locale = new Locale(langSymbol);

		ResourceBundle bundle = ResourceBundle.getBundle("resource", locale);

		if(bundle == null){
			Bukkit.getLogger().warning("[BF] Failed to load Resource Bundle.");
		}

		Variables.setMessagesBundle(bundle);
	}

	private void LoadFish(){
		Map<String, Object> fishMap = Setup.config.getConfigurationSection("fish").getValues(false);
		for(final String key : fishMap.keySet()) {
			String lore = Setup.config.getString("fish." + key + ".Lore");
			double minSize = (Setup.config.getDouble("fish." + key + ".Min Size"));
			double maxSize = Setup.config.getDouble("fish." + key + ".Max Size");
			int modelData = Setup.config.getInt("fish." + key + ".ModelData");
			boolean raining = Setup.config.getBoolean("fish." + key + ".Raining");
			int weight = Setup.config.getInt("fish." + key + ".Weight");
			double baseCost = Setup.config.getDouble("fish." + key + ".Base Price");

			int minHeight = Setup.config.getInt("fish." + key + ".Min Height");
			int maxHeight = Setup.config.getInt("fish." + key + ".Max Height");

			String area = Setup.config.getString("fish." + key + ".Area");
			List<String> areas = Setup.config.getStringList("fish." + key + ".Areas");
			if(areas == null || areas.size() <= 0){
				areas = new ArrayList<>();
				areas.add(area);
			}


			BaseFishObject base = new BaseFishObject(key, lore, minSize, maxSize, modelData, raining, baseCost, areas, minHeight, maxHeight)
					.weight(weight);
			Variables.BaseFishList.add(base);
		}

		try {
			String dictPath = Setup.dataFolder + "/Data/" +  "/fish.data";
			ObjectInputStream input = null;
			File tempFile = new File(dictPath);
			if(tempFile.exists()) {
				input = new ObjectInputStream(new FileInputStream (dictPath));
				Variables.FishDict = (HashMap<String, List<FishObject>>) input.readObject();
			}
			if(input != null)
				input.close();
		} catch (IOException | ClassNotFoundException ex) {
			sender.sendMessage(Variables.Prefix + "Loading of fish Failed");
			ex.printStackTrace();
		}

	}
}
