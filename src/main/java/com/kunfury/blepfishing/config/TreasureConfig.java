package com.kunfury.blepfishing.config;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.objects.treasure.Casket;
import com.kunfury.blepfishing.objects.treasure.CompassPiece;
import com.kunfury.blepfishing.objects.treasure.TreasureType;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TreasureConfig {

    public FileConfiguration config;

    public TreasureConfig(){
        File treasureConfigFile = new File(BlepFishing.instance.getDataFolder(), "treasure.yml");

        if(!treasureConfigFile.exists()){
            BlepFishing.getPlugin().saveResource("treasure.yml", false);
            treasureConfigFile = new File(BlepFishing.instance.getDataFolder(), "treasure.yml");
        }

        config = YamlConfiguration.loadConfiguration(treasureConfigFile);

        Load();
    }

    private void Load(){
        TreasureType.Clear();

        UpdateOld();

        var casketConfig = config.getConfigurationSection("Caskets");
        var compassConfig = config.getConfigurationSection("Compasses");

        if(casketConfig != null){
            for(final String key : casketConfig.getValues(false).keySet()){
                String name = casketConfig.getString(key + ".Name");
                int weight = casketConfig.getInt(key + ".Weight");
                boolean announce = casketConfig.getBoolean(key + ".Announce");

                var rewardsConfig = casketConfig.getConfigurationSection(key + ".Rewards");

                List<Casket.TreasureReward> rewards = new ArrayList<>();
                double cashReward = 0;

                if(rewardsConfig != null){
                    for(var i : rewardsConfig.getValues(false).keySet()){
                        double dropChance = rewardsConfig.getDouble(i + ".Drop Chance");
                        boolean rewardAnnounce = rewardsConfig.getBoolean(i + ".Announce");
                        double cash = rewardsConfig.getDouble(i + ".Cash");
                        ItemStack item = rewardsConfig.getItemStack(i + ".Item");
                        rewards.add(new Casket.TreasureReward(dropChance, item, rewardAnnounce, cash));
                    }
                }



                Casket casket = new Casket(key, name, weight, announce, rewards, cashReward);
                Casket.AddNew(casket);
            }
            //Bukkit.getLogger().warning("Caskets Found: " + Casket.GetAll().size());
        }

        if(compassConfig != null){
            int weight = compassConfig.getInt("Weight");
            boolean announce = compassConfig.getBoolean("Announce");

            CompassPiece compassPiece = new CompassPiece("compassPiece", weight, announce);
            CompassPiece.AddNew(compassPiece);
        }
    }

    private void UpdateOld(){
        if(!config.contains("Caskets")) {
            Bukkit.getLogger().warning("Outdated Treasure Config Detected. Updating");
            config.set("Enabled", true);
            config.set("Treasure Chance", 10.0);
            for (final String key : config.getValues(false).keySet()) {
                String name = config.getString(key + ".Name");
                int weight = config.getInt(key + ".Weight");
                boolean announce = config.getBoolean(key + ".Announce");

                var rewardsConfig = config.getConfigurationSection(key + ".Rewards");

                List<Casket.TreasureReward> rewards = new ArrayList<>();
                double cashReward = 0;

                if (rewardsConfig != null) {
                    for (var i : rewardsConfig.getValues(false).keySet()) {
                        double dropChance = rewardsConfig.getDouble(i + ".Drop Chance");
                        boolean rewardAnnounce = rewardsConfig.getBoolean(i + ".Announce");
                        double cash = rewardsConfig.getDouble(i + ".Cash");
                        ItemStack item = rewardsConfig.getItemStack(i + ".Item");
                        rewards.add(new Casket.TreasureReward(dropChance, item, rewardAnnounce, cash));
                    }
                }


                Casket casket = new Casket(key, name, weight, announce, rewards, cashReward);
                TreasureType.AddNew(casket);
            }
            Save();

            File treasureConfigFile = new File(BlepFishing.instance.getDataFolder(), "treasure.yml");
            config = YamlConfiguration.loadConfiguration(treasureConfigFile);
        }
    }

    public void Save(){
        FileConfiguration newTreasureConfig = new YamlConfiguration();

        newTreasureConfig.set("Settings.Enabled", Enabled());
        newTreasureConfig.set("Settings.Treasure Chance", getTreasureChance());

        for(var t : TreasureType.ActiveTreasureTypes.values()){
            String key;
            if(t instanceof CompassPiece){
                key = "Compasses";
                newTreasureConfig.set(key + ".Enabled", getCompassEnabled());
            }else if(t instanceof  Casket casket){
                key = "Caskets." + casket.Id;
                newTreasureConfig.set(key + ".Name", casket.Name);
                for(var i : casket.Rewards){
                    var path = key + ".Rewards." + casket.Rewards.indexOf(i);
                    newTreasureConfig.set(path + ".Drop Chance", i.DropChance);
                    newTreasureConfig.set(path + ".Announce", i.Announce);
                    newTreasureConfig.set(path + ".Cash", i.Cash);
                    newTreasureConfig.set(path + ".Item", i.Item);
                }
            }else
                continue;

            newTreasureConfig.set(key + ".Weight", t.Weight);
            newTreasureConfig.set(key + ".Announce", t.Announce);
        }
        try {
            FileWriter fileWriter = new FileWriter(BlepFishing.instance.getDataFolder() + "/treasure.yml");
            fileWriter.write(newTreasureConfig.saveToString());
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean Enabled(){
        return config.getBoolean("Settings.Enabled");
    }
    public double getTreasureChance(){return config.getDouble("Settings.Treasure Chance");}

    public boolean getCompassEnabled(){return config.getBoolean("Compasses.Enabled");}
}
