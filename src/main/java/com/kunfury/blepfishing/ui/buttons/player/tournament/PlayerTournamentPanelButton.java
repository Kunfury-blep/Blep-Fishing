package com.kunfury.blepfishing.ui.buttons.player.tournament;

import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.panels.player.PlayerTournamentPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.Format;
import java.util.ArrayList;
import java.util.List;

public class PlayerTournamentPanelButton extends MenuButton {
    @Override
    public ItemStack buildItemStack() {
        ItemStack item = new ItemStack(Material.FISHING_ROD);
        ItemMeta m = item.getItemMeta();
        assert m != null;




        List<String> lore = new ArrayList<>();
        var runningTournaments = Database.Tournaments.GetActive();
        if(runningTournaments.isEmpty()){
            lore.add(Formatting.GetLanguageString("UI.Player.Buttons.Base.Tournaments.empty"));
        }else{
            for(var t : runningTournaments){
                lore.add(Formatting.GetLanguageString("UI.Player.Buttons.Base.Tournaments.tournament")
                                .replace("{name}", t.getType().Name)
                                .replace("{time}", Formatting.asTime(t.getTimeRemaining(), ChatColor.WHITE)));
            }
        }

        var name = Formatting.GetLanguageString("UI.Player.Buttons.Base.Tournaments.name")
                .replace("{amount}", String.valueOf(runningTournaments.size()));

        m.setLore(lore);
        m.setDisplayName(name);
        item.setItemMeta(m);

        return item;
    }

    @Override
    protected void click_left() {
        new PlayerTournamentPanel().Show(player);
    }
}
