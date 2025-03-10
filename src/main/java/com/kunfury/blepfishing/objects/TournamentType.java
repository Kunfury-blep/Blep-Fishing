package com.kunfury.blepfishing.objects;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.helpers.Utilities;
import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.plugins.McMMO;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MusicInstrumentMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;

import static com.kunfury.blepfishing.objects.TournamentType.GradingType.LONGEST;

public class TournamentType {
    public String Id;
    public String Name;
    public List<String> FishTypeIds;
    public double Duration;
    public HashMap<TournamentDay, List<String>> StartTimes = new HashMap<>();
    public HashMap<Integer, Double> CashRewards;
    public HashMap<Integer, List<ItemStack>> ItemRewards;

    public boolean VillagerHorn;
    public int HornLevel = 1;
    public GradingType Grading = LONGEST;


    //Not in editor yet
    public boolean HasBossBar = true;
    public BarColor BossBarColor = BarColor.BLUE;


    public boolean ConfirmedDelete;

    public TournamentType(String id, String name, double duration, List<String> fishTypeIds, HashMap<TournamentDay, List<String>> startTimes,
                          HashMap<Integer, Double> cashRewards, HashMap<Integer, List<ItemStack>> itemRewards, boolean villagerHorn,
                          int hornLevel, boolean bossBar){
        Id = id;
        Name = name;
        Duration = duration;
        FishTypeIds = fishTypeIds;
        StartTimes = startTimes;
        CashRewards = cashRewards;
        ItemRewards = itemRewards;
        VillagerHorn = villagerHorn;
        HornLevel = hornLevel;
        HasBossBar = bossBar;
    }

    //For creating new Tournaments in the GUI
    public TournamentType(String id){
        Id = id;
        Name = id;
        Duration = 1;
        FishTypeIds = new ArrayList<>();
        CashRewards = new HashMap<>();
        ItemRewards = new HashMap<>();
        VillagerHorn = false;
    }


    public void TryStart(DayOfWeek dayOfWeek, String time){
        TournamentDay tDay = TournamentDay.valueOf(dayOfWeek.toString());

        if((!StartTimes.containsKey( tDay) || !StartTimes.get(tDay).contains(time))
        && (!StartTimes.containsKey(TournamentDay.EVERYDAY) || !StartTimes.get(TournamentDay.EVERYDAY).contains(time)))
            return;

        Start();
    }

    public TournamentObject Start(){
        if(IsRunning()){
            return null;
        }
        var tournament = new TournamentObject(this);

        Utilities.Announce(Formatting.GetFormattedMessage("Tournament.start")
                .replace("{tournament}", Name));
        return tournament;
    }

    public void Cancel(){
        if(!Database.Tournaments.IsRunning(Id)){
            return;
        }

        Database.Tournaments.GetActiveOfType(Id).Cancel();
    }

    public void Finish(){
        if(!Database.Tournaments.IsRunning(Id)){
            return;
        }

        Database.Tournaments.GetActiveOfType(Id).Finish();
    }

    public boolean IsRunning(){
        return Database.Tournaments.IsRunning(Id);
    }

