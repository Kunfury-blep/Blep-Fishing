package com.kunfury.blepfishing.ui.buttons.player.tournament;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.objects.TournamentObject;
import com.kunfury.blepfishing.ui.panels.player.PlayerTournamentDetailPanel;
import com.kunfury.blepfishing.ui.panels.player.PlayerTournamentPanel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PlayerTournamentBtn extends MenuButton {

    TournamentObject tournament;
    public PlayerTournamentBtn(TournamentObject tournament){
        this.tournament = tournament;
    }

    @Override
    public ItemStack buildItemStack(Player player) {
        ItemStack item = new ItemStack(Material.SALMON);
        ItemMeta m = item.getItemMeta();

        var tournamentType = tournament.getType();

        var duration = Formatting.asTime(tournament.getTimeRemaining(), ChatColor.BLUE);
        m.setDisplayName(ChatColor.AQUA + tournamentType.Name +
                ChatColor.BLUE  + " | " + duration);

        List<String> lore = new ArrayList<>();
        lore.add("");

        lore.addAll(tournament.getType().getFormattedCatchList());

        lore.add("");
        lore.add(Formatting.GetLanguageString("UI.Player.Buttons.Tournaments.view"));

        if(tournamentType.HasBossBar)
            lore.add(Formatting.GetLanguageString("UI.Player.Buttons.Tournaments.bossBar"));

        if(player.hasPermission("bf.admin")){
            lore.add("");
            if(tournament.ConfirmCancel)
                lore.add(Formatting.GetLanguageString("UI.Player.Buttons.Tournaments.cancelConfirm"));
            else
                lore.add(Formatting.GetLanguageString("UI.Player.Buttons.Tournaments.cancel"));
        }

        m.setLore(lore);

        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        dataContainer.set(ItemHandler.TourneyId, PersistentDataType.INTEGER, tournament.Id);

        var fishTypes = tournament.getType().getFishTypes();

        if(!fishTypes.isEmpty()){
            var randomType = fishTypes.get(ThreadLocalRandom.current().nextInt(fishTypes.size()));
            m.setCustomModelData(randomType.ModelData);
        }




        item.setItemMeta(m);

        return item;
    }

    @Override
    protected void click_left() {
        TournamentObject tournament = getTournament();
        new PlayerTournamentDetailPanel(tournament, 1).Show(player);
    }

    @Override
    protected void click_left_shift() {
        TournamentObject tournament = getTournament();
        tournament.ToggleBossBar(player);
    }

    @Override
    protected void click_right_shift() {
        if(!player.hasPermission("bf.admin"))
            return;

        TournamentObject tournament = getTournament();

        if(!tournament.ConfirmCancel){
            tournament.ConfirmCancel = true;

            Bukkit.getScheduler ().runTaskLater (BlepFishing.getPlugin(), () ->{
                tournament.ConfirmCancel = false;
            } , 300);
        }else{
            tournament.Cancel();
        }
        new PlayerTournamentPanel().Show(player);
    }

    protected TournamentObject getTournament(){
        int tourneyId = ItemHandler.getTagInt(ClickedItem, ItemHandler.TourneyId);
        return Database.Tournaments.Get(tourneyId);
    }
}
