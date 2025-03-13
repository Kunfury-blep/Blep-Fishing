package com.kunfury.blepfishing.config;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.objects.FishingArea;
import com.kunfury.blepfishing.objects.treasure.Casket;
import com.kunfury.blepfishing.objects.treasure.CompassPiece;
import com.kunfury.blepfishing.objects.treasure.TreasureType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.FileWriter;
import java.io.IOException;

public class BaseConfig {

    public final FileConfiguration config;

    public BaseConfig(){
        BlepFishing plugin = BlepFishing.instance;

        plugin.saveDefaultConfig();
        plugin.saveConfig();
        config = BlepFishing.getPlugin().getConfig();
    }

    public boolean getEnableFishBags(){
        return config.getBoolean("Enable Fish Bags");
    }

    public boolean getShowScoreboard(){
        return config.getBoolean("Show Scoreboard");
    }

    public boolean getChatScoreboard(){
        return config.getBoolean("Show Chat");
    }
    public boolean getActionBarScoreboard() { return config.getBoolean("Show Actionbar"); }

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

    public void Save(){
        FileConfiguration newConfig = new YamlConfiguration();

        newConfig.set("Show Scoreboard", getShowScoreboard());
        newConfig.set("Enable Fish Bags", getEnableFishBags());
        newConfig.set("Show Chat", getChatScoreboard());
        newConfig.set("Show Actionbar", getActionBarScoreboard());

        newConfig.set("All Blue.Radius", getAllBlueRadius());
        newConfig.set("All Blue.Area", getAllBlueArea().Id);
        newConfig.set("All Blue.Radius", getAllBlueSizeBonus());


        try {
            FileWriter fileWriter = new FileWriter(BlepFishing.instance.getDataFolder() + "/config.yml");
            fileWriter.write(newConfig.saveToString());
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
