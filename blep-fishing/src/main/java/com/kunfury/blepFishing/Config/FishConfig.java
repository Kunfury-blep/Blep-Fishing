package com.kunfury.blepFishing.Config;

import com.kunfury.blepFishing.BlepFishing;
import com.kunfury.blepFishing.Objects.BaseFishObject;
import com.kunfury.blepFishing.Objects.FishObject;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FishConfig {

    public FileConfiguration fishConfig;
    
    public FishConfig(){
        File fishConfigFile = new File(BlepFishing.blepFishing.getDataFolder(), "fish.yml");
        fishConfig = YamlConfiguration.loadConfiguration(fishConfigFile);

        fishConfig = BlepFishing.configBase.config; //Temp just to keep fish in the config.yml

//        if (!fishConfigFile.exists()) {
//            BlepFishing.getPlugin().saveResource("fish.yml", false);
//        }

        LoadFish();
    }
    
    private void LoadFish(){
        Variables.BaseFishList.clear();
        Map<String, Object> fishMap = fishConfig.getConfigurationSection("fish").getValues(false);
        for(final String key : fishMap.keySet()) {
            String lore = fishConfig.getString("fish." + key + ".Lore");
            double minSize = (fishConfig.getDouble("fish." + key + ".Min Size"));
            double maxSize = fishConfig.getDouble("fish." + key + ".Max Size");
            int modelData = fishConfig.getInt("fish." + key + ".ModelData");
            boolean raining = fishConfig.getBoolean("fish." + key + ".Raining");
            int weight = fishConfig.getInt("fish." + key + ".Weight");
            double baseCost = fishConfig.getDouble("fish." + key + ".Base Price");

            int minHeight = fishConfig.getInt("fish." + key + ".Min Height");
            int maxHeight = fishConfig.getInt("fish." + key + ".Max Height");

            String timeStr = fishConfig.getString("fish." + key + ".Time");

            FishTime time = FishTime.ALL;

            if(timeStr != null)
                time = FishTime.valueOf(timeStr);


            String area = fishConfig.getString("fish." + key + ".Area");
            List<String> areas = fishConfig.getStringList("fish." + key + ".Areas");
            if(areas.size() == 0){
                areas = new ArrayList<>();
                areas.add(area);
            }


            BaseFishObject base = new BaseFishObject(key, lore, minSize, maxSize, modelData, raining, baseCost, areas, minHeight, maxHeight, time)
                    .weight(weight);
            Variables.BaseFishList.add(base);
        }

        try {
            String dictPath = BlepFishing.dataFolder + "/Data/" +  "/fish.data";
            ObjectInputStream input = null;
            File tempFile = new File(dictPath);
            if(tempFile.exists()) {
                input = new ObjectInputStream(new FileInputStream(dictPath));
                Variables.FishDict = (HashMap<String, List<FishObject>>) input.readObject();
            }
            if(input != null)
                input.close();
        } catch (IOException | ClassNotFoundException ex) {
            //Variables.AddError("Error adding quest " + key + " due to unrecognized fish name: " + fishName);
            Bukkit.getLogger().warning(Variables.getPrefix(true) + "Loading of fish Failed");
            ex.printStackTrace();
        }

        Variables.FishTotalWeight = 0;
        for (final BaseFishObject fish : Variables.BaseFishList)
            Variables.FishTotalWeight += fish.Weight;

    }
}
