package com.kunfury.blepFishing.Endgame;

import com.kunfury.blepFishing.Objects.AreaObject;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class BiomeLocator {



    public Location FindViableOcean(){
        int attempt = 1000;
        int i = 0;
        World world = Bukkit.getServer().getWorlds().get(0);
        assert world != null;
        
        //Loops through random locations in the world to find a suitable All Blue
        //Continues looping until one is found or 1,000 attempts are made
        while(i < attempt){
            Location tempLoc = getRandomLoc(world);


            if(ConfirmArea(tempLoc, 0, 0, 0)){
                int d = EndgameVars.AreaRadius;
                if(ConfirmArea(tempLoc, d, 0, 0) && ConfirmArea(tempLoc, -d, 0, 0)
                && ConfirmArea(tempLoc, 0, 0, d) && ConfirmArea(tempLoc, 0, 0, -d)){
                    tempLoc.setY(64);
                    return tempLoc;
                }

            }
            i++;
        }
        return null;
    }

    /**
     * @param origLoc -  Original location
     * @param x - Offset on the X-Axis
     * @param y - Offset on the Y-Axis
     * @param z - Offset on the Z-Axis
     * @return
     */
    public boolean ConfirmArea(Location origLoc, int x, int y, int z){
        Location loc = new Location(origLoc.getWorld(), origLoc.getX() + x, origLoc.getY() + y, origLoc.getZ() + z); //Gets new location offset from original

        List<AreaObject> areaList = AreaObject.GetAreas(loc);

        return areaList.contains(EndgameVars.EndgameArea);
    }

    public Biome getBiome(Location loc){

        return(loc.getBlock().getBiome());
    }

    //Generates a random location inside the world border
    public Location getRandomLoc(World world) {

        double worldSize = world.getWorldBorder().getSize();

        if(worldSize == 5.9999968E7) worldSize = 20000; //Keeps the All Blue in line within a reasonable distance


        final double randomX = ThreadLocalRandom.current().nextDouble(0, worldSize / 2);
        final double randomY = ThreadLocalRandom.current().nextDouble(0, worldSize / 2);
        return world.getWorldBorder().getCenter().add(randomX, 0, randomY);
    }
}
