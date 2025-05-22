package com.kunfury.blepfishing.ui.buttons.admin.treasure;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.objects.treasure.Casket;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTreasureRewardMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.treasure.AdminTreasureEditRewardsSelectionPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TreasureEditRewardSaveBtn extends AdminTreasureRewardMenuButton {


    public TreasureEditRewardSaveBtn(Casket casket, Casket.TreasureReward reward){
        super(casket, reward);
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
        Casket casket = (Casket)getTreasureType();
        var reward = getReward();
        player.sendMessage(Formatting.GetMessagePrefix() + "Saved rewards for " + casket.Name);

        var panel = player.getOpenInventory().getTopInventory();
        reward.Item = panel.getItem(4);

        ConfigHandler.instance.treasureConfig.Save();
        new AdminTreasureEditRewardsSelectionPanel(casket, reward).Show(player);
    }

}
