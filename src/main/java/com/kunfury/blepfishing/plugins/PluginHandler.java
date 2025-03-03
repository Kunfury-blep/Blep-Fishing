package com.kunfury.blepfishing.plugins;

import com.kunfury.blepfishing.BlepFishing;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.logging.Level;

public class PluginHandler {

    public static boolean hasMcMMO;

    public void Initialize(){
        SetupMcMMO();
        SetupPlaceholderApi();
    }

    private void SetupPlaceholderApi(){
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new ExpandPlaceholder().register();
        }
    }

    private void SetupMcMMO(){
        if(Bukkit.getPluginManager().getPlugin("McMMO") != null){
            BlepFishing.instance.getServer().getPluginManager().registerEvents(new McMMO(), BlepFishing.instance);
            Bukkit.getLogger().log(Level.INFO, "McMMO successfully registered for Blep Fishing.");
            hasMcMMO = true;
        }else{
            //Setup.HandlerList.unregisterAll
            //This enables base listener if McMMO is not installed
        }
    }

    public static boolean HasWorldGuard(){
        Plugin plugin = Bukkit.getPluginManager().getPlugin("WorldGuard");

        return plugin instanceof WorldGuardPlugin;
    }
}
