package com.kunfury.blepFishing.Interfaces.MenuButtons;

import com.kunfury.blepFishing.BlepFishing;
import com.kunfury.blepFishing.Interfaces.Admin.AdminTournamentMenu;
import com.kunfury.blepFishing.Interfaces.MenuButton;
import com.kunfury.blepFishing.Interfaces.Player.TournamentPanel;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Tournament.TournamentHandler;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PlayerTourneyMenuButton extends MenuButton {
    @Override
    public String getId() {
        return "playerTournamentMenu";
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public ItemStack getItemStack(Object o) {
        ItemStack item = new ItemStack(Material.FISHING_ROD);
        ItemMeta m = item.getItemMeta();
        List<String> lore = new ArrayList<>();

        int size = TournamentHandler.ActiveTournaments.size();
        String title;
        if(size <= 0) title = Formatting.getMessage("Player Panel.noTournaments");
        else{
            title = size + " ";
            if(size > 1) {
                title += Formatting.getMessage("Player Panel.multiTournaments");
            }else
                title += Formatting.getMessage("Player Panel.singleTournament");
            lore.add("");

        }

        m.setDisplayName(ChatColor.YELLOW + title);



        for(var t : TournamentHandler.ActiveTournaments){
            lore.add(Formatting.formatColor(t.getName()));
        }

        m.setLore(lore);

        item.setItemMeta(m);

        item = NBTEditor.set(item, getId(),"blep", "item", "buttonId");

        return item;
    }

    @Override
    protected void click_left() {
        new TournamentPanel().ClickBase(player);
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
