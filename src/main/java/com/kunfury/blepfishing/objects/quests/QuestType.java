package com.kunfury.blepfishing.objects.quests;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.helpers.Utilities;
import com.kunfury.blepfishing.objects.FishType;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.objects.UnclaimedReward;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;

public class QuestType {
    public String Id;
    public String Name;
    public double Duration;
    public int CatchAmount;
    public List<String> FishTypeIds;
    public HashMap<TournamentType.TournamentDay, List<String>> StartTimes = new HashMap<>();
    public double CashReward;
    public List<ItemStack> ItemRewards;

    public boolean ConfirmedDelete;

    //For creating new Quests in the GUI
    public QuestType(String id){
        Id = id;
        Name = id;
        Duration = 1;
        CatchAmount = 1;
        FishTypeIds = new ArrayList<>();
        CashReward = 0;
        ItemRewards = new ArrayList<>();
    }

    public QuestType(String id, String name, double duration, int catchAmount, List<String> fishTypeIds, HashMap<TournamentType.TournamentDay, List<String>> startTimes,
                         double cashReward, List<ItemStack> itemRewards){
        Id = id;
        Name = name;
        Duration = duration;
        CatchAmount = catchAmount;
        FishTypeIds = fishTypeIds;
        StartTimes = startTimes;
        CashReward = cashReward;
        ItemRewards = itemRewards;
    }



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

    public void ResetCatchList(){
        formattedCatchList = null;
        fishTypes = null;
    }

    public void TryStart(DayOfWeek dayOfWeek, String time){
        TournamentType.TournamentDay tDay = TournamentType.TournamentDay.valueOf(dayOfWeek.toString());

        if((!StartTimes.containsKey( tDay) || !StartTimes.get(tDay).contains(time))
                && (!StartTimes.containsKey(TournamentType.TournamentDay.EVERYDAY) || !StartTimes.get(TournamentType.TournamentDay.EVERYDAY).contains(time)))
            return;

        Start();
    }

    public QuestObject Start(){
        if(Database.Quests.IsRunning(Id)){
            return null;
        }
        var questObject = new QuestObject(this);

        Utilities.Announce(Formatting.GetLanguageString("Quest.start")
                .replace("{quest}", Name));

        return questObject;
    }

    public void GiveRewards(UUID playerUUID){
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);

        Player player = offlinePlayer.getPlayer();

        if(BlepFishing.hasEconomy() && CashReward > 0){
            EconomyResponse r = BlepFishing.getEconomy().depositPlayer(offlinePlayer, CashReward);
            if(!r.transactionSuccess())
                Utilities.Severe(r.errorMessage);

            if(player != null){
                Bukkit.getScheduler().runTaskLater (BlepFishing.getPlugin(), () ->{
                    player.sendMessage(Formatting.GetFormattedMessage("Quest.rewardCurrency")
                            .replace("{amount}", Formatting.toBigNumber(CashReward))
                            .replace("{quest}", Name));
                } , 20);
            }

            if(!offlinePlayer.isOnline()){
                for(var i : ItemRewards){
                    new UnclaimedReward(playerUUID, i); //Saves reward if unable to claim
                }
                return;
            }
        }

        for(var i : ItemRewards){
            if(!Utilities.GiveItem(offlinePlayer.getPlayer(), i, false)){
                new UnclaimedReward(playerUUID, i); //Saves reward if unable to claim
                continue;
            }

            if(player != null){
                Bukkit.getScheduler().runTaskLater (BlepFishing.getPlugin(), () ->{
                    player.sendMessage(Formatting.GetFormattedMessage("Quest.reward")
                            .replace("{item}", Formatting.GetItemName(i) + " x" + i.getAmount())
                            .replace("{quest}", Name));
                } , 20);

            }
        }
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

    public static Collection<QuestType> GetAll(){
        return QuestTypes.values();
    }

    public static void UpdateId(String oldId, QuestType type){
        QuestTypes.remove(oldId);
        QuestTypes.put(type.Id, type);
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

    public static void CheckQuests() {
        LocalDateTime dateTime = LocalDateTime.now();

        DayOfWeek dayOfWeek = dateTime.getDayOfWeek();


        String timeStr = Formatting.asTime(dateTime);

        for(var q : GetAll()){
            q.TryStart(dayOfWeek, timeStr);
        }

        for(var q : Database.Quests.GetActive()){
            if(q.canFinish()){
                q.Finish();
            }
        }
    }
}
