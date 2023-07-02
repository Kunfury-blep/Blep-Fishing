package com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.Tournament.Config.Rewards;

import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Interfaces.Admin.AdminTournamentRewardsMenu;
import com.kunfury.blepFishing.Interfaces.MenuButton;
import com.kunfury.blepFishing.Miscellaneous.ItemHandler;
import com.kunfury.blepFishing.Tournament.TournamentHandler;
import com.kunfury.blepFishing.Tournament.TournamentObject;
import com.kunfury.blepFishing.Miscellaneous.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class AdminTourneyConfigRewardValueSaveButton extends MenuButton  {
    @Override
    public String getId() {
        return "adminTournamentRewardValueSave";
    }

    @Override
    public String getPermission() {
        return "bf.admin";
    }

    @Override
    public ItemStack getItemStack(Object o) {
        if(!(o instanceof TournamentObject t))
            return null;


        ItemStack item = new ItemStack(Material.SUNFLOWER);
        ItemMeta m = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();

        m.setDisplayName(ChatColor.GREEN + "Save Rank");

        m.setLore(lore);
        item.setItemMeta(m);

        item = NBTEditor.set(item, getId(),"blep", "item", "buttonId");
        item = NBTEditor.set(item, t.getName(),"blep", "item", "tourneyId");

        return item;
    }

    @Override
    protected void click_left() {
        TournamentObject t = getTournament();

        Inventory inv = player.getOpenInventory().getTopInventory();

        if(!inv.contains(ClickedItem)){
            player.sendMessage(Variables.getPrefix() + "Invalid Inventory Clicked");
            return;
        }

        List<String> rewardValues = new ArrayList<>();

        for(var item : inv.getStorageContents()){
            if(item == null || item.getType() == Material.AIR)
                continue;

            if(NBTEditor.contains(item, "blep", "item", "buttonId")){
                String buttonId = NBTEditor.getString(item, "blep", "item", "buttonId");
                if(buttonId.equals(new AdminTourneyConfigRewardValueCashButton().getId())){
                    int rewardCash = NBTEditor.getInt(item, "blep", "item", "tourneyCash");
                    if(rewardCash > 0)
                        rewardValues.add("CASH: " + rewardCash);

                }
                if(buttonId.equals(new AdminTourneyConfigRewardValueTextButton().getId())){
                    String rewardText = NBTEditor.getString(item, "blep", "item", "tourneyText");
                    if(rewardText != null && !rewardText.isEmpty())
                        rewardValues.add("TEXT: " + rewardText);
                }
                continue;
            }

            if(!item.hasItemMeta()){
                rewardValues.add("ITEM: " + item.getType() + " " + item.getAmount());
                continue;
            }

            rewardValues.add("BYTE: " + ItemHandler.itemStackToBase64(item));
        }

        String rewardKey = NBTEditor.getString(ClickedItem, "blep", "item", "rewardId");

        TournamentHandler.UpdateTournamentValue(t.getName() + ".Rewards." + rewardKey, rewardValues);
        t.Rewards.put(rewardKey, rewardValues);

//        var menu = new AdminTournamentRewardsMenu();
//        menu.ShowMenu(player, t);
    }


}

