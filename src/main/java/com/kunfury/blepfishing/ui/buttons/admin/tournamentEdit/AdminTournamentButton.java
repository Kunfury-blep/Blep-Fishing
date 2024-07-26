package com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTournamentMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentEditPanel;
import com.kunfury.blepfishing.items.ItemHandler;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentPanel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class AdminTournamentButton extends AdminTournamentMenuButton {

    public AdminTournamentButton(TournamentType tournamentType) {
        super(tournamentType);
    }

    @Override
    public ItemStack buildItemStack() {
        ItemStack item = new ItemStack(Material.SALMON);
        ItemMeta m = item.getItemMeta();

        m.setDisplayName(ChatColor.AQUA + tournament.Name);
        m = setButtonId(m, getId());

        List<String> lore = new ArrayList<>(tournament.getItemLore());

        lore.add("");
        lore.add(ChatColor.YELLOW + "Left-Click to Edit");
        lore.add(ChatColor.YELLOW + "Shift Left-Click to Start");
        lore.add("");
        if(!tournament.ConfirmedDelete)
            lore.add( ChatColor.RED + "Shift Right-Click to Delete");
        else
            lore.add(ChatColor.RED + "Really Delete?");

        m.setLore(lore);

        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        dataContainer.set(ItemHandler.TourneyTypeId, PersistentDataType.STRING, tournament.Id);

        item.setItemMeta(m);

        return item;
    }

    @Override
    protected void click_left() {
        TournamentType type = getTournamentType();
        new AdminTournamentEditPanel(type).Show(player);
    }

    @Override
    protected void click_right_shift() {
        TournamentType type = getTournamentType();

        if(!type.ConfirmedDelete){
            type.ConfirmedDelete = true;

            Bukkit.getScheduler ().runTaskLater (BlepFishing.getPlugin(), () ->{
                type.ConfirmedDelete = false;
            } , 300);
        }
        else{
            TournamentType.Delete(type);
            ConfigHandler.instance.tourneyConfig.Save();
        }

        new AdminTournamentPanel().Show(player);
    }

    @Override
    protected void click_left_shift() {
        TournamentType type = getTournamentType();
        type.Start();
    }
}
