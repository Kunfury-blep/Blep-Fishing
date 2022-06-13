package com.kunfury.blepFishing.Crafting.Equipment.FishBag;

import com.kunfury.blepFishing.Miscellaneous.Variables;
import com.kunfury.blepFishing.Objects.FishObject;
import org.bukkit.ChatColor;

import java.util.*;
import java.util.stream.Collectors;

public class ParseFish {

    public List<FishObject> RetrieveFish(String bagId, String fishName){
        fishName = ChatColor.stripColor(fishName);
        if(Variables.GetFishList(fishName) != null){
            return Variables.GetFishList(fishName).stream()
                    .filter(f -> f.BagID != null && f.BagID .equals(bagId))
                    .sorted(Comparator.comparingDouble(FishObject::GetScore))
                    .collect(Collectors.toList());
        }else return new ArrayList<>();

    }

    public FishObject FishFromId(String fishID){
        List<FishObject> fishList = Variables.GetFishList("ALL").stream()
                .filter(f -> fishID.equals(f.FishID))
                .collect(Collectors.toList());


        if(fishList.size() <= 0) return null;
        return fishList.get(0);
    }

}
