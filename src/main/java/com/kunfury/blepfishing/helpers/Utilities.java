package com.kunfury.blepfishing.helpers;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.items.ItemHandler;
import com.kunfury.blepfishing.objects.FishObject;
import com.kunfury.blepfishing.objects.TournamentObject;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.objects.UnclaimedReward;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.Normalizer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class Utilities {

    public static boolean DebugMode = false;

    public static int getFreeSlots(Inventory inventory){
        int freeSlots = 0;
        for (ItemStack it : inventory.getStorageContents()) {
            if (it == null) freeSlots++;
        }
        return freeSlots;
    }

    static boolean running;
    public static void RunTimers(){
        if(!running){
            running = true;

            //Tournament Checker
            new BukkitRunnable() {
                @Override
                public void run() {
                    TournamentType.CheckCanStart();
                    TournamentObject.CheckActive();
                }

            }.runTaskTimer(BlepFishing.getPlugin(), 0, 1200);

        }
    }

    public static int getInventorySize(int baseAmt){

        baseAmt = (((int) Math.ceil(baseAmt / 9.0) ) * 9);

        if(baseAmt > 54)
            baseAmt = 54;

        return baseAmt;
    }

    public static long TimeToLong(LocalDateTime time){
        return time.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    public static LocalDateTime TimeFromLong(long milli){
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(milli), ZoneOffset.UTC);
    }

    public static void Severe(String message){
        Bukkit.getLogger().severe("BlepFishing: " + message);
    }

    public static boolean GiveItem(Player player, ItemStack item, boolean drop){
        if(player == null || !player.isOnline()){
            return false;
        }
        for(var badItem : player.getInventory().addItem(item).values()){
            if(drop){
                player.getWorld().dropItem(player.getLocation(), badItem);
                return true;
            }
            else
                return false;
        }
        return true;
    }

    public static void Announce(String message){
        for(var p : Bukkit.getOnlinePlayers()){
            p.sendMessage(message);
        }
    }

    public static void Announce(TextComponent textComponent){
        for(var p : Bukkit.getOnlinePlayers()){
            p.spigot().sendMessage(textComponent);
        }
    }

    public static void SellAllFish(Player player) {
        double totalValue = 0;
        int count = 0;
        for(var i : player.getInventory().getContents()){
            if(!ItemHandler.hasTag(i, ItemHandler.FishIdKey))
                continue;

            FishObject fish = FishObject.GetFromItem(i);
            if(fish == null){
                Severe("Tried to sell invalid fish");
                continue;
            }
            i.setAmount(0);
            totalValue += fish.Value;
            count++;
        }

        if(totalValue <= 0)
            return;


        EconomyResponse r = BlepFishing.getEconomy().depositPlayer(player, totalValue);
        if(!r.transactionSuccess())
            Utilities.Severe(r.errorMessage);

        player.sendMessage(Formatting.GetMessagePrefix() +
                Formatting.GetLanguageString("Economy.soldAllFish")
                        .replace("{amount}", String.valueOf(count))
                        .replace("{value}", String.valueOf(totalValue)));
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, .3f, 1f);
    }

    public static void SellFish(Player player) {
        ItemStack fishItem = player.getInventory().getItemInMainHand();
        FishObject fish = FishObject.GetFromItem(fishItem);

        if(fish == null){
            Severe("Tried to sell invalid fish");
            return;
        }
        fishItem.setAmount(0);

        EconomyResponse r = BlepFishing.getEconomy().depositPlayer(player, fish.Value);
        if(!r.transactionSuccess())
            Utilities.Severe(r.errorMessage);

        player.sendMessage(Formatting.GetMessagePrefix() +
                Formatting.GetLanguageString("Economy.soldFish")
                        .replace("{fish}", fish.getFormattedName())
                        .replace("{value}", String.valueOf(fish.Value)));

        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, .3f, 1f);
    }
}
