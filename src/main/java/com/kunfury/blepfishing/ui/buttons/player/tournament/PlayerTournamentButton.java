package com.kunfury.blepfishing.ui.buttons.player.tournament;

import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentEditPanel;
import com.kunfury.blepfishing.items.ItemHandler;
import com.kunfury.blepfishing.objects.FishType;
import com.kunfury.blepfishing.objects.TournamentObject;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.ui.panels.player.PlayerTournamentDetailPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class PlayerTournamentButton extends MenuButton {

    TournamentObject tournament;
    public PlayerTournamentButton(TournamentObject tournament){
        this.tournament = tournament;
    }

    @Override
    public ItemStack buildItemStack() {
        ItemStack item = new ItemStack(Material.SALMON);
        ItemMeta m = item.getItemMeta();

        var duration = Formatting.asTime(tournament.getTimeRemaining(), ChatColor.BLUE);
        m.setDisplayName(ChatColor.AQUA + tournament.getType().Name +
                ChatColor.BLUE  + " | " + duration);

        List<String> lore = new ArrayList<>();
        lore.add("");

        lore.addAll(tournament.getType().getFormattedCatchList());

        lore.add("");
        lore.add(Formatting.GetLanguageString("UI.Player.Buttons.Tournaments.view"));

        m.setLore(lore);

        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        dataContainer.set(ItemHandler.TourneyId, PersistentDataType.INTEGER, tournament.Id);

        item.setItemMeta(m);

        return item;
    }

    @Override
    protected void click_left() {
        TournamentObject tournament = getTournament();
        new PlayerTournamentDetailPanel(tournament, 1).Show(player);
    }

    protected TournamentObject getTournament(){
        int tourneyId = ItemHandler.getTagInt(ClickedItem, ItemHandler.TourneyId);
        return Database.Tournaments.Get(tourneyId);
    }
}
