package com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.Tournament;

import com.kunfury.blepFishing.Config.ItemsConfig;
import com.kunfury.blepFishing.Interfaces.Admin.AdminMenu;
import com.kunfury.blepFishing.Interfaces.Admin.AdminTournamentConfigMenu;
import com.kunfury.blepFishing.Interfaces.Admin.AdminTournamentMenu;
import com.kunfury.blepFishing.Interfaces.MenuButton;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Tournament.TournamentHandler;
import com.kunfury.blepFishing.Tournament.TournamentObject;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AdminTourneyButton extends MenuButton {
    @Override
    public String getId() {
        return "adminTournamentButton";
    }

    @Override
    public String getPermission() {
        return "bf.admin";
    }


    @Override
    public ItemStack getItemStack(Object o) {
        if(!(o instanceof TournamentObject t))
            return null;
        ItemStack item = new ItemStack(ItemsConfig.FishMat);
        ItemMeta m = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();

        m.setDisplayName(Formatting.formatColor(t.getName()));
        m.setCustomModelData(t.getModelData());

        lore.add("Duration: " + t.getDuration());
        lore.add("Fish: " + t.getFishType());

        lore.add("");

        if(t.isRunning()){
            lore.add(ChatColor.GREEN + "Running");
            lore.add("");
            lore.add("Right-Click to Finish");
            lore.add("Shift-Right-Click to Cancel");
        }else{
            lore.add(ChatColor.RED + "Not Running");
            lore.add("");
            lore.add("Left-Click to Start");
            lore.add("Right-Click to Edit");
        }

        m.setLore(lore);
        item.setItemMeta(m);

        item = NBTEditor.set(item, getId(),"blep", "item", "buttonId");
        item = NBTEditor.set(item, t.getName() ,"blep", "item", "tourneyId");

        return item;
    }

    @Override
    protected void click_left() {
        TournamentObject tourney = getTournament();

        if(!tourney.isRunning()){
            new TournamentHandler().Start(tourney);
            new AdminTournamentMenu().ShowMenu(player);
        }
    }

    @Override
    protected void click_right() {
        TournamentObject tourney = getTournament();

        if(tourney.isRunning()){
            new TournamentHandler().Finish(tourney);
            new AdminTournamentMenu().ShowMenu(player);
            return;
        }

        new AdminTournamentConfigMenu().ShowMenu(player, tourney);
    }

    @Override
    protected void click_right_shift() {
        TournamentObject tourney = getTournament();

        if(tourney.isRunning()){
            new TournamentHandler().Cancel(tourney);
            new AdminTournamentMenu().ShowMenu(player);
        }
    }

}
