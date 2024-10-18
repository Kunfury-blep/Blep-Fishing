package com.kunfury.blepfishing.objects;
import com.kunfury.blepfishing.config.ConfigHandler;
import org.bukkit.Bukkit;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class FishType {
    public String Id;
    public String Name;
    public String Lore;
    public final String Description;
    public double LengthMin;
    public double LengthMax;
    public int ModelData;
    public double PriceBase;
    public final List<String> AreaIds;
    public boolean RequireRain;
    public int HeightMin;
    public int HeightMax;

    public boolean ConfirmedDelete;

    public FishType(String id, String name, String lore, String desc, double lengthMin, double lengthMax, int modelData, double priceBase,
                    List<String> areaIds, boolean requireRain, int heightMin, int heightMax){

        Id = id;
        Name = name;
        Lore = lore;
        Description = desc;
        LengthMin = lengthMin;
        LengthMax = lengthMax;
        ModelData = modelData;
        PriceBase = priceBase;
        AreaIds = areaIds;
        RequireRain = requireRain;

        if(heightMin == 0 && heightMax == 0){ //Ensures fish can be caught at any height if values left at 0
            HeightMin = -1000;
            HeightMax = 1000;
        }else{
            HeightMin = heightMin;
            HeightMax = heightMax;
        }


    }

    @Override
    public String toString() {
        return super.toString();
    }

    public FishObject GenerateFish(Rarity rarity, UUID playerId, Integer rodId, Integer bagId, boolean allBlue){
        double adjustedMax = LengthMax;
        if(allBlue)
            adjustedMax += LengthMax * ConfigHandler.instance.baseConfig.getAllBlueSizeBonus();

        double length = ThreadLocalRandom.current().nextDouble(LengthMin, adjustedMax);
        length = (double) Math.round(length * 100) / 100;
        return new FishObject(rarity.Id, Id, length, playerId, rodId, bagId);
    }

    public boolean canCatch(boolean isRaining, int height, boolean isNight, Collection<FishingArea> fishingAreas){
        if(!(!RequireRain || isRaining)){
            //Bukkit.broadcastMessage("Rain Check Failed for " + Name);
            return false;
        }
        if(!(HeightMin <= height && HeightMax >= height)){
            //Bukkit.broadcastMessage("Height Check Failed for " + Name);
            return false;
        }
        boolean areaCheck = false;
        for (var area : fishingAreas) {
            if(AreaIds.contains(area.Id)){
                areaCheck = true;
                break;
            }
        }

//        if(!areaCheck){
//            Bukkit.broadcastMessage("Area Check Failed for " + Name);
//        }

        return areaCheck;
    }

    public void ToggleArea(String areaId){
        if(AreaIds.contains(areaId))
            AreaIds.remove(areaId);
        else
            AreaIds.add(areaId);
    }

    public double getAverageLength(){
        return (LengthMin + LengthMax)/2;
    }


    private static final HashMap<String, FishType> ActiveTypes = new HashMap<>();
    public static void AddFishType(FishType fishType){
        if(ActiveTypes.containsKey(fishType.Id)){
            Bukkit.getLogger().warning("Attempted to create duplicate Fish Type with ID: " + fishType.Id);
            return;
        }

        ActiveTypes.put(fishType.Id, fishType);
    }

    public static void Delete(FishType fishType){
        ActiveTypes.remove(fishType.Id);
    }

    public static void UpdateId(String oldId, FishType fishType){
        ActiveTypes.remove(oldId);
        ActiveTypes.put(fishType.Id, fishType);
    }

    public static boolean IdExists(String id){
        return ActiveTypes.containsKey(id);
    }

    public static Collection<FishType> GetAll(){
        return ActiveTypes.values();
    }
    public static FishType FromId(String typeId){
        if(ActiveTypes.containsKey(typeId)){
            return ActiveTypes.get(typeId);
        }

        Bukkit.getLogger().warning("Tried to get invalid fish type with ID: " + typeId);
        return null;
    }

    public String getId() {
        return Id;
    }
}
