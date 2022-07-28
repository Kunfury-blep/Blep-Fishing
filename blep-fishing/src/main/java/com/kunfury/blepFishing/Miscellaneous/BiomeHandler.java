package com.kunfury.blepFishing.Miscellaneous;


import org.bukkit.Location;

public class BiomeHandler {

    public String getBiomeKey(Location loc) {
        return loc.getBlock().getBiome().name();
    }


}
