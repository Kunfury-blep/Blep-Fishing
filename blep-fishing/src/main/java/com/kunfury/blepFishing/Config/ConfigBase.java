package com.kunfury.blepFishing.Config;

import com.kunfury.blepFishing.BlepFishing;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ConfigBase {

    private FileConfiguration config;
    private BlepFishing plugin;

    public ConfigBase(BlepFishing plugin){
        this.plugin = plugin;
        reload();
    }
    public void reload(){
        plugin.reloadConfig();
        config = plugin.getConfig();
        BlepFishing.econEnabled = config.getBoolean("Use Economy");
        Bukkit.broadcastMessage("Config Reload Complete");
    }

    public boolean getShowScoreboard(){
        return config.getBoolean("Show ScoreBoard");
    }

    public boolean getShowChat(){
        return config.getBoolean("Show Chat");
    }

    public boolean getHighPriority(){
        return config.getBoolean("High Priority");
    }

    public boolean getEnableTournaments(){
        return config.getBoolean("Enable Tournaments");
    }

    public boolean getTournamentOnly(){
        return config.getBoolean("Tournament Only");
    }

    public boolean getAreaPermissions(){
        return config.getBoolean("Area Permissions");
    }

    public boolean getAnnounceLegendary(){
        return config.getBoolean("Announce Legendary");
    }

    public boolean getUseEconomy(){
        return config.getBoolean("Use Economy");
    }

    public boolean getEnableFishBags(){
        return config.getBoolean("Enable Fish Bags");
    }

    public boolean getEnableTeasers(){
        return config.getBoolean("Enable Teasers");
    }

    public boolean getEnablePatrons(){
        return config.getBoolean("Enable Patrons");
    }

    public boolean getEnableQuests(){
        return config.getBoolean("Enable Quests");
    }

    public boolean getAnnounceQuests(){
        return config.getBoolean("Announce Quests");
    }
    public int getMaxQuests(){return BlepFishing.config.getInt("Max Quests");}

    public boolean getEnableWorldWhitelist(){ return config.getBoolean("World Whitelist");}
    public boolean getAllowWanderingTraders(){ return config.getBoolean("Allow Wandering Traders");}

    public double getTraderMod(){ return config.getDouble("Wandering Traders Modifier");}

    public boolean getEnableTreasure(){return config.getBoolean("Enable Treasure");}
    public double getTreasureChance(){return config.getDouble("Treasure Chance");}
    public boolean getEnableAllBlue(){return config.getBoolean("Enable All Blue");}
    public boolean getPermanentAllBlue(){return config.getBoolean("Permanent All Blue");}
    public List<String> getAllowedWorlds(){return BlepFishing.config.getStringList("Allowed Worlds");}
    public String getDayReset(){return BlepFishing.config.getString("New Day Time");}
    public int getAllBlueFish(){return BlepFishing.config.getInt("All Blue Fish");}
    public double getMobChance(){return BlepFishing.config.getDouble("Endgame Mob Chance");}
    public int getEndgameRadius(){return BlepFishing.config.getInt("Endgame Radius");}
    public double getParrotBonus(){return BlepFishing.config.getDouble("Parrot Treasure Bonus");}
    public double getBoatBonus(){return BlepFishing.config.getDouble("Boat Treasure Bonus");}
}
