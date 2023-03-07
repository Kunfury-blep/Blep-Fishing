package com.kunfury.blepFishing.Quests;

import com.kunfury.blepFishing.Config.CacheHandler;
import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Objects.FishObject;
import com.kunfury.blepFishing.Setup;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestHandler {
    private static List<QuestObject> QuestList;
    public static List<QuestObject> ActiveQuests;
    public static boolean isActive;
    public static boolean announceQuests;

    public static int MaxQuests = 3;



    public void Start(QuestObject quest){
        if(ActiveQuests.contains(quest)){
            return;
        }

        quest.Start();
        ActiveQuests.add(quest);
        SaveActive();
    }

    public void NewDay(){

//        ActiveQuests.removeIf(QuestObject::isComplete);

        for(var q : new ArrayList<>(ActiveQuests)){
            if(q.isCompleted()){
                ActiveQuests.remove(q);
            }
        }

        Collections.shuffle(QuestList); //Shuffles the list to ensure diversity in quests

        for(var q : QuestList){
            if(ActiveQuests.size() >= MaxQuests)
                break;

            if(q.canStart()){
                Start(q);
            }
        }

    }

    public void FinishQuest(QuestObject q){
        q.Finish();
    }

    public void CancelQuest(QuestObject q){
        ActiveQuests.remove(q);
        new CacheHandler().SaveCache();
        SaveActive();
    }

    public void CancelQuest(String questName){
        for(var q : ActiveQuests){
            if(q.getName().equals(Formatting.formatColor(questName))){
                CancelQuest(q);
                break;
            }else{
                Bukkit.broadcastMessage("No Match: " + questName + " - " + Formatting.formatColor(q.getName()));
            }
        }
    }

    public static int getActiveCount(){
        int size = 0;

        for(var q : ActiveQuests){
            if (!q.isCompleted()) {
                size++;
            }
        }

        return size;
    }

    public void QuestTimer(){
        for(var q: ActiveQuests){
            if(!q.isCompleted() && q.canFinish())
                FinishQuest(q);
        }
    }

    public void UpdateFishQuest(Player p, FishObject f){
        for(var q : ActiveQuests){
            if(!q.isValid(f))  continue;

            q.AddFish(f, p);
            SaveActive();
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

    private void SaveActive(){
        try {
            String questPath = Setup.dataFolder + "/Data" + "/quests.data";
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(questPath));
            output.writeObject(ActiveQuests);
            output.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void CheckDaySchedule(){
        if(Variables.RecordedDay == null || Variables.RecordedDay.getDayOfYear() < LocalDateTime.now().getDayOfYear()){
            NewDay();
        }
    }
}
