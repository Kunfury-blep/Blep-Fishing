package com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentEditPanel;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class AdminTournamentCreateButton extends MenuButton {
    @Override
    public ItemStack buildItemStack(Player player) {
        Material mat = Material.TURTLE_SCUTE;

        ItemStack item = new ItemStack(mat);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName("Create New Tournament");
        ArrayList<String> lore = new ArrayList<>();
        m.setLore(lore);
        m = setButtonId(m, getId());
        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        String tourneyId = "new_tournament";

        int i = 0;
        while(TournamentType.IdExists(tourneyId)){
            tourneyId = "new_tournament" + i;
            i++;
        }

        TournamentType tournamentType = new TournamentType(tourneyId);
        TournamentType.AddNew(tournamentType);

        ConfigHandler.instance.tourneyConfig.Save();
        new AdminTournamentEditPanel(tournamentType).Show(player);
    }


}
