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
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Miscellaneous.ItemHandler;
import com.kunfury.blepFishing.Objects.*;
import com.kunfury.blepFishing.Quests.QuestHandler;
import com.kunfury.blepFishing.Quests.QuestObject;
import com.kunfury.blepFishing.Tournament.TournamentHandler;
import com.kunfury.blepFishing.Tournament.TournamentMode;
import com.kunfury.blepFishing.Tournament.TournamentObject;
import com.kunfury.blepFishing.Tournament.TournamentType;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.command.CommandSender;

import com.kunfury.blepFishing.BlepFishing;
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
    	BlepFishing.config = BlepFishing.blepFishing.getConfig();

    	if(BlepFishing.config.getKeys(false).size() == 0){
			SendError("No/Empty Config for Blep Fishing! Blep Fishing has been disabled.", null);
			return;
		}


		if(!LoadMessages()){
			SendError("Out of date messages.yml file! Blep Fishing has been disabled.",
						"Update yours at https://kunfury-blep.github.io/Messages.html");
			return;
		}

		FixFileLocations(); //Quick fix for updating file formats

//		Variables.HighPriority = BlepFishing.config.getBoolean("High Priority");
//		BlepFishing.econEnabled = BlepFishing.config.getBoolean("Use Economy");
//		Variables.TournamentOnly = BlepFishing.config.getBoolean("Tournament Only");
//		Variables.RequireAreaPerm = BlepFishing.config.getBoolean("Area Permissions");
//		Variables.WorldsWhitelist = BlepFishing.config.getBoolean("World Whitelist");
//		Variables.LegendaryFishAnnounce = BlepFishing.config.getBoolean("Announce Legendary");
//		Variables.AllowWanderingTraders = BlepFishing.config.getBoolean("Allow Wandering Traders");
//		Variables.EnableFishBags = BlepFishing.config.getBoolean("Enable Fish Bags");
//		Variables.Teasers = BlepFishing.config.getBoolean("Enable Teasers");
//		Variables.Patrons = BlepFishing.config.getBoolean("Enable Patrons");

//		DisplayFishInfo.InfoScoreboard = BlepFishing.config.getBoolean("Show ScoreBoard");
//		DisplayFishInfo.InfoChat = BlepFishing.config.getBoolean("Show Chat");

		//EndgameVars.TreasureEnabled = BlepFishing.config.getBoolean("Enable Treasure");
//		EndgameVars.Enabled = BlepFishing.config.getBoolean("Enable All Blue");
//		EndgameVars.Permanent = BlepFishing.config.getBoolean("Permanent All Blue");

//		Variables.AllowedWorlds = BlepFishing.config.getStringList("Allowed Worlds");
//		Variables.DayReset = BlepFishing.config.getString("New Day Time");

//		if (ConfigBase.  Variables.AllowWanderingTraders)
//			Variables.TraderMod = BlepFishing.config.getDouble("Wandering Traders Modifier");

//		if (EndgameVars.TreasureEnabled)
//			EndgameVars.TreasureChance = BlepFishing.config.getInt("Treasure Chance");



//		EndgameVars.AvailableFish = BlepFishing.config.getInt("All Blue Fish");
//		EndgameVars.MobSpawnChance = BlepFishing.config.getDouble("Endgame Mob Chance");

//		EndgameVars.AreaRadius = BlepFishing.config.getInt("Endgame Radius");

//		Variables.ParrotBonus = BlepFishing.config.getDouble("Parrot Treasure Bonus");
//		Variables.BoatBonus = BlepFishing.config.getDouble("Boat Treasure Bonus");

//		QuestHandler.MaxQuests = BlepFishing.config.getInt("Max Quests");
		//QuestHandler.isActive = BlepFishing.config.getBoolean("Enable Quests");
