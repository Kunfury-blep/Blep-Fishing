package com.kunfury.blepfishing.config;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.objects.FishingArea;
import org.bukkit.configuration.file.FileConfiguration;

public class BaseConfig {

    private final FileConfiguration config;

    public BaseConfig(){
        config = BlepFishing.getPlugin().getConfig();
    }

    public boolean getEnableFishBags(){
        return config.getBoolean("Enable Fish Bags");
    }
    public boolean getEnableTournaments(){
        return config.getBoolean("Enable Tournaments");
    }

    public boolean getShowScoreboard(){
        return config.getBoolean("Show Scoreboard");
    }


    public int getAllBlueRadius() {
        return config.getInt("All Blue.Radius");
    }

    public FishingArea getAllBlueArea(){
        String areaId = config.getString("All Blue.Area");
        return FishingArea.FromId(areaId);
    }

    public double getAllBlueSizeBonus(){
        return config.getDouble("All Blue.Size Bonus");
    }
}
