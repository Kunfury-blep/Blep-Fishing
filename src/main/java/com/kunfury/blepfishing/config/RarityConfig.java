package com.kunfury.blepfishing.config;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.objects.FishType;
import com.kunfury.blepfishing.objects.FishingArea;
import com.kunfury.blepfishing.objects.Rarity;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;

public class RarityConfig {

    private final FileConfiguration rarityConfig;

    public RarityConfig(){
        File rarityConfigFile = new File(BlepFishing.instance.getDataFolder(), "rarities.yml");

        if(!rarityConfigFile.exists()){
            BlepFishing.getPlugin().saveResource("rarities.yml", false);
            rarityConfigFile = new File(BlepFishing.instance.getDataFolder(), "rarities.yml");
        }

        rarityConfig = YamlConfiguration.loadConfiguration(rarityConfigFile);

        Load();
    }

    private void Load(){
        Rarity.Clear();
        for(final String key : rarityConfig.getValues(false).keySet()){
            String name = rarityConfig.getString(key + ".Name");
            int weight = rarityConfig.getInt(key + ".Weight");
            String prefix = rarityConfig.getString(key + ".Prefix");
            boolean announce = rarityConfig.getBoolean(key + ".Announce");
            double valueMod = rarityConfig.getDouble(key + ".ValueMod");
            if(valueMod == 0)
                valueMod = 1.0;


            Rarity rarity = new Rarity(key, name, prefix, weight, announce, valueMod);
            Rarity.AddNew(rarity);
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
            newRarityConfig.set(key + ".ValueMod", rarity.ValueMod);
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
