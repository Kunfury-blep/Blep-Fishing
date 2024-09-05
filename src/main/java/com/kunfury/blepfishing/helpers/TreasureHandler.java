package com.kunfury.blepfishing.helpers;

import com.kunfury.blepfishing.config.*;
import com.kunfury.blepfishing.items.ItemHandler;
import com.kunfury.blepfishing.objects.treasure.Casket;
import com.kunfury.blepfishing.objects.treasure.CompassPiece;
import com.kunfury.blepfishing.objects.treasure.TreasureType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TreasureHandler {
    public boolean Enabled = ConfigHandler.instance.baseConfig.getEnableTournaments();

    public static TreasureHandler instance;
    public final static HashMap<Player, Inventory> OpenInventories = new HashMap<>();

    public TreasureHandler(){
        if(instance != null){
            Utilities.Severe("Treasure Handler Instance Already Exists");
            return;
        }
        instance = this;

        for(var treasure : TreasureType.ActiveTypes.values()){
            totalWeight += treasure.Weight;
        }
    }

    public boolean TreasureCaught(){
        if(!Enabled) return false;
        double randR = ThreadLocalRandom.current().nextDouble(0, 100);
        var treasureChance = ConfigHandler.instance.treasureConfig.getTreasureChance();

        return randR <= treasureChance;
    }

    public ItemStack GetTreasureItem(){
        var treasure = GetTreasure();

        if(treasure == null){
            return null;
        }

        return treasure.GetItem();
    }

    public TreasureType GetTreasure(){
        //Rarity Selection
        int randR = ThreadLocalRandom.current().nextInt(0, GetTotalWeight());

        var treasures = new ArrayList<>(TreasureType.ActiveTypes.values());

        treasures.sort(Comparator.comparingInt((TreasureType t) -> t.Weight));

        for(var t : treasures){
            //Bukkit.broadcastMessage(t.Id + " - " + t.Weight);
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
        var casket = Casket.FromId(treasureId);

        if(casket == null){
            Utilities.Severe("Could not find treasure type to open with id " + treasureId);
            return;
        }

        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, .30f, 1f);

        if(casket.Rewards.isEmpty()){
            Utilities.Severe(player.getDisplayName() + " tried to open a " + casket.Name + " which contained no drops.");
        }

        List<ItemStack> treasureDrops = new ArrayList<>();
        int i = 0;
        while(treasureDrops.isEmpty()){ //Ensures at least one item drops
            i++;
            if(i >= 1000){
                Utilities.Severe("No rewards found after 1,000 iterations on " + casket.Name);
                return;
            }
            for(var reward : casket.Rewards){
                if(!reward.Drops())
                    continue;

                treasureDrops.add(reward.Item);
            }
        }

        Inventory inv = Bukkit.createInventory(player, Utilities.getInventorySize(treasureDrops.size()), Formatting.formatColor(casket.Name));

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

    private int totalWeight = 0;
    public int GetTotalWeight(){
        return totalWeight;
    }

}
