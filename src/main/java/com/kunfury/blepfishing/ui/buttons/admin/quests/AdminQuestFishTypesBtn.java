package com.kunfury.blepfishing.ui.buttons.admin.quests;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.objects.quests.QuestType;
import com.kunfury.blepfishing.ui.objects.buttons.AdminQuestMenuButton;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTournamentMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.quests.AdminQuestEditPanel;
import com.kunfury.blepfishing.ui.panels.admin.quests.AdminQuestFishTypesPanel;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentEditFishTypesPanel;
import org.bukkit.ChatColor;
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

        ArrayList<String> lore = new ArrayList<>();
        lore.add("The Fish to be Caught");
        lore.add("");
        if(questType.RandomFishType){
            lore.add(ChatColor.DARK_AQUA + "RANDOM");
            lore.add("");
        }

        lore.addAll(questType.getFormattedCatchList());

        lore.add("");
        lore.add(ChatColor.RED + "Shift-Right-Click for Random");

        m.setLore(lore);

        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        new AdminQuestFishTypesPanel(getQuestType()).Show(player);
    }

    @Override
    protected void click_right_shift() {
        questType = getQuestType();
        questType.RandomFishType = !questType.RandomFishType;

        ConfigHandler.instance.questConfig.Save();
        new AdminQuestEditPanel(questType).Show(player);
    }

}
