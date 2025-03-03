package com.kunfury.blepfishing.ui.buttons.player.quests;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.objects.TournamentObject;
import com.kunfury.blepfishing.objects.quests.QuestObject;
import com.kunfury.blepfishing.ui.objects.MenuButton;
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

public class PlayerQuestBtn extends MenuButton {

    QuestObject quest;
    public PlayerQuestBtn(QuestObject quest){
        this.quest = quest;
    }

    @Override
    public ItemStack buildItemStack(Player player) {
        ItemStack item = new ItemStack(Material.SALMON);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        var questType = quest.getType();

        var duration = Formatting.asTime(quest.getTimeRemaining(), ChatColor.BLUE);
        setButtonTitle(m, Formatting.GetLanguageString("UI.Player.Buttons.Quests.name")
                .replace("{name}", questType.Name)
                .replace("{duration}", duration));

        List<String> lore = new ArrayList<>();
        lore.add("");

        lore.addAll(questType.getFormattedCatchList());

        lore.add("");
        lore.add(Formatting.GetLanguageString("UI.Player.Buttons.Quests.view"));

//        if(player.hasPermission("bf.admin")){
//            lore.add("");
//            if(tournament.ConfirmCancel)
//                lore.add(Formatting.GetLanguageString("UI.Player.Buttons.Tournaments.cancelConfirm"));
//            else
//                lore.add(Formatting.GetLanguageString("UI.Player.Buttons.Tournaments.cancel"));
//        }

        m.setLore(lore);

        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        dataContainer.set(ItemHandler.QuestId, PersistentDataType.INTEGER, quest.Id);

        var fishTypes = questType.getFishTypes();

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
