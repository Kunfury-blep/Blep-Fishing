package com.kunfury.blepfishing.listeners;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.helpers.CraftingHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        Player player = e.getPlayer();

        CraftingHandler.LearnRecipes(player);

        for(var t : Database.Tournaments.GetActive()){
            t.BossBarJoin(player);
        }

        if(player.hasPermission("bf.admin") && !ConfigHandler.instance.ErrorMessages.isEmpty()){
            ConfigHandler.instance.ErrorMessages.forEach(er ->
                    player.sendMessage(Formatting.formatColor(Formatting.GetMessagePrefix() + ChatColor.RED + er)));
        }
    }
}
