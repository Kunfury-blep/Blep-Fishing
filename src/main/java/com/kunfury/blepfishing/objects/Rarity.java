package com.kunfury.blepfishing.objects;

import org.bukkit.Bukkit;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class Rarity {
    public String Id;
    public String Name;
    public String Prefix;
    public int Weight;
    //public final double PriceMod;

    public boolean ConfirmedDelete;
    public boolean Announce;

    public Rarity(String id, String name, String prefix, int weight, boolean announce){
        Id = id;
        Name = name;
        Prefix = prefix;
        Weight = weight;
        //PriceMod = priceMod;
        Announce = announce;
    }

    public static Rarity GetRandom(){
        //Rarity Selection
        int randR = ThreadLocalRandom.current().nextInt(0, RarityTotalWeight);

        for(var r : Rarities.values()){
            if(randR <= r.Weight) {
                return r;
            }else
                randR -= r.Weight;
        }

        Bukkit.getLogger().severe("Unable to generate rarity.");
        return null;
    }


    //TODO: Is this being sorted anywhere?
    private static final HashMap<String, Rarity> Rarities = new HashMap<>();
    private static int RarityTotalWeight = 0;
    public static void AddNew(Rarity rarity){
        if(Rarities.containsKey(rarity.Id)){
            Bukkit.getLogger().warning("Attempted to create duplicate Rarity with ID: " + rarity.Id);
            return;
        }

        Rarities.put(rarity.Id, rarity);
        RarityTotalWeight += rarity.Weight;
    }

    public static Collection<Rarity> GetAll(){
        return Rarities.values();
    }

    public static Rarity FromId(String rarityId){
        if(Rarities.containsKey(rarityId)){
            return Rarities.get(rarityId);
        }

        Bukkit.getLogger().warning("Tried to get invalid Rarity with ID: " + rarityId);
        return null;
    }

    public static boolean IdExists(String id){
        return Rarities.containsKey(id);
    }

    public static void Delete(Rarity rarity){
        Rarities.remove(rarity.Id);
    }

    public static void UpdateId(String oldId, Rarity rarity){
        Rarities.remove(oldId);
        Rarities.put(rarity.Id, rarity);
    }

}
