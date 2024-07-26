package com.kunfury.blepfishing.helpers;

import com.kunfury.blepfishing.config.*;
import com.kunfury.blepfishing.objects.TreasureType;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

public class TreasureHandler {
    public boolean Enabled = ConfigHandler.instance.baseConfig.getEnableTournaments();

    public static TreasureHandler instance;

    public void Initialize(){
        instance = this;
    }

    public boolean TreasureCaught(){
        return Enabled;
//        if(!Enabled) return false;
//        int randR = ThreadLocalRandom.current().nextInt(0, 100);
//        return randR > 50;
    }

    public ItemStack GetTreasureItem(){
        return GetTreasure().GetItem();
    }

    private TreasureType GetTreasure(){
        //TODO: Loop through all stored treasure objects
        //TODO: Treasure Objects work like fishObjects, pull from config
        //TODO: All Blue Compass Pieces inherit from treasureObject

        for(var t : TreasureType.GetAll()){
            if(t.Rewards != null && !t.Rewards.isEmpty())
                return t;
        }
        return null;
    }
}
