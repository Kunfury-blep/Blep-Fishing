package com.kunfury.blepfishing.ui.buttons.admin.quests;

import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.objects.quests.QuestType;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.objects.buttons.AdminQuestMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.quests.AdminQuestStartTimesPanel;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentEditStartTimesPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class AdminQuestStartTimesDayBtn extends AdminQuestMenuButton {

    TournamentType.TournamentDay day;
    public AdminQuestStartTimesDayBtn(QuestType questType, TournamentType.TournamentDay day){
        super(questType);
        this.day = day;
    }

    @Override
    public ItemStack buildItemStack(Player player) {
        Material mat = Material.YELLOW_CONCRETE;

        ItemStack item = new ItemStack(mat);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName(day.toString());

        ArrayList<String> lore = new ArrayList<>();

        if(!questType.StartTimes.containsKey(day)){
            questType.StartTimes.put(day, new ArrayList<>());
        }

        if(questType.StartTimes.get(day).isEmpty())
            lore.add(ChatColor.RED + "n/a");
        else
            lore.add(Formatting.ToCommaList(questType.StartTimes.get(day), ChatColor.WHITE, ChatColor.BLUE));

        m.setLore(lore);

        m = setButtonId(m, getId());

        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        dataContainer.set(ItemHandler.TourneyTypeId, PersistentDataType.STRING, questType.Id);
        dataContainer.set(dayKey, PersistentDataType.STRING, day.toString());

        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        QuestType questType = getQuestType();

        PersistentDataContainer dataContainer = ClickedItem.getItemMeta().getPersistentDataContainer();
        TournamentType.TournamentDay day = TournamentType.TournamentDay.valueOf(dataContainer.get(dayKey, PersistentDataType.STRING));

        new AdminQuestStartTimesPanel(questType, day).Show(player);
    }

}
