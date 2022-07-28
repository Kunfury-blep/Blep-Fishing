package com.kunfury.blepFishing.Events;

import com.kunfury.blepFishing.Objects.FishObject;
import com.kunfury.blepFishing.Tournament.TournamentObject;
import net.milkbowl.vault.chat.plugins.Chat_TotalPermissions;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class TournamentFinishEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private TournamentObject tournament;
    private boolean isCancelled;

    public TournamentFinishEvent(TournamentObject _tournament){
        tournament = _tournament;
    }

    public TournamentObject getTournament(){ return tournament; }

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
