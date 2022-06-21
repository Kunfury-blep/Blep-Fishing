package com.kunfury.blepFishing.AllBlue;

import com.kunfury.blepFishing.Miscellaneous.Variables;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class BiomeLocator {



    public Location FindViableOcean(ItemStack compass){
        int attempt = 1000;
        int i = 0;
        World world = Bukkit.getServer().getWorlds().get(0);

        //Loops through random locations in the world to find a suitable All Blue
        //Continues looping until one is found or 1,000 attempts are made
        while(i < attempt){
            assert world != null;
            Location tempLoc = getRandomLoc(world);
            Biome biome = getBiome(tempLoc);
            if(biome.equals(Biome.DEEP_LUKEWARM_OCEAN)){
                int d = AllBlueVars.AllBlueRadius;
                if(ConfirmBiome(AllBlueVars.oceanBiomes, tempLoc, d, 0, 0) && ConfirmBiome(AllBlueVars.oceanBiomes, tempLoc, -d, 0, 0)
                && ConfirmBiome(AllBlueVars.oceanBiomes, tempLoc, 0, 0, d) && ConfirmBiome(AllBlueVars.oceanBiomes, tempLoc, 0, 0, -d)){
                    tempLoc.setY(64);
                    return tempLoc;
                }

            }
            i++;
        }
        return null;
    }

    private boolean ConfirmBiome(List<String> biomes, Location loc, int x, int y, int z){
        Location newLoc = new Location(loc.getWorld(), loc.getX() + x, loc.getY() + y, loc.getZ() + z);
        Biome biome = newLoc.getBlock().getBiome();

        return biomes.contains(biome.toString());
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