//		QuestHandler.announceQuests = BlepFishing.config.getBoolean("Announce Quests");

		LoadFish();
		LoadRarities();
		LoadAreas();
		LoadAllBlue();
		LoadCaskets();
		LoadCollections();
		LoadTournaments();
		LoadItems();
		LoadQuests();

		String areaStr = BlepFishing.config.getString("Endgame Area");
		EndgameVars.EndgameArea = AreaObject.FromString(areaStr);

		success = true;
		Variables.setPrefix(Formatting.getMessage("System.prefix"));
		sender.sendMessage(Formatting.getFormattedMesage("System.reload"));

	}

	/**
	 * Used to update the previous data save locations
	 */
	private void FixFileLocations(){

		try {
			Files.createDirectories(Paths.get(BlepFishing.dataFolder + "/Data"));

			if(new File(BlepFishing.dataFolder + "/tournaments.data").exists()){
				Files.move(Paths.get(BlepFishing.dataFolder + "/tournaments.data"),
						Paths.get(BlepFishing.dataFolder + "/Data/" + "/tournaments.data"));
			}

			if(new File(BlepFishing.dataFolder + "/fish.data").exists()){
				Files.move(Paths.get(BlepFishing.dataFolder + "/fish.data"),
						Paths.get(BlepFishing.dataFolder + "/Data/" + "/fish.data"));
			}

			if(new File(BlepFishing.dataFolder + "/endgameArea.data").exists()){
				Files.move(Paths.get(BlepFishing.dataFolder + "/endgameArea.data"),
						Paths.get(BlepFishing.dataFolder + "/Data/" + "/endgameArea.data"));
			}

			//Needed due to changing of file name in v1.10.1
			if(new File(BlepFishing.dataFolder + "/AllBlue.data").exists()){
				Files.move(Paths.get(BlepFishing.dataFolder + "/AllBlue..data"),
						Paths.get(BlepFishing.dataFolder + "/Data/" + "/endgameArea.data"));
			}

			if(new File(BlepFishing.dataFolder + "/markets.data").exists()){
				Files.move(Paths.get(BlepFishing.dataFolder + "/markets.data"),
						Paths.get(BlepFishing.dataFolder + "/Data/" + "/markets.data"));
			}

			if(new File(BlepFishing.dataFolder + "/signs.data").exists()){
				Files.move(Paths.get(BlepFishing.dataFolder + "/signs.data"),
						Paths.get(BlepFishing.dataFolder + "/Data/" + "/signs.data"));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void LoadAllBlue(){
		EndgameVars.AllBlueList = new ArrayList<>();
		try {
			String path = BlepFishing.dataFolder + "/Data/" + "/endgameArea.data";
			ObjectInputStream input = null;
			File tempFile = new File(path);
			if(tempFile.exists()) {
				input = new ObjectInputStream(new FileInputStream (path));
				EndgameVars.AllBlueList = (List<AllBlueObject>) input.readObject();
			}
			if(input != null)
				input.close();
		} catch (IOException | ClassNotFoundException ex) {
			sender.sendMessage(Variables.getPrefix(true) + "Loading of all blue Failed"); //TODO: Add to messages.yml
			ex.printStackTrace();
		}
	}

	private void LoadCaskets(){
		TreasureHandler.CasketList = new ArrayList<>();
		var configSection = BlepFishing.config.getConfigurationSection("treasure");
		if(configSection != null){
			Map<String, Object> treasureMap = configSection.getValues(false); //Haha, treasure map. Classic.
			for(final String key : treasureMap.keySet()) {
				int weight = BlepFishing.config.getInt("treasure." + key + ".Weight");
				String prefix = BlepFishing.config.getString("treasure." + key + ".Prefix");
				int modelData = BlepFishing.config.getInt("treasure." + key + ".ModelData");
				String path = "treasure." + key + ".Drop Table";
				List<String> dtList = BlepFishing.config.getStringList(path);

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
			String path = BlepFishing.dataFolder + "/Data/" + "/collections.data";
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
			sender.sendMessage(Variables.getPrefix(true) + "Loading of Collection Logs Failed"); //TODO: Add to messages.yml
			ex.printStackTrace();
		}
	}

	private void LoadFish(){
		Variables.BaseFishList.clear();
		Map<String, Object> fishMap = BlepFishing.config.getConfigurationSection("fish").getValues(false);
		for(final String key : fishMap.keySet()) {
			String lore = BlepFishing.config.getString("fish." + key + ".Lore");
			double minSize = (BlepFishing.config.getDouble("fish." + key + ".Min Size"));
			double maxSize = BlepFishing.config.getDouble("fish." + key + ".Max Size");
			int modelData = BlepFishing.config.getInt("fish." + key + ".ModelData");
			boolean raining = BlepFishing.config.getBoolean("fish." + key + ".Raining");
			int weight = BlepFishing.config.getInt("fish." + key + ".Weight");
			double baseCost = BlepFishing.config.getDouble("fish." + key + ".Base Price");

			int minHeight = BlepFishing.config.getInt("fish." + key + ".Min Height");
			int maxHeight = BlepFishing.config.getInt("fish." + key + ".Max Height");

			String timeStr = BlepFishing.config.getString("fish." + key + ".Time");

			FishTime time = FishTime.ALL;

			if(timeStr != null)
				time = FishTime.valueOf(timeStr);


			String area = BlepFishing.config.getString("fish." + key + ".Area");
			List<String> areas = BlepFishing.config.getStringList("fish." + key + ".Areas");
			if(areas.size() == 0){
				areas = new ArrayList<>();
				areas.add(area);
			}


			BaseFishObject base = new BaseFishObject(key, lore, minSize, maxSize, modelData, raining, baseCost, areas, minHeight, maxHeight, time)
					.weight(weight);
			Variables.BaseFishList.add(base);
		}

		try {
			String dictPath = BlepFishing.dataFolder + "/Data/" +  "/fish.data";
			ObjectInputStream input = null;
			File tempFile = new File(dictPath);
			if(tempFile.exists()) {
				input = new ObjectInputStream(new FileInputStream (dictPath));
				Variables.FishDict = (HashMap<String, List<FishObject>>) input.readObject();
			}
			if(input != null)
				input.close();
		} catch (IOException | ClassNotFoundException ex) {
			sender.sendMessage(Variables.getPrefix(true) + "Loading of fish Failed");
			ex.printStackTrace();
		}

		Variables.FishTotalWeight = 0;
		for (final BaseFishObject fish : Variables.BaseFishList)
			Variables.FishTotalWeight += fish.Weight;

	}

	private void LoadTournaments(){
		if(!BlepFishing.configBase.getEnableTournaments()){ return; }

		//Loads Active Tournaments from file
		List<TournamentObject> tObjs = TournamentHandler.TournamentList;

		TournamentHandler.Reset(false);
		try {
			Files.createDirectories(Paths.get(BlepFishing.dataFolder + "/Data"));
			String tourneyPath = BlepFishing.dataFolder + "/Data/" + "/tournaments.data";
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
			sender.sendMessage(Variables.getPrefix(true) + "Loading of tournaments Failed"); //TODO: Add to messages.yml
			ex.printStackTrace();
		}

		for(var a : TournamentHandler.ActiveTournaments){
			new TournamentHandler().AddTournament(a);
			if(a.UseBossbar) a.CreateBossbar();
		}

		//Checks tournament config file and ensures they are added
		var tourneyConfigFile = new File(BlepFishing.blepFishing.getDataFolder(), "tournaments.yml");
		if (!tourneyConfigFile.exists()) {
			BlepFishing.getPlugin().saveResource("tournaments.yml", false);
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

		File cacheFile = new File(BlepFishing.getPlugin().getDataFolder(), "cache.json");
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
		double version = 1.4;

		var messageConfigFile = new File(BlepFishing.blepFishing.getDataFolder(), "messages.yml");
		if (!messageConfigFile.exists()) {
			BlepFishing.getPlugin().saveResource("messages.yml", false);
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
		var itemsConfigFile = new File(BlepFishing.blepFishing.getDataFolder(), "items.yml");
		if (!itemsConfigFile.exists()) {
			BlepFishing.getPlugin().saveResource("items.yml", false);
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
		var questsConfigFile = new File(BlepFishing.blepFishing.getDataFolder(), "quests.yml");
		if (!questsConfigFile.exists()) {
			BlepFishing.getPlugin().saveResource("quests.yml", false);
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
			Files.createDirectories(Paths.get(BlepFishing.dataFolder + "/Data"));
			String questPath = BlepFishing.dataFolder + "/Data/" + "/quests.data";
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
			sender.sendMessage(Variables.getPrefix(true) + "Loading of Active Quests Failed");
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

			BaseFishObject baseFish = BaseFishObject.getBase(fishName);

			if(baseFish == null && !fishName.equalsIgnoreCase("ALL") && !fishName.equalsIgnoreCase("ANY")){
				Variables.AddError("Error adding quest " + key + " due to unrecognized fish name: " + fishName);
				continue;
			}

			QuestObject quest = new QuestObject(key, amount, fishName, maxSize, minSize, duration, cooldown, rewards, announceProgress);
			QuestHandler.AddQuest(quest);


		}

		File cacheFile = new File(BlepFishing.getPlugin().getDataFolder(), "cache.json");
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
		Map<String, Object> rarityMap = BlepFishing.config.getConfigurationSection("rarities").getValues(false);
		for (final String key : rarityMap.keySet()) {
			int weight = BlepFishing.config.getInt("rarities." + key + ".Weight");
			String prefix = BlepFishing.config.getString("rarities." + key + ".Color Code");
			double priceMod = BlepFishing.config.getDouble("rarities." + key + ".Price Mod");

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
		Map<String, Object> areaMap = BlepFishing.config.getConfigurationSection("areas").getValues(false);
		for (final String key : areaMap.keySet()) {
			List<String> origBiomes = BlepFishing.config.getStringList("areas." + key + ".Biomes");

			List<String> biomes = new ArrayList<>();
			for (var b : origBiomes) {
				if (b.startsWith("minecraft:"))
					b = b.replace("minecraft:", "");
				biomes.add(b.toUpperCase());
			}
			String compassHint = BlepFishing.config.getString("areas." + key + ".Compass Hint");
			boolean hasCompass = BlepFishing.config.getBoolean("areas." + key + ".Has Compass");

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

		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(BlepFishing.getPlugin(), () -> {
			Bukkit.broadcastMessage("Blep Fishing Disabled. Please Check Server Console for Error.");
			BlepFishing.getPlugin().getServer().getPluginManager().disablePlugin(BlepFishing.getPlugin());
		}, 0, 100);
	}

}
