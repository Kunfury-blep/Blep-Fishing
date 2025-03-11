package com.kunfury.blepfishing.ui.buttons.admin.quests;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.objects.quests.QuestType;
import com.kunfury.blepfishing.ui.objects.buttons.AdminQuestMenuButton;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTournamentRewardsMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.quests.AdminQuestRewardsPanel;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentEditRewardsPlacementPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.ArrayList;
import java.util.List;

public class AdminQuestRewardsSaveBtn extends AdminQuestMenuButton {


    public AdminQuestRewardsSaveBtn(QuestType type){
        super(type);
    }


    @Override
    public ItemStack buildItemStack(Player player) {

        ItemStack item = new ItemStack(Material.TURTLE_SCUTE);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName("Save Rewards");

        List<String> lore = new ArrayList<>();

        lore.add(ChatColor.GREEN + "Saves all item in this inventory as rewards");

        m.setLore(lore);

        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        questType = getQuestType();
        player.sendMessage(Formatting.GetMessagePrefix() + "Saved rewards for " + questType.Name);

        questType.ItemRewards = getTrackedItems(player.getOpenInventory().getTopInventory().getContents());

        ConfigHandler.instance.questConfig.Save();
        new AdminQuestRewardsPanel(questType).Show(player);
    }

    private List<ItemStack> getTrackedItems(ItemStack[] contents){
        List<ItemStack> trackedItems = new ArrayList<>();

        for(var i : contents){
            if(i == null)
                continue;

            PersistentDataContainer dataContainer = i.getItemMeta().getPersistentDataContainer();

            if(dataContainer.has(ItemHandler.QuestTypeId))
                continue;

            trackedItems.add(i);
        }

        return trackedItems;
    }

}
