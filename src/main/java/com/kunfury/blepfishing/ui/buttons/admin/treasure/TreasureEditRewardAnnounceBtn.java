package com.kunfury.blepfishing.ui.buttons.admin.treasure;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.objects.treasure.Casket;
import com.kunfury.blepfishing.objects.treasure.TreasureType;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTreasureRewardMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.treasure.AdminTreasureEditRewardsSelectionPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class TreasureEditRewardAnnounceBtn extends AdminTreasureRewardMenuButton {

    public TreasureEditRewardAnnounceBtn(Casket casket, Casket.TreasureReward reward) {
        super(casket, reward);
    }

    @Override
    public ItemStack buildItemStack(Player player) {
        Material mat = Material.RED_CONCRETE;
        if(Reward.Announce)
            mat = Material.GREEN_CONCRETE;

        ItemStack item = new ItemStack(mat);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName(Formatting.GetLanguageString("UI.Admin.Buttons.Treasure.Rewards.Announce.name"));
        ArrayList<String> lore = new ArrayList<>();
        if(casket.Announce)
            Formatting.GetLanguageString("UI.System.Buttons.enabled");
        else
            Formatting.GetLanguageString("UI.System.Buttons.disabled");
        lore.add("");
        lore.add(Formatting.GetLanguageString("UI.Admin.Buttons.Treasure.Rewards.Announce.lore"));

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
