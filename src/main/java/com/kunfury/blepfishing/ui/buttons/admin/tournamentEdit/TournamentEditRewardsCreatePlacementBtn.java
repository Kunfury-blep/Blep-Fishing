package com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTournamentRewardsMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentEditRewardsPlacementPanel;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class TournamentEditRewardsCreatePlacementBtn extends AdminTournamentRewardsMenuButton {


    public TournamentEditRewardsCreatePlacementBtn(TournamentType tournament){
        super(tournament, 0);
    }


    public ItemStack buildItemStack(Player player) {

        ItemStack item = new ItemStack(Material.TURTLE_SCUTE);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName("Create Placement");

        ArrayList<String> lore = new ArrayList<>();

        m.setLore(lore);

        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        var type = getTournamentType();

        int i = 1;

        while(type.ItemRewards.containsKey(i) || type.CashRewards.containsKey(i)){
            i++;
        }

        type.ItemRewards.put(i, new ArrayList<>());
        ConfigHandler.instance.tourneyConfig.Save();


        new AdminTournamentEditRewardsPlacementPanel(type, i).Show(player);
    }

}
