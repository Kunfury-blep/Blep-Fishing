package com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTournamentMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentEditPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class TournamentEditHornTradeBtn extends AdminTournamentMenuButton {

    public TournamentEditHornTradeBtn(TournamentType tournamentType) {
        super(tournamentType);
    }

    @Override
    public ItemStack buildItemStack() {
        Material mat = Material.RED_CONCRETE;
        if(tournament.VillagerHorn)
            mat = Material.GREEN_CONCRETE;

        ItemStack item = new ItemStack(mat);
        ItemMeta m = item.getItemMeta();
        assert m != null;



        m.setDisplayName("Villager Horn Trade");
        ArrayList<String> lore = new ArrayList<>();
        if(tournament.VillagerHorn)
            lore.add(ChatColor.GREEN + "Enabled");
        else
            lore.add(ChatColor.RED + "Disabled");

        lore.add("");
        lore.add("If enabled, fishermen villagers sell Tournament Horns");
        lore.add("When used, these start a fishing tournament");

        m.setLore(lore);
        m = setButtonId(m, getId());

        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        dataContainer.set(ItemHandler.TourneyTypeId, PersistentDataType.STRING, tournament.Id);


        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        var tournament = getTournamentType();

        tournament.VillagerHorn = !tournament.VillagerHorn;
        ConfigHandler.instance.tourneyConfig.Save();
        new AdminTournamentEditPanel(getTournamentType()).Show(player);
    }


}
