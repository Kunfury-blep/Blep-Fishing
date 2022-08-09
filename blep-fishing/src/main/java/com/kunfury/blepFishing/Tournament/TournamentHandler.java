package com.kunfury.blepFishing.Tournament;

import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Setup;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.kunfury.blepFishing.Config.Variables.Prefix;

public class TournamentHandler {

    public static List<TournamentObject> TournamentList = new ArrayList<>();
    public static List<TournamentObject> ActiveTournaments = new ArrayList<>();
    public static HashMap<TournamentObject, BossBar> BossBars = new HashMap<>();
    public static List<Player> FishingPlayers = new ArrayList<>();

    public static void Reset(){
        ActiveTournaments = new ArrayList<>();
        TournamentList = new ArrayList<>();
        BossBars.forEach((key, value) -> {
            value.removeAll();
            value.setVisible(false);
        });
        BossBars.clear();
        new TournamentHandler().UpdateBossbars();
    }

    public void AddTournament(TournamentObject tournament){
        TournamentList.add(tournament);
        RunChecker();
    }

    private static boolean running;
    private void RunChecker(){
        if(running) return;
        running = true;
        new BukkitRunnable() {
            @Override
            public void run() {
                for(var t: TournamentList){
                    //Bukkit.broadcastMessage("Checking: " + t.getName());

                    if(!ActiveTournaments.contains(t) && t.canRun()){
                        Start(t);
                    }else{
                        if(ActiveTournaments.contains(t) && t.isComplete()) Finish(t);
                    }

                }
            }

        }.runTaskTimer(Setup.getPlugin(), 0, 60);
    }

    public void Start(TournamentObject t){
        if(ActiveTournaments.contains(t)){
            return;
        }
        ActiveTournaments.add(t);
        String announcement = Prefix + Formatting.getMessage("Tournament.start")
                .replace("{tournament}", t.getName());

        announcement = Formatting.formatColor(announcement);

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(announcement);
        }

        SaveActive();
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
    }

    public void Cancel(TournamentObject t){
        ActiveTournaments.remove(t);
        t.FinishEvent();
        SaveActive();
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

        }.runTaskTimer(Setup.getPlugin(), 0, 20);
    }

    public void Finish(TournamentObject t){
        ActiveTournaments.remove(t);
        SaveActive();
        t.FinishEvent();

        if(t.getFish().size() == 0){
            String tempName = t.getFishType().toLowerCase();
            if(tempName.equalsIgnoreCase("ALL")) tempName = "fish";
            Bukkit.broadcastMessage(Prefix + Formatting.getMessage("Tournament.noneCaught")
                    .replace("{fish}", tempName));
            return;
        }

        int pLength = 15; //Initializes the size of the chatbox
        List<TextComponent> textComponents = new ArrayList<>();

        t.getWinners().forEach((rank, fish) -> {
            String pString = Formatting.FixFontSize(fish.PlayerName, pLength);
            String lbString = Formatting.FixFontSize(rank + ".", 4)+ pString + fish.Rarity + " " + fish.Name;
            TextComponent mainComponent = new TextComponent (Formatting.formatColor(lbString));
            mainComponent.setHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT, fish.GetHoverText()));
            textComponents.add(mainComponent);
        });

        String banner =  Formatting.getMessage("Tournament.noneCaught");
        for(Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(" ");
            p.sendMessage(banner);
            for(var c : textComponents){
                p.spigot().sendMessage(c);
            }
            p.sendMessage(" ");
        }



        new Rewards().Generate(t);


    }

    private void SaveActive(){
        try {
            String tourneyPath = Setup.dataFolder + "/Data" + "/tournaments.data";
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(tourneyPath));
            output.writeObject(ActiveTournaments);
            output.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void ShowBars(Player p){
        if(!FishingPlayers.contains(p)){
            FishingPlayers.add(p);
            BossBars.forEach((key, bar) -> {
                bar.addPlayer(p);
            });
        }
    }
}
