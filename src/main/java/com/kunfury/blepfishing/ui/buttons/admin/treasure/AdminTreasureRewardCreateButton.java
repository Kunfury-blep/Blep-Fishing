package com.kunfury.blepfishing.ui.buttons.admin.treasure;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.objects.treasure.Casket;
import com.kunfury.blepfishing.objects.treasure.TreasureType;
import com.kunfury.blepfishing.ui.panels.admin.treasure.AdminTreasureEditRewardsSelectionPanel;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class AdminTreasureRewardCreateButton extends AdminTreasureButton {
    public AdminTreasureRewardCreateButton(Casket casket) {
        super(casket);
    }

    @Override
    public ItemStack buildItemStack() {
        Material mat = Material.TURTLE_SCUTE;

        ItemStack item = new ItemStack(mat);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName("Create New Reward");
        ArrayList<String> lore = new ArrayList<>();
        m.setLore(lore);
        m = setButtonId(m, getId());
        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        Casket casket = getCasket();

        var reward = new Casket.TreasureReward(0, null, false, 0);
        casket.Rewards.add(reward);

        ConfigHandler.instance.treasureConfig.Save();
        new AdminTreasureEditRewardsSelectionPanel(casket, reward).Show(player);
    }


}
