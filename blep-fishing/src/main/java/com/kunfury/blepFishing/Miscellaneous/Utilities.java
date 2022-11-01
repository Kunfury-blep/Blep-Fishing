package com.kunfury.blepFishing.Miscellaneous;

import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Quests.QuestHandler;
import com.kunfury.blepFishing.Setup;
import com.kunfury.blepFishing.Tournament.TournamentHandler;
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
                    int hour = dt.getHour();     // gets the current month
                    int minute = dt.getMinute(); // gets hour of day

                    if((hour + ":" + minute).equals(Variables.DayReset)){
                        new QuestHandler().NewDay();
                    }
                }

            }.runTaskTimer(Setup.getPlugin(), 0, 1200);


            new BukkitRunnable() { //Each Second
                final TournamentHandler tHandler = new TournamentHandler();
                final QuestHandler qHandler = new QuestHandler();
                @Override
                public void run() {
                    tHandler.TournamentTimer();
                    qHandler.QuestTimer();
                }

            }.runTaskTimer(Setup.getPlugin(), 0, 60);

        }



    }

}
