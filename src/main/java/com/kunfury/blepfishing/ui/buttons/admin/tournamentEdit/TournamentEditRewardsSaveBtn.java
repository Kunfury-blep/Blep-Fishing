package com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.items.ItemHandler;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTournamentRewardsMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentEditRewardsPlacementPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class TournamentEditRewardsSaveBtn extends AdminTournamentRewardsMenuButton {


    public TournamentEditRewardsSaveBtn(TournamentType type, int placement){
        super(type, placement);
    }


    @Override
    public ItemStack buildItemStack() {

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
        var type = getTournamentType();
        var placement = getPlacement();
        player.sendMessage(Formatting.getPrefix() + "Saved rewards for " + type.Name);

        var trackedItems = getTrackedItems(player.getOpenInventory().getTopInventory().getContents());

        type.ItemRewards.put(placement, trackedItems);

        ConfigHandler.instance.tourneyConfig.Save();
        new AdminTournamentEditRewardsPlacementPanel(type, placement).Show(player);
    }

    private List<ItemStack> getTrackedItems(ItemStack[] contents){
        List<ItemStack> trackedItems = new ArrayList<>();

        for(var i : contents){
            if(i == null)
                continue;

            PersistentDataContainer dataContainer = i.getItemMeta().getPersistentDataContainer();

            if(dataContainer.has(ItemHandler.TourneyTypeId))
                continue;

            trackedItems.add(i);
        }

        return trackedItems;
    }

}
