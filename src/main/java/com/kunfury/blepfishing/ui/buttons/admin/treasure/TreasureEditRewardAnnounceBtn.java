package com.kunfury.blepfishing.ui.buttons.admin.treasure;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.objects.TreasureType;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTreasureMenuButton;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTreasureRewardMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.treasure.AdminTreasureEditPanel;
import com.kunfury.blepfishing.ui.panels.admin.treasure.AdminTreasureEditRewardsPanel;
import com.kunfury.blepfishing.ui.panels.admin.treasure.AdminTreasureEditRewardsSelectionPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class TreasureEditRewardAnnounceBtn extends AdminTreasureRewardMenuButton {

    public TreasureEditRewardAnnounceBtn(TreasureType type, TreasureType.TreasureReward reward) {
        super(type, reward);
    }

    @Override
    public ItemStack buildItemStack() {
        Material mat = Material.RED_CONCRETE;
        if(Reward.Announce)
            mat = Material.GREEN_CONCRETE;

        ItemStack item = new ItemStack(mat);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName("Announce Item");
        ArrayList<String> lore = new ArrayList<>();
        if(treasureType.Announce)
            lore.add(ChatColor.GREEN + "Enabled");
        else
            lore.add(ChatColor.RED + "Disabled");

        lore.add("");
        lore.add("If enabled, announces to the world when a player gets this item from the treasure.");

        m.setLore(lore);

        item.setItemMeta(m);
        return item;
    }

    protected void click_left() {
        var treasure = getTreasureType();
        var reward = getReward();

        reward.Announce = !reward.Announce;
        ConfigHandler.instance.treasureConfig.Save();
        new AdminTreasureEditRewardsSelectionPanel(treasure, reward).Show(player);
    }


}
