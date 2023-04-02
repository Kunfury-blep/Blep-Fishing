package com.kunfury.blepFishing.Plugins;

import com.kunfury.blepFishing.BlepFishing;
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
            BlepFishing.blepFishing.getServer().getPluginManager().registerEvents(new McMMOListener(), BlepFishing.blepFishing);
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

//            FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
//            try {
//                // create a flag with the name "my-custom-flag", defaulting to true
//                StateFlag flag = new StateFlag("blep-fish", true);
//                registry.register(flag);
//                PluginHandler.BLEP_FISH = flag; // only set our field if there was no error
//            } catch (FlagConflictException e) {
//                // some other plugin registered a flag by the same name already.
//                // you can use the existing flag, but this may cause conflicts - be sure to check type
//                Flag<?> existing = registry.get("blep-fish");
//                if (existing instanceof StateFlag) {
//                    PluginHandler.BLEP_FISH = (StateFlag) existing;
//                } else {
//                    // types don't match - this is bad news! some other plugin conflicts with you
//                    // hopefully this never actually happens
//                }
//            }

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
