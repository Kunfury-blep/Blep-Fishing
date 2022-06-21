package com.kunfury.blepFishing.Objects;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.Serializable;

public class LocationObject implements Serializable {
    String worldName;
    double x, y, z;


    public LocationObject(Location location){
        x = location.getX();
        y = location.getY();
        z = location.getZ();

        worldName = location.getWorld().getName();
    }

    public Location GetLocation(){
        World world = Bukkit.getServer().getWorld(worldName);
        return new Location(world, x, y, z);
    }
}
