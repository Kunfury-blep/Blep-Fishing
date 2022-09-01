package com.kunfury.blepFishing.Config;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;

public class ItemsConfig {
    public static Material FishMat = Material.SALMON;

    public static Material BagMat = Material.HEART_OF_THE_SEA;
    public static int BagModel = 1;

    public static void Initialize(FileConfiguration config){
        if(config.contains("Fish.material"))
            FishMat = Material.getMaterial(Objects.requireNonNull(config.getString("Fish.material")));

        if(config.contains("Fish Bag.material"))
            BagMat = Material.getMaterial(Objects.requireNonNull(config.getString("Fish Bag.material")));

        if(config.contains("Fish Bag.modelData"))
            BagModel = config.getInt("Fish Bag.modelData");

    }


}
