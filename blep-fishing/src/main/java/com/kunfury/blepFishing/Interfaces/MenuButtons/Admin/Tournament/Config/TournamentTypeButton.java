package com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.Tournament.Config;

import com.kunfury.blepFishing.Interfaces.Admin.AdminTournamentConfigMenu;
import com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.AdminConfigButtonBase;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Tournament.TournamentHandler;
import com.kunfury.blepFishing.Tournament.TournamentObject;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class TournamentTypeButton extends AdminConfigButtonBase {
    @Override
    public String getId() {
        return "adminTournamentConfigTypeButton";
    }

    @Override
    public String getPermission() {
        return "bf.admin";
    }


    @Override
    public ItemStack getItemStack(Object o) {
        if(!(o instanceof TournamentObject t))
            return null;

        ItemStack item = new ItemStack(Material.CLOCK);
        ItemMeta m = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();

        assert m != null;
        m.setDisplayName("Tournament Type");

        lore.add(Formatting.formatColor(t.Type.toString()));

        lore.add("");
        lore.add(ChatColor.WHITE + t.Type.getDescription());

        m.setLore(lore);
        item.setItemMeta(m);

        item = NBTEditor.set(item, getId(),"blep", "item", "buttonId");
        item = NBTEditor.set(item, t.getName() ,"blep", "item", "tourneyId");

        return item;
    }

    protected void click_left() {
        TournamentObject t = getTournament();

        var newType = t.Type.next();

        TournamentHandler.UpdateTournamentValue(t.getName() + ".Type", newType.toString());
        t.Type = newType;

        AdminTournamentConfigMenu menu = new AdminTournamentConfigMenu();
        menu.ShowMenu(player, t);
    }


}
