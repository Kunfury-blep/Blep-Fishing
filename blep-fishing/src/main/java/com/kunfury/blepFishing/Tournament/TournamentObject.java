package com.kunfury.blepFishing.Tournament;

import com.kunfury.blepFishing.BlepFishing;
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
    public String Name; //The name of the tournament
    public TournamentMode Mode; //Which mode the tournament will operate in, HOUR or DAY
    public TournamentType Type;

    public String FishType; //The type of fish being caught. RANDOM + ALL as options
    private String ActiveFishType;
    public List<DayOfWeek> Days; //Days of the week the tournament will run on
    public double DailyDelay; //How long to delay before starting daily tournaments
    public int MaxFish; //The amount of fish that can be caught
    public int MinimumPlayers; //Minimum amount of online players required to sta
    public int MinimumFish; //Minimum amount of fish player needed to catch to compete
    public double Cooldown;
    public boolean AnnounceNewWinner;
    public boolean DiscordStart;


    public boolean UseBossbar; //Whether the tournament will show a bossbar
    public boolean BossbarTimer; //Whether to show time on the bossbar or not
    public BarColor BossbarColor;
    public double BossbarPercent; //The percentage of time left the bossbar will show
    public double BossbarTimePercent; //When the time will show on bossbar


    public final HashMap<String, List<String>> Rewards; //The rewards given to players. The integer is place and the List is serialized items
    public HashMap<String, Integer> CaughtMap = new HashMap<>(); //Amount of fish caught by each player

    private double duration; //Hours the tournament will run
    private LocalDateTime startDate;
    private LocalDateTime lastRan;
    private LocalDateTime endDate;
    private FishObject bestFish;
    private int maxRank; //The max rank available in the Rewards map

    private transient HashMap<Integer, FishObject> winners;
    private transient List<FishObject> caughtFish;
    private transient  List<OfflinePlayer> participants;

    private boolean needsUpdate = true;

    public TournamentObject(String _name, TournamentMode _mode, double _duration, String _fishType, List<DayOfWeek> _days, int _fishAmount, double _dailyDelay,
                            int _minimumPlayers, boolean _useBossbar, double _bossbarPercent,  BarColor _bossbarColor, double _cooldown, HashMap<String,
                            List<String>> _rewards, int _minimumFish, boolean _bossbarTime, double _bossbarTimePercent, TournamentType _type, Boolean _announceNewWinner,
                            LocalDateTime _lastRan, boolean discordStart){
        Name = _name;
        Mode = _mode;
        Type = _type;
        duration = _duration;
        FishType = _fishType;
        Days = _days;
        MaxFish = _fishAmount;
        DailyDelay = _dailyDelay;
        MinimumPlayers = _minimumPlayers;
        Cooldown = _cooldown;
        Rewards = _rewards;
        AnnounceNewWinner = _announceNewWinner;

        startDate = LocalDateTime.now();
        lastRan = _lastRan;
        MinimumFish = _minimumFish;

        UseBossbar = _useBossbar;
        BossbarPercent = _bossbarPercent;
        BossbarTimePercent = _bossbarTimePercent;
        BossbarColor = _bossbarColor;
        BossbarTimer = _bossbarTime;

        //Gets the max rank available in the rewards file
        Rewards.forEach((key, value) ->{
            if(Formatting.isNumeric(key) && Integer.parseInt(key) > maxRank)
                maxRank = Integer.parseInt(key);
        });

        DiscordStart = discordStart;
    }

    public String getName(){
        if(Name == null){
            Name = FishType +  " : " + Mode + " " + Type + " " + duration; //Used as a backup if previous name can't be found
        }
        return Name;
    }

    public boolean canRun(){
        LocalDateTime dt = LocalDateTime.now();

        boolean downtimeCheck = getDowntime() >= Cooldown;
        boolean playerCheck = MinimumPlayers <= Bukkit.getServer().getOnlinePlayers().size();
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
            bar = Bukkit.createBossBar(Formatting.formatColor(getName()), BossbarColor, BarStyle.SEGMENTED_10);
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
        if(caughtFish == null || needsUpdate){
            UpdateCaughtFish();
            UpdateWinners();
            needsUpdate = false;
        }

        return caughtFish;
    }

    public void StartEvent(){
        if(FishType.equalsIgnoreCase("RANDOM")){
            Random rand = new Random();
            ActiveFishType = Variables.BaseFishList.get(rand.nextInt(Variables.BaseFishList.size())).Name;
        }else
            ActiveFishType = FishType;
        startDate = LocalDateTime.now();
        winners = null;
        caughtFish = null;
        participants = null;
        CaughtMap = new HashMap<>();
        bestFish = null;
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
        return BossbarTimer && getProgress() * 100 >= 100 - BossbarTimePercent;
    }

    private long getAmountCaught(UUID uuid, List<FishObject> caughtFish){
        //TOOD: Return amount of fish caught by player
        return caughtFish.stream().filter(f -> f.getPlayerUUID().equals(uuid)).count();
    }

    public List<OfflinePlayer> getParticipants(){
        return participants;
    }

    public int getModelData(){
        BaseFishObject base = BaseFishObject.getBase(getFishType());
        if(base != null)
            return base.ModelData;
        return 0;
    }

    public ItemStack getItemStack(boolean admin){
        ItemStack item = new ItemStack(Material.SALMON);
        ItemMeta m = item.getItemMeta();
        assert m != null;
        m.setDisplayName(Formatting.formatColor(getName()));

        ArrayList<String> lore = new ArrayList<>();

        //String desc = "Catch the ";

        String type = Formatting.getMessage("Tournament.Type." + Type.toString().toLowerCase());
        String fishName;


        if(getFishType().equalsIgnoreCase("ALL"))
            fishName = Formatting.getMessage("Tournament.allFish");
        else{
            m.setCustomModelData(getModelData());

            fishName = getFishType();
        }


        String desc = Formatting.getMessage("Tournament.Type.description")
                        .replace("{type}", type)
                        .replace("{fish}", fishName);

        lore.add(desc);

        lore.add("");

        String amountCaught = Formatting.getMessage("Tournament.amountCaught")
                .replace("{amount}", String.valueOf(getFish().size()));
        lore.add(amountCaught);

        lore.add("");

        if(winners == null || winners.size() == 0)
            UpdateWinners();

        if(winners.size() == 0){
            lore.add(Formatting.getMessage("Tournament.noneCaughtItem"));
        }else{
            winners.forEach((rank, fish) -> {
                if(fish.getPlayer() != null && fish.getPlayer().getName() != null){
                    String playerRank = Formatting.getMessage("Tournament.playerRank")
                            .replace("{rank}", String.valueOf(rank))
                            .replace("{player}", fish.getPlayer().getName());
                    lore.add(playerRank);
                }
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

    public boolean isBest(FishObject fish){
        if(!AnnounceNewWinner || Type.equals(TournamentType.AMOUNT)){
            return false;
        }

        if(bestFish == null){
            bestFish = fish;
            return true;
        }

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

    public FishObject getBestFish(){
        return bestFish;
    }

    public LocalDateTime getLastRan() {
        return lastRan;
    }

    public void setLastRan(LocalDateTime _lastRan) {
        lastRan = _lastRan;
    }

    public String getFishType(){
        if(ActiveFishType == null){
            if(FishType.equalsIgnoreCase("RANDOM")){
                Random rand = new Random();
                ActiveFishType = Variables.BaseFishList.get(rand.nextInt(Variables.BaseFishList.size())).Name;
            }else
                ActiveFishType = FishType;
        }

        return ActiveFishType;
    }

    public boolean isRunning(){
        return(TournamentHandler.ActiveTournaments.contains(this));
    }

    public void newCatch(FishObject fish, Player p){
        if(!getFishType().equalsIgnoreCase("ALL") && !fish.Name.equalsIgnoreCase(FishType)){
            return;
        }

        String uuid = p.getUniqueId().toString();

        if(CaughtMap == null){
            CaughtMap = new HashMap<>();
        }

        if(CaughtMap.containsKey(uuid)){
            int caughtAmt = CaughtMap.get(uuid) + 1;
            CaughtMap.put(uuid, caughtAmt);
        }else
            CaughtMap.put(uuid, 1);


        needsUpdate = true;

        if(isBest(fish)){
            new TournamentHandler().AnnounceBest(this, fish);
        }

        if(Variables.DebugMode){

            var winners = getWinners();

            var logger = BlepFishing.getPlugin().getLogger();

            logger.info("Test Message");

            logger.info(" ");
            logger.info( "~Start Tournament Catch Debug~ " + winners.size());
            logger.info( " ");

            winners.forEach((rank, f) -> {
                logger.info( rank + " : " + f.Name + " : " + f.getSize() + " : $" + f.getValue() + " : S:" + f.getScore() + " : " + f.getPlayerUUID());
            });
        }

    }

    public double getDuration(){
        return duration;
    }

    public void setDuration(double _duration){
        duration = _duration;
    }
    public HashMap<Integer, FishObject> getWinners(){
        if(winners == null || needsUpdate || winners.size() == 0){
            UpdateWinners();
        }

        return winners;
    }

    private void UpdateWinners(){
        winners = new HashMap<>();

        if(participants == null)
            participants = new ArrayList<>();

        if(caughtFish == null)
            UpdateCaughtFish();

        switch(Type){ //Sorts the list so first place winner is ALWAYS at the top
            case LARGEST -> {
                caughtFish.sort((o1, o2) -> (int) (o2.RealSize - o1.RealSize));
            }
            case SMALLEST -> {
                caughtFish.sort((o1, o2) -> (int) (o1.RealSize - o2.RealSize));
            }
            case EXPENSIVE -> {
                caughtFish.sort((o1, o2) -> (int) (o2.RealCost - o1.RealCost));
            }
            case CHEAPEST -> {
                caughtFish.sort((o1, o2) -> (int) (o1.RealCost - o2.RealCost));
            }
            case SCORE -> {
                caughtFish.sort((o1, o2) -> (int) (o2.Score - o1.Score));
            }
        }

        boolean requireAmt = MinimumFish > 1; //Only runs amount checker if necessary

        List<FishObject> winningFish = new ArrayList<>(); //Contains a


        for(var f: caughtFish){ //Loops through all valid fish for the tournament
            UUID uuid = f.getPlayerUUID();


            if(winningFish.stream().anyMatch(w -> w.getPlayerUUID().equals(uuid)) || requireAmt && getAmountCaught(uuid, caughtFish) < MinimumFish){
                //Skip if there is already a winning fish cuaght by the player
                continue;
            }

            OfflinePlayer p = f.getPlayer();

            if(!participants.contains(p))
                participants.add(p);

            winningFish.add(f);
        }

        for(var i = 0; i < maxRank; i++){
            if(winningFish.size() <= i){
                break;
            }

            String key = String.valueOf(i + 1);

            if(!Rewards.containsKey(key)){
                continue;
            }

            winners.put(i + 1, winningFish.get(i));
        }
    }

    private void UpdateCaughtFish(){
        List<FishObject> fishList = Variables.getFishList(getFishType());
        caughtFish = Objects.requireNonNull(fishList).stream().filter(f -> f.DateCaught.isAfter(startDate)).collect(Collectors.toList());
    }
}
