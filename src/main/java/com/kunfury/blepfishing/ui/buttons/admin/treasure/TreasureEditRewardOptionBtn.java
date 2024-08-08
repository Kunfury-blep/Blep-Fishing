package com.kunfury.blepfishing.ui.buttons.admin.treasure;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.helpers.Utilities;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.objects.TreasureType;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTournamentRewardsMenuButton;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTreasureMenuButton;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTreasureRewardMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentEditRewardsPlacementPanel;
import com.kunfury.blepfishing.ui.panels.admin.treasure.AdminTreasureEditRewardsPanel;
import com.kunfury.blepfishing.ui.panels.admin.treasure.AdminTreasureEditRewardsSelectionPanel;
import com.kunfury.blepfishing.ui.panels.admin.treasure.AdminTreasurePanel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TreasureEditRewardOptionBtn extends AdminTreasureRewardMenuButton {


    public TreasureEditRewardOptionBtn(TreasureType type, TreasureType.TreasureReward reward){
        super(type, reward);
    }


    @Override
    protected ItemStack buildItemStack() {

        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta m = item.getItemMeta();
        assert m != null;



        ArrayList<String> lore = new ArrayList<>();
        //List<String> rewardStrings = new ArrayList<>();

        String itemName = ChatColor.RED + "No Item Set";

        ItemStack rItem = Reward.Item;
        if(rItem != null && rItem.getAmount() > 0){
            item.setType(rItem.getType());

            ItemMeta rMeta = rItem.getItemMeta();
            if(rMeta != null && !rMeta.getDisplayName().isEmpty())
                itemName = rMeta.getDisplayName() + " x" + rItem.getAmount();
            else
                itemName = rItem.getType() + " x" + rItem.getAmount();
        }

        m.setDisplayName(itemName);

        if(Reward.Cash > 0){
            lore.add(ChatColor.WHITE + "$" + ChatColor.GREEN + Reward.Cash);
            lore.add("");
        }

//        lore.add(ChatColor.BLUE + Formatting.getCommaList(rewardStrings, ChatColor.WHITE, ChatColor.BLUE));

        lore.add(ChatColor.BLUE + "Drop Chance: " + ChatColor.BLUE + Reward.DropChance + "%");
        lore.add(ChatColor.BLUE + "Announce: " + ChatColor.WHITE + Reward.Announce);

        lore.add("");
        lore.add(ChatColor.YELLOW + "Left-Click to Edit");
        if(!Reward.ConfirmedDelete)
            lore.add( ChatColor.RED + "Shift Right-Click to Delete");
        else
            lore.add(ChatColor.RED + "Really Delete?");

        m.setLore(lore);

        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        new AdminTreasureEditRewardsSelectionPanel(getTreasureType(), getReward()).Show(player);
    }

    @Override
    protected void click_right_shift() {
        TreasureType type = getTreasureType();
        var reward = getReward();

        if(!reward.ConfirmedDelete){
            reward.ConfirmedDelete = true;

            Bukkit.getScheduler ().runTaskLater (BlepFishing.getPlugin(), () ->{
                reward.ConfirmedDelete = false;
            } , 300);
        }
        else{
            type.Rewards.remove(reward);
            ConfigHandler.instance.treasureConfig.Save();
            player.sendMessage(ChatColor.YELLOW + "Successfully Deleted Reward From " + ChatColor.WHITE + Formatting.formatColor(type.Name));
        }

        new AdminTreasureEditRewardsPanel(type).Show(player);
    }

}
