package com.kunfury.blepfishing.helpers;

import com.kunfury.blepfishing.config.*;
import com.kunfury.blepfishing.items.ItemHandler;
import com.kunfury.blepfishing.objects.TreasureType;
import com.kunfury.blepfishing.ui.objects.Panel;
import jdk.jshell.execution.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.Format;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TreasureHandler {
    public boolean Enabled = ConfigHandler.instance.baseConfig.getEnableTournaments();

    public static TreasureHandler instance;
    public final static HashMap<Player, Inventory> OpenInventories = new HashMap<>();

    public void Initialize(){
        instance = this;
    }

    public boolean TreasureCaught(){
        if(!Enabled) return false;
        double randR = ThreadLocalRandom.current().nextDouble(0, 100);
        var treasureChance = ConfigHandler.instance.baseConfig.getTreasureChance();

//        Bukkit.broadcastMessage("Treasure Chance: " + treasureChance);
//        Bukkit.broadcastMessage("Roll: " + randR);

        return randR <= treasureChance;
    }

    public ItemStack GetTreasureItem(){
        return GetTreasure().GetItem();
    }

    private TreasureType GetTreasure(){
        //Rarity Selection
        int randR = ThreadLocalRandom.current().nextInt(0, TreasureType.GetTotalWeight());

        List<TreasureType> types = new java.util.ArrayList<>(TreasureType.GetAll().stream().toList());
        types.sort((TreasureType t1, TreasureType t2) -> t1.Weight - t2.Weight);

        for(var t : types){
            if(randR <= t.Weight) {
                return t;
            }else
                randR -= t.Weight;
        }

        Bukkit.getLogger().severe("Unable to generate treasure.");
        return null;
    }

    public void Open(ItemStack item, Player player) {
        var treasureId = ItemHandler.getTagString(item, ItemHandler.TreasureTypeId);
        var treasureType = TreasureType.FromId(treasureId);

        if(treasureType == null){
            Utilities.Severe("Could not find treasure type to open with id " + treasureId);
            return;
        }

        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, .30f, 1f);

        if(treasureType.Rewards.isEmpty()){
            Utilities.Severe(player.getDisplayName() + " tried to open a " + treasureType.Name + " which contained no drops.");
        }

        List<ItemStack> treasureDrops = new ArrayList<>();
        int i = 0;
        while(treasureDrops.isEmpty()){ //Ensures at least one item drops
            i++;
            if(i >= 1000){
                Utilities.Severe("No rewards found after 1,000 iterations on " + treasureType.Name);
                return;
            }
            for(var reward : treasureType.Rewards){
                if(!reward.Drops())
                    continue;

                treasureDrops.add(reward.Item);
            }
        }

        Inventory inv = Bukkit.createInventory(player, Utilities.getInventorySize(treasureDrops.size()), Formatting.formatColor(treasureType.Name));

        for(var itemDrop : treasureDrops){
            inv.addItem(itemDrop);
            player.sendMessage(Formatting.getPrefix() +
                    ChatColor.YELLOW + "You found " + ChatColor.AQUA + Formatting.getItemName(itemDrop) + ChatColor.YELLOW + " x" + itemDrop.getAmount());

        }

        player.openInventory(inv);
        OpenInventories.put(player, inv);

        item.setAmount(item.getAmount() - 1);
    }

    public void CloseTreasure(Player player){
        if(!OpenInventories.get(player).isEmpty()){
            for(var item : OpenInventories.get(player).getContents()){
                if(item == null)
                    continue;
                Utilities.GiveItem(player, item, true);
            }
        }


        OpenInventories.remove(player);
    }
}
