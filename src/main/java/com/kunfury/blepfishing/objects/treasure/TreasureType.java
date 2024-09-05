package com.kunfury.blepfishing.objects.treasure;

import com.kunfury.blepfishing.items.ItemHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

///
//The base class for all Treasures that can be caught while fishing
///
public abstract class TreasureType {
    public String Id;
    public int Weight;

    public boolean ConfirmedDelete;
    public boolean Announce;

    public TreasureType(String id, int weight, boolean announce){
        //Bukkit.getLogger().warning(id + " - " + weight);
        Id = id;
        Weight = weight;
        Announce = announce;
    }

    public abstract ItemStack GetItem();

    public abstract ItemStack GetItem(PlayerFishEvent e);

    public abstract boolean CanGenerate(Player player);

    public static boolean IsTreasure(ItemStack item){
        return ItemHandler.hasTag(item, ItemHandler.TreasureTypeId);
    }

    public static final HashMap<String, TreasureType> ActiveTypes = new HashMap<>();
    public static void AddNew(TreasureType treasureType) {
        if(ActiveTypes.containsKey(treasureType.Id)){
            Bukkit.getLogger().warning("Attempted to create duplicate Treasure Type with ID: " + treasureType.Id);
            return;
        }

        ActiveTypes.put(treasureType.Id, treasureType);
    }


    public static TreasureType FromId(String typeId){
        if(ActiveTypes.containsKey(typeId)){
            return ActiveTypes.get(typeId);
        }

        Bukkit.getLogger().warning("Tried to get invalid Treasure with ID: " + typeId);
        return null;
    }

    public static boolean IdExists(String id){
        return ActiveTypes.containsKey(id);
    }

    public static void UpdateId(String oldId, TreasureType type){
        ActiveTypes.remove(oldId);
        ActiveTypes.put(type.Id, type);
    }

    public static void Delete(TreasureType type){
        ActiveTypes.remove(type.Id);
    }

    public static void Clear(){
        ActiveTypes.clear();
    }
}
