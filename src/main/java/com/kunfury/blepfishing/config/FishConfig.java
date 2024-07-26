package com.kunfury.blepfishing.config;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.objects.FishType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FishConfig {

    public FileConfiguration fishConfig;

    public FishConfig(){
        File fishConfigFile = new File(BlepFishing.instance.getDataFolder(), "fish.yml");

        if(!fishConfigFile.exists()){
            BlepFishing.getPlugin().saveResource("fish.yml", false);
            fishConfigFile = new File(BlepFishing.instance.getDataFolder(), "fish.yml");
        }

        fishConfig = YamlConfiguration.loadConfiguration(fishConfigFile);

        LoadFishTypes();
    }

    private void LoadFishTypes(){
        for(final String key : fishConfig.getValues(false).keySet()){
            String name = fishConfig.getString(key + ".Name");
            String lore = fishConfig.getString(key + ".Lore");
            String desc = fishConfig.getString(key + ".Description");
            double lengthMin = fishConfig.getDouble(key + ".Length Min");
            double lengthMax = fishConfig.getDouble(key + ".Length Max");
            int modelData = fishConfig.getInt(key + ".Model Data");
            double priceBase = fishConfig.getDouble(key + ".Base Price");
            boolean requireRain = fishConfig.getBoolean(key + ".Raining");
            int heightMin = fishConfig.getInt(key + ".Height Min");
            int heightMax = fishConfig.getInt(key + ".Height Max");

            List<String> areaIds = getAreas(key);

            FishType fishType = new FishType(key, name, lore, desc, lengthMin, lengthMax, modelData, priceBase, areaIds, requireRain, heightMin, heightMax);
            FishType.AddFishType(fishType);
        }
    }

    public void Save(){
        FileConfiguration newFishConfig = new YamlConfiguration();
        for(var type : FishType.GetAll()){
            String key = type.Id;
            newFishConfig.set(key + ".Name", type.Name);
            newFishConfig.set(key + ".Lore", type.Lore);
            newFishConfig.set(key + ".Description", type.Description);
            newFishConfig.set(key + ".Areas", type.AreaIds);
            newFishConfig.set(key + ".Length Min", type.LengthMin);
            newFishConfig.set(key + ".Length Max", type.LengthMax);
            newFishConfig.set(key + ".Model Data", type.ModelData);
            newFishConfig.set(key + ".Base Price", type.PriceBase);
            newFishConfig.set(key + ".Raining", type.RequireRain);

            int heightMin = type.HeightMin;
            if(heightMin == -1000) heightMin = 0;

            int heightMax = type.HeightMax;
            if(heightMax == 1000) heightMax = 0;

            newFishConfig.set(key + ".Height Min", heightMin);
            newFishConfig.set(key + ".Height Max", heightMax);
        }
        try {
            FileWriter fishWriter = new FileWriter(BlepFishing.instance.getDataFolder() + "/fish.yml");
            fishWriter.write(newFishConfig.saveToString());
            fishWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> getAreas(String key){
        List<String> areaIds = new ArrayList<>();
        if(fishConfig.contains(key + ".Area")){
            areaIds.add(fishConfig.getString(key + ".Area"));
            BlepFishing.instance.configHandler.ReportIssue("Single Area defined in config instead of multiple for " + key);
        }

        areaIds.addAll(fishConfig.getStringList(key + ".Areas"));

        return areaIds;
    }


}
