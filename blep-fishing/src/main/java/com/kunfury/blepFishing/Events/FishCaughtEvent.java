package com.kunfury.blepFishing.Events;

import com.kunfury.blepFishing.Objects.FishObject;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class FishCaughtEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private FishObject fish;
    private Player player;
    private Item caughtItem;
    private boolean isCancelled;

    public FishCaughtEvent(Item _caughtItem, FishObject _fish, Player _p){
        fish = _fish;
        player = _p;
        caughtItem = _caughtItem;
    }

    public FishObject getCaughtFish(){
        return fish;
    }

    public Item getCaughtItem(){return caughtItem;}

    public Player getWhoCaught(){
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean _isCancelled) {
        isCancelled = _isCancelled;
    }
}
