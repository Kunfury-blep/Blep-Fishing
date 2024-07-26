package com.kunfury.blepfishing.helpers;

import com.kunfury.blepfishing.config.*;
import com.kunfury.blepfishing.objects.TreasureType;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;
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
        //Rarity Selection
        int randR = ThreadLocalRandom.current().nextInt(0, TreasureType.GetTotalWeight());

        List<TreasureType> types = new java.util.ArrayList<>(TreasureType.GetAll().stream().toList());
        types.sort((TreasureType t1, TreasureType t2) -> t1.Weight - t2.Weight);

        for(var t : types){
            if(randR <= t.Weight) {
                return t;
            }else
                randR -= t.Weight;
        }

        Bukkit.getLogger().severe("Unable to generate treasure.");
        return null;
    }
}
