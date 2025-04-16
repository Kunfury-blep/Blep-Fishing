package com.kunfury.blepfishing.ui.buttons.admin.quests;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.objects.quests.QuestObject;
import com.kunfury.blepfishing.objects.quests.QuestType;
import com.kunfury.blepfishing.ui.objects.buttons.AdminQuestMenuButton;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTournamentMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.quests.AdminQuestEditPanel;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentEditPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class AdminQuestGlobalBtn extends AdminQuestMenuButton {

    public AdminQuestGlobalBtn(QuestType questType) {
        super(questType);
    }

    @Override
    public ItemStack buildItemStack(Player player) {
        Material mat = Material.RED_CONCRETE;
        if(questType.Global)
            mat = Material.GREEN_CONCRETE;

        ItemStack item = new ItemStack(mat);
        ItemMeta m = item.getItemMeta();
        assert m != null;



        setButtonTitle(m, "Global Progress");
        ArrayList<String> lore = new ArrayList<>();
        if(questType.Global)
            lore.add(ChatColor.GREEN + "Enabled");
        else
            lore.add(ChatColor.RED + "Disabled");

        lore.add("");
        lore.add("If enabled, progress of all players counts towards goal");
        m.setLore(lore);
        m = setButtonId(m, getId());


        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        questType = getQuestType();

        questType.Global = !questType.Global;
        ConfigHandler.instance.questConfig.Save();
        new AdminQuestEditPanel(questType).Show(player);
    }


}
