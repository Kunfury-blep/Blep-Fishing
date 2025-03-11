package com.kunfury.blepfishing.ui.buttons.admin.quests;

import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.objects.quests.QuestType;
import com.kunfury.blepfishing.ui.objects.buttons.AdminQuestMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.quests.AdminQuestStartTimesDaysPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class AdminQuestStartTimesBtn extends AdminQuestMenuButton {
    public AdminQuestStartTimesBtn(QuestType questType) {
        super(questType);
    }


    @Override
    public ItemStack buildItemStack(Player player) {
        ItemStack item = new ItemStack(Material.WRITTEN_BOOK);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        setButtonTitle(m, "Auto Start Times");
        ArrayList<String> lore = new ArrayList<>();

        var sortedStartTimes = questType.StartTimes.keySet().stream()
                .sorted(Enum::compareTo).toList();

        for(var key : sortedStartTimes){


            StringBuilder timeStrBuilder = new StringBuilder();

            if(key == TournamentType.TournamentDay.SATURDAY || key == TournamentType.TournamentDay.SUNDAY)
                timeStrBuilder.insert(0,ChatColor.GOLD).append(key).append(ChatColor.BLUE).append(": ");
            else
                timeStrBuilder.insert(0,ChatColor.YELLOW).append(key).append(ChatColor.BLUE).append(": ");

            while(timeStrBuilder.length() < 12){
                timeStrBuilder.append(" ");
            }

            var times = questType.StartTimes.get(key);
            if(times.isEmpty())
                timeStrBuilder.append(ChatColor.RED).append("n/a");
            else
                timeStrBuilder.append(Formatting.ToCommaList(times, ChatColor.WHITE, ChatColor.BLUE));

            lore.add(timeStrBuilder.toString());
        }

        m.setLore(lore);

        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        new AdminQuestStartTimesDaysPanel(getQuestType()).Show(player);
    }
}
