package com.kunfury.blepFishing.Interfaces.Admin;

import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.Tournament.AdminTourneyButton;
import com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.Tournament.Config.Rewards.AdminTourneyConfigRewardValueCashButton;
import com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.Tournament.Config.Rewards.AdminTourneyConfigRewardValueSaveButton;
import com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.Tournament.Config.Rewards.AdminTourneyConfigRewardValueTextButton;
import com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.Tournament.Config.Rewards.AdminTourneyConfigRewardValueButton;
import com.kunfury.blepFishing.Interfaces.MenuHandler;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Tournament.TournamentObject;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

import static com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.Tournament.Config.Rewards.AdminTourneyConfigRewardValueButton.*;
import static com.kunfury.blepFishing.Miscellaneous.ItemHandler.itemStackFromBase64;

public class AdminTournamentRewardsValueMenu {
    public void ShowMenu(CommandSender sender, TournamentObject tourney, String rewardKey) {
        Player p = (Player)sender;

        if(tourney == null || rewardKey == null){
            p.sendMessage(Variables.getPrefix() + "Reward or tournament is null.");
            return;
        }

        var rewardValues = tourney.Rewards.get(rewardKey);

        int invSize = 27;

        Inventory inv = Bukkit.createInventory(null, invSize, Formatting.getMessage("Admin.panelTitle"));

        int slot = 0;
        int cashReward = 0;
        StringBuilder textReward = new StringBuilder();
        for(var v : rewardValues){
            String dataType = v.substring(0, 6);
            String itemStr = v.replace(dataType, "");
            ItemStack item = new ItemStack(Material.AIR, 1);

            //TODO: Parse the rewards, place all items in the inventory using ItemHandler

            switch (dataType) {
                case "CASH: " -> cashReward += Integer.parseInt(itemStr);
                case "TEXT: " -> {
                    if (textReward.isEmpty())
                        textReward = new StringBuilder(itemStr);
                    else
                        textReward.append("\n").append(itemStr);
                }
                case "ITEM: " -> {
                    String[] infoArray = itemStr.split(" ");
                    if (infoArray.length >= 2) {
                        Material type = Material.getMaterial(infoArray[0]);

                        if (type != null) {
                            item.setType(Material.valueOf(infoArray[0]));
                            item.setAmount(Integer.parseInt(infoArray[1]));
                        } else {
                            Bukkit.getLogger().warning("Error creating Blep Fishing casket reward from " + infoArray[0] + ". Defaulting to air.");
                            item.setType(Material.AIR);
                            item.setAmount(0);
                        }


                    }
                    inv.addItem(item);
                }
                case "BYTE: " -> {
                    try {
                        inv.addItem(itemStackFromBase64(itemStr));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        ItemStack textItem = new AdminTourneyConfigRewardValueTextButton(textReward.toString(), rewardKey).getItemStack(tourney);

        ItemStack cashItem = new AdminTourneyConfigRewardValueCashButton(cashReward, rewardKey).getItemStack(tourney);

        ItemStack saveItem = new AdminTourneyConfigRewardValueSaveButton().getItemStack(tourney);
        saveItem = NBTEditor.set(saveItem, rewardKey, "blep", "item", "rewardId");

        inv.setItem(inv.getSize() - 1, textItem);
        inv.setItem(inv.getSize() - 5, saveItem);
        inv.setItem(inv.getSize() - 9, cashItem);

        p.openInventory(inv);
    }
}
