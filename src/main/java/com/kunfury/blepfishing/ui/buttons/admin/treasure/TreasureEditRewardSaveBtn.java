package com.kunfury.blepfishing.ui.buttons.admin.treasure;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.items.ItemHandler;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.objects.TreasureType;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTournamentRewardsMenuButton;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTreasureRewardMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentEditRewardsPlacementPanel;
import com.kunfury.blepfishing.ui.panels.admin.treasure.AdminTreasureEditRewardsSelectionPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.ArrayList;
import java.util.List;

public class TreasureEditRewardSaveBtn extends AdminTreasureRewardMenuButton {


    public TreasureEditRewardSaveBtn(TreasureType type, TreasureType.TreasureReward reward){
        super(type, reward);
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
        var type = getTreasureType();
        var reward = getReward();
        player.sendMessage(Formatting.getPrefix() + "Saved rewards for " + type.Name);

        var panel = player.getOpenInventory().getTopInventory();
        reward.Item = panel.getItem(4);

        ConfigHandler.instance.treasureConfig.Save();
        new AdminTreasureEditRewardsSelectionPanel(type, reward).Show(player);
    }

}
