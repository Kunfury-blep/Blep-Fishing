package com.kunfury.blepfishing.ui.buttons.player.quests;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.objects.TournamentObject;
import com.kunfury.blepfishing.objects.quests.QuestObject;
import com.kunfury.blepfishing.objects.quests.QuestType;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.panels.player.quests.PlayerQuestPanel;
import com.kunfury.blepfishing.ui.panels.player.tournaments.PlayerTournamentDetailPanel;
import com.kunfury.blepfishing.ui.panels.player.tournaments.PlayerTournamentPanel;
import net.milkbowl.vault.chat.Chat;
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



        lore.add(ChatColor.BLUE + "[" + ChatColor.WHITE + quest.GetPlayerCatchAmount(player)  + ChatColor.BLUE + "/" + ChatColor.WHITE + questType.CatchAmount
                + ChatColor.BLUE + "]");

        lore.add("");
        lore.addAll(questType.getFormattedCatchList());

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
    protected void click_right_shift() {
        if(!player.hasPermission("bf.admin"))
            return;

        QuestObject quest = getQuest();

        if(!quest.ConfirmCancel){
            quest.ConfirmCancel = true;

            Bukkit.getScheduler ().runTaskLater (BlepFishing.getPlugin(), () ->{
                quest.ConfirmCancel = false;
            } , 300);
        }else{
            quest.Finish();
        }
        new PlayerQuestPanel().Show(player);
    }

    protected QuestObject getQuest(){
        int questId = ItemHandler.getTagInt(ClickedItem, ItemHandler.QuestId);
        return Database.Quests.Get(questId);
    }

}
