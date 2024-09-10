package com.kunfury.blepfishing.ui.panels.player;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.objects.FishType;
import com.kunfury.blepfishing.objects.TournamentObject;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.ui.buttons.player.tournament.PlayerTournamentButton;
import com.kunfury.blepfishing.ui.objects.Panel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class PlayerTournamentDetailPanel extends Panel {

    private Inventory inv;

    public PlayerTournamentDetailPanel(TournamentObject tourney) {
        super(tourney.getType().Name, FishType.GetAll().size() + 9);
    }

//    @Override
//    public void Show(Player player) {
//        inv = Bukkit.createInventory(player, InventorySize, Title);
//        BuildInventory(player);
//        player.openInventory(inv);
//
//        new BukkitRunnable() {
//            @Override
//            public void run() {
//                if(!player.getOpenInventory().getTitle().equals(Title)){
//                    cancel();
//                    return;
//                }
//
//                inv = Bukkit.createInventory(player, InventorySize, Title);
//                BuildInventory(player);
//                player.openInventory(inv);
//            }
//
//        }.runTaskTimer(BlepFishing.getPlugin(), 0, 20);
//    }

    @Override
    public void BuildInventory(Player player) {
//
//        AddFooter(new PlayerPanelButton(), null, null);
    }
}