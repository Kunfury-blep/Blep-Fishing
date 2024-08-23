package com.kunfury.blepfishing.ui.buttons.admin.treasure;

import com.kunfury.blepfishing.objects.treasure.Casket;
import com.kunfury.blepfishing.objects.treasure.TreasureType;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTreasureMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.treasure.AdminTreasureEditRewardsPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class TreasureEditRewardsBtn extends AdminTreasureMenuButton {


    public TreasureEditRewardsBtn(Casket casket) {
        super(casket);
    }

    @Override
    public ItemStack buildItemStack() {
        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName("Rewards");

        ArrayList<String> lore = new ArrayList<>();

        int finishedRewards = 0;
        int unfinishedRewards = 0;

        for(var i : casket.Rewards){
            if(i.Item == null && i.Cash == 0)
                unfinishedRewards++;
            else
                finishedRewards++;
        }

        if(finishedRewards == 0 && unfinishedRewards == 0){
            lore.add(ChatColor.RED + "No Rewards Set");
        }else{
            if(finishedRewards > 0)
                lore.add(ChatColor.GREEN.toString() + finishedRewards + "x Rewards");
            if(unfinishedRewards > 0)
                lore.add(ChatColor.YELLOW.toString() + unfinishedRewards + "x Empty Rewards");
        }





        m.setLore(lore);
        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        new AdminTreasureEditRewardsPanel(getCasket()).Show(player);
    }

}
