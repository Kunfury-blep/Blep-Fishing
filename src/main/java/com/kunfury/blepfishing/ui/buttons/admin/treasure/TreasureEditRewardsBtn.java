package com.kunfury.blepfishing.ui.buttons.admin.treasure;

import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.objects.treasure.Casket;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTreasureMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.treasure.AdminTreasureEditRewardsPanel;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class TreasureEditRewardsBtn extends AdminTreasureMenuButton {


    public TreasureEditRewardsBtn(Casket casket) {
        super(casket);
    }

    @Override
    public ItemStack buildItemStack(Player player) {
        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName(Formatting.GetLanguageString("UI.Admin.Buttons.Treasure.Base.editRewards"));

        ArrayList<String> lore = new ArrayList<>();

        int finishedRewards = 0;
        int unfinishedRewards = 0;

        Casket casket = (Casket)treasureType;


        for(var i : casket.Rewards){
            if(i.Item == null && i.Cash == 0)
                unfinishedRewards++;
            else
                finishedRewards++;
        }

        if(finishedRewards == 0 && unfinishedRewards == 0){

            lore.add(Formatting.GetLanguageString("UI.Admin.Buttons.Treasure.Base.noRewards"));
        }else{
            if(finishedRewards > 0)
                lore.add(Formatting.GetLanguageString("UI.Admin.Buttons.Treasure.Base.rewards")
                        .replace("{amount}", String.valueOf(finishedRewards)));
            if(unfinishedRewards > 0)
                lore.add(Formatting.GetLanguageString("UI.Admin.Buttons.Treasure.Base.unfinishedRewards")
                        .replace("{amount}", String.valueOf(unfinishedRewards)));
        }





        m.setLore(lore);
        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        new AdminTreasureEditRewardsPanel((Casket)getTreasureType()).Show(player);
    }

}
