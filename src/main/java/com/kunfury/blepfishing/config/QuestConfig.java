package com.kunfury.blepfishing.config;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.helpers.Utilities;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.objects.quests.QuestType;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
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

            ConfigurationSection questConfig = config.getConfigurationSection(key);
            if(questConfig == null)
                continue;

            QuestType questType = new QuestType(key, questConfig);
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
            if(type.RandomFishType)
                newQuestConfig.set(key + ".Random Fish Type", true);

            newQuestConfig.set(key + ".Fishing Areas", type.FishingAreaIds);
            if(type.RandomFishArea)
                newQuestConfig.set(key + ".Random Fish Area", true);

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
