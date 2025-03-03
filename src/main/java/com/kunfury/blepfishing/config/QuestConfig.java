package com.kunfury.blepfishing.config;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.objects.quests.QuestType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

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
    }

    public void Save(){
        FileConfiguration newTourneyConfig = new YamlConfiguration();

        newTourneyConfig.set("Enabled", Enabled());
        try {
            FileWriter fishWriter = new FileWriter(BlepFishing.instance.getDataFolder() + "/tournaments.yml");
            fishWriter.write(newTourneyConfig.saveToString());
            fishWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean Enabled(){
        return config.getBoolean("Enabled");
    }

}
