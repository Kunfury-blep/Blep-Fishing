package com.kunfury.blepFishing.Plugins;

import com.kunfury.blepFishing.FishSwitch;
import com.kunfury.blepFishing.Setup;
import org.bukkit.Bukkit;

public class PluginHandler {

    public static boolean hasMcMMO;

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
            Setup.setup.getServer().getPluginManager().registerEvents(new McMMOListener(), Setup.setup);
            Bukkit.broadcastMessage("Registering MCMMO");
            hasMcMMO = true;
        }else{
            //Setup.HandlerList.unregisterAll
            //This enables base listener if McMMO is not installed
        }
    }


}
