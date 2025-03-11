package com.kunfury.blepfishing.config;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.helpers.Utilities;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.objects.quests.QuestType;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class QuestConfig {

    public FileConfiguration config;

    public QuestConfig(){
        File questConfigFile = new File(BlepFishing.instance.getDataFolder(), "quests.yml");

        if(!questConfigFile.exists()){
            BlepFishing.getPlugin().saveResource("quests.yml", false);
            questConfigFile = new File(BlepFishing.instance.getDataFolder(), "quests.yml");
        }

        config = YamlConfiguration.loadConfiguration(questConfigFile);

        Load();
    }

    private void Load(){
        QuestType.Clear();
        for(final String key : config.getValues(false).keySet()){
            if(key.equals("Enabled"))
                continue;


            String name = config.getString(key + ".Name");
            List<String> fishTypes = config.getStringList(key + ".Fish Types");
            double duration = config.getDouble(key + ".Duration");
            int catchAmount = config.getInt(key + ".Catch Amount");

            HashMap<TournamentType.TournamentDay, List<String>> startTimes = new HashMap<>();
            //List<String> everyday = tournamentConfig.getStringList(key + ".Start Times.EVERYDAY");

            var sortedStartTimes = Arrays.stream(TournamentType.TournamentDay.values()).toList().stream()
                    .sorted(Enum::compareTo).toList();
            for(var d : sortedStartTimes){
                if(!config.contains(key + ".Start Times." + d))
                    continue;

                startTimes.put(d, config.getStringList(key + ".Start Times." + d));
            }

            double cashReward = 0;
            List<ItemStack> itemRewards = new ArrayList<>();

            if(config.contains(key + ".Rewards.Cash"))
                cashReward = config.getDouble(key + ".Rewards.Cash");
            if(config.contains(key + ".Rewards.Items")){
                var configList = config.getList(key + ".Rewards.Items");
                assert configList != null;
                for(var i : configList){
                    if(!(i instanceof ItemStack)){
                        Utilities.Severe("Tried to load invalid Itemstack from quest: " + key);
                        continue;
                    }
                    itemRewards.add((ItemStack) i);
                }
            }

            Bukkit.broadcastMessage("Loaded quest " + name + " with " + itemRewards.size() + " rewards and $" + cashReward);

            QuestType questType = new QuestType(key, name, duration, catchAmount, fishTypes, startTimes, cashReward, itemRewards);
            QuestType.AddNew(questType);
        }
    }

    public void Save(){
        FileConfiguration newQuestConfig = new YamlConfiguration();

        newQuestConfig.set("Enabled", Enabled());

        for(var type : QuestType.GetAll()){
            String key = type.Id;
            newQuestConfig.set(key + ".Name", type.Name);
            newQuestConfig.set(key + ".Fish Types", type.FishTypeIds);
            newQuestConfig.set(key + ".Duration", type.Duration);
            newQuestConfig.set(key + ".Catch Amount", type.CatchAmount);

            var sortedStartTimes = Arrays.stream(TournamentType.TournamentDay.values()).toList().stream()
                    .sorted(Enum::compareTo).toList();

            for(var d : sortedStartTimes){
                newQuestConfig.set(key + ".Start Times." + d, type.StartTimes.get(d));
            }
            newQuestConfig.set(key + ".Rewards" + ".Cash", type.CashReward);

            newQuestConfig.set(key + ".Rewards" + ".Items", type.ItemRewards);
        }



        try {
            FileWriter fishWriter = new FileWriter(BlepFishing.instance.getDataFolder() + "/quests.yml");
            fishWriter.write(newQuestConfig.saveToString());
            fishWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean Enabled(){
        return config.getBoolean("Enabled");
    }

}
