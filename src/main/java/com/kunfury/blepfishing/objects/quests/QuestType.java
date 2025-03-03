package com.kunfury.blepfishing.objects.quests;

import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.helpers.Utilities;
import com.kunfury.blepfishing.objects.FishType;
import com.kunfury.blepfishing.objects.TournamentType;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class QuestType {
    public String Id;
    public String Name;
    public double Duration;
    public List<String> FishTypeIds;
    public HashMap<TournamentType.TournamentDay, List<String>> StartTimes = new HashMap<>();
    public double CashReward;
    public List<ItemStack> ItemRewards;


    private List<FishType> fishTypes;
    public List<FishType> getFishTypes(){
        if(fishTypes == null || fishTypes.isEmpty()){
            fishTypes = new ArrayList<>();
            for(var typeId : FishTypeIds){
                var fishType = FishType.FromId(typeId);
                if(fishType == null){
                    Utilities.Severe("Invalid Fish Type Found By Id");
                    continue;
                }
                fishTypes.add(fishType);
            }
        }
        return fishTypes;
    }

    private List<String> formattedCatchList;
    public List<String> getFormattedCatchList(){
        if(formattedCatchList == null || formattedCatchList.isEmpty()){
            if(new HashSet<>(getFishTypes()).containsAll(FishType.GetAll())){
                formattedCatchList = Collections.singletonList(
                        Formatting.GetLanguageString("Tournament.allFish"));
                return formattedCatchList;
            }

            var fishTypes = getFishTypes();

            if(fishTypes.isEmpty()){
                formattedCatchList = Collections.singletonList(
                        Formatting.GetLanguageString("Tournament.noFish"));
            }else{
                formattedCatchList = new ArrayList<>();
                for(var fishType : fishTypes){
                    formattedCatchList.add(fishType.Name);
                }
                formattedCatchList = Formatting.ToCommaLoreList(formattedCatchList, ChatColor.WHITE, ChatColor.BLUE);
            }
        }
        return formattedCatchList;
    }


    ///
    //STATIC METHODS
    ///
    private static final HashMap<String, QuestType> QuestTypes = new HashMap<>();
    public static void AddNew(QuestType questType){
        if(QuestTypes.containsKey(questType.Id)){
            Utilities.Severe("Attempted to create duplicate Quest Type with ID: " + questType.Id);
            return;
        }
        QuestTypes.put(questType.Id, questType);}

    public static void Delete(QuestType questType){
        QuestTypes.remove(questType.Id);
    }

    public static void Clear(){
        QuestTypes.clear();
    }

    public static Collection<QuestType> GetQuests(){
        return QuestTypes.values();
    }


    public static QuestType FromId(String questTypeId){
        if(QuestTypes.containsKey(questTypeId)){
            return QuestTypes.get(questTypeId);
        }
        Utilities.Severe("Tried to get invalid Quest Type with ID: " + questTypeId);
        return null;
    }

    public static boolean IdExists(String id){
        return QuestTypes.containsKey(id);
    }
}
