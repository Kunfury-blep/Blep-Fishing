package com.kunfury.blepFishing.Events;

import com.kunfury.blepFishing.BlepFishing;
import com.kunfury.blepFishing.FishSwitch;
import com.kunfury.blepFishing.Plugins.McMMOListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class FishingListener implements Listener {
    @org.bukkit.event.EventHandler(priority = EventPriority.LOWEST)
    public void onFishNormal(PlayerFishEvent e) {
        if(!BlepFishing.configBase.getHighPriority() && e.getState() == PlayerFishEvent.State.CAUGHT_FISH) MoveToSwap(e);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFishHigh(PlayerFishEvent e) {
        if(BlepFishing.configBase.getHighPriority() && e.getState() == PlayerFishEvent.State.CAUGHT_FISH) MoveToSwap(e);
    }

    private void MoveToSwap(PlayerFishEvent e){
        //Delays the check by 1 tick to ensure the MCMMO event has run.
        Bukkit.getScheduler ().runTaskLater (BlepFishing.getPlugin(), () ->{
            if(McMcMmoCanFish(e.getPlayer())) new FishSwitch().FishHandler(e);
        } , 1);
    }

    private boolean McMcMmoCanFish(Player player) {
        if(McMMOListener.canFishList.contains(player)){
            McMMOListener.canFishList.remove(player);
            return false;
        }else return true;
    }
}
