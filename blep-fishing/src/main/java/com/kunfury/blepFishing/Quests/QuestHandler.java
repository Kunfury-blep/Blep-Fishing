package com.kunfury.blepFishing.Quests;

import com.kunfury.blepFishing.Config.CacheHandler;
import com.kunfury.blepFishing.Config.ConfigExtra;
import com.kunfury.blepFishing.Config.FileHandler;
import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Objects.FishObject;
import com.kunfury.blepFishing.BlepFishing;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestHandler {
    private static List<QuestObject> QuestList;

    public static List<QuestObject> getActiveQuests() {
        if(activeQuests == null)
            activeQuests = new ArrayList<>();
        return activeQuests;
    }

    public static void setActiveQuests(List<QuestObject> activeQuests) {
        QuestHandler.activeQuests = activeQuests;
    }

    private static List<QuestObject> activeQuests;

//    public static int MaxQuests = 3;


    public void Start(QuestObject quest){
        if(activeQuests.contains(quest)){
            return;
        }

        quest.Start();
        activeQuests.add(quest);
        FileHandler.QuestData = true;
    }

    public void NewDay(){

        Bukkit.broadcastMessage(Variables.getPrefix() + "A new day has begun, new quests are available!"); //TODO: Add to messages.yml file
//        ActiveQuests.removeIf(QuestObject::isComplete);

        for(var q : new ArrayList<>(activeQuests)){
            if(q.isCompleted()){
                activeQuests.remove(q);
            }
        }

        Collections.shuffle(QuestList); //Shuffles the list to ensure diversity in quests

        for(var q : QuestList){
            if(activeQuests.size() >= BlepFishing.configBase.getMaxQuests())
                break;

            if(q.canStart()){
                Start(q);
            }
        }

    }

    public void CancelQuest(QuestObject q){
        activeQuests.remove(q);
        new CacheHandler().SaveCache();
        FileHandler.QuestData = true;
    }

    public static int getActiveCount(){
        int size = 0;

        for(var q : activeQuests){
            if (!q.isCompleted()) {
                size++;
            }
        }

        return size;
    }

    public void QuestTimer(){
        for(var q: activeQuests){
            if(!q.isCompleted() && q.canFinish())
                q.Finish();
        }
    }

    public void UpdateFishQuest(Player p, FishObject f){
        for(var q : activeQuests){
            if(!q.isValid(f))  continue;

            q.AddFish(f, p);

            FileHandler.QuestData = true;
        }
    }

    public static List<QuestObject> getQuestList(){
        if(QuestList == null){
            QuestList = new ArrayList<>();
        }

        return QuestList;
    }

    public static void resetQuestList(){
        QuestList = new ArrayList<>();
    }

    public static void AddQuests(List<QuestObject> questList){
        if(questList != null){
            QuestList.addAll(questList);
        }
    }

    public static void AddQuest(QuestObject quest){
        if(quest != null){
            QuestList.add(quest);
        }
    }

    public void CheckDaySchedule(){
        if(Variables.RecordedDay == null || Variables.RecordedDay.getDayOfYear() < LocalDateTime.now().getDayOfYear()){
            NewDay();
        }
    }

    public static QuestObject FindQuest(String id){
        return QuestList.stream().filter(q -> q.getName().equals(id))
                .findFirst().orElse(null);
    }

    public static void EnableQuests(boolean enabled, Player player){
        FileConfiguration config = BlepFishing.configBase.config;
        config.set("Enable Quests", enabled);

        BlepFishing.blepFishing.saveConfig();

        if(enabled){
            new ConfigExtra().LoadQuests();
            player.sendMessage(Variables.getPrefix() + "Fishing Quests have been enabled.");
        }else{
            player.sendMessage(Variables.getPrefix() + "Fishing Quests have been disabled.");
        }

    }
}
