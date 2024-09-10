package com.kunfury.blepfishing.ui.buttons.admin.treasure;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.objects.treasure.Casket;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTreasureMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.treasure.AdminTreasureEditPanel;
import com.kunfury.blepfishing.ui.panels.admin.treasure.AdminTreasurePanel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class AdminTreasureButton extends AdminTreasureMenuButton {

    public AdminTreasureButton(Casket casket) {
        super(casket);
    }

    @Override
    public ItemStack buildItemStack() {
        ItemStack item = new ItemStack(Material.CHEST);
        ItemMeta m = item.getItemMeta();

        m.setDisplayName(Formatting.formatColor(casket.Name));
        m = setButtonId(m, getId());

        List<String> lore = new ArrayList<>();

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

        lore.add("");
        lore.add(ChatColor.YELLOW + "Left-Click to Edit");
        if(!casket.ConfirmedDelete)
            lore.add( ChatColor.RED + "Shift Right-Click to Delete");
        else
            lore.add(ChatColor.RED + "Really Delete?");

        m.setLore(lore);

        item.setItemMeta(m);

        return item;
    }

    @Override
    protected void click_left() {
        Casket casket = getCasket();
        new AdminTreasureEditPanel(casket).Show(player);
    }

    @Override
    protected void click_right_shift() {
        Casket casket = getCasket();

        if(!casket.ConfirmedDelete){
            casket.ConfirmedDelete = true;

            Bukkit.getScheduler ().runTaskLater (BlepFishing.getPlugin(), () ->{
                casket.ConfirmedDelete = false;
            } , 300);
        }
        else{
            Casket.Delete(casket);
            ConfigHandler.instance.treasureConfig.Save();
            player.sendMessage(ChatColor.YELLOW + "Successfully Deleted " + ChatColor.WHITE + Formatting.formatColor(casket.Name));
        }

        new AdminTreasurePanel().Show(player);
    }
}
