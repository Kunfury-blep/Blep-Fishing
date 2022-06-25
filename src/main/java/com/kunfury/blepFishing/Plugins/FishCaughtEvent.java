package com.kunfury.blepFishing.Plugins;

import com.kunfury.blepFishing.Objects.FishObject;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class FishCaughtEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private FishObject fish;
    private Player player;

    public FishCaughtEvent(FishObject _fish, Player _p){
        fish = _fish;
        player = _p;
    }

    public FishObject GetCaughtFish(){
        return fish;
    }

    public Player GetWhoCaught(){
        return player;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
