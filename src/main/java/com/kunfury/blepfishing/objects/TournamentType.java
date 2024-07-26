package com.kunfury.blepfishing.objects;

import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.items.ItemHandler;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MusicInstrumentMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;

import static com.kunfury.blepfishing.objects.TournamentType.Grading.LONGEST;

public class TournamentType {
    public String Id;
    public String Name;
    public List<String> FishTypeIds;
    public double Duration;
    public HashMap<TournamentDay, List<String>> StartTimes = new HashMap<>();
    public HashMap<Integer, Double> CashRewards;
    public HashMap<Integer, List<ItemStack>> ItemRewards;
    public Grading Grading = LONGEST;

    public boolean VillagerHorn;
    public int HornLevel = 1;

    public boolean ConfirmedDelete;

    public TournamentType(String id, String name, double duration, List<String> fishTypeIds, HashMap<TournamentDay, List<String>> startTimes,
                          HashMap<Integer, Double> cashRewards, HashMap<Integer, List<ItemStack>> itemRewards, boolean villagerHorn,
                          int hornLevel){
        Id = id;
        Name = name;
        Duration = duration;
        FishTypeIds = fishTypeIds;
        StartTimes = startTimes;
        CashRewards = cashRewards;
        ItemRewards = itemRewards;
        VillagerHorn = villagerHorn;
        HornLevel = hornLevel;
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

        Bukkit.broadcastMessage("Starting Tourney: " + Name);
        Start();
    }

    public boolean Start(){
        if(Database.Tournaments.IsRunning(Id)){
            return false;
        }
        new TournamentObject(this);
        Bukkit.broadcastMessage("Started tournament " + Name + "!");
        return true;

    }

    public ItemStack getHorn(){
        ItemStack item = new ItemStack(Material.GOAT_HORN);


        MusicInstrumentMeta m = (MusicInstrumentMeta)item.getItemMeta();
        assert m != null;
        m.setDisplayName(ChatColor.AQUA + Name + " Horn");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE + "Duration: " + ChatColor.WHITE + Formatting.asTime(Duration, ChatColor.BLUE));
        StringBuilder sb = new StringBuilder();
        sb.append(ChatColor.BLUE + "Types: ");
        for(var f : FishTypeIds){
            sb.append(ChatColor.WHITE).append(f).append(ChatColor.BLUE).append(", ");
        }
        lore.add(sb.toString());
        m.setLore(lore);

        m.setInstrument(MusicInstrument.SING_GOAT_HORN);

        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        dataContainer.set(ItemHandler.TourneyTypeId, PersistentDataType.STRING, Id);

        item.setItemMeta(m);

        return item;
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

    public void GiveRewards(int place, UUID playerId){

        List<ItemStack> items = new ArrayList<>();
        double cash = 0;

        if(ItemRewards.containsKey(place))
            items.addAll(ItemRewards.get(place));

        if(CashRewards.containsKey(place))
            cash = CashRewards.get(place);

        for(var i : items){
            var reward = new UnclaimedReward(playerId, i);
        }


        OfflinePlayer player = Bukkit.getOfflinePlayer(playerId);
        Bukkit.broadcastMessage("Giving " + player.getName() + "$" + cash + " and " + items.size() + " items!");
    }

    public List<String> getItemLore(){
        List<String> lore = new ArrayList<>();
        lore.add("Tournament Details");
        return lore;
    }

    ///
    //STATIC METHODS
    ///

    private static final HashMap<String, TournamentType> Tournaments = new HashMap<>();
    public static void AddNew(TournamentType tournament){
        if(Tournaments.containsKey(tournament.Id)){
            Bukkit.getLogger().warning("Attempted to create duplicate Tournament with ID: " + tournament.Id);
            return;
        }

        Tournaments.put(tournament.Id, tournament);
        Bukkit.getLogger().warning("Loaded Tournament: " + tournament.Name);
    }

    public static void Delete(TournamentType type){
        Tournaments.remove(type.Id);
    }

    public static Collection<TournamentType> GetTournaments(){
        return Tournaments.values();
    }


    public static TournamentType FromId(String tourneyId){
        if(Tournaments.containsKey(tourneyId)){
            return Tournaments.get(tourneyId);
        }

        Bukkit.getLogger().warning("Tried to get invalid Tournament with ID: " + tourneyId);
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
        String time = dateTime.getHour() + ":" + dateTime.getMinute();
        //Bukkit.broadcastMessage("Trying to Start " + GetTournaments().size() + " Tournaments: " + time);
        for(var t : GetTournaments()){
            t.TryStart(dayOfWeek, time);
        }
    }

    public static List<TournamentType> GetHornTournaments(){
        return Tournaments.values().stream()
                .filter(t -> t.VillagerHorn)
                .toList();
    }

    public enum Grading{
        LONGEST,
        SHORTEST,
        SCORE_HIGH,
        SCORE_LOW
    }

    public enum TournamentDay{
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

