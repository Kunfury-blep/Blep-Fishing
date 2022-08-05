package com.kunfury.blepFishing.Objects;

import com.kunfury.blepFishing.Endgame.EndgameVars;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.io.Serial;
import java.io.Serializable;

public class AllBlueObject implements Serializable {
    @Serial
    private static final long serialVersionUID = -279553730631021733L;
    LocationObject location;
    int fishRemaining;


    public AllBlueObject(Location _location){
        location = new LocationObject(_location);

        fishRemaining = EndgameVars.AvailableFish;
    }

    public Location getLocation(){
        return location.GetLocation();
    }

    public void RemoveFish(int amt, Player p){
        if(EndgameVars.Permanent) return;
        fishRemaining -= amt;
        if(fishRemaining <= 0 && EndgameVars.AllBlueList.contains(this)){
            EndgameVars.AllBlueList.remove(this);
            Location lodeLoc = getLocation();
            lodeLoc.setY(0);
            lodeLoc.getBlock().setType(Material.STONE);
            p.sendMessage(ChatColor.GRAY + "You sense that the abundance of fish in this area has been reduced to the normal amount...");

        }
    }
}