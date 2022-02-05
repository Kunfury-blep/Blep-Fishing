package com.kunfury.blepFishing.Plugins;

import com.kunfury.blepFishing.Setup;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PluginHandler {

    public static boolean hasMcMMO, hasWorldGuard;
    public static StateFlag BLEP_FISH;

    public void InitializePlugins(){
        SetupPlaceholderApi();
        SetupMcMMO();
        SetupWorldGuard();
    }


    private void SetupPlaceholderApi(){
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new ExpandPlaceholder().register();
        }
    }

    private void SetupMcMMO(){
        if(Bukkit.getPluginManager().getPlugin("McMMO") != null){
            Setup.setup.getServer().getPluginManager().registerEvents(new McMMOListener(), Setup.setup);
            Bukkit.broadcastMessage("Registering MCMMO");
            hasMcMMO = true;
        }else{
            //Setup.HandlerList.unregisterAll
            //This enables base listener if McMMO is not installed
        }
    }


    private void SetupWorldGuard(){
        if(Bukkit.getPluginManager().getPlugin("WorldGuard") != null){
            hasWorldGuard = true;
        }
    }

    public static boolean CheckWorldGuard(Location loc, Player player){
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(loc);

        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        //boolean canFish = set.testState(localPlayer, BLEP_FISH);

        return true;
    }

}
