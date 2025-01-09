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
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class AdminTreasureButton extends AdminTreasureMenuButton {

    public AdminTreasureButton(Casket casket) {
        super(casket);
    }

    @Override
    public ItemStack buildItemStack(Player player) {
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
            lore.add(Formatting.GetLanguageString("UI.Admin.Buttons.Treasure.Base.noRewards"));
        }else{
            if(finishedRewards > 0)
                lore.add(Formatting.GetLanguageString("UI.Admin.Buttons.Treasure.Base.rewards")
                        .replace("{amount}", String.valueOf(finishedRewards)));
            if(unfinishedRewards > 0)
                lore.add(Formatting.GetLanguageString("UI.Admin.Buttons.Treasure.Base.unfinishedRewards")
                        .replace("{amount}", String.valueOf(unfinishedRewards)));
        }

        lore.add("");
        lore.add(Formatting.GetLanguageString("UI.System.Buttons.edit"));
        if(!casket.ConfirmedDelete)
            lore.add(Formatting.GetLanguageString("UI.System.Buttons.delete"));
        else
            lore.add(Formatting.GetLanguageString("UI.System.Buttons.deleteConfirm"));

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
            player.sendMessage(Formatting.GetMessagePrefix() + Formatting.GetLanguageString("System.deleted")
                    .replace("{item}", casket.Id));
        }

        new AdminTreasurePanel().Show(player);
    }
}
