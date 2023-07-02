package com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.Tournament.Config.Rewards;

import com.kunfury.blepFishing.Interfaces.Admin.AdminTournamentConfigMenu;
import com.kunfury.blepFishing.Interfaces.Admin.AdminTournamentRewardsMenu;
import com.kunfury.blepFishing.Interfaces.Admin.AdminTournamentRewardsValueMenu;
import com.kunfury.blepFishing.Interfaces.MenuButton;
import com.kunfury.blepFishing.Tournament.TournamentHandler;
import com.kunfury.blepFishing.Tournament.TournamentObject;
import com.kunfury.blepFishing.Miscellaneous.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AdminTourneyConfigRewardCreateButton extends MenuButton  {
    @Override
    public String getId() {
        return "adminTournamentRewardCreate";
    }

    @Override
    public String getPermission() {
        return "bf.admin";
    }

    @Override
    public ItemStack getItemStack(Object o) {
        if(!(o instanceof TournamentObject t))
            return null;


        ItemStack item = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta m = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();

        m.setDisplayName("Create New Reward");

        m.setLore(lore);
        item.setItemMeta(m);

        item = NBTEditor.set(item, getId(),"blep", "item", "buttonId");
        item = NBTEditor.set(item, t.getName(),"blep", "item", "tourneyId");

        return item;
    }

    @Override
    protected void click_left() {
        TournamentObject t = getTournament();

        int i = 1;
        String rewardKey = null;

        while(rewardKey == null && i < 1000){
            if(!t.Rewards.containsKey(String.valueOf(i))){
                rewardKey = String.valueOf(i);
            }
            i++;
        }

        List<String> rewardValues = new ArrayList<>();
        rewardValues.add("TEXT: Thank you for participating in the tournament!");

        TournamentHandler.UpdateTournamentValue(t.getName() + ".Rewards." + rewardKey, rewardValues);
        t.Rewards.put(rewardKey, rewardValues);

        var menu = new AdminTournamentRewardsMenu();
        menu.ShowMenu(player, t);
    }


}

