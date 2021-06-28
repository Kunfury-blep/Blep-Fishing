package com.kunfury.blepFishing;

import com.kunfury.blepFishing.Miscellaneous.Variables;
import com.kunfury.blepFishing.Plugins.McMMOListener;
import com.kunfury.blepFishing.Plugins.PluginHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class FishListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onFishNormal(PlayerFishEvent e) {
        if(!Variables.HighPriority && e.getState() == PlayerFishEvent.State.CAUGHT_FISH) MoveToSwap(e);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFishHigh(PlayerFishEvent e) {
        if(Variables.HighPriority && e.getState() == PlayerFishEvent.State.CAUGHT_FISH) MoveToSwap(e);
    }

    private void MoveToSwap(PlayerFishEvent e){
        //Delays the check by 1 tick to ensure the MCMMO event has run.
        Bukkit.getScheduler ().runTaskLater (Setup.getPlugin(), () ->{
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
