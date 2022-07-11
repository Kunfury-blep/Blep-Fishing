package com.kunfury.blepFishing.Tournament;

import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Endgame.EndgameVars;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Setup;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class TournamentHandler {

    public static List<TournamentObject> TournamentList = new ArrayList<>();
    public static List<TournamentObject> ActiveTournaments = new ArrayList<>();

    public void Load(){
        //TODO: Pull tournament.config
    }

    public void AddTournament(TournamentObject tournament){
        TournamentList.add(tournament);
        RunChecker();
        //TODO: Save actively running tournaments
        //TODO: Check that similar tournament is not running before starting

    }

    private static boolean running;
    private void RunChecker(){
        if(running) return;
        running = true;
        new BukkitRunnable() {
            @Override
            public void run() {
                LocalDateTime dt = LocalDateTime.now();
                for(var t: TournamentList){
                    if(!ActiveTournaments.contains(t)){
                        if(t.canRun()){
                            ActiveTournaments.add(t);
                            SaveActive();
                        }
                    }else{
                        if(t.isComplete()) FinishTournament(t);
                    }


                }


            }

        }.runTaskTimer(Setup.getPlugin(), 0, 20);
    }

    public void FinishTournament(TournamentObject t){
        ActiveTournaments.remove(t);
        t.Complete();

        t.Rewards.forEach((key, value) -> {
            if(Formatting.isNumeric(key)){

            }
        });
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
}
