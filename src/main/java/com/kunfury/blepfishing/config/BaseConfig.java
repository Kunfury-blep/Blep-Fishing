package com.kunfury.blepfishing.config;

import com.kunfury.blepfishing.BlepFishing;
import org.bukkit.configuration.file.FileConfiguration;

public class BaseConfig {

    private final FileConfiguration config;

    public BaseConfig(){
        config = BlepFishing.getPlugin().getConfig();
    }

    public boolean getEnableFishBags(){
        return config.getBoolean("Enable Fish Bags");
    }
    public boolean getShowScoreboard(){
        return config.getBoolean("Show Scoreboard");
    }


}
