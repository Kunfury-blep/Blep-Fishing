package com.kunfury.blepFishing.Plugins;


import com.gmail.nossr50.events.skills.fishing.McMMOPlayerFishingTreasureEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class McMMOListener implements Listener {

    public static List<Player> canFishList = new ArrayList<>();

//    @EventHandler
//    private void onMcMMOFish(FakePlayerFishEvent e){
//        //Bukkit.broadcastMessage("Running mcmmo fish event!");
//    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onMcMMOTreasure(McMMOPlayerFishingTreasureEvent e){
        if(!canFishList.contains(e.getPlayer())) canFishList.add(e.getPlayer());
    }



}
