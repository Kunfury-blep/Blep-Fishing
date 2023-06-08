package com.kunfury.blepFishing.Tournament;

import com.kunfury.blepFishing.Config.*;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Objects.FishObject;
import com.kunfury.blepFishing.BlepFishing;
import com.kunfury.blepFishing.Plugins.DiscordSRVHandler;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;

public class TournamentHandler {

    public static List<TournamentObject> TournamentList = new ArrayList<>();
    public static List<TournamentObject> ActiveTournaments = new ArrayList<>();
    public static HashMap<TournamentObject, BossBar> BossBars = new HashMap<>();
    public static List<Player> FishingPlayers = new ArrayList<>();

    public static void Reset(boolean disable){
        ActiveTournaments = new ArrayList<>();
        TournamentList = new ArrayList<>();
        BossBars.forEach((key, value) -> {
            value.removeAll();
            value.setVisible(false);
        });
        BossBars.clear();
        if(!disable) new TournamentHandler().UpdateBossbars();

    }

    public void AddTournament(TournamentObject tournament){
        TournamentList.add(tournament);
    }

    //
    public void TournamentTimer(){
        for(var t: TournamentList){
            //Bukkit.broadcastMessage("Checking: " + t.getName());

            if(!ActiveTournaments.contains(t) && t.canRun()){
                Start(t);
            }else{
                if(ActiveTournaments.contains(t) && t.isComplete()) Finish(t);
            }

        }
    }

    public void Start(TournamentObject t){
        if(ActiveTournaments.contains(t)){
            return;
        }
        ActiveTournaments.add(t);
        String announcement = Formatting.getFormattedMesage("Tournament.start")
                .replace("{tournament}", t.getName());

        announcement = Formatting.formatColor(announcement);

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(announcement);
        }

        FileHandler.TournamentData = true;
        t.StartEvent();

        if(t.UseBossbar){
            BossBar bar = BossBars.get(t);
            if(bar == null) bar = t.CreateBossbar();
            bar.setVisible(false);
            bar.setProgress(0);
            for(var p : FishingPlayers){
                bar.addPlayer(p);
            }
            UpdateBossbars();
        }

        if(t.DiscordStart)
            DiscordSRVHandler.SendTournamentStart(t);
    }

    public void Cancel(TournamentObject t){
        ActiveTournaments.remove(t);
        t.FinishEvent();
        FileHandler.TournamentData = true;
    }

    static boolean barRunning;
    public void UpdateBossbars(){
        if(barRunning) return;
        barRunning = true;
        new BukkitRunnable() {
            @Override
            public void run() {
                BossBars.forEach((t, bar) -> {
                    if(t.canShow()){
                        bar.setVisible(true);
                        String title = Formatting.formatColor(t.getName() + "&f");
                        if(t.showTimer()) title += " " + Formatting.asTime(t.getTimeRemaining());
                        bar.setTitle(title);
                        bar.setProgress(t.getProgress());
                    }

                });


            }

        }.runTaskTimer(BlepFishing.getPlugin(), 0, 20);
    }

    public void Finish(TournamentObject t){
        ActiveTournaments.remove(t);
        FileHandler.TournamentData = true;

        t.FinishEvent();

        new CacheHandler().SaveCache();

        if(t.getFish().size() == 0){
            String tempName = t.getFishType().toLowerCase();
            if(tempName.equalsIgnoreCase("ALL")) tempName = "fish";
            Bukkit.broadcastMessage(Formatting.getFormattedMesage("Tournament.noneCaught")
                    .replace("{fish}", tempName));
            return;
        }

        int pLength = 15; //Initializes the size of the chatbox
        List<TextComponent> textComponents = new ArrayList<>();

        var tournamentWinners = t.getWinners();

        t.Rewards.forEach((rank, reward) -> {
            if(!Formatting.isNumeric(rank))
                return;

            int rankInt = Integer.parseInt(rank);

            if(!tournamentWinners.containsKey(rankInt)){
                String pString = Formatting.FixFontSize("Nothing Caught", pLength);
                String lbString = Formatting.FixFontSize(rank + ".", 4)+ pString;
                TextComponent mainComponent = new TextComponent (Formatting.formatColor(lbString));
                textComponents.add(mainComponent);
            }else{
                FishObject fish = tournamentWinners.get(rankInt);


                String pString = Formatting.FixFontSize(fish.PlayerName, pLength);
                String lbString = Formatting.FixFontSize(rank + ".", 4)+ pString + fish.getFormattedRarity() + " " + fish.Name;
                TextComponent mainComponent = new TextComponent (Formatting.formatColor(lbString));
                mainComponent.setHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT, fish.getHoverText()));
                textComponents.add(mainComponent);
            }
        });

