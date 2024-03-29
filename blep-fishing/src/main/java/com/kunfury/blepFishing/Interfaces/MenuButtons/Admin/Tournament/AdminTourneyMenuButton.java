package com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.Tournament;

import com.kunfury.blepFishing.BlepFishing;
import com.kunfury.blepFishing.Endgame.EndgameVars;
import com.kunfury.blepFishing.Interfaces.Admin.AdminMenu;
import com.kunfury.blepFishing.Interfaces.Admin.AdminTournamentMenu;
import com.kunfury.blepFishing.Interfaces.MenuButton;
import com.kunfury.blepFishing.Interfaces.MenuHandler;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Quests.QuestHandler;
import com.kunfury.blepFishing.Tournament.TournamentHandler;
import com.kunfury.blepFishing.Tournament.TournamentObject;
import com.kunfury.blepFishing.Miscellaneous.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AdminTourneyMenuButton extends MenuButton {
    @Override
    public String getId() {
        return "adminTournamentMenu";
    }

    @Override
    public String getPermission() {
        return "bf.admin";
    }

    @Override
    public ItemStack getItemStack(Object o) {
        ItemStack item = new ItemStack(Material.FISHING_ROD);
        ItemMeta m = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();

        m.setDisplayName("Tournaments");


        if(!BlepFishing.configBase.getEnableTournaments()){
            lore.add(ChatColor.RED + "Tournaments Are Disabled");
            lore.add(ChatColor.YELLOW +"Right-Click to Enable");
        }

        m.setLore(lore);
        item.setItemMeta(m);

        item = NBTEditor.set(item, getId(),"blep", "item", "buttonId");

        return item;
    }

    @Override
    protected void click_left() {
        if(!player.hasPermission(getPermission()) || !BlepFishing.configBase.getEnableTournaments())
            return;

        AdminTournamentMenu menu = new AdminTournamentMenu();
        menu.ShowMenu(player);
    }

    @Override
    protected void click_right(){
        if(BlepFishing.configBase.getEnableTournaments()){
            click_left();
            return;
        }

        TournamentHandler.EnableTournaments(true, player);
        new AdminTournamentMenu().ShowMenu(player);
    }
}
