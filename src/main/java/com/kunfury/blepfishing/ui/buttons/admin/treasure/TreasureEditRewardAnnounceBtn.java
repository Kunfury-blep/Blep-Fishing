package com.kunfury.blepfishing.ui.buttons.admin.treasure;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.objects.treasure.Casket;
import com.kunfury.blepfishing.objects.treasure.TreasureType;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTreasureRewardMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.treasure.AdminTreasureEditRewardsSelectionPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class TreasureEditRewardAnnounceBtn extends AdminTreasureRewardMenuButton {

    public TreasureEditRewardAnnounceBtn(Casket casket, Casket.TreasureReward reward) {
        super(casket, reward);
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
        if(casket.Announce)
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
        var treasure = getCasket();
        var reward = getReward();

        reward.Announce = !reward.Announce;
        ConfigHandler.instance.treasureConfig.Save();
        new AdminTreasureEditRewardsSelectionPanel(treasure, reward).Show(player);
    }


}
