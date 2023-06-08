package com.kunfury.blepFishing.Plugins;

import com.kunfury.blepFishing.BlepFishing;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Objects.FishObject;
import com.kunfury.blepFishing.Tournament.TournamentObject;
import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.util.DiscordUtil;
import org.bukkit.Bukkit;
import java.util.Arrays;

public class DiscordSRVHandler {

    public static void Load(){
        if(!BlepFishing.configBase.getEnableDiscordSRV()) return;
    }

    public static void FishCaught(FishObject fish){
        if(!BlepFishing.configBase.getEnableDiscordSRV()) return;

        SendMessage(fish.getPlayer().getName() + " just caught a " + fish.getName() + "!");
    }


    public static void SendMessage(String message){
        //TODO: Hook into messages.yml
        //TODO: Build wiki page
        //DiscordUtil.sendMessageBlocking(DiscordSRV.getPlugin().getMainTextChannel(), message);
    }

    public static void SendTournamentStart(TournamentObject tourney){
        String message = Formatting.getMessage("Discord.tournamentStart")
                .replace("{name}", tourney.getName())
                .replace("{fish}", tourney.getFishType());

        message = Formatting.formatColor(message);

        DiscordUtil.sendMessageBlocking(DiscordSRV.getPlugin().getMainTextChannel(), message);
    }

    public static void SendTournamentEnd(TournamentObject tourney){
        String message = Formatting.getMessage("Discord.tournamentEnd")
                .replace("{name}", tourney.getName())
                .replace("{fish}", tourney.getFishType());

        message = Formatting.formatColor(message);

        DiscordUtil.sendMessageBlocking(DiscordSRV.getPlugin().getMainTextChannel(), message);
    }
}
