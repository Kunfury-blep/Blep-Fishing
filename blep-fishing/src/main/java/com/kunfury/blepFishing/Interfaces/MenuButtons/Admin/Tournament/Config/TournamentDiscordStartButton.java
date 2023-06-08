package com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.Tournament.Config;

import com.kunfury.blepFishing.Interfaces.Admin.AdminTournamentConfigMenu;
import com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.AdminConfigButtonBase;
import com.kunfury.blepFishing.Tournament.TournamentHandler;
import com.kunfury.blepFishing.Tournament.TournamentObject;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class TournamentDiscordStartButton extends AdminConfigButtonBase {
    @Override
    public String getId() {
        return "adminTournamentConfigDiscordStartButton";
    }

    @Override
    public String getPermission() {
        return "bf.admin";
    }


    @Override
    public ItemStack getItemStack(Object o) {
        if(!(o instanceof TournamentObject t))
            return null;

        Material material = Material.RED_CANDLE;
        if(t.DiscordStart)
            material = Material.LIME_CANDLE;

        ItemStack item = new ItemStack(material);
        ItemMeta m = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();

        assert m != null;
        m.setDisplayName("Discord Start");

        lore.add(String.valueOf(t.DiscordStart).toUpperCase());

        lore.add("");
        lore.add("Announce the tournament starting through DiscordSRV Integration");

        m.setLore(lore);
        item.setItemMeta(m);

        item = NBTEditor.set(item, getId(),"blep", "item", "buttonId");
        item = NBTEditor.set(item, t.getName() ,"blep", "item", "tourneyId");

        return item;
    }

    protected void click_left() {
        TournamentObject t = getTournament();

        TournamentHandler.UpdateTournamentValue(t.getName() + ".Discord Start", !t.DiscordStart);
        t.DiscordStart = !t.DiscordStart;

        AdminTournamentConfigMenu menu = new AdminTournamentConfigMenu();
        menu.ShowMenu(player, t);
    }


}
