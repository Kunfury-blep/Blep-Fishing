package com.kunfury.blepFishing.Config;

import com.kunfury.blepFishing.Quests.QuestHandler;
import com.kunfury.blepFishing.BlepFishing;
import com.kunfury.blepFishing.Tournament.TournamentHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

public class CacheHandler {
    public void SaveCache(){

        JSONObject obj = new JSONObject();

        JSONObject tourneyObj = new JSONObject();
        for(var t : TournamentHandler.TournamentList){

            JSONObject tObj = new JSONObject();
            tObj.put("Last Ran", t.getLastRan().toString());
            tourneyObj.put(t.getName(), tObj);
        }

        JSONObject questObj = new JSONObject();
        for(var q : QuestHandler.getQuestList()){

            JSONObject qObj = new JSONObject();
            qObj.put("Last Ran", q.getLastRan().toString());
            questObj.put(q.getName(), qObj);
        }

        obj.put("Touranments", tourneyObj);
        obj.put("Quests", questObj);

        Variables.RecordedDay = LocalDateTime.now();
        obj.put("Current Day", Variables.RecordedDay.toString());

        FileWriter file = null;

        try {
            file = new FileWriter(BlepFishing.dataFolder + "/cache.json");
            file.write(obj.toJSONString());
            file.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void LoadGeneralCache(){
        Variables.RecordedDay = LocalDateTime.now();
    }


    public JSONObject getTourneyCache(){
        JSONObject tourneyJson = new JSONObject();

        try{
            JSONParser parser = new JSONParser();
            String content = Files.readString(Paths.get(BlepFishing.blepFishing.getDataFolder() + "/cache.json"));
            JSONObject json = (JSONObject) parser.parse(content);

            tourneyJson = (JSONObject) json.get("Touranments");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return tourneyJson;
    }

    public JSONObject getQuestCache(){
        JSONObject tourneyJson = new JSONObject();

        try{
            JSONParser parser = new JSONParser();
            String content = Files.readString(Paths.get(BlepFishing.blepFishing.getDataFolder() + "/cache.json"));
            JSONObject json = (JSONObject) parser.parse(content);

            tourneyJson = (JSONObject) json.get("Quests");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return tourneyJson;
    }
}
