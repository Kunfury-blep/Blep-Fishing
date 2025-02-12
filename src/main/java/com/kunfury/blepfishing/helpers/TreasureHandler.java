package com.kunfury.blepfishing.helpers;

import com.kunfury.blepfishing.config.*;
import com.kunfury.blepfishing.objects.treasure.TreasureType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class TreasureHandler {
    public boolean Enabled = ConfigHandler.instance.treasureConfig.Enabled();

    public static TreasureHandler instance;
    public final static HashMap<Player, Inventory> OpenInventories = new HashMap<>();

    public TreasureHandler(){
        if(instance != null){
            Utilities.Severe("Treasure Handler Instance Already Exists");
            return;
        }
        instance = this;

        for(var treasure : TreasureType.ActiveTreasureTypes.values()){
            totalWeight += treasure.Weight;
        }
    }

    public boolean TreasureCaught(){
        if(!Enabled) return false;
        double randR = ThreadLocalRandom.current().nextDouble(0, 100);
        var treasureChance = ConfigHandler.instance.treasureConfig.getTreasureChance();

        return randR <= treasureChance;
    }

    public TreasureType GetTreasure(){
        int randR = ThreadLocalRandom.current().nextInt(0, GetTotalWeight());

        var treasures = new ArrayList<>(TreasureType.ActiveTreasureTypes.values());

        treasures.sort(Comparator.comparingInt((TreasureType t) -> t.Weight));

        for(var t : treasures){
            //Bukkit.broadcastMessage(t.Id + " - " + t.Weight);
            if(randR <= t.Weight) {
                return t;
            }else
                randR -= t.Weight;
        }

        Bukkit.getLogger().severe("Unable to generate treasure.");
        return null;
    }

    public void CloseTreasureInterface(Player player){
        if(!OpenInventories.get(player).isEmpty()){
            for(var item : OpenInventories.get(player).getContents()){
                if(item == null)
                    continue;
                Utilities.GiveItem(player, item, true);
            }
        }


        OpenInventories.remove(player);
    }

    private int totalWeight = 0;
    public int GetTotalWeight(){
        return totalWeight;
    }

}
