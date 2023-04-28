package com.kunfury.blepFishing.Plugins;

import com.kunfury.blepFishing.BlepFishing;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.internal.annotation.Selection;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PluginHandler {

    public static boolean hasMcMMO, hasWorldGuard;

    public void InitializePlugins(){
        SetupPlaceholderApi();
        SetupMcMMO();
    }


    private void SetupPlaceholderApi(){
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new ExpandPlaceholder().register();
        }
    }

    private void SetupMcMMO(){
        if(Bukkit.getPluginManager().getPlugin("McMMO") != null){
            BlepFishing.blepFishing.getServer().getPluginManager().registerEvents(new McMMOListener(), BlepFishing.blepFishing);
            Bukkit.broadcastMessage("Registering MCMMO");
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
