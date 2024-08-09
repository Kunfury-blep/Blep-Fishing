package com.kunfury.blepfishing.ui.panels.player;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.ui.buttons.admin.AdminPanelButton;
import com.kunfury.blepfishing.ui.objects.Panel;
import com.kunfury.blepfishing.ui.buttons.player.tournament.PlayerTournamentPanelButton;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerPanel extends Panel {
    public PlayerPanel() {
        super("Player Panel", 27);
    }

    @Override
    public void Show(Player player) {
        inv = Bukkit.createInventory(player, InventorySize, Title);
        BuildInventory(player);
        player.openInventory(inv);

        new BukkitRunnable() {
            @Override
            public void run() {
                if(!player.getOpenInventory().getTitle().equals(Title)){
                    cancel();
                    return;
                }

                inv = Bukkit.createInventory(player, InventorySize, Title);
                BuildInventory(player);
                player.openInventory(inv);
            }

        }.runTaskTimer(BlepFishing.getPlugin(), 0, 20);
    }

    @Override
    public void BuildInventory(Player player) {
        if(player.hasPermission("bf.admin"))
            inv.setItem(4, new AdminPanelButton().getItemStack());

        inv.setItem(10, new PlayerTournamentPanelButton().getItemStack());
    }
}
