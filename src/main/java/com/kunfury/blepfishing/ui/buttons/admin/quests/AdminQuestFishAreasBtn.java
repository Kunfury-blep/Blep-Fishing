package com.kunfury.blepfishing.ui.buttons.admin.quests;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.objects.quests.QuestType;
import com.kunfury.blepfishing.ui.objects.buttons.AdminQuestMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.quests.AdminQuestEditPanel;
import com.kunfury.blepfishing.ui.panels.admin.quests.AdminQuestFishAreasPanel;
import com.kunfury.blepfishing.ui.panels.admin.quests.AdminQuestFishTypesPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class AdminQuestFishAreasBtn extends AdminQuestMenuButton {
    public AdminQuestFishAreasBtn(QuestType questType) {
        super(questType);
    }

    @Override
    public ItemStack buildItemStack(Player player) {
        ItemStack item = new ItemStack(Material.COMPASS);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        setButtonTitle(m, "Fish Areas");
        ArrayList<String> lore = new ArrayList<>();
        lore.add("Areas the fish must be caught in");
        lore.add("");
        if(questType.RandomFishArea){
            lore.add(ChatColor.DARK_AQUA + "RANDOM");
            lore.add("");
        }

        lore.addAll(questType.getFormattedAreaList());

        lore.add("");
        lore.add(ChatColor.RED + "Shift-Right-CLick for Random");

        m.setLore(lore);

        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        new AdminQuestFishAreasPanel(getQuestType()).Show(player);
    }

    @Override
    protected void click_right_shift() {
        questType = getQuestType();
        questType.RandomFishArea = !questType.RandomFishArea;

        ConfigHandler.instance.questConfig.Save();
        new AdminQuestEditPanel(questType).Show(player);
    }
}