//        t.getWinners().forEach((rank, fish) -> {
//            String pString = Formatting.FixFontSize(fish.PlayerName, pLength);
//            String lbString = Formatting.FixFontSize(rank + ".", 4)+ pString + fish.getFormattedRarity() + " " + fish.Name;
//            TextComponent mainComponent = new TextComponent (Formatting.formatColor(lbString));
//            mainComponent.setHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT, fish.getHoverText()));
//            textComponents.add(mainComponent);
//        });

        String banner =  Formatting.getMessage("Tournament.leaderboard");
        for(Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(" ");
            p.sendMessage(banner);
            for(var c : textComponents){
                p.spigot().sendMessage(c);
            }
            p.sendMessage(" ");
        }

        new Rewards().Generate(t);

        if(t.DiscordStart)
            DiscordSRVHandler.SendTournamentEnd(t);
    }

    public void ShowBars(Player p){
        if(!FishingPlayers.contains(p)){
            FishingPlayers.add(p);
            BossBars.forEach((key, bar) -> {
                bar.addPlayer(p);
            });
        }
    }

    public void AnnounceBest(TournamentObject t, FishObject fish){
        String lbString = Formatting.getMessage("Tournament.newBest")
                .replace("{player}", fish.PlayerName)
                .replace("{tournament}", t.getName())
                .replace("{rarity}", fish.getFormattedRarity())
                .replace("{fish}", fish.Name);
        TextComponent mainComponent = new TextComponent (Formatting.formatColor(lbString));
        mainComponent.setHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT, fish.getHoverText()));

        for(Player p : Bukkit.getOnlinePlayers()) {
            p.spigot().sendMessage(mainComponent);
        }
    }

    public static TournamentObject FindTournament(String id){
        return TournamentList.stream().filter(t -> t.getName().equals(id))
                .findFirst().orElse(null);
    }

    public static void EnableTournaments(boolean enabled, Player player){
        FileConfiguration config = BlepFishing.configBase.config;
        config.set("Enable Tournaments", enabled);

        BlepFishing.blepFishing.saveConfig();

        if(enabled){
            new ConfigExtra().LoadTournaments();
            player.sendMessage(Variables.getPrefix() + "Fishing Tournaments have been enabled.");
        }else{
            player.sendMessage(Variables.getPrefix() + "Fishing Tournaments have been disabled.");
        }

    }

    public static void UpdateTournamentValue(String key, Object value){
        File tournamentFile = new File(BlepFishing.getPlugin().getDataFolder(), "tournaments.yml");
        YamlConfiguration tournamenYaml = YamlConfiguration.loadConfiguration(tournamentFile);

        tournamenYaml.set(key, value);
        try {
            tournamenYaml.save(tournamentFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //RefreshTournaments(tournamenYaml);
    }

    public static void UpdateTournamentName(String oldKey, String newKey) {
        Bukkit.broadcastMessage("Setting " + oldKey + " to " + newKey);
        File tournamentFile = new File(BlepFishing.getPlugin().getDataFolder(), "tournaments.yml");
        YamlConfiguration tournamentYaml = YamlConfiguration.loadConfiguration(tournamentFile);


        for(var key : tournamentYaml.getKeys(true)){
            if(!key.contains(oldKey) || key.equals(oldKey))
                continue;

            var value = tournamentYaml.get(key);

            String formattedNewKey = key.replace(oldKey, newKey);
            tournamentYaml.set(formattedNewKey, value);
        }

        tournamentYaml.set(oldKey, null);

        try {
            tournamentYaml.save(tournamentFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//
//
//
//        for(var key : tournamenYaml.get){
//            Bukkit.broadcastMessage(key);
//            var oldValue = section.get(key);
//            key = key.replace(oldKey, newKey);
//            tournamenYaml.set(key, oldValue);
//                    //TODO: Clear out old tournament yaml and add all values to new
//        }
//
//        tournamenYaml.set(oldKey, null);
//
//        try {
//            tournamenYaml.save(tournamentFile);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        RefreshTournaments(tournamentYaml);
    }

    public static void RefreshTournaments(YamlConfiguration yaml){
        List<TournamentObject> tObjs = new ArrayList<>(); //Loads Active Tournaments from file

        for(final String key : yaml.getKeys(false)) {
            if(TournamentHandler.ActiveTournaments.stream().anyMatch(o -> o.getName().equals(key))){
                continue;
            }
            TournamentMode mode = TournamentMode.valueOf(yaml.getString(key + ".Mode"));
            TournamentType type = TournamentType.valueOf(yaml.getString(key + ".Type"));
            double duration = yaml.getDouble(key + ".Duration");
            String fishType = yaml.getString(key + ".Fish Type");
            boolean announceWinner = yaml.getBoolean(key + ".Announce New Winner");

            boolean useBossbar = yaml.getBoolean(key + ".Use Bossbar");
            boolean bossbarTime = yaml.getBoolean(key + ".Bossbar Timer");
            double bossbarPercent = yaml.getDouble(key + ".Bossbar Percent");
            double bossbarTimePercent = yaml.getDouble(key + ".Bossbar Timer Percent");
            int maxAmount = yaml.getInt(key + ".Max Amount");
            double startDelay = yaml.getDouble(key + ".Start Delay");
            double delay = yaml.getDouble(key + ".Cooldown");
            int minPlayers = yaml.getInt(key + ".Minimum Players");
            int minFish = yaml.getInt(key + ".Minimum Fish");

            String barString = yaml.getString(key + ".Bossbar Color");
            BarColor barColor = BarColor.PINK;

            if(barString != null && !barString.equals("")){
                barColor = BarColor.valueOf(barString.toUpperCase());
            }

            List<String> dayStrings = yaml.getStringList(key + ".Days");
            List<DayOfWeek> dayList = new ArrayList<>();
            for(var d : dayStrings){
                dayList.add(DayOfWeek.valueOf(d));
            }

            HashMap<String, List<String>> rewards = new HashMap<>();

            Map<String, Object> rewardsMap = yaml.getConfigurationSection(key + ".Rewards").getValues(false);
            for(final String spot : rewardsMap.keySet()) {
                List<String> items = yaml.getStringList(key + ".Rewards." + spot);
                rewards.put(spot.toUpperCase(), items);
            }

            List<TournamentObject> foundTourn = tObjs.stream()
                    .filter(t -> t.getName().equals(key)).toList();

            LocalDateTime lastRan = LocalDateTime.MIN;
            if(foundTourn.size() > 0){
                lastRan = foundTourn.get(0).getLastRan();
            }

            try {
                new TournamentHandler().AddTournament(new TournamentObject(
                        key, mode, duration, fishType, dayList, maxAmount, startDelay, minPlayers, useBossbar, bossbarPercent, barColor,
                        delay, rewards, minFish, bossbarTime, bossbarTimePercent, type, announceWinner, lastRan,
                        yaml.getBoolean(key + ".Discord Start")));
            }catch (Exception e){
                Bukkit.getLogger().warning("Unable to create fishing tournament: " + key);
            }
        }
    }
}
