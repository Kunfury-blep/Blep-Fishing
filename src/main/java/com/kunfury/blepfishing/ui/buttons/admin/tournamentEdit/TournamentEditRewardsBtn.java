package com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit;

import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTournamentMenuButton;
import com.kunfury.blepfishing.items.ItemHandler;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentEditRewardsPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class TournamentEditRewardsBtn extends AdminTournamentMenuButton {


    public TournamentEditRewardsBtn(TournamentType tournamentType) {
        super(tournamentType);
    }

    @Override
    public ItemStack buildItemStack() {
        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName("Rewards");

        ArrayList<String> lore = new ArrayList<>();

        for(var i : tournament.getPlacements()){
            List<String> rewardStrings = new ArrayList<>();

            if(tournament.CashRewards.containsKey(i)){
                rewardStrings.add(ChatColor.WHITE + "$" + ChatColor.GREEN + tournament.CashRewards.get(i));
            }

            if(tournament.ItemRewards.containsKey(i)){
                for(var rItem : tournament.ItemRewards.get(i)){
                    String itemName = rItem.getType().name();
                    ItemMeta rMeta = rItem.getItemMeta();

                    if(rMeta != null && !rMeta.getDisplayName().isEmpty()){
                        itemName = rMeta.getDisplayName();
                    }

                    rewardStrings.add(rItem.getAmount() + "x " + itemName);
                }
            }
            lore.add(ChatColor.GOLD + "#" + i + ChatColor.BLUE + ": " + Formatting.ToCommaList(rewardStrings, ChatColor.WHITE, ChatColor.BLUE));
        }

        if(tournament.getPlacements().isEmpty()){
            lore.add(ChatColor.RED + "No Rewards Set");
        }

        m.setLore(lore);

        m = setButtonId(m, getId());

        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        dataContainer.set(ItemHandler.TourneyTypeId, PersistentDataType.STRING, tournament.Id);

        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        new AdminTournamentEditRewardsPanel(getTournamentType()).Show(player);
    }

}
