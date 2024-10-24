package com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit;

import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTournamentRewardsMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentEditRewardsPlacementPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TournamentEditRewardPlacementBtn extends AdminTournamentRewardsMenuButton {


    public TournamentEditRewardPlacementBtn(TournamentType tournament, int placement){
        super(tournament, placement);
    }


    @Override
    protected ItemStack buildItemStack() {

        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName(ChatColor.GOLD + "#" + Placement);

        ArrayList<String> lore = new ArrayList<>();
        List<String> rewardStrings = new ArrayList<>();

        if(tournament.CashRewards.containsKey(Placement)){
            rewardStrings.add(ChatColor.WHITE + "$" + ChatColor.GREEN + tournament.CashRewards.get(Placement));
        }

        if(tournament.ItemRewards.containsKey(Placement)){
            for(var rItem : tournament.ItemRewards.get(Placement)){
                String itemName = rItem.getType().name();
                ItemMeta rMeta = rItem.getItemMeta();

                if(rMeta != null && !rMeta.getDisplayName().isEmpty()){
                    itemName = rMeta.getDisplayName();
                }

                rewardStrings.add(rItem.getAmount() + "x " + itemName);
            }
        }
        lore.add("" + ChatColor.GOLD + Placement + ChatColor.BLUE + ": " + Formatting.ToCommaList(rewardStrings, ChatColor.WHITE, ChatColor.BLUE));


        m.setLore(lore);

        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        int placement = getPlacement();
        new AdminTournamentEditRewardsPlacementPanel(getTournamentType(), placement).Show(player);
    }

}
