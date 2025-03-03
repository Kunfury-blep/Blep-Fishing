package com.kunfury.blepfishing.config;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.helpers.Utilities;
import jdk.jshell.execution.Util;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ConfigHandler {

    public static ConfigHandler instance;

    public BaseConfig baseConfig;
    public FishConfig fishConfig;
    public TournamentConfig tourneyConfig;
    public RarityConfig rarityConfig;
    public AreaConfig areaConfig;
    public TreasureConfig treasureConfig;
    public QuestConfig questConfig;


    public HashMap<String, YamlConfiguration> Translations;

    public void Initialize(){
        instance = this;
        baseConfig = new BaseConfig();
        fishConfig = new FishConfig();
        tourneyConfig = new TournamentConfig();
        rarityConfig = new RarityConfig();
        areaConfig = new AreaConfig();
        treasureConfig = new TreasureConfig();
        questConfig = new QuestConfig();

        UpdateMessages();
        LoadTranslations();
    }

    public List<String> ErrorMessages = new ArrayList<>();

    public void ReportIssue(String issue){
        ErrorMessages.add(issue);
        //TODO: Show error to those with bf admin
        //TODO: Button in message to overwrite file with fixed version
    }


    private final static File messageFile = new File(BlepFishing.instance.getDataFolder(), "messages.yml");
    private void UpdateMessages() {
        if (!messageFile.exists()) { //Loads base language if none found
            LoadLanguage("English");
            return;
        }

        File messageFile = new File(BlepFishing.instance.getDataFolder() + "/messages.yml");
        var savedMessageConfig = YamlConfiguration.loadConfiguration(messageFile);

        InputStream languageResource = BlepFishing.instance.getResource("translations/English.yml");
        YamlConfiguration internalYaml = YamlConfiguration.loadConfiguration(new InputStreamReader(languageResource));


        for (String string : internalYaml.getKeys(true)) {
            // Checks if the external file contains the key already.
            if (!savedMessageConfig.contains(string)) {
                // If it doesn't contain the key, we set the key based off what was found inside the plugin jar
                savedMessageConfig.set(string, internalYaml.get(string));
            }
        }
        try {
            savedMessageConfig.save(messageFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Formatting.languageYaml = savedMessageConfig;
    }

    private void LoadTranslations(){
        final File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        Translations = new HashMap<>();

        try {
            if(jarFile.isFile()) {  // Run with JAR file
                final JarFile jar = new JarFile(jarFile);
                final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
                while(entries.hasMoreElements()) {
                    final String path = entries.nextElement().getName();
                    if (path.startsWith("translations/") && !path.equals("translations/")) { //filter according to the path
                        InputStream languageResource = BlepFishing.instance.getResource(path);
                        if(languageResource == null)
                            continue;
                        YamlConfiguration languageYaml = YamlConfiguration.loadConfiguration(new InputStreamReader(languageResource));
                        var name = path.replace("translations/", "").replace(".yml", "");
                        Translations.put(name, languageYaml);
                    }
                }
                jar.close();
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void LoadLanguage(String language){
        var path = "translations/" + language + ".yml";

        //Bukkit.getLogger().warning("Path: " + path);

        InputStream languageResource = BlepFishing.instance.getResource(path);

        try {
            if(languageResource == null){
                Utilities.Severe("Tried to load invalid Language: " + path);
                return;
            }
            File messageFile = new File(BlepFishing.instance.getDataFolder() + "/messages.yml");

            YamlConfiguration languageYaml = YamlConfiguration.loadConfiguration(new InputStreamReader(languageResource));
            languageYaml.set("Language", language);

            if (!language.equals("English")) { //Ensures that no messages are missed
                var englishResource = BlepFishing.getPlugin().getResource("translations/English.yml");
                assert englishResource != null;
                YamlConfiguration englishYaml = YamlConfiguration.loadConfiguration(new InputStreamReader(englishResource));
                for (String string : englishYaml.getKeys(true)) {
                    if (!languageYaml.contains(string)) {
                        languageYaml.set(string, englishYaml.get(string));
                    }
                }
            }

            languageYaml.save(messageFile);
            Formatting.languageYaml = languageYaml;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void Reload() {
        baseConfig = new BaseConfig();
        fishConfig = new FishConfig();
        tourneyConfig = new TournamentConfig();
        rarityConfig = new RarityConfig();
        areaConfig = new AreaConfig();
        treasureConfig = new TreasureConfig();

        UpdateMessages();
        LoadTranslations();
    }
}
