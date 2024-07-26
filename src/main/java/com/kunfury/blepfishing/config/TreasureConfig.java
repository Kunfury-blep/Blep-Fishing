package com.kunfury.blepfishing.config;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.helpers.Utilities;
import com.kunfury.blepfishing.objects.Rarity;
import com.kunfury.blepfishing.objects.TreasureType;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class TreasureConfig {

    private final FileConfiguration treasureConfig;

    public TreasureConfig(){
        File treasureConfigFile = new File(BlepFishing.instance.getDataFolder(), "treasure.yml");

        if(!treasureConfigFile.exists()){
            BlepFishing.getPlugin().saveResource("treasure.yml", false);
            treasureConfigFile = new File(BlepFishing.instance.getDataFolder(), "treasure.yml");
        }

        treasureConfig = YamlConfiguration.loadConfiguration(treasureConfigFile);

        Load();
    }

    private void Load(){
        //Map<String, Object> fishMap = fishConfig.getDefaultSection().getValues(false);

        for(final String key : treasureConfig.getValues(false).keySet()){
            String name = treasureConfig.getString(key + ".Name");
            int weight = treasureConfig.getInt(key + ".Weight");

            var rewardsConfig = treasureConfig.getConfigurationSection(key + ".Rewards");

            List<ItemStack> itemRewards = new ArrayList<>();
            double cashReward = 0;

            if(rewardsConfig != null){
                //Get all cash rewards
                if(rewardsConfig.contains("Cash")){
                    cashReward = rewardsConfig.getDouble("Cash");
                }

                //Get all basic item rewards
                if(rewardsConfig.contains("Items")){
                    var configList = rewardsConfig.getList("Items");
                    assert configList != null;
                    for(var i : configList){
                        if(!(i instanceof ItemStack)){
                            Utilities.Severe("Tried to load invalid Itemstack from treasure: " + key);
                            continue;
                        }
                        itemRewards.add((ItemStack) i);
                    }
                }
            }


            TreasureType treasureType = new TreasureType(key, name, weight, itemRewards, cashReward);
            TreasureType.AddNew(treasureType);
        }
    }

    public void Save(){
        FileConfiguration newRarityConfig = new YamlConfiguration();

        var sortedRarities = Rarity.GetAll()
                .stream().sorted(Comparator.comparing(rarity -> rarity.Weight)).toList();
        for(var rarity : sortedRarities){
            String key = rarity.Id;
            newRarityConfig.set(key + ".Name", rarity.Name);
            newRarityConfig.set(key + ".Prefix", rarity.Prefix);
            newRarityConfig.set(key + ".Weight", rarity.Weight);
            newRarityConfig.set(key + ".Announce", rarity.Announce);
        }
        try {
            FileWriter fileWriter = new FileWriter(BlepFishing.instance.getDataFolder() + "/rarities.yml");
            fileWriter.write(newRarityConfig.saveToString());
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
