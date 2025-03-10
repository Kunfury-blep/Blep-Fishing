package com.kunfury.blepfishing.ui.buttons.admin.quests;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.objects.quests.QuestType;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.panels.admin.quests.AdminQuestEditPanel;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentEditPanel;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class AdminQuestCreateButton extends MenuButton {
    @Override
    public ItemStack buildItemStack(Player player) {
        Material mat = Material.TURTLE_SCUTE;

        ItemStack item = new ItemStack(mat);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        setButtonTitle(m, "Create New Quest");

        ArrayList<String> lore = new ArrayList<>();
        m.setLore(lore);
        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        String questId = "new_quest";

        int i = 0;
        while(QuestType.IdExists(questId)){
            questId = "new_quest" + i;
            i++;
        }

        QuestType questType = new QuestType(questId);
        QuestType.AddNew(questType);

        ConfigHandler.instance.questConfig.Save();
        new AdminQuestEditPanel(questType).Show(player);
    }


}