    public ItemStack getHorn(){
        ItemStack item = new ItemStack(Material.GOAT_HORN);


        MusicInstrumentMeta m = (MusicInstrumentMeta)item.getItemMeta();
        assert m != null;
        m.setDisplayName(Formatting.GetLanguageString("Equipment.Tournament Horn.name")
                        .replace("{tournament}", Name));

        List<String> lore = new ArrayList<>();
        lore.add(Formatting.GetLanguageString("Equipment.Tournament Horn.duration")
                .replace("{time}", Formatting.asTime(Duration)));

        List<String> fishList = new ArrayList<>();
        for(var f : getFishTypes()){
            fishList.add(f.Name);
        }

        String commaString = Formatting.ToCommaList(fishList, ChatColor.WHITE, ChatColor.BLUE);

        List<String> finalLoreList = Formatting.ToLoreList(Formatting.GetLanguageString("Equipment.Tournament Horn.tournamentFish")
                .replace("{fish}", commaString));

        lore.addAll(finalLoreList);

        m.setLore(lore);
        m.setInstrument(MusicInstrument.SING_GOAT_HORN);

        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        dataContainer.set(ItemHandler.TourneyTypeId, PersistentDataType.STRING, Id);

        item.setItemMeta(m);

        return item;
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

    public List<Integer> getPlacements(){
        List<Integer> placements = new ArrayList<>(ItemRewards.keySet());
        for(var key : CashRewards.keySet()){
            if(placements.contains(key))
                continue;

            placements.add(key);
        }
        return placements;
    }

    public void GiveRewards(int place, UUID playerUUID){

        List<ItemStack> items = new ArrayList<>();

        if(ItemRewards.containsKey(place))
            items.addAll(ItemRewards.get(place));

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);

        Player player = offlinePlayer.getPlayer();

        if(BlepFishing.hasEconomy() && CashRewards.containsKey(place)){
            var amount = CashRewards.get(place);
            EconomyResponse r = BlepFishing.getEconomy().depositPlayer(offlinePlayer, amount);
            if(!r.transactionSuccess())
                Utilities.Severe(r.errorMessage);

            if(player != null){
                Bukkit.getScheduler().runTaskLater (BlepFishing.getPlugin(), () ->{
                    player.sendMessage(Formatting.GetFormattedMessage("Tournament.rewardCurrency")
                            .replace("{amount}", Formatting.toBigNumber(amount))
                            .replace("{tournament}", Name));
                } , 20);
            }
        }

        if(!offlinePlayer.isOnline()){
            for(var i : items){
                new UnclaimedReward(playerUUID, i); //Saves reward if unable to claim
            }
            return;
        }

        for(var i : items){
            if(!Utilities.GiveItem(offlinePlayer.getPlayer(), i, false)){
                new UnclaimedReward(playerUUID, i); //Saves reward if unable to claim
                continue;
            }

            if(player != null){
                Bukkit.getScheduler().runTaskLater (BlepFishing.getPlugin(), () ->{
                    player.sendMessage(Formatting.GetFormattedMessage("Tournament.reward")
                            .replace("{item}", Formatting.GetItemName(i) + " x" + i.getAmount())
                            .replace("{tournament}", Name));
                } , 20);
            }
        }
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

    ///
    //STATIC METHODS
    ///

    private static final HashMap<String, TournamentType> Tournaments = new HashMap<>();
    public static void AddNew(TournamentType tournament){
        if(Tournaments.containsKey(tournament.Id)){
            Utilities.Severe("Attempted to create duplicate Tournament with ID: " + tournament.Id);
            return;
        }
        Tournaments.put(tournament.Id, tournament);}

    public static void Delete(TournamentType type){
        Tournaments.remove(type.Id);
    }

    public static void Clear(){
        Tournaments.clear();
    }

    public static Collection<TournamentType> GetAll(){
        return Tournaments.values();
    }


    public static TournamentType FromId(String tourneyId){
        if(Tournaments.containsKey(tourneyId)){
            return Tournaments.get(tourneyId);
        }
        Utilities.Severe("Tried to get invalid Tournament with ID: " + tourneyId);
        return null;
    }

    public static boolean IdExists(String id){
        return Tournaments.containsKey(id);
    }

    public static void UpdateId(String oldId, TournamentType type){
        Tournaments.remove(oldId);
        Tournaments.put(type.Id, type);
    }

    public static void CheckCanStart(){
        LocalDateTime dateTime = LocalDateTime.now();

        DayOfWeek dayOfWeek = dateTime.getDayOfWeek();


        String timeStr = Formatting.asTime(dateTime);

        //Bukkit.getLogger().warning(dayOfWeek + " - " + timeStr);
        //Bukkit.broadcastMessage(dayOfWeek + " - " + timeStr);
        for(var t : GetAll()){
            t.TryStart(dayOfWeek, timeStr);
        }
    }

    public static List<TournamentType> GetHornTournaments(){
        return Tournaments.values().stream()
                .filter(t -> t.VillagerHorn)
                .toList();
    }

    public enum GradingType {
        LONGEST,
        SHORTEST,
        SCORE_HIGH,
        SCORE_LOW
    }

    public enum TournamentDay{ //TODO: Convert to Event Day for Quests
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY,
        SUNDAY,
        EVERYDAY
    }

}


