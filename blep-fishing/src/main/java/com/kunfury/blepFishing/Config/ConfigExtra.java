package com.kunfury.blepFishing.Config;

import com.kunfury.blepFishing.BlepFishing;
import com.kunfury.blepFishing.Config.CacheHandler;
import com.kunfury.blepFishing.Config.FishTime;
import com.kunfury.blepFishing.Config.ItemsConfig;
import com.kunfury.blepFishing.Config.Variables;
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
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;

public class ConfigExtra {

    //private FileConfiguration config;
    private FileConfiguration config;
    public void Load(FileConfiguration _config){
        config = _config;
        if(!LoadMessages()){
            new ConfigHandler().SendError("Out of date messages.yml file! Blep Fishing has been disabled.",
                    "Update yours at https://kunfury-blep.github.io/Messages.html");
            return;
        }

        LoadRarities();
        LoadAreas();
        LoadAllBlue();
        LoadCaskets();
        LoadCollections();
        LoadTournaments();
        LoadItems();
        LoadQuests();

        String areaStr = config.getString("Endgame Area");
        EndgameVars.EndgameArea = AreaObject.FromString(areaStr);
        Variables.setPrefix(Formatting.getMessage("System.prefix"));
    }


    private void LoadRarities(){
        Variables.RarityList.clear();
        Map<String, Object> rarityMap = config.getConfigurationSection("rarities").getValues(false);
        for (final String key : rarityMap.keySet()) {
            int weight = config.getInt("rarities." + key + ".Weight");
            String prefix = config.getString("rarities." + key + ".Color Code");
            double priceMod = config.getDouble("rarities." + key + ".Price Mod");

            RarityObject rarity = new RarityObject(key, weight, prefix, priceMod);
            Variables.RarityList.add(rarity);
        }
        Collections.sort(Variables.RarityList);

        Variables.RarityTotalWeight = 0;
        for (final RarityObject rarity : Variables.RarityList)
            Variables.RarityTotalWeight += rarity.getWeight();
    }

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
            Bukkit.getLogger().warning(Variables.getPrefix(true) + "Loading of all blue Failed"); //TODO: Add to messages.yml
            ex.printStackTrace();
        }
    }

    private void LoadCaskets(){
        TreasureHandler.CasketList = new ArrayList<>();
        var configSection = config.getConfigurationSection("treasure");
        if(configSection != null){
            Map<String, Object> treasureMap = configSection.getValues(false); //Haha, treasure map. Classic.
            for(final String key : treasureMap.keySet()) {
                int weight = config.getInt("treasure." + key + ".Weight");
                String prefix = config.getString("treasure." + key + ".Prefix");
                int modelData = config.getInt("treasure." + key + ".ModelData");
                String path = "treasure." + key + ".Drop Table";
                List<String> dtList = config.getStringList(path);

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
            Bukkit.getLogger().warning(Variables.getPrefix(true) + "Loading of Collection Logs Failed"); //TODO: Add to messages.yml
            ex.printStackTrace();
        }
    }


    public void LoadTournaments(){
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
            Bukkit.getLogger().warning(Variables.getPrefix(true) + "Loading of tournaments Failed"); //TODO: Add to messages.yml
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
            Variables.AddError("Error reading tournaments.yml, no tournaments were able to be loaded. Please check the server console for details.");
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

        File messageConfigFile = new File(BlepFishing.blepFishing.getDataFolder(), "messages.yml");
        YamlConfiguration externalYamlConfig = YamlConfiguration.loadConfiguration(messageConfigFile);

        if (!messageConfigFile.exists()) {
            BlepFishing.getPlugin().saveResource("messages.yml", false);
        }


        if(externalYamlConfig.getDouble("version") < version){
            return false;
        }


        Formatting.messages = externalYamlConfig;
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

    public void LoadQuests(){
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
        QuestHandler.setActiveQuests(new ArrayList<>());
        try {
            Files.createDirectories(Paths.get(BlepFishing.dataFolder + "/Data"));
            String questPath = BlepFishing.dataFolder + "/Data/" + "/quests.data";
            ObjectInputStream input = null;
            File tempFile = new File(questPath);
            if(tempFile.exists()) {
                input = new ObjectInputStream(new FileInputStream (questPath));
                QuestHandler.setActiveQuests((List<QuestObject>)input.readObject());
                input.close();
            }
            if(input != null)
                input.close();
        } catch (IOException | ClassNotFoundException ex) {
            Bukkit.getLogger().warning(Variables.getPrefix(true) + "Loading of Active Quests Failed");
            ex.printStackTrace();
        }

        QuestHandler.AddQuests(QuestHandler.getActiveQuests());


        for(final String key : questFile.getKeys(false)) {

            if(QuestHandler.getActiveQuests().stream().anyMatch(o -> o.getName().equals(key))){
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

    private void LoadAreas(){
        Variables.AreaList.clear();
        Map<String, Object> areaMap = config.getConfigurationSection("areas").getValues(false);
        for (final String key : areaMap.keySet()) {
            List<String> origBiomes = config.getStringList("areas." + key + ".Biomes");

            List<String> biomes = new ArrayList<>();
            for (var b : origBiomes) {
                if (b.startsWith("minecraft:"))
                    b = b.replace("minecraft:", "");
                biomes.add(b.toUpperCase());
            }
            String compassHint = config.getString("areas." + key + ".Compass Hint");
            boolean hasCompass = config.getBoolean("areas." + key + ".Has Compass");

            AreaObject area = new AreaObject(key, biomes, compassHint, hasCompass);
            Variables.AreaList.add(area);


        }
    }
}
