package com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.Tournament.Config;

import com.kunfury.blepFishing.Interfaces.Admin.AdminTournamentConfigMenu;
import com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.AdminConfigButtonBase;
import com.kunfury.blepFishing.Tournament.TournamentHandler;
import com.kunfury.blepFishing.Tournament.TournamentObject;
import com.kunfury.blepFishing.Miscellaneous.NBTEditor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class TournamentBossbarTimerButton extends AdminConfigButtonBase {
    @Override
    public String getId() {
        return "adminTournamentConfigBossbarTimerButton";
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
        if(t.BossbarTimer)
            material = Material.LIME_CANDLE;

        ItemStack item = new ItemStack(material);
        ItemMeta m = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();

        assert m != null;
        m.setDisplayName("Bossbar Timer");

        lore.add(String.valueOf(t.BossbarTimer).toUpperCase());

        m.setLore(lore);
        item.setItemMeta(m);

        item = NBTEditor.set(item, getId(),"blep", "item", "buttonId");
        item = NBTEditor.set(item, t.getName() ,"blep", "item", "tourneyId");

        return item;
    }

    protected void click_left() {
        TournamentObject t = getTournament();

        TournamentHandler.UpdateTournamentValue(t.getName() + ".Bossbar Timer", !t.BossbarTimer);
        t.BossbarTimer = !t.BossbarTimer;

        AdminTournamentConfigMenu menu = new AdminTournamentConfigMenu();
        menu.ShowMenu(player, t);
    }


}
