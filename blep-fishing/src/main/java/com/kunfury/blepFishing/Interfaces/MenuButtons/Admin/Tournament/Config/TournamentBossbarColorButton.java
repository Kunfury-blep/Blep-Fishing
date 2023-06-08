package com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.Tournament.Config;

import com.kunfury.blepFishing.BlepFishing;
import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Interfaces.Admin.AdminTournamentConfigMenu;
import com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.AdminConfigButtonBase;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Tournament.TournamentHandler;
import com.kunfury.blepFishing.Tournament.TournamentObject;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class TournamentBossbarColorButton extends AdminConfigButtonBase {
    @Override
    public String getId() {
        return "adminTournamentConfigBossbarColorButton";
    }

    @Override
    public String getPermission() {
        return "bf.admin";
    }

    @Override
    public ItemStack getItemStack(Object o) {
        if(!(o instanceof TournamentObject t))
            return null;

        Material material = switch(t.BossbarColor){
            case RED -> Material.RED_BANNER;
            case PINK -> Material.PINK_BANNER;
            case BLUE -> Material.BLUE_BANNER;
            case GREEN -> Material.GREEN_BANNER;
            case PURPLE -> Material.PURPLE_BANNER;
            case WHITE -> Material.WHITE_BANNER;
            case YELLOW -> Material.YELLOW_BANNER;
        };

        ItemStack item = new ItemStack(material);
        ItemMeta m = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();

        assert m != null;
        m.setDisplayName("Bossbar Color");

        lore.add(t.BossbarColor.toString());

        m.setLore(lore);
        item.setItemMeta(m);

        item = NBTEditor.set(item, getId(),"blep", "item", "buttonId");
        item = NBTEditor.set(item, t.getName() ,"blep", "item", "tourneyId");

        return item;
    }

    protected void click_left() {
        TournamentObject t = getTournament();

        BarColor newColor = BarColor.values()[(t.BossbarColor.ordinal() + 1) % BarColor.values().length];


        TournamentHandler.UpdateTournamentValue(t.getName() + ".Bossbar Color", newColor.toString());
        t.BossbarColor = newColor;

        AdminTournamentConfigMenu menu = new AdminTournamentConfigMenu();
        menu.ShowMenu(player, t);
    }
}
