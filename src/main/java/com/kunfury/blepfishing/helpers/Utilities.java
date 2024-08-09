package com.kunfury.blepfishing.helpers;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.objects.TournamentObject;
import com.kunfury.blepfishing.objects.TournamentType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

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

//            new BukkitRunnable() {
//                @Override
//                public void run() {
//                    LocalDateTime dt = LocalDateTime.now();
////                    int hour = dt.getHour();
////                    int minute = dt.getMinute();
////                    int seconds = dt.getSecond();
//
//                    FileHandler.SaveData();
//
//                }
//
//            }.runTaskTimer(BlepFishing.getPlugin(), 0, 1200);

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

    public static void GiveItem(Player player, ItemStack item, boolean drop){
        for(var badItem : player.getInventory().addItem(item).values()){
            player.getWorld().dropItem(player.getLocation(), badItem);
        }
    }

    public static void Announce(String message){
        for(var s : Bukkit.getOnlinePlayers()){
            s.sendMessage(message);
        }
    }
}
