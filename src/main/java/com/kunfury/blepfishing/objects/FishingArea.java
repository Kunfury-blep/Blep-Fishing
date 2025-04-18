package com.kunfury.blepfishing.objects;

import com.kunfury.blepfishing.plugins.PluginHandler;
import com.kunfury.blepfishing.plugins.WorldGuardHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class FishingArea {
    public String Id;
    public String Name;
    public List<String> Biomes;

    public boolean HasCompassPiece;
    public String CompassHint;

    public boolean ConfirmedDelete;

    public FishingArea(String id, String name, List<String> biomes, boolean hasCompassPiece, String compassHint){
        Id = id;
        Name = name;
        HasCompassPiece = hasCompassPiece;
        if(compassHint == null)
            compassHint = "";
        CompassHint = compassHint;

        Biomes = new ArrayList<>();

        biomes.forEach(b -> {
            Biomes.add(b.toUpperCase());
        });
    }

    private static final HashMap<String, FishingArea> ActiveAreas = new HashMap<>();
    public static void AddNew(FishingArea fishingArea){
        if(ActiveAreas.containsKey(fishingArea.Id)){
            Bukkit.getLogger().warning("Attempted to create duplicate Fishing Area with ID: " + fishingArea.Id);
            return;
        }

        ActiveAreas.put(fishingArea.Id, fishingArea);
    }

    public static FishingArea FromId(String areaId){
        if(ActiveAreas.containsKey(areaId)){
            return ActiveAreas.get(areaId);
        }

        Bukkit.getLogger().warning("Tried to get invalid fish area with ID: " + areaId);
        return null;
    }

    public static Collection<FishingArea> GetAll(){
        return ActiveAreas.values();
    }

    public static List<FishingArea> GetAvailableAreas(Location loc){
        List<FishingArea> fishingAreas = new ArrayList<>();

        if(PluginHandler.HasWorldGuard()){
            fishingAreas = WorldGuardHandler.GetAreas(loc);

            if(!fishingAreas.isEmpty()){
                return fishingAreas;
            }
        }

        String biome = loc.getBlock().getBiome().toString();

        for(var entrySet : ActiveAreas.entrySet()){
            var value = entrySet.getValue();
            if(value.Biomes.contains(biome)){
                fishingAreas.add(value);
            }
        }

        return fishingAreas;
    }

    public static List<FishingArea> GetCompassAreas(){
        return ActiveAreas.values().stream().filter(a -> a.HasCompassPiece).toList();
    }

    public static boolean IdExists(String id){
        return ActiveAreas.containsKey(id);
    }

    public static void Delete(FishingArea area){
        ActiveAreas.remove(area.Id);
    }

    public static void UpdateId(String oldId, FishingArea area){
        ActiveAreas.remove(oldId);
        ActiveAreas.put(area.Id, area);
    }

    public static void Clear() {
        ActiveAreas.clear();
    }

    public static FishingArea GetRandom(){
        return ActiveAreas.values().stream().toList().get(ThreadLocalRandom.current().nextInt(ActiveAreas.size()));
    }
}
