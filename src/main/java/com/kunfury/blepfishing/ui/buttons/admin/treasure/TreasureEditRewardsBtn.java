package com.kunfury.blepfishing.ui.buttons.admin.treasure;

import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.items.ItemHandler;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.objects.TreasureType;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTournamentMenuButton;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTreasureMenuButton;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTreasureRewardMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentEditRewardsPanel;
import com.kunfury.blepfishing.ui.panels.admin.treasure.AdminTreasureEditRewardsPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class TreasureEditRewardsBtn extends AdminTreasureMenuButton {


    public TreasureEditRewardsBtn(TreasureType type) {
        super(type);
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

        for(var i : treasureType.Rewards){
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
            if(finishedRewards > 0)
                lore.add(ChatColor.YELLOW.toString() + unfinishedRewards + "x Empty Rewards");
        }





        m.setLore(lore);
        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        new AdminTreasureEditRewardsPanel(getTreasureType()).Show(player);
    }

}
