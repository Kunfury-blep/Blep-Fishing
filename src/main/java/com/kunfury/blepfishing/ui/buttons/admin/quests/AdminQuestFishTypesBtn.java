package com.kunfury.blepfishing.ui.buttons.admin.quests;

import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.objects.quests.QuestType;
import com.kunfury.blepfishing.ui.objects.buttons.AdminQuestMenuButton;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTournamentMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.quests.AdminQuestFishTypesPanel;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentEditFishTypesPanel;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class AdminQuestFishTypesBtn extends AdminQuestMenuButton {
    public AdminQuestFishTypesBtn(QuestType questType) {
        super(questType);
    }

    @Override
    public ItemStack buildItemStack(Player player) {
        ItemStack item = new ItemStack(Material.SALMON);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        setButtonTitle(m, "Fish Types");

        ArrayList<String> lore = new ArrayList<>(questType.getFormattedCatchList());

        m.setLore(lore);

        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        dataContainer.set(ItemHandler.TourneyTypeId, PersistentDataType.STRING, questType.Id);

        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        new AdminQuestFishTypesPanel(getQuestType()).Show(player);
    }

}
