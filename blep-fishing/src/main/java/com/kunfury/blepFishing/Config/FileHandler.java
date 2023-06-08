package com.kunfury.blepFishing.Config;

import com.kunfury.blepFishing.BlepFishing;
import com.kunfury.blepFishing.Quests.QuestHandler;
import com.kunfury.blepFishing.Tournament.TournamentHandler;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class FileHandler {


    public static boolean FishData, QuestData, CollectionData, TournamentData;
    public static void SaveData(){
        BukkitRunnable runnable = new BukkitRunnable() {

            @Override
            public void run() {
                if(FishData) UpdateFishData();
                if(QuestData) UpdateQuestData();
                if(CollectionData) UpdateCollectionData();
                if(TournamentData) UpdateTournamentData();
            }
        };

        runnable.runTaskAsynchronously(BlepFishing.getPlugin());

    }

    public static void SaveAll(){
        UpdateFishData();
        UpdateQuestData();
        UpdateCollectionData();
        UpdateTournamentData();
    }

    private static void UpdateFishData(){
        try {
            String dictPath = BlepFishing.dataFolder + "/Data" + "/fish.data";
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(dictPath));

            output.writeObject(Variables.FishDict.clone());
            output.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        FishData = false;
    }

    private static void UpdateQuestData(){
        try {
            String questPath = BlepFishing.dataFolder + "/Data" + "/quests.data";
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(questPath));
            output.writeObject(QuestHandler.getActiveQuests());
            output.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        QuestData = false;
    }

    private static void UpdateCollectionData(){
        try {
            String logPath = BlepFishing.dataFolder + "/Data" + "/collections.data";
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(logPath));

            output.writeObject(Variables.CollectionLogs);
            output.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        CollectionData = false;
    }

    private static void UpdateTournamentData(){
        try {
            String tourneyPath = BlepFishing.dataFolder + "/Data" + "/tournaments.data";
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(tourneyPath));
            output.writeObject(TournamentHandler.ActiveTournaments);
            output.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        TournamentData = false;
    }

}
