package com.kunfury.blepFishing.CollectionLog;

import com.kunfury.blepFishing.Config.FileHandler;
import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Objects.CollectionLogObject;
import com.kunfury.blepFishing.Objects.FishObject;
import com.kunfury.blepFishing.Quests.QuestObject;
import com.kunfury.blepFishing.BlepFishing;
import org.bukkit.entity.Player;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class CollectionHandler {


    public CollectionLogObject GetLog(Player p){
        CollectionLogObject logObject = null;

        String UUID = p.getUniqueId().toString();

        if(Variables.CollectionLogs.stream().anyMatch(log -> UUID.equals(log.getUUID())))
            logObject = Variables.CollectionLogs.stream()
                        .filter(log -> log.getUUID().equals(UUID))
                        .collect(toSingleton());

        if(logObject != null) return logObject;
        logObject = new CollectionLogObject(p);
        Variables.CollectionLogs.add(logObject);

        FileHandler.CollectionData = true;
        return logObject;

    }

    public void CaughtFish(Player p, FishObject fish){
        CollectionLogObject log = GetLog(p);
        log.CaughtFish(fish);
    }


    public void CraftedBag(Player p, String bagType){
        CollectionLogObject log = GetLog(p);

        //Bukkit.broadcastMessage("Crafted Bag: " + bagType);
    }

    public void CompletedQuest(Player p, QuestObject q){
        CollectionLogObject log = GetLog(p);
        log.FinishQuest(q);

    }

    public static <T> Collector<T, ?, T> toSingleton() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if (list.size() != 1) {
                        throw new IllegalStateException();
                    }
                    return list.get(0);
                }
        );
    }


}
