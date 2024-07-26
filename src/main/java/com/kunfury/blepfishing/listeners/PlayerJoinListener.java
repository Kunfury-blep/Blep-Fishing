package com.kunfury.blepfishing.listeners;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.items.CraftingHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.ObjectInputFilter;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        Player p = e.getPlayer();
        if(ConfigHandler.instance.baseConfig.getEnableFishBags()) p.discoverRecipe(CraftingHandler.FishBagCraftKey);

        //new TournamentHandler().ShowBars(p);

        if(p.hasPermission("bf.admin") && !ConfigHandler.instance.ErrorMessages.isEmpty()){
            ConfigHandler.instance.ErrorMessages.forEach(er ->
                    p.sendMessage(Formatting.formatColor(Formatting.getPrefix() + ChatColor.RED + er)));
        }
    }
}
