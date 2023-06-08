package com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.Tournament.Config.Rewards;

import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Interfaces.Admin.AdminTournamentMenu;
import com.kunfury.blepFishing.Interfaces.Admin.AdminTournamentRewardsMenu;
import com.kunfury.blepFishing.Interfaces.Admin.AdminTournamentRewardsValueMenu;
import com.kunfury.blepFishing.Interfaces.MenuButton;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Tournament.TournamentHandler;
import com.kunfury.blepFishing.Tournament.TournamentObject;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class AdminTourneyConfigRewardButton extends MenuButton  {
    @Override
    public String getId() {
        return "adminTournamentReward";
    }

    @Override
    public String getPermission() {
        return "bf.admin";
    }

    @Override
    public ItemStack getItemStack(Object o) {
        if(!(o instanceof RewardObject r))
            return null;

        String title = "Default";
        Material material = Material.CHEST;

        if(!r.Key.equalsIgnoreCase("DEFAULT")){
            title = "Rank #" + r.Key;
            material = Material.BELL;
        }

        ItemStack item = new ItemStack(material);
        ItemMeta m = item.getItemMeta();

        m.setDisplayName(title);

        ArrayList<String> lore = new ArrayList<>();


        for(var v : r.Values){
            lore.add(v.substring(0, 4));
        }

        lore.add("");

        lore.add(Formatting.formatColor("Left Click to &7Edit"));
        lore.add(Formatting.formatColor("Right Click to &4Delete"));

        m.setLore(lore);
        item.setItemMeta(m);

        item = NBTEditor.set(item, getId(),"blep", "item", "buttonId");
        item = NBTEditor.set(item, r.TourneyId ,"blep", "item", "tourneyId");
        item = NBTEditor.set(item, r.Key ,"blep", "item", "rewardKey");

        return item;
    }

    @Override
    protected void click_left() {
        var menu = new AdminTournamentRewardsValueMenu();

        String rewardKey = NBTEditor.getString(ClickedItem, "blep", "item", "rewardKey");

        menu.ShowMenu(player, getTournament(), rewardKey);
    }

    @Override
    protected  void click_right(){
        String rewardKey = NBTEditor.getString(ClickedItem, "blep", "item", "rewardKey");

        if(rewardKey.equalsIgnoreCase("DEFAULT")){
            player.sendMessage(Variables.getPrefix() + "You cannot delete the default reward.");
            return;
        }

        TournamentObject t = getTournament();

        TournamentHandler.UpdateTournamentValue(t.getName() + ".Rewards." + rewardKey, null);
        t.Rewards.remove(rewardKey);

        var menu = new AdminTournamentRewardsMenu();
        menu.ShowMenu(player, t);
    }

    public static class RewardObject{
        public String Key;
        public List<String> Values;
        public String TourneyId;

        public RewardObject(String key, List<String> values, String tourneyId){
            Key = key;
            Values = values;
            TourneyId = tourneyId;
        }
    }


}

