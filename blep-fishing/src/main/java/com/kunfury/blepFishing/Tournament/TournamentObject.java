package com.kunfury.blepFishing.Tournament;

import com.kunfury.blepFishing.Config.TournamentMode;
import com.kunfury.blepFishing.Config.TournamentType;
import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Objects.BaseFishObject;
import com.kunfury.blepFishing.Objects.FishObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.Serial;
import java.io.Serializable;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class TournamentObject implements Serializable{

    @Serial
    private static final long serialVersionUID = -7127107612942708629L;
    private final String name; //The name of the tournament
    public final TournamentMode Mode; //Which mode the tournament will operate in, HOUR or DAY
    public final TournamentType Type;
    public final String FishType; //The type of fish being caught. RANDOM + ALL as options
    public final List<DayOfWeek> Days; //Days of the week the tournament will run on
    public double DailyDelay; //How long to delay before starting daily tournaments
    public int FishAmount; //The amount of fish that can be caught
    private final int minimumPlayers; //Minimum amount of online players required to sta
    private final int minimumFish; //Minimum amount of fish player needed to catch to compete
    public double Cooldown;
    private final boolean announceNewWinner;


    public boolean UseBossbar; //Whether the tournament will show a bossbar
    private final boolean BossbarTime; //Whether to show time on the bossbar or not
    public BarColor BossbarColor;
    public double BossbarPercent; //The percentage of time left the bossbar will show
    public double BossbarTimePercent; //When the time will show on bossbar


    public final HashMap<String, List<String>> Rewards; //The rewards given to players. The integer is place and the List is serialized items

    private final double duration; //Hours the tournament will run
    private LocalDateTime startDate;
    private LocalDateTime lastRan;
    private LocalDateTime endDate;
    private int maxRank; //The max rank available in the Rewards map

    private transient HashMap<Integer, FishObject> winners;
    private transient List<FishObject> caughtFish;
    private transient  List<OfflinePlayer> participants;
    private transient int trackedFish = 0; //The amount of tracked fish

    public TournamentObject(String _name, TournamentMode _mode, double _duration, String _fishType, List<DayOfWeek> _days, int _fishAmount, double _dailyDelay,
                            int _minimumPlayers, boolean _useBossbar, double _bossbarPercent,  BarColor _bossbarColor, double _cooldown, HashMap<String,
                            List<String>> _rewards, int _minimumFish, boolean _bossbarTime, double _bossbarTimePercent, TournamentType _type, Boolean _announceNewWinner,
                            LocalDateTime _lastRan){
        name = _name;
        Mode = _mode;
        Type = _type;
        duration = _duration;
        FishType = _fishType;
        Days = _days;
        FishAmount = _fishAmount;
        DailyDelay = _dailyDelay;
        minimumPlayers = _minimumPlayers;
        Cooldown = _cooldown;
        Rewards = _rewards;
        announceNewWinner = _announceNewWinner;

        startDate = LocalDateTime.now();
        lastRan = _lastRan;
        minimumFish = _minimumFish;

        UseBossbar = _useBossbar;
        BossbarPercent = _bossbarPercent;
        BossbarTimePercent = _bossbarTimePercent;
        BossbarColor = _bossbarColor;
        BossbarTime = _bossbarTime;

        //Gets the max rank available in the rewards file
        Rewards.forEach((key, value) ->{
            if(Formatting.isNumeric(key) && Integer.parseInt(key) > maxRank)
                maxRank = Integer.parseInt(key);
        });
    }

    public String getName(){
        return name;
    }

    public boolean canRun(){
        LocalDateTime dt = LocalDateTime.now();

        boolean downtimeCheck = getDowntime() >= Cooldown;
        boolean playerCheck = minimumPlayers <= Bukkit.getServer().getOnlinePlayers().size();
        boolean delayCheck = (ChronoUnit.MINUTES.between(dt.toLocalDate().atStartOfDay(), dt) / 60.0) >= DailyDelay;
        if(!downtimeCheck || !playerCheck || !delayCheck){ return false; }

        if(Mode == TournamentMode.DAY){
            return Days.contains(dt.getDayOfWeek());
        }
        return true;
    }

    public double getDowntime(){
        return ChronoUnit.MINUTES.between(lastRan, LocalDateTime.now()) / 60.0;
    }

    public BossBar CreateBossbar(){
        BossBar bar = TournamentHandler.BossBars.get(this);
        if(bar == null){
            bar = Bukkit.createBossBar(Formatting.formatColor(name), BossbarColor, BarStyle.SEGMENTED_10);
            bar.setVisible(false);
            TournamentHandler.BossBars.put(this, bar);
        }

        for(var p : TournamentHandler.FishingPlayers){
            bar.addPlayer(p);
        }

        return bar;
    }

    public double getProgress(){
        Duration d = Duration.between(startDate, LocalDateTime.now());

        double elapsed = d.toSeconds() / 60.0 / 60.0;
        double progress = elapsed/duration;
        if(progress > 1) progress = 1;
        return progress;
    }

    public Long getTimeRemaining(){
        LocalDateTime now = LocalDateTime.now();

        if(endDate == null) setEndDate();

        return ChronoUnit.MILLIS.between(now, endDate);
    }

    public List<FishObject> getFish(){
        List<FishObject> fishList = Variables.getFishList(FishType);
        if(needsUpdate(fishList) || caughtFish == null){
            caughtFish = Objects.requireNonNull(fishList).stream().filter(f -> f.DateCaught.isAfter(startDate)).collect(Collectors.toList());
        }
        return caughtFish;
    }

    //TODO: Improve this, make it not parse through the entire of the fish list
    private boolean needsUpdate(List<FishObject> fishList){
        if(trackedFish == fishList.size()) return false;
        trackedFish = fishList.size();
        return true;
    }

    public void StartEvent(){
        startDate = LocalDateTime.now();
        winners = null;
        caughtFish = null;
        participants = null;
        setEndDate();
    }

    private void setEndDate(){
        long seconds = (long) (duration * 60 * 60);
        endDate = startDate.plusSeconds(seconds);
    }

    public void FinishEvent(){
        lastRan = LocalDateTime.now();
        BossBar bossbar = TournamentHandler.BossBars.get(this);
        if(bossbar != null){
            TournamentHandler.BossBars.get(this).setVisible(false);
            TournamentHandler.BossBars.remove(this);
        }
    }

    public boolean isComplete(){
        return getTimeRemaining() <= 0;
    }

    public boolean canShow(){
        return getProgress() * 100 >= 100 - BossbarPercent;
    }

    public boolean showTimer(){
        return BossbarTime && getProgress() * 100 >= 100 - BossbarTimePercent;
    }

    public HashMap<Integer, FishObject> getWinners(){
        if(!needsUpdate(getFish()) && winners != null) return winners;
        winners = new HashMap<>();
        List<FishObject> caughtFish = getFish();


        switch(Type){ //Sorts the list so first place winner is ALWAYS at the top
            case LARGEST -> {
                caughtFish.sort(Comparator.comparing(o -> o.RealSize));
            }
            case SMALLEST -> {
                caughtFish.sort(Comparator.comparing(o -> o.RealSize));
                caughtFish.sort(Collections.reverseOrder());
            }
            case EXPENSIVE -> {
                caughtFish.sort(Comparator.comparing(o -> o.RealCost));
            }
            case CHEAPEST -> {
                caughtFish.sort(Comparator.comparing(o -> o.RealCost));
                caughtFish.sort(Collections.reverseOrder());
            }
            case SCORE -> {
                caughtFish.sort(Comparator.comparing(o -> o.Score));
            }
        }

        boolean requireAmt = minimumFish > 1; //Only runs amount checker if necessary

        List<FishObject> winningFish = new ArrayList<>();
        participants = new ArrayList<>();
        //Fills winningFish with fish caught by unique players
        for(var f: caughtFish){
            UUID uuid = f.getPlayerUUID();
            if(winningFish.stream().anyMatch(w -> w.getPlayerUUID().equals(uuid)) || (requireAmt && getAmountCaught(uuid, caughtFish) < minimumFish)){
                OfflinePlayer p = f.getPlayer();
                if(!participants.contains(p)) participants.add(p);
                continue;
            }
            winningFish.add(f);
        }

        for(var i = 0; i < maxRank; i++){
            if(winningFish.size() < i) break;
            String key = String.valueOf(i);
            if(!Rewards.containsKey(key)) continue; //Ensures the rewards file actually contains the key
            winners.put(i, winningFish.get(i - 1));
        }

        return winners;
    }

    public HashMap<OfflinePlayer, Integer> getWinnersAmount(){
        HashMap<OfflinePlayer, Integer> winners = new HashMap<>();

        List<FishObject> caughtFish = getFish();

        for(var f : caughtFish){
            OfflinePlayer p = f.getPlayer();
            if(winners.containsKey(p)){
                winners.put(p , winners.get(p) + 1);
            }else
                winners.put(p, 1);
        }

        return winners;
    }

    private long getAmountCaught(UUID uuid, List<FishObject> caughtFish){
        long amount = caughtFish.stream().filter(f -> f.getPlayerUUID().equals(uuid)).count();
        //TOOD: Return amount of fish caught by player
        return amount;
    }

    public List<OfflinePlayer> getParticipants(){
        return participants;
    }

    public ItemStack getItemStack(boolean admin){
        ItemStack item = new ItemStack(Material.SALMON);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(Formatting.formatColor(getName()));

        ArrayList<String> lore = new ArrayList<>();

        //String desc = "Catch the ";

        String type = Formatting.getMessage("Tournament.Type." + Type.toString().toLowerCase());
        String fishName;


        if(FishType.equalsIgnoreCase("ALL"))
            fishName = Formatting.getMessage("Tournament.allFish");
        else{
            m.setCustomModelData(BaseFishObject.GetBase(FishType).ModelData);
            fishName = FishType;
        }


        String desc = Formatting.getMessage("Tournament.Type.description")
                        .replace("{type}", type)
                        .replace("{fish}", fishName);

        lore.add(desc);

        var winners = getWinners();
        lore.add("");

        if(winners.size() <= 0){
            lore.add(Formatting.getMessage("Tournament.noneCaught"));
        }else{
            winners.forEach((rank, fish) -> {
                lore.add(ChatColor.WHITE + "" + rank + ": " + fish.getPlayer().getName());
            });
        }

        lore.add("");
        lore.add(ChatColor.WHITE + Formatting.asTime(getTimeRemaining()));

//        if(admin){
//            lore.add("");
//            lore.add(ChatColor.RED + "Right-Click to " + ChatColor.YELLOW + ChatColor.ITALIC + "Cancel");
//        }

        m.setLore(lore);
        item.setItemMeta(m);
        return item;
    }

    private FishObject bestFish;
    public boolean isBest(FishObject fish){

        if(!announceNewWinner || (!FishType.equalsIgnoreCase("ALL") && !fish.Name.equalsIgnoreCase(FishType))){
            return false;
        }

        if(bestFish == null){
            bestFish = fish;
            return true;
        }

        if(Type == TournamentType.AMOUNT) return false;

        boolean best = false;
        switch(Type){ //Sorts the list so first place winner is ALWAYS at the top
            case LARGEST -> {
                if(fish.RealSize > bestFish.RealSize)
                    best = true;
            }
            case SMALLEST -> {
                if(fish.RealSize < bestFish.RealSize)
                    best = true;
            }
            case EXPENSIVE -> {
                if(fish.RealCost > bestFish.RealCost)
                    best = true;
            }
            case CHEAPEST -> {
                if(fish.RealCost < bestFish.RealCost)
                    best = true;
            }
            case SCORE -> {
                if(fish.Score > bestFish.Score)
                    best = true;
            }
        }

        if(best)
            bestFish = fish;

        return best;
    }

    public LocalDateTime getLastRan() {
        return lastRan;
    }
}
