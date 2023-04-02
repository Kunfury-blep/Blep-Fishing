package com.kunfury.blepFishing.Events;

import com.kunfury.blepFishing.BlepFishing;
import com.kunfury.blepFishing.Interfaces.Admin.AdminMenu;
import com.kunfury.blepFishing.Interfaces.MenuHandler;
import com.kunfury.blepFishing.Signs.FishSign;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class EventHandler {

    public void SetupEvents(PluginManager pm){
        Plugin plugin = BlepFishing.getPlugin();

        pm.registerEvents(new EventListener(), plugin);
        pm.registerEvents(new FishSign(), plugin);
        pm.registerEvents(new AdminMenu(), plugin);
        pm.registerEvents(new MenuHandler(), plugin);
        pm.registerEvents(new FishingListener(), plugin);
    }
}
