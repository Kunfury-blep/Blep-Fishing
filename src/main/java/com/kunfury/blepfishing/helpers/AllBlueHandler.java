package com.kunfury.blepfishing.helpers;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.objects.FishingArea;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.persistence.PersistentDataType;

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

    public boolean FinalizeCompass(ItemStack clickedItem, Player player){
        //Bukkit.broadcastMessage("Successfully crafted a compass to the all blue!");

        if(clickedItem == null || clickedItem.getType() != Material.COMPASS){
            Utilities.Severe("Tried to finalize compass on invalid item.");
            return false;
        }

        Location allBlueLoc = FindViableOcean(player.getWorld());

        if(allBlueLoc == null){
            Utilities.Severe("Error Creating All Blue in " + player.getWorld());
            Utilities.SendPlayerMessage(player, Formatting.GetLanguageString("Treasure.Compass.error"));
            return false;
        }

        Utilities.AnnounceNether(Formatting.GetLanguageString("Treasure.Compass.announceNether"));
        Utilities.AnnounceEnd(Formatting.GetLanguageString("Treasure.Compass.announceEnd"));

        World allBlueWorld = allBlueLoc.getWorld();


        for(var p : Bukkit.getOnlinePlayers().stream()
                .filter(p ->p.getWorld() == allBlueWorld).toList()){

            Location pLoc = p.getLocation();

            int finalX = pLoc.getBlockX() - allBlueLoc.getBlockX();
            int finalZ = pLoc.getBlockZ() - allBlueLoc.getBlockZ();

            String direction = Formatting.GetLanguageString("Treasure.Compass.name"); //Just in case the calc comes out to 0 for one of them

            if(finalX > 0 && finalZ >= 0) direction = Formatting.GetLanguageString("System.west");
            if(finalX < 0 && finalZ <= 0) direction = Formatting.GetLanguageString("System.east");
            if(finalX <= 0 && finalZ > 0) direction = Formatting.GetLanguageString("System.north");
            if(finalX >= 0 && finalZ < 0) direction = Formatting.GetLanguageString("System.south");


            String message = Formatting.GetLanguageString("Treasure.Compass.announce")
                    .replace("{direction}", direction);

            p.sendMessage(message);
        }



        allBlueLoc.setY(0);
        allBlueLoc.getBlock().setType(Material.LODESTONE);

        CompassMeta compassMeta = (CompassMeta) clickedItem.getItemMeta();
        assert compassMeta != null;

        compassMeta.setLodestone(allBlueLoc);

        int compassId = Database.AllBlues.Add(allBlueLoc);


        compassMeta.getPersistentDataContainer().set(ItemHandler.CompassKey, PersistentDataType.INTEGER, compassId);

        clickedItem.setItemMeta(compassMeta);
        return true;
    }

    public Location FindViableOcean(World world){
        int attempts = 1000;
        int i = 0;

        //Loops through random locations in the world to find a suitable All Blue
        //Continues looping until one is found or 1,000 attempts are made
        while(i < attempts){
            Location tempLoc = getRandomLoc(world);


            if(ConfirmArea(tempLoc, 0, 0)){
                int d = ConfigHandler.instance.baseConfig.getAllBlueRadius();
                if(ConfirmArea(tempLoc, d, 0) && ConfirmArea(tempLoc, -d, 0)
                        && ConfirmArea(tempLoc, 0,  d) && ConfirmArea(tempLoc, 0, -d)
                        &&  Database.AllBlues.VerifyPosition(tempLoc)){
                    tempLoc.setY(64);
                    //Bukkit.broadcastMessage("Found Location: " + tempLoc.toVector());
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

        List<FishingArea> areaList = FishingArea.GetAvailableAreas(loc);

        return areaList.contains(ConfigHandler.instance.baseConfig.getAllBlueArea());
    }

}
