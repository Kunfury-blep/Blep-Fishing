package com.kunfury.blepfishing.config;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.objects.FishingArea;
import com.kunfury.blepfishing.objects.Rarity;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class AreaConfig {

    public FileConfiguration areaConfig;

    public AreaConfig(){
        File areaConfigFile = new File(BlepFishing.instance.getDataFolder(), "areas.yml");

        if(!areaConfigFile.exists()){
            BlepFishing.getPlugin().saveResource("areas.yml", false);
            areaConfigFile = new File(BlepFishing.instance.getDataFolder(), "areas.yml");
        }

        areaConfig = YamlConfiguration.loadConfiguration(areaConfigFile);

        Load();
    }

    private void Load(){
        FishingArea.Clear();
        for(final String key : areaConfig.getValues(false).keySet()){
            String name = areaConfig.getString(key + ".Name");
            List<String> biomes = areaConfig.getStringList(key + ".Biomes");
            boolean compassPiece = areaConfig.getBoolean(key + ".Compass Piece");
            String compassHint = areaConfig.getString(key + ".Compass Hint");

            FishingArea fishingArea = new FishingArea(key, name, biomes, compassPiece, compassHint);
            FishingArea.AddArea(fishingArea);
        }
    }

    public void Save(){
        FileConfiguration newAreaConfig = new YamlConfiguration();


        var sortedAreas = FishingArea.GetAll()
                .stream().sorted(Comparator.comparing(area -> area.Name)).toList();
        for(var area : sortedAreas){
            String key = area.Id;
            newAreaConfig.set(key + ".Name", area.Name);
            newAreaConfig.set(key +".Compass Piece", area.HasCompassPiece);
            if(area.HasCompassPiece)
                newAreaConfig.set(key + ".Compass Hint", area.CompassHint);


            newAreaConfig.set(key + ".Biomes", area.Biomes);
        }
        try {
            FileWriter fileWriter = new FileWriter(BlepFishing.instance.getDataFolder() + "/areas.yml");
            fileWriter.write(newAreaConfig.saveToString());
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
