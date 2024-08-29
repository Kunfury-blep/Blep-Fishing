package com.kunfury.blepfishing.helpers;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.objects.FishingArea;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class AllBlueHandler {

    public static AllBlueHandler Instance;

    public AllBlueHandler(){
        if(Instance != null){
            Utilities.Severe("All Blue Handler Instance Already Exists");
            return;
        }
        Instance = this;
    }

    public void FinalizeCompass(ItemStack clickedItem, Player player){
        Bukkit.broadcastMessage("Successfully crafted a compass to the all blue!");

        if(clickedItem == null || clickedItem.getType() != Material.COMPASS){
            Utilities.Severe("Tried to finalize compass on invalid item.");
            return;
        }

        Location allBlueLoc = FindViableOcean(player.getWorld());
        allBlueLoc.setY(0);
        allBlueLoc.getBlock().setType(Material.LODESTONE);

        CompassMeta compassMeta = (CompassMeta) clickedItem.getItemMeta();
        assert compassMeta != null;

        compassMeta.setLodestone(allBlueLoc);

        clickedItem.setItemMeta(compassMeta);
    }

    public Location FindViableOcean(World world){
        int attempt = 1000;
        int i = 0;

        //Loops through random locations in the world to find a suitable All Blue
        //Continues looping until one is found or 1,000 attempts are made
        while(i < attempt){
            Location tempLoc = getRandomLoc(world);


            if(ConfirmArea(tempLoc, 0, 0)){
                int d = ConfigHandler.instance.baseConfig.getAllBlueRadius();
                if(ConfirmArea(tempLoc, d, 0) && ConfirmArea(tempLoc, -d, 0)
                        && ConfirmArea(tempLoc, 0,  d) && ConfirmArea(tempLoc, 0, -d)){
                    tempLoc.setY(64);
                    Database.AllBlues.Add(tempLoc);
                    Bukkit.broadcastMessage("Found Location: " + tempLoc.toVector());
                    return tempLoc;
                }
            }
            i++;
        }
        return null;
    }

    private Location getRandomLoc(World world) {

        double worldSize = world.getWorldBorder().getSize();

        if(worldSize == 5.9999968E7) worldSize = 20000; //Keeps the All Blue in line within a reasonable distance


        final double randomX = ThreadLocalRandom.current().nextDouble(0, worldSize / 2);
        final double randomZ = ThreadLocalRandom.current().nextDouble(0, worldSize / 2);
        return world.getWorldBorder().getCenter().add(randomX, 0, randomZ);
    }

    private boolean ConfirmArea(Location origLoc, int x, int z){
        Location loc = new Location(origLoc.getWorld(), origLoc.getX() + x, 0, origLoc.getZ() + z); //Gets new location offset from original

        List<FishingArea> areaList = FishingArea.GetAvailableAreas(loc.getBlock().getBiome().toString());

        return areaList.contains(ConfigHandler.instance.baseConfig.getAllBlueArea());
    }

}
