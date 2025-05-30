package com.kunfury.blepfishing.objects;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.helpers.Utilities;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class TournamentObject {
    public final int Id;
    public final String TypeId;
    public final LocalDateTime StartTime;
    public boolean ConfirmCancel;

    private FishObject bestFish;


    public TournamentObject(TournamentType type){
        TypeId = type.Id;
        StartTime = LocalDateTime.now();
        active = true;
        Id = Database.Tournaments.Add(this);
        StartTimer(this);
        SetupBossBar();
    }

    public TournamentObject(ResultSet rs) throws SQLException {
        Id = rs.getInt("id");
        TypeId = rs.getString("typeId");
        StartTime = Utilities.TimeFromLong(rs.getLong("startTime"));

        if(getType() == null){ //Disables the tournament if invalid type
            active = false;
            Database.Tournaments.Update(Id, "active", false);
            return;
        }

        active = rs.getBoolean("active");

        StartTimer(this);
        SetupBossBar();
    }

    BossBar bossBar;
    public void SetupBossBar(){
        if(!getType().HasBossBar)
            return;

        bossBar = Bukkit.createBossBar(Formatting.formatColor(getType().Name), getType().BossBarColor, BarStyle.SEGMENTED_10);
        bossBar.setVisible(false);

        for(var p : Bukkit.getOnlinePlayers()){
            bossBar.addPlayer(p);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                bossBar.setVisible(true);
                String title = getType().Name + ChatColor.WHITE + " - " + Formatting.asTime(getTimeRemaining(), ChatColor.WHITE);
                bossBar.setTitle(title);
                bossBar.setProgress(getProgress());
            }

        }.runTaskTimer(BlepFishing.getPlugin(), 0, 20);
    }

    public BossBar GetBossbar(){
        if(bossBar == null)
            SetupBossBar();
        return bossBar;
    }

    public void RefreshBossBar(){
        if(ConfigHandler.instance.tourneyConfig.Enabled() && getType().HasBossBar){
            for(var player : Bukkit.getOnlinePlayers()){
                if(playerBossBars.getOrDefault(player.getUniqueId(), false))
                    bossBar.addPlayer(player);
            }
        }else{
            for(var player : bossBar.getPlayers()){
                bossBar.removePlayer(player);
            }
        }
    }

    private final Map<UUID, Boolean> playerBossBars = new HashMap<>();
    public void ToggleBossBar(Player player){
        if(!getType().HasBossBar)
            return;

        UUID pUUID = player.getUniqueId();
        //Bukkit.broadcastMessage("Toggling BossBar. Contains: " + playerBossBars.containsKey(pUUID));
        boolean show = !(playerBossBars.getOrDefault(pUUID, false));

        playerBossBars.put(pUUID, show);

        if(show)
            GetBossbar().addPlayer(player);
        else
            GetBossbar().removePlayer(player);
    }

    public double getProgress(){
        Duration d = Duration.between(StartTime, LocalDateTime.now());
        double elapsed = d.toSeconds() / 60.0 / 60.0;
        double progress = elapsed/getType().Duration;
        if(progress > 1) progress = 1;
        return progress;
    }

    //Handles persistence of bossBar state for player across sessions
    public void BossBarJoin(Player player){
        if(!getType().HasBossBar)
            return;

        if(!playerBossBars.containsKey(player.getUniqueId())){
            ToggleBossBar(player);
            return;
        }

        if(playerBossBars.get(player.getUniqueId()))
            GetBossbar().addPlayer(player);

    }

    public void DisableBossBar(){
        if(bossBar != null)
            bossBar.removeAll();
    }



    private boolean active;
    public boolean Active(){
        return active;
    }

    private TournamentType type;
    public TournamentType getType(){
        if(type == null){
            type = TournamentType.FromId(TypeId);
        }
        return type;
    }

    public List<FishObject> getWinningFish(){
        List<FishObject> winningFish = new ArrayList<>();
        //Bukkit.broadcastMessage("Checking Winning Fish");

        for(var fish : Database.Tournaments.GetWinningFish(this)){
            int maxPlace = winningFish.size() + 1;

            //Bukkit.broadcastMessage("Checking winning fish for position " + maxPlace);

            //Ensures that a reward exists, either item or currency
            if(!type.ItemRewards.containsKey(maxPlace) && !type.CashRewards.containsKey(maxPlace))
                break;

            //Ignores any fish that were caught by someone already winning
            if(winningFish.stream().anyMatch(f -> f.PlayerId.equals(fish.PlayerId))){
                //Bukkit.broadcastMessage("Duplicate Player Found For " + maxPlace);
                continue;
            }

            winningFish.add(fish);
        }

        return winningFish;
    }

    private void checkAgainstWinner(FishObject fish){
        if(bestFish != null && bestFish.Length > fish.Length)
            return;

        if(bestFish == null || bestFish.getCatchingPlayer() != fish.getCatchingPlayer()){
            TextComponent mainComponent = new TextComponent(Formatting.formatColor(Formatting.GetLanguageString("Tournament.newBest")
                    .replace("{player}", fish.getCatchingPlayer().getName())
                    .replace("{tournament}", getType().Name)
                    .replace("{rarity}", fish.getRarity().getFormattedName())
                    .replace("{fish}", fish.getType().Name)));
            mainComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, fish.getHoverText()));


            Utilities.Announce(mainComponent);
        }

        bestFish = fish;
    }

    public void Cancel(){
        if(!active)
            return;

        active = false;
        Database.Tournaments.Update(Id, "active", false);

        if(bossBar != null)
            bossBar.removeAll();
    }

    public void Finish(){
        if(!active)
            return;

        active = false;
        Database.Tournaments.Update(Id, "active", false);

        if(bossBar != null)
            bossBar.removeAll();

        List<FishObject> winningFish = getWinningFish();

        if(winningFish.isEmpty()){
            var fishTypes = getType().getFishTypes();
            String fishName = (fishTypes.size() == 1) ? fishTypes.get(0).Name : "fish"; //Returns name of fish if only single type

            Utilities.Announce(Formatting.GetFormattedMessage("Tournament.noneCaught")
                    .replace("{fish}", fishName));
            return;
        }

        int pLength = 15; //Initializes the size of the chatbox
        List<TextComponent> textComponents = new ArrayList<>();

        //Reward Handling
        int place = 1;
        for(var fish : winningFish){
            FishType fishType = fish.getType();
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(fish.PlayerId);

            type.GiveRewards(place, fish.PlayerId);

            String pString = Formatting.FixFontSize(offlinePlayer.getName(), pLength);
            String lbString = Formatting.FixFontSize("#" + place + " - ", 4) + pString + fish.getRarity().Name + " " + fishType.Name;
            TextComponent mainComponent = new TextComponent(Formatting.formatColor(lbString));
            mainComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, fish.getHoverText()));
            textComponents.add(mainComponent);
            place++;
        }

        //Creates announcement banner for all players
        String banner = Formatting.GetLanguageString("Tournament.leaderboard")
                .replace("{tournament}", getType().Name);
        Utilities.Announce("_____________________________________________________");
        Utilities.Announce(banner);
        for(var c : textComponents){
            Utilities.Announce(c);
        }
        Utilities.Announce("_____________________________________________________");
    }

    private LocalDateTime endTime;
    public LocalDateTime getEndTime(){
        if(endTime == null){
            long seconds = (long) (getType().Duration * 60 * 60);
            endTime = StartTime.plusSeconds(seconds);
            //Bukkit.getLogger().warning("Start Time: " + StartTime + " - Duration: " + getType().Duration + " - End Time: " + endTime);
        }
        return endTime;
    }

    public Long getTimeRemaining(){
        return ChronoUnit.MILLIS.between(LocalDateTime.now(), getEndTime());
    }

    public boolean CanFinish(){
        return active && LocalDateTime.now().isAfter(getEndTime());
    }




    ///
    //Static Methods
    ///
    public static void CheckActive(){
        //Bukkit.broadcastMessage("Checking Active Tournaments");
        for(var t : Database.Tournaments.GetActive()){
            if(t.CanFinish()){
                t.Finish();
            }
        }
    }

    private static final HashMap<Integer, TournamentObject> TournamentTimers = new HashMap<>();
    public static boolean StartTimer(TournamentObject tournament){
        if(TournamentTimers.containsKey(tournament.Id))
            return false;



        TournamentTimers.put(tournament.Id, tournament);

        var seconds = ChronoUnit.SECONDS.between(LocalDateTime.now(), tournament.getEndTime()) + 2;
        new BukkitRunnable() {
            @Override
            public void run() {
                if(tournament.CanFinish())
                    tournament.Finish();

                TournamentTimers.remove(tournament.Id);
            }

        }.runTaskLater(BlepFishing.getPlugin(), seconds * 20);
        return  true;
    }

    public static void CheckWinning(FishObject fish){
        FishType fishType = fish.getType();

        for(var t : Database.Tournaments.GetActive()){
            if(!t.getType().getFishTypes().contains(fishType))
                continue;

            t.checkAgainstWinner(fish);
        }
    }
}
