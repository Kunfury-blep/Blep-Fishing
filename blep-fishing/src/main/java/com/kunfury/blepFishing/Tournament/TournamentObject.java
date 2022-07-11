package com.kunfury.blepFishing.Tournament;

import com.kunfury.blepFishing.Config.TournamentMode;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Player;

import java.io.Serial;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;

public class TournamentObject implements Serializable {

    @Serial
    private static final long serialVersionUID = -7127107612942708629L;
    private final String name; //The name of the tournament
    public TournamentMode Mode; //The type the tournament will be
    public double Duration; //Hours the tournament will run
    public String FishType; //The type of fish being caught. RANDOM + ALL as options
    public List<DayOfWeek> Days; //Days of the week the tournament will run on
    public double DailyDelay; //How long to delay before starting daily tournaments
    public int FishAmount; //The amount of fish that can be caught
    private int minimumPlayers; //Minimum amount of online players required to sta
    private int minimumFish; //Minimum amount of fish player needed to catch to compete
    public double Delay;



    public boolean UseBossbar; //Whether the tournament will show a bossbar
    public BarColor BossbarColor;
    public double BossbarPercent; //The percentage of time left the bossbar will show
    public final HashMap<String, List<String>> Rewards; //The rewards given to players. The integer is place and the List is serialized items


    private LocalDateTime startTime;
    private LocalDateTime lastRan;

    public TournamentObject(String _name, TournamentMode _mode, double _duration, String _fishType, List<DayOfWeek> _days, int _fishAmount, double _dailyDelay,
                            int _minimumPlayers, boolean _useBossbar, double _bossbarPercent,  BarColor _bossbarColor, double _delay, HashMap<String,
                            List<String>> _rewards, int _minimumFish){
        name = _name;
        Mode = _mode;
        Duration = _duration;
        FishType = _fishType;
        Days = _days;
        FishAmount = _fishAmount;
        DailyDelay = _dailyDelay;
        minimumPlayers = _minimumPlayers;
        UseBossbar = _useBossbar;
        BossbarPercent = _bossbarPercent;
        BossbarColor = _bossbarColor;
        Delay = _delay;
        Rewards = _rewards;

        startTime = LocalDateTime.now();
        lastRan = LocalDateTime.MIN;
        minimumFish = _minimumFish;
    }

    public List<String> GetRewards(int place){
        return Rewards.get(place);
    }

    public double getTimeLeft(){
        return ChronoUnit.HOURS.between(startTime, LocalDateTime.now());
    }

    public String getName(){
        return name;
    }

    public boolean isComplete(){


        return false;
    }

    public boolean canRun(){
        LocalDateTime dt = LocalDateTime.now();
        double startDiff = ChronoUnit.MINUTES.between(dt.toLocalDate().atStartOfDay(), dt) / 60.0;
        double runDiff = ChronoUnit.MINUTES.between(lastRan, dt) / 60.0;
        return startDiff >= DailyDelay && runDiff >= Delay && minimumPlayers <= Bukkit.getServer().getOnlinePlayers().size();
    }

    public List<Player> GetWinners(){
        //TODO: Get all players matching criteria who competed
            //Ensure minimum fish is checked

        return null;
    }

    public void Complete(){
        lastRan = LocalDateTime.now();
    }
}
