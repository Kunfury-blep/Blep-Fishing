package com.kunfury.blepFishing.Config;

import com.kunfury.blepFishing.BlepFishing;
import com.kunfury.blepFishing.Crafting.CraftingManager;
import com.kunfury.blepFishing.Interfaces.Admin.AdminMenu;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

public class ConfigBase {

    public FileConfiguration config;
    private final BlepFishing plugin;

    public ConfigBase(BlepFishing plugin){
        this.plugin = plugin;
        reload();
    }

    public void reload(){
        reload(null);
    }

    public void reload(CommandSender sender){
        plugin.reloadConfig();
        config = plugin.getConfig();
        BlepFishing.econEnabled = config.getBoolean("Use Economy");


        if(sender != null)
            sender.sendMessage(Formatting.getFormattedMesage("System.reload"));
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
    public int getMaxQuests(){return config.getInt("Max Quests");}

    public boolean getEnableWorldWhitelist(){ return config.getBoolean("World Whitelist");}
    public boolean getAllowWanderingTraders(){ return config.getBoolean("Allow Wandering Traders");}

    public double getTraderMod(){ return config.getDouble("Wandering Traders Modifier");}

    public boolean getEnableTreasure(){return config.getBoolean("Enable Treasure");}
    public double getTreasureChance(){return config.getDouble("Treasure Chance");}
    public boolean getEnableAllBlue(){return config.getBoolean("Enable All Blue");}
    public boolean getPermanentAllBlue(){return config.getBoolean("Permanent All Blue");}
    public List<String> getAllowedWorlds(){return config.getStringList("Allowed Worlds");}
    public String getDayReset(){return config.getString("New Day Time");}
    public int getAllBlueFish(){return config.getInt("All Blue Fish");}
    public double getMobChance(){return config.getDouble("Endgame Mob Chance");}
    public int getEndgameRadius(){return config.getInt("Endgame Radius");}
    public double getParrotBonus(){return config.getDouble("Parrot Treasure Bonus");}
    public double getBoatBonus(){return config.getDouble("Boat Treasure Bonus");}
    public boolean getEnableDiscordSRV(){
        return config.getBoolean("Enable DiscordSRV");
    }
}
