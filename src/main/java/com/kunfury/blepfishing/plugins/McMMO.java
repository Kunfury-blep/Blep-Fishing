package com.kunfury.blepfishing.plugins;

import com.gmail.nossr50.events.skills.fishing.McMMOPlayerFishingTreasureEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class McMMO implements Listener {

    public static List<Player> canFishList = new ArrayList<>();
    @EventHandler(priority = EventPriority.HIGHEST)
    private void onMcMMOTreasure(McMMOPlayerFishingTreasureEvent e){
        if(!canFishList.contains(e.getPlayer())) canFishList.add(e.getPlayer());
    }

    public static boolean McMcMmoCanFish(Player player) {
        if(!PluginHandler.hasMcMMO) return true;

        if(canFishList.contains(player)){
            canFishList.remove(player);
            return false;
        }else return true;
    }


}
