package com.kunfury.blepFishing.Config;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;

import com.kunfury.blepFishing.Objects.CollectionLogObject;
import com.kunfury.blepFishing.Endgame.EndgameVars;
import com.kunfury.blepFishing.Endgame.TreasureHandler;
import com.kunfury.blepFishing.DisplayFishInfo;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Miscellaneous.ItemHandler;
import com.kunfury.blepFishing.Objects.*;
import com.kunfury.blepFishing.Quests.QuestHandler;
import com.kunfury.blepFishing.Quests.QuestObject;
import com.kunfury.blepFishing.Tournament.TournamentHandler;
import com.kunfury.blepFishing.Tournament.TournamentObject;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.command.CommandSender;

import com.kunfury.blepFishing.Setup;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;

public class Reload {

	CommandSender sender;
	public static boolean success;

	public void ReloadPlugin(CommandSender s) {
		sender = s;
		success = false;
    	Setup.config = Setup.setup.getConfig();

    	if(Setup.config.getKeys(false).size() == 0){
			SendError("No/Empty Config for Blep Fishing! Blep Fishing has been disabled.", null);
			return;
		}


		if(!LoadMessages()){
			SendError("Out of date messages.yml file! Blep Fishing has been disabled.",
						"Update yours at https://kunfury-blep.github.io/Messages.html");
			return;
		}

		FixFileLocations(); //Quick fix for updating file formats

		Variables.HighPriority = Setup.config.getBoolean("High Priority");
		Setup.econEnabled = Setup.config.getBoolean("Use Economy");
		Variables.TournamentOnly = Setup.config.getBoolean("Tournament Only");
		Variables.RequireAreaPerm = Setup.config.getBoolean("Area Permissions");
		Variables.WorldsWhitelist = Setup.config.getBoolean("World Whitelist");
		Variables.LegendaryFishAnnounce = Setup.config.getBoolean("Announce Legendary");
		Variables.AllowWanderingTraders = Setup.config.getBoolean("Allow Wandering Traders");
		Variables.EnableFishBags = Setup.config.getBoolean("Enable Fish Bags");
		Variables.Teasers = Setup.config.getBoolean("Enable Teasers");

		DisplayFishInfo.InfoScoreboard = Setup.config.getBoolean("Show ScoreBoard");
		DisplayFishInfo.InfoChat = Setup.config.getBoolean("Show Chat");

		EndgameVars.TreasureEnabled = Setup.config.getBoolean("Enable Treasure");
		EndgameVars.Enabled = Setup.config.getBoolean("Enable All Blue");
		EndgameVars.Permanent = Setup.config.getBoolean("Permanent All Blue");

		Variables.AllowedWorlds = Setup.config.getStringList("Allowed Worlds");
		Variables.DayReset = Setup.config.getString("New Day Time");

		if (Variables.AllowWanderingTraders)
			Variables.TraderMod = Setup.config.getDouble("Wandering Traders Modifier");

		if (EndgameVars.TreasureEnabled)
			EndgameVars.TreasureChance = Setup.config.getInt("Treasure Chance");



		EndgameVars.AvailableFish = Setup.config.getInt("All Blue Fish");
		EndgameVars.MobSpawnChance = Setup.config.getDouble("Endgame Mob Chance");

		EndgameVars.AreaRadius = Setup.config.getInt("Endgame Radius");

		Variables.ParrotBonus = Setup.config.getDouble("Parrot Treasure Bonus");
		Variables.BoatBonus = Setup.config.getDouble("Boat Treasure Bonus");

		QuestHandler.MaxQuests = Setup.config.getInt("Max Quests");
		QuestHandler.isActive = Setup.config.getBoolean("Enable Quests");
		QuestHandler.announceQuests = Setup.config.getBoolean("Announce Quests");

		LoadFish();
		LoadRarities();
		LoadAreas();
		LoadAllBlue();
		LoadCaskets();
		LoadCollections();
		LoadTournaments();
		LoadItems();
		LoadQuests();

		String areaStr = Setup.config.getString("Endgame Area");
		EndgameVars.EndgameArea = AreaObject.FromString(areaStr);

		success = true;
		Variables.Prefix = Formatting.getMessage("System.prefix");
		sender.sendMessage(Variables.Prefix + Formatting.getMessage("System.reload"));

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

	private void LoadCaskets(){
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
						itemStacks.add(ItemHandler.parseItem(i));
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

	private void LoadFish(){
		Variables.BaseFishList.clear();
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

			String timeStr = Setup.config.getString("fish." + key + ".Time");

			FishTime time = FishTime.ALL;

			if(timeStr != null)
				time = FishTime.valueOf(timeStr);


			String area = Setup.config.getString("fish." + key + ".Area");
			List<String> areas = Setup.config.getStringList("fish." + key + ".Areas");
			if(areas.size() == 0){
				areas = new ArrayList<>();
				areas.add(area);
			}


			BaseFishObject base = new BaseFishObject(key, lore, minSize, maxSize, modelData, raining, baseCost, areas, minHeight, maxHeight, time)
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

		Variables.FishTotalWeight = 0;
		for (final BaseFishObject fish : Variables.BaseFishList)
			Variables.FishTotalWeight += fish.Weight;

	}

	private void LoadTournaments(){
		if(!Setup.config.getBoolean("Enable Tournaments")){
			TournamentHandler.isActive = false;
			return;
		} else
			TournamentHandler.isActive = true;

		//Loads Active Tournaments from file
		List<TournamentObject> tObjs = TournamentHandler.TournamentList;

		TournamentHandler.Reset(false);
		try {
			Files.createDirectories(Paths.get(Setup.dataFolder + "/Data"));
			String tourneyPath = Setup.dataFolder + "/Data/" + "/tournaments.data";
			ObjectInputStream input = null;
			File tempFile = new File(tourneyPath);
			if(tempFile.exists()) {
				input = new ObjectInputStream(new FileInputStream (tourneyPath));
				TournamentHandler.ActiveTournaments = (List<TournamentObject>) input.readObject();
				input.close();
			}
			if(input != null)
				input.close();
		} catch (IOException | ClassNotFoundException ex) {
			sender.sendMessage(Variables.Prefix + "Loading of tournaments Failed");
			ex.printStackTrace();
		}

		for(var a : TournamentHandler.ActiveTournaments){
			new TournamentHandler().AddTournament(a);
			if(a.UseBossbar) a.CreateBossbar();
		}

		//Checks tournament config file and ensures they are added
		var tourneyConfigFile = new File(Setup.setup.getDataFolder(), "tournaments.yml");
		if (!tourneyConfigFile.exists()) {
			Setup.getPlugin().saveResource("tournaments.yml", false);
		}

		FileConfiguration tourney = new YamlConfiguration();
		try {
			tourney.load(tourneyConfigFile);
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}

		for(final String key : tourney.getKeys(false)) {
			if(TournamentHandler.ActiveTournaments.stream().anyMatch(o -> o.getName().equals(key))){
				continue;
			}
			TournamentMode mode = TournamentMode.valueOf(tourney.getString(key + ".Mode"));
			TournamentType type = TournamentType.valueOf(tourney.getString(key + ".Type"));
			double duration = tourney.getDouble(key + ".Duration");
			String fishType = tourney.getString(key + ".Fish Type");
			boolean announceWinner = tourney.getBoolean(key + ".Announce New Winner");

			boolean useBossbar = tourney.getBoolean(key + ".Use Bossbar");
			boolean bossbarTime = tourney.getBoolean(key + ".Bossbar Timer");
			double bossbarPercent = tourney.getDouble(key + ".Bossbar Percent");
			double bossbarTimePercent = tourney.getDouble(key + ".Bossbar Timer Percent");
			int maxAmount = tourney.getInt(key + ".Max Amount");
			double startDelay = tourney.getDouble(key + ".Start Delay");
			double delay = tourney.getDouble(key + ".Cooldown");
			int minPlayers = tourney.getInt(key + ".Minimum Players");
			int minFish = tourney.getInt(key + ".Minimum Fish");

			String barString = tourney.getString(key + ".Bossbar Color");
			BarColor barColor = BarColor.PINK;

			if(barString != null && !barString.equals("")){
				barColor = BarColor.valueOf(barString.toUpperCase());
			}

			List<String> dayStrings = tourney.getStringList(key + ".Days");
			List<DayOfWeek> dayList = new ArrayList<>();
			for(var d : dayStrings){
				dayList.add(DayOfWeek.valueOf(d));
			}

			HashMap<String, List<String>> rewards = new HashMap<>();

			Map<String, Object> rewardsMap = tourney.getConfigurationSection(key + ".Rewards").getValues(false);
			for(final String spot : rewardsMap.keySet()) {
				List<String> items = tourney.getStringList(key + ".Rewards." + spot);
				rewards.put(spot.toUpperCase(), items);
			}

			List<TournamentObject> foundTourn = tObjs.stream()
					.filter(t -> t.getName().equals(key)).toList();

			LocalDateTime lastRan = LocalDateTime.MIN;
			if(foundTourn.size() > 0){
				lastRan = foundTourn.get(0).getLastRan();
			}

			new TournamentHandler().AddTournament(new TournamentObject(
					key, mode, duration, fishType, dayList, maxAmount, startDelay, minPlayers, useBossbar, bossbarPercent, barColor,
					delay, rewards, minFish, bossbarTime, bossbarTimePercent, type, announceWinner, lastRan));
		}

		File cacheFile = new File(Setup.getPlugin().getDataFolder(), "cache.json");
		if (cacheFile.exists()){
			JSONObject json = new CacheHandler().getTourneyCache();

			for(var t : TournamentHandler.TournamentList){
				if(TournamentHandler.ActiveTournaments.contains(t)){ //Skips loading cache if tournament is actively running
					continue;
				}

				if(json.containsKey(t.getName())){
					JSONObject jObj = (JSONObject) json.get(t.getName());
					t.setLastRan(LocalDateTime.parse((CharSequence) jObj.get("Last Ran")));
				}
			}
		}


	}

	private boolean LoadMessages(){
		double version = 1.2;

		var messageConfigFile = new File(Setup.setup.getDataFolder(), "messages.yml");
		if (!messageConfigFile.exists()) {
			Setup.getPlugin().saveResource("messages.yml", false);
		}

		FileConfiguration messages = new YamlConfiguration();
		try {
			messages.load(messageConfigFile);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		if(messages.getDouble("version") < version){
			return false;
		}


		Formatting.messages = messages;
		return true;
	}

	private void LoadItems(){
		var itemsConfigFile = new File(Setup.setup.getDataFolder(), "items.yml");
		if (!itemsConfigFile.exists()) {
			Setup.getPlugin().saveResource("items.yml", false);
		}
		FileConfiguration items = new YamlConfiguration();
		try {
			items.load(itemsConfigFile);
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}

		ItemsConfig.Initialize(items);
	}

	private void LoadQuests(){
		//Checks tournament config file and ensures they are added
		var questsConfigFile = new File(Setup.setup.getDataFolder(), "quests.yml");
		if (!questsConfigFile.exists()) {
			Setup.getPlugin().saveResource("quests.yml", false);
		}

		FileConfiguration questFile = new YamlConfiguration();
		try {
			questFile.load(questsConfigFile);
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}

		QuestHandler.resetQuestList();
		QuestHandler.ActiveQuests = new ArrayList<>();
		try {
			Files.createDirectories(Paths.get(Setup.dataFolder + "/Data"));
			String questPath = Setup.dataFolder + "/Data/" + "/quests.data";
			ObjectInputStream input = null;
			File tempFile = new File(questPath);
			if(tempFile.exists()) {
				input = new ObjectInputStream(new FileInputStream (questPath));
				QuestHandler.ActiveQuests = (List<QuestObject>) input.readObject();
				input.close();
			}
			if(input != null)
				input.close();
		} catch (IOException | ClassNotFoundException ex) {
			sender.sendMessage(Variables.Prefix + "Loading of Active Quests Failed");
			ex.printStackTrace();
		}

		QuestHandler.AddQuests(QuestHandler.ActiveQuests);


		for(final String key : questFile.getKeys(false)) {

			if(QuestHandler.ActiveQuests.stream().anyMatch(o -> o.getName().equals(key))){
				continue;
			}

			int amount = questFile.getInt(key + ".Amount");
			String fishName = questFile.getString(key + ".Fish Type");
			double minSize = questFile.getDouble(key + ".Min Size");
			double maxSize = questFile.getDouble(key + ".Max Size");
			double duration = questFile.getDouble(key + ".Duration");
			double cooldown = questFile.getDouble(key + ".Cooldown");
			List<String> rewards = questFile.getStringList (key + ".Rewards");
			boolean announceProgress = questFile.getBoolean(key + ".Announce Progress");

			QuestObject quest = new QuestObject(key, amount, fishName, maxSize, minSize, duration, cooldown, rewards, announceProgress);
			QuestHandler.AddQuest(quest);
		}

		File cacheFile = new File(Setup.getPlugin().getDataFolder(), "cache.json");
		if (cacheFile.exists()){
			JSONObject json = new CacheHandler().getQuestCache();
			for(var q : QuestHandler.getQuestList()){
				//TODO: Is this really needed? Seems completely unecessary
				if(QuestHandler.getQuestList().contains(q)){ //Skips loading cache if tournament is actively running
					continue;
				}

				if(json.containsKey(q.getName())){
					JSONObject jObj = (JSONObject) json.get(q.getName());
					q.setLastRan(LocalDateTime.parse((CharSequence) jObj.get("Last Ran")));
				}
			}
		}


		new QuestHandler().CheckDaySchedule();

	}

	private void LoadRarities(){
		Variables.RarityList.clear();
		Map<String, Object> rarityMap = Setup.config.getConfigurationSection("rarities").getValues(false);
		for (final String key : rarityMap.keySet()) {
			int weight = Setup.config.getInt("rarities." + key + ".Weight");
			String prefix = Setup.config.getString("rarities." + key + ".Color Code");
			double priceMod = Setup.config.getDouble("rarities." + key + ".Price Mod");

			RarityObject rarity = new RarityObject(key, weight, prefix, priceMod);
			Variables.RarityList.add(rarity);
		}
		Collections.sort(Variables.RarityList);

		Variables.RarityTotalWeight = 0;
		for (final RarityObject rarity : Variables.RarityList)
			Variables.RarityTotalWeight += rarity.Weight;
	}

	private void LoadAreas(){
		Variables.AreaList.clear();
		Map<String, Object> areaMap = Setup.config.getConfigurationSection("areas").getValues(false);
		for (final String key : areaMap.keySet()) {
			List<String> origBiomes = Setup.config.getStringList("areas." + key + ".Biomes");

			List<String> biomes = new ArrayList<>();
			for (var b : origBiomes) {
				if (b.startsWith("minecraft:"))
					b = b.replace("minecraft:", "");
				biomes.add(b.toUpperCase());
			}
			String compassHint = Setup.config.getString("areas." + key + ".Compass Hint");
			boolean hasCompass = Setup.config.getBoolean("areas." + key + ".Has Compass");

			AreaObject area = new AreaObject(key, biomes, compassHint, hasCompass);
			Variables.AreaList.add(area);


		}
	}

	private void SendError(String error, String error2){
		Bukkit.getLogger().warning("------------------------------------");
		Bukkit.getLogger().warning(" ");
		Bukkit.getLogger().warning(error);
		if(error2 != null) Bukkit.getLogger().warning(error2);
		Bukkit.getLogger().warning(" ");
		Bukkit.getLogger().warning("------------------------------------");

		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Setup.getPlugin(), () -> {
			Bukkit.broadcastMessage("Blep Fishing Disabled. Please Check Server Console for Error.");
			Setup.getPlugin().getServer().getPluginManager().disablePlugin(Setup.getPlugin());
		}, 0, 100);
	}



	private void OldConfig() {
		Set<String> existCheck = Setup.config.getConfigurationSection("").getKeys(false); //Gets all the config-

		if (existCheck.contains("fish") && existCheck.contains("rarities")) {
			//Reset all variables to reduce crashing
			Variables.BaseFishList.clear();
			Variables.RarityList.clear();
			Variables.AreaList.clear();
			Variables.RarityTotalWeight = 0;
			Variables.FishTotalWeight = 0;

			LoadFish();


//			//Reloading Rarities
//			Map<String, Object> rarityMap = Setup.config.getConfigurationSection("rarities").getValues(false);
//			for (final String key : rarityMap.keySet()) {
//				int weight = Setup.config.getInt("rarities." + key + ".Weight");
//				String prefix = Setup.config.getString("rarities." + key + ".Color Code");
//				double priceMod = Setup.config.getDouble("rarities." + key + ".Price Mod");
//
//				RarityObject rarity = new RarityObject(key, weight, prefix, priceMod);
//				Variables.RarityList.add(rarity);
//			}
//			Collections.sort(Variables.RarityList);

			//Reloading Areas
//			Map<String, Object> areaMap = Setup.config.getConfigurationSection("areas").getValues(false);
//			for (final String key : areaMap.keySet()) {
//				List<String> origBiomes = Setup.config.getStringList("areas." + key + ".Biomes");
//
//				List<String> biomes = new ArrayList<>();
//				for (var b : origBiomes) {
//					if (b.startsWith("minecraft:"))
//						b = b.replace("minecraft:", "");
//					biomes.add(b.toUpperCase());
//				}
//				String compassHint = Setup.config.getString("areas." + key + ".Compass Hint");
//				boolean hasCompass = Setup.config.getBoolean("areas." + key + ".Has Compass");
//
//				AreaObject area = new AreaObject(key, biomes, compassHint, hasCompass);
//				Variables.AreaList.add(area);
//			}

			//Sets the total weight of rarities and fish
			for (final RarityObject rarity : Variables.RarityList)
				Variables.RarityTotalWeight += rarity.Weight;

			for (final BaseFishObject fish : Variables.BaseFishList)
				Variables.FishTotalWeight += fish.Weight;

			String t = Setup.config.getString("Currency Symbol");
			if (t != null)
				Variables.CurrSym = t;

			Variables.HighPriority = Setup.config.getBoolean("High Priority");
			Variables.TournamentOnly = Setup.config.getBoolean("Tournament Only");
			Variables.RequireAreaPerm = Setup.config.getBoolean("Area Permissions");
			Variables.WorldsWhitelist = Setup.config.getBoolean("World Whitelist");
			Variables.AllowedWorlds = Setup.config.getStringList("Allowed Worlds");
			Variables.LegendaryFishAnnounce = Setup.config.getBoolean("Announce Legendary");
			Variables.DayReset = Setup.config.getString("New Day Time");

			DisplayFishInfo.InfoScoreboard = Setup.config.getBoolean("Show ScoreBoard");
			DisplayFishInfo.InfoChat = Setup.config.getBoolean("Show Chat");

			Variables.AllowWanderingTraders = Setup.config.getBoolean("Allow Wandering Traders");
			if (Variables.AllowWanderingTraders)
				Variables.TraderMod = Setup.config.getDouble("Wandering Traders Modifier");


			EndgameVars.TreasureEnabled = Setup.config.getBoolean("Enable Treasure");
			if (EndgameVars.TreasureEnabled) EndgameVars.TreasureChance = Setup.config.getInt("Treasure Chance");
			Setup.econEnabled = Setup.config.getBoolean("Use Economy");
			Variables.EnableFishBags = Setup.config.getBoolean("Enable Fish Bags");

			EndgameVars.Enabled = Setup.config.getBoolean("Enable All Blue");
			EndgameVars.Permanent = Setup.config.getBoolean("Permanent All Blue");
			EndgameVars.AvailableFish = Setup.config.getInt("All Blue Fish");
			EndgameVars.MobSpawnChance = Setup.config.getDouble("Endgame Mob Chance");

			EndgameVars.AreaRadius = Setup.config.getInt("Endgame Radius");
			String areaStr = Setup.config.getString("Endgame Area");
			EndgameVars.EndgameArea = AreaObject.FromString(areaStr);

			Variables.ParrotBonus = Setup.config.getDouble("Parrot Treasure Bonus");
			Variables.BoatBonus = Setup.config.getDouble("Boat Treasure Bonus");

			Variables.Teasers = Setup.config.getBoolean("Enable Teasers");

			QuestHandler.MaxQuests = Setup.config.getInt("Max Quests");
			QuestHandler.isActive = Setup.config.getBoolean("Enable Quests");


			FixFileLocations();

			LoadAllBlue();

			LoadCaskets();

			LoadCollections();

			LoadTournaments();

			LoadItems();

			LoadQuests();

			success = true;
			Variables.Prefix = Formatting.getMessage("System.prefix");
			sender.sendMessage(Variables.Prefix + Formatting.getMessage("System.reload"));
		}
	}

}
