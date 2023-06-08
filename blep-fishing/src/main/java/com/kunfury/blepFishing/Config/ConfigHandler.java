package com.kunfury.blepFishing.Config;

import com.kunfury.blepFishing.BlepFishing;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConfigHandler {
    private static FishConfig fishConfig;

    public void Initialize(BlepFishing plugin){
        FixFileLocations(); //Quick fix for updating file formats
        plugin.saveConfig();

        File tournamentFile = new File(plugin.getDataFolder(), "tournaments.yml");
        if (!tournamentFile.exists())
            plugin.saveResource("tournaments.yml", false);

        File messageFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!messageFile.exists())
            plugin.saveResource("messages.yml", false);

        File itemsFile = new File(plugin.getDataFolder(), "items.yml");
        if (!itemsFile.exists())
            plugin.saveResource("items.yml", false);

        File questFile = new File(plugin.getDataFolder(), "quests.yml");
        if (!questFile.exists())
            plugin.saveResource("quests.yml", false);

//        File fishFile = new File(plugin.getDataFolder(), "fish.yml");
//        if (!fishFile.exists())
//            plugin.saveResource("fish.yml", false);

        BlepFishing.configBase = new ConfigBase(plugin);

        fishConfig = new FishConfig();
        new ConfigExtra().Load(BlepFishing.configBase.config);

        UpdateMain();
        UpdateMessages();

        Variables.setPrefix(Formatting.getMessage("System.prefix"));
    }

    public void SendError(String error, String error2){
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

    private void UpdateMain(){
        File configFile = new File(BlepFishing.blepFishing.getDataFolder(), "config.yml");
        YamlConfiguration externalYamlConfig = YamlConfiguration.loadConfiguration(configFile);

        InputStreamReader internalConfigFileStream = new InputStreamReader(Objects.requireNonNull(BlepFishing.getPlugin().getResource("config.yml")), StandardCharsets.UTF_8);
        YamlConfiguration internalYamlConfig = YamlConfiguration.loadConfiguration(internalConfigFileStream);

        for (String string : internalYamlConfig.getKeys(true)) {
            if(string.contains("rarities.") || string.contains("areas.") || string.contains("fish.") || string.contains("treasure.")){
                continue;
            }

            // Checks if the external file contains the key already.
            if (!externalYamlConfig.contains(string)) {
                // If it doesn't contain the key, we set the key based off what was found inside the plugin jar
                externalYamlConfig.set(string, internalYamlConfig.get(string));
            }
        }

        try {
            externalYamlConfig.save(configFile);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    private void UpdateMessages(){
        File messageConfigFile = new File(BlepFishing.blepFishing.getDataFolder(), "messages.yml");
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

            if(!new File(BlepFishing.dataFolder + "/config.yml").exists()){
                return; //All below methods are only needed if a config.yml already exists
            }
//            File congigFile = new File(BlepFishing.blepFishing.getDataFolder(), "config.yml");
//            YamlConfiguration configYaml = YamlConfiguration.loadConfiguration(congigFile);
//
//            //Migrates fish from config.yml to fish.yml
//            if(!new File(BlepFishing.dataFolder + "/fish.yml").exists()){
//                YamlConfiguration fishYaml = new YamlConfiguration();
//                List<String> keysToRemove = new ArrayList<>();
//
//                for (String key : new ArrayList<>(configYaml.getKeys(true))) {
//                    if(key.contains("fish.")){
//                        fishYaml.set(key, configYaml.get(key));
//                        keysToRemove.add(key);
//                    }
//                }
//
//                Path fishPath = Paths.get(BlepFishing.dataFolder + "/fish.yml");
//                final BufferedWriter fishWriter = Files.newBufferedWriter(fishPath,
//                        StandardCharsets.UTF_8, StandardOpenOption.CREATE);
//
//                fishWriter.write(fishYaml.saveToString());
//                fishWriter.flush();
//
//                for(var key : keysToRemove){
//                    configYaml.set(key, null);
//                }
//
//                configYaml.set("fish", null);
//                configYaml.save(congigFile);
//                Bukkit.getLogger().warning("Updated Fish.yml");
//
////                Files.move(Paths.get(BlepFishing.dataFolder + "/signs.data"),
////                        Paths.get(BlepFishing.dataFolder + "/Data/" + "/signs.data"));
//            }



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static FishConfig getFishConfig(){
        return fishConfig;
    }
}
