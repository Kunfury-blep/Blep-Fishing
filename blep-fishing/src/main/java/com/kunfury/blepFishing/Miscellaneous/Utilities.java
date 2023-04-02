package com.kunfury.blepFishing.Miscellaneous;

import com.kunfury.blepFishing.Config.FileHandler;
import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Quests.QuestHandler;
import com.kunfury.blepFishing.BlepFishing;
import com.kunfury.blepFishing.Tournament.TournamentHandler;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalDateTime;

public class Utilities {

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

            new BukkitRunnable() {
                @Override
                public void run() {
                    LocalDateTime dt = LocalDateTime.now();
                    int hour = dt.getHour();
                    int minute = dt.getMinute();
                    int seconds = dt.getSecond();

//                    Bukkit.broadcastMessage("Time: " + hour + ":" + minute + ":" + seconds);
//                    Bukkit.broadcastMessage("Original: " + Variables.DayReset);
//                    Bukkit.broadcastMessage("Variable Test: " + BlepFishing.configBase.getShowScoreboard());

                    if((hour + ":" + minute).equals(BlepFishing.configBase.getDayReset())){
                        new QuestHandler().NewDay();
                    }

                    FileHandler.SaveData();

                }

            }.runTaskTimer(BlepFishing.getPlugin(), 0, 1200);


            new BukkitRunnable() { //Each Second
                final TournamentHandler tHandler = new TournamentHandler();
                final QuestHandler qHandler = new QuestHandler();
                @Override
                public void run() {
                    tHandler.TournamentTimer();
                    qHandler.QuestTimer();
                }

            }.runTaskTimer(BlepFishing.getPlugin(), 0, 60);

        }



    }

}
