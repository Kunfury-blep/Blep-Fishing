package com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.objects.TournamentObject;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTournamentMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentEditPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.xml.crypto.Data;
import java.util.ArrayList;

public class TournamentEditBossBarBtn extends AdminTournamentMenuButton {

    public TournamentEditBossBarBtn(TournamentType tournamentType) {
        super(tournamentType);
    }

    @Override
    public ItemStack buildItemStack(Player player) {
        Material mat = Material.RED_CONCRETE;
        if(tournament.HasBossBar)
            mat = Material.GREEN_CONCRETE;

        ItemStack item = new ItemStack(mat);
        ItemMeta m = item.getItemMeta();
        assert m != null;



        m.setDisplayName("Tournament BossBar");
        ArrayList<String> lore = new ArrayList<>();
        if(tournament.HasBossBar)
            lore.add(ChatColor.GREEN + "Enabled");
        else
            lore.add(ChatColor.RED + "Disabled");

        lore.add("");
        lore.add("Shows a BossBar at the top of the screen");
        lore.add("Players can toggle in their tournament panel");

        m.setLore(lore);
        m = setButtonId(m, getId());

        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        dataContainer.set(ItemHandler.TourneyTypeId, PersistentDataType.STRING, tournament.Id);


        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        var tournamentType = getTournamentType();

        tournamentType.HasBossBar = !tournamentType.HasBossBar;

        for(var tourney : Database.Tournaments.GetActive()){
            if(!tourney.TypeId.equals(tournamentType.Id))
                continue;

            tourney.RefreshBossBar();
        }

        ConfigHandler.instance.tourneyConfig.Save();
        new AdminTournamentEditPanel(getTournamentType()).Show(player);
    }


}
