package com.kunfury.blepfishing.config;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.helpers.Formatting;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConfigHandler {

    public static ConfigHandler instance;

    public BaseConfig baseConfig;
    public FishConfig fishConfig;
    public TournamentConfig tourneyConfig;
    public RarityConfig rarityConfig;
    public AreaConfig areaConfig;

    public void Initialize(){
        instance = this;

        BlepFishing plugin = BlepFishing.instance;
        plugin.saveDefaultConfig();
        plugin.saveConfig();

        baseConfig = new BaseConfig();
        fishConfig = new FishConfig();
        tourneyConfig = new TournamentConfig();
        rarityConfig = new RarityConfig();
        areaConfig = new AreaConfig();

        UpdateMessages();
    }

    public List<String> ErrorMessages = new ArrayList<>();

    public void ReportIssue(String issue){
        ErrorMessages.add(issue);
        //TODO: Show error to those with bf admin
        //TODO: Button in message to overwrite file with fixed version
    }

    private void UpdateMessages(){
        File messageFile = new File(BlepFishing.instance.getDataFolder(), "messages.yml");
        if (!messageFile.exists()){
            BlepFishing.instance.saveResource("messages.yml", false);
        }


        File messageConfigFile = new File(BlepFishing.instance.getDataFolder(), "messages.yml");
        YamlConfiguration externalYamlConfig = YamlConfiguration.loadConfiguration(messageConfigFile);

        InputStreamReader internalConfigFileStream = new InputStreamReader(Objects.requireNonNull(BlepFishing.getPlugin().getResource("messages.yml")), StandardCharsets.UTF_8);
        YamlConfiguration internalYamlConfig = YamlConfiguration.loadConfiguration(internalConfigFileStream);

        for (String string : internalYamlConfig.getKeys(true)) {
            // Checks if the external file contains the key already.
            if (!externalYamlConfig.contains(string)) {
                // If it doesn't contain the key, we set the key based off what was found inside the plugin jar
                externalYamlConfig.set(string, internalYamlConfig.get(string));
            }
        }
        try {
            externalYamlConfig.save(messageConfigFile);
        } catch (IOException io) {
            io.printStackTrace();
        }

        Formatting.messages = externalYamlConfig;
    }

}
