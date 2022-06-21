package com.kunfury.blepFishing.Objects;

import com.kunfury.blepFishing.AllBlue.AllBlueVars;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.io.Serializable;

public class AllBlueObject implements Serializable {
    LocationObject location;
    int fishRemaining;


    public AllBlueObject(Location _location){
        location = new LocationObject(_location);

        fishRemaining = AllBlueVars.AllBlueFishCount;
    }

    public Location getLocation(){
        return location.GetLocation();
    }

    public void RemoveFish(int amt, Player p){
        if(AllBlueVars.Permanent) return;
        fishRemaining -= amt;
        if(fishRemaining <= 0 && AllBlueVars.AllBlueList.contains(this)){
            AllBlueVars.AllBlueList.remove(this);
            Location lodeLoc = getLocation();
            lodeLoc.setY(0);
            lodeLoc.getBlock().setType(Material.STONE);
            p.sendMessage(ChatColor.GRAY + "You sense that the abundance of fish in this area has been reduced to the normal amount...");

        }
    }
}
