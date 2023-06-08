package com.kunfury.blepFishing.Interfaces.Admin;

import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.Tournament.AdminTourneyButton;
import com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.Tournament.AdminTourneyConfigMenuButton;
import com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.Tournament.Config.Rewards.AdminTourneyConfigRewardButton;
import com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.Tournament.Config.Rewards.AdminTourneyConfigRewardCreateButton;
import com.kunfury.blepFishing.Interfaces.MenuHandler;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Tournament.TournamentObject;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.Tournament.Config.Rewards.AdminTourneyConfigRewardButton.*;

public class AdminTournamentRewardsMenu {
    public void ShowMenu(CommandSender sender, TournamentObject t) {
        Player p = (Player)sender;

        if(t == null){
            p.sendMessage(Variables.getPrefix() + "Tournament is null.");
            return;
        }

        int invSize = Formatting.getInventorySize(t.Rewards.size());

        Inventory inv = Bukkit.createInventory(null, invSize, Formatting.getMessage("Admin.panelTitle"));

        for(int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, MenuHandler.getBackgroundItem());
        }
        AtomicInteger slot = new AtomicInteger();

        t.Rewards.forEach((key, value) -> {
            if(!key.equalsIgnoreCase("DEFAULT")){ //Makes sure that default is placed at the end of the rewards
                RewardObject reward = new RewardObject(key, value, t.getName());

                int slotVal = Integer.parseInt(key) - 1;

                if(slotVal > 44){
                    sender.sendMessage(Variables.getPrefix() + "In-Game Editor can only display up to 44 rewards for a tournament.");
                    return;
                }

                if(inv.getSize() < slotVal){
                    return;
                }

                inv.setItem(slotVal, new AdminTourneyConfigRewardButton().getItemStack(reward));
                if(slotVal > slot.get()){
                    slot.set(slotVal);
                }
            }
        });


        if(!t.Rewards.containsKey("DEFAULT")){
            RewardObject reward = new RewardObject("DEFAULT", new ArrayList<>(), t.getName());

            inv.setItem(slot.get(), new AdminTourneyConfigRewardButton().getItemStack(reward));
        }else{
            RewardObject reward = new RewardObject("DEFAULT", t.Rewards.get("DEFAULT"), t.getName());

            inv.setItem(slot.get(), new AdminTourneyConfigRewardButton().getItemStack(reward));
        }

        inv.setItem(inv.getSize() - 1, MenuHandler.getBackButton(new AdminTourneyButton(), t));
        inv.setItem(inv.getSize() - 9, new AdminTourneyConfigRewardCreateButton().getItemStack(t));

        p.openInventory(inv);
    }
}
