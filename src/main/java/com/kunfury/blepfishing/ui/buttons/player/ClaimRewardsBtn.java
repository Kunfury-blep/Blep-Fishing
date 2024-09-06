package com.kunfury.blepfishing.ui.buttons.player;

import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.helpers.Utilities;
import com.kunfury.blepfishing.objects.UnclaimedReward;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.panels.player.PlayerPanel;
import com.kunfury.blepfishing.ui.panels.player.PlayerTournamentPanel;
import jdk.jshell.execution.Util;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ClaimRewardsBtn extends MenuButton {

    public ClaimRewardsBtn(Player player){
        super();
        this.player = player;
    }

    @Override
    public ItemStack buildItemStack() {
        ItemStack item = new ItemStack(Material.DIAMOND);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.addEnchant(Enchantment.FORTUNE, 1, true);
        m.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        var name = ChatColor.AQUA + "Claim Rewards";


        List<String> lore = new ArrayList<>();

        lore.add(ChatColor.BLUE + "Total Rewards: " + ChatColor.WHITE + Database.Rewards.GetTotalRewards(player.getUniqueId().toString()));

        m.setLore(lore);
        m.setDisplayName(name);
        item.setItemMeta(m);

        return item;
    }

    @Override
    protected void click_left() {
        var rewards = Database.Rewards.GetRewards(player.getUniqueId().toString());

        int undropped = 0;

        for(var reward : rewards){

            if(Utilities.GiveItem(player, reward.Item, false))
                Database.Rewards.Delete(reward.Id);
            else{
                undropped++;;
            }

        }

        if(undropped > 0){
            var msg = Formatting.getPrefix() + "Unable to give " + ChatColor.AQUA + undropped + ChatColor.WHITE + " items";
            player.sendMessage(msg);
        }

        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, .33f, 1f);

        new PlayerPanel().Show(player);
    }
}
