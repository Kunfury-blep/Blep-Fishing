package com.kunfury.blepFishing.Endgame;

import com.kunfury.blepFishing.Objects.AllBlueObject;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

import static com.kunfury.blepFishing.Endgame.EndgameVars.AllBlueList;

public class AllBlueInfo {

    public static boolean InAllBlue(Location loc){

        if(!new BiomeLocator().ConfirmArea(loc, 0, 0, 0)) return false; //Quick check to ensure that the player is even in an appropriate biome before checking

        loc.setY(64);

        return GetAllBlue(loc) != null;
    }

    public static boolean IsCompass(ItemStack compass){
        return NBTEditor.contains(compass, "blep", "item", "allBlueCompassComplete");
    }

    public static boolean IsCompassComplete(ItemStack compass){
        return NBTEditor.getBoolean(compass, "blep", "item", "allBlueCompassComplete");
    }

    public static AllBlueObject GetAllBlue(Location loc){
        for(int i = 0; i < AllBlueList.size(); i++){
            AllBlueObject obj = AllBlueList.get(i);

//            if(Objects.equals(obj.getLocation().getWorld(), loc.getWorld())){
//                Bukkit.broadcastMessage("Matching Worlds - " + loc.getWorld());
//                Bukkit.broadcastMessage("OBJ - " + obj.getLocation().getWorld());
//            }

            if(Objects.equals(obj.getLocation().getWorld(), loc.getWorld()) && obj.getLocation().distance(loc) < 32){
                return AllBlueList.get(i);
            }
        }
        return null;
    }
}
//
