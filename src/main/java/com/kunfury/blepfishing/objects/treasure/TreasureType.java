package com.kunfury.blepfishing.objects.treasure;

import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.helpers.Utilities;
import com.kunfury.blepfishing.helpers.ItemHandler;
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
        Id = id;
        Weight = weight;
        Announce = announce;
    }

    public abstract ItemStack GetItem();

    public abstract ItemStack GetItem(PlayerFishEvent e);

    public abstract boolean CanGenerate(Player player);

    protected abstract void Use(ItemStack item, Player player);

    public abstract String getFormattedName();

    ///
    //Static Methods
    ///

    public static boolean IsTreasure(ItemStack item){
        return ItemHandler.hasTag(item, ItemHandler.TreasureTypeId);
    }

    public static final HashMap<String, TreasureType> ActiveTreasureTypes = new HashMap<>();
    public static void AddNew(TreasureType treasureType) {
        if(ActiveTreasureTypes.containsKey(treasureType.Id)){
            Utilities.Severe("Attempted to create duplicate Treasure Type with ID: " + treasureType.Id);
            return;
        }
        ActiveTreasureTypes.put(treasureType.Id, treasureType);
    }

    public static TreasureType FromId(String typeId){
        if(ActiveTreasureTypes.containsKey(typeId)){
            return ActiveTreasureTypes.get(typeId);
        }

        Utilities.Severe("Tried to get invalid Treasure with ID: " + typeId);
        return null;
    }

    public static boolean IdExists(String id){
        return ActiveTreasureTypes.containsKey(id);
    }

    public static void UpdateId(String oldId, TreasureType type){
        ActiveTreasureTypes.remove(oldId);
        ActiveTreasureTypes.put(type.Id, type);
    }

    public static void Delete(TreasureType type){
        ActiveTreasureTypes.remove(type.Id);
    }

    public static void Clear(){
        ActiveTreasureTypes.clear();
    }

    public static void UseItem(ItemStack item, Player player){
        var treasureId = ItemHandler.getTagString(item, ItemHandler.TreasureTypeId);
        TreasureType treasureType = TreasureType.FromId(treasureId);
        if(treasureType == null){
            Utilities.Severe("Could not find treasure type to open with id " + treasureId);
            return;
        }

        treasureType.Use(item, player);
    }
}
