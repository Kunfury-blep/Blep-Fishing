package com.kunfury.blepfishing.ui.buttons.admin.treasure;

import com.gmail.nossr50.mcmmo.kyori.adventure.platform.facet.Facet;
import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.items.ItemHandler;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.objects.TreasureType;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTournamentMenuButton;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTreasureMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentEditPanel;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentPanel;
import com.kunfury.blepfishing.ui.panels.admin.treasure.AdminTreasureEditPanel;
import com.kunfury.blepfishing.ui.panels.admin.treasure.AdminTreasurePanel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class AdminTreasureButton extends AdminTreasureMenuButton {

    public AdminTreasureButton(TreasureType treasureType) {
        super(treasureType);
    }

    @Override
    public ItemStack buildItemStack() {
        ItemStack item = new ItemStack(Material.CHEST);
        ItemMeta m = item.getItemMeta();

        m.setDisplayName(Formatting.formatColor(treasureType.Name));
        m = setButtonId(m, getId());

        List<String> lore = new ArrayList<>();

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

        lore.add("");
        lore.add(ChatColor.YELLOW + "Left-Click to Edit");
        if(!treasureType.ConfirmedDelete)
            lore.add( ChatColor.RED + "Shift Right-Click to Delete");
        else
            lore.add(ChatColor.RED + "Really Delete?");

        m.setLore(lore);

        item.setItemMeta(m);

        return item;
    }

    @Override
    protected void click_left() {
        TreasureType type = getTreasureType();
        new AdminTreasureEditPanel(type).Show(player);
    }

    @Override
    protected void click_right_shift() {
        TreasureType type = getTreasureType();

        if(!type.ConfirmedDelete){
            type.ConfirmedDelete = true;

            Bukkit.getScheduler ().runTaskLater (BlepFishing.getPlugin(), () ->{
                type.ConfirmedDelete = false;
            } , 300);
        }
        else{
            TreasureType.Delete(type);
            ConfigHandler.instance.treasureConfig.Save();
            player.sendMessage(ChatColor.YELLOW + "Successfully Deleted " + ChatColor.WHITE + Formatting.formatColor(type.Name));
        }

        new AdminTreasurePanel().Show(player);
    }

    @Override
    protected void click_left_shift() {
        TournamentType type = getTournamentType();
        type.Start();
    }
}
