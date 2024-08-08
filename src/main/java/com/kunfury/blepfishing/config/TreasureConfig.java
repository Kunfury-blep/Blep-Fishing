package com.kunfury.blepfishing.config;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.helpers.Utilities;
import com.kunfury.blepfishing.objects.TreasureType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
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
            boolean announce = treasureConfig.getBoolean(key + ".Announce");

            var rewardsConfig = treasureConfig.getConfigurationSection(key + ".Rewards");

            List<TreasureType.TreasureReward> rewards = new ArrayList<>();
            double cashReward = 0;

            if(rewardsConfig != null){
                for(var i : rewardsConfig.getValues(false).keySet()){
                    double dropChance = rewardsConfig.getDouble(i + ".Drop Chance");
                    boolean rewardAnnounce = rewardsConfig.getBoolean(i + ".Announce");
                    double cash = rewardsConfig.getDouble(i + ".Cash");
                    ItemStack item = rewardsConfig.getItemStack(i + ".Item");
                    rewards.add(new TreasureType.TreasureReward(dropChance, item, rewardAnnounce, cash));
                }

                //Get all cash rewards
//                if(rewardsConfig.contains("Cash")){
//                    cashReward = rewardsConfig.getDouble("Cash");
//                }

                //Get all basic item rewards
//                if(rewardsConfig.contains("Items")){
//                    var configList = rewardsConfig.getList("Items");
//                    assert configList != null;
//                    for(var i : configList){
//                        if(!(i instanceof ItemStack)){
//                            Utilities.Severe("Tried to load invalid Itemstack from treasure: " + key);
//                            continue;
//                        }
//                        itemRewards.add((ItemStack) i);
//                    }
//                }
            }


            TreasureType treasureType = new TreasureType(key, name, weight, announce, rewards, cashReward);
            TreasureType.AddNew(treasureType);
        }
    }

    public void Save(){
        FileConfiguration newTreasureConfig = new YamlConfiguration();

        var sortedTreasures = TreasureType.GetAll()
                .stream().sorted(Comparator.comparing(treasure -> treasure.Weight)).toList();
        for(var type : sortedTreasures){
            String key = type.Id;
            newTreasureConfig.set(key + ".Name", type.Name);
            newTreasureConfig.set(key + ".Weight", type.Weight);
            newTreasureConfig.set(key + ".Announce", type.Announce);

            for(var i : type.Rewards){
                var path = key + ".Rewards." + type.Rewards.indexOf(i);
                newTreasureConfig.set(path + ".Drop Chance", i.DropChance);
                newTreasureConfig.set(path + ".Announce", i.Announce);
                newTreasureConfig.set(path + ".Cash", i.Cash);
                newTreasureConfig.set(path + ".Item", i.Item);
            }
        }
        try {
            FileWriter fileWriter = new FileWriter(BlepFishing.instance.getDataFolder() + "/treasure.yml");
            fileWriter.write(newTreasureConfig.saveToString());
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
