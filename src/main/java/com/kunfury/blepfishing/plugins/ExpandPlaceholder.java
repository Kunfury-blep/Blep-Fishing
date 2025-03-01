package com.kunfury.blepfishing.plugins;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.objects.FishObject;
import com.kunfury.blepfishing.objects.FishType;
import com.kunfury.blepfishing.objects.TournamentObject;
import com.kunfury.blepfishing.objects.TournamentType;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ExpandPlaceholder extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "bf";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Kunfury";
    }

    @Override
    public @NotNull String getVersion() {
        return "2.0";
    }

    @Override
    public @NotNull String getName() {
        return "Blep Fishing";
    }

    @Override
    public String onPlaceholderRequest(Player p, @NotNull String params){
        if(p == null) return "";

        if(params.isEmpty()) return "";

        String[] args = params.split(" ");

        return switch (args[0].toUpperCase()) {
            case "INFO" -> "Blep Fishing was made by Kunfury!";
            case "TOP" -> getTopFish(args);
            case "TOURNAMENT" -> getTournament(args);
            case "FISH" -> getFish(args);
            case "TOTAL_CAUGHT" -> getTotalFish(args);
            default -> Formatting.GetMessagePrefix() + "The input of: " + params + " is not recognized";
        };
    }

    private String getTotalFish(String[] args){
        String pUuid = null;
        if(args.length >= 2)
            pUuid = args[1];

        return String.format("%,d", Database.Fish.GetTotalCatchAmount(pUuid));
    }

    /**
     * @param args Content of Chat Message
     * @return
     */
    private String getTopFish(String[] args){

        String returnString = "";

        if(args.length < 2) return "You need to provide the type of fish";
        String typeId = args[1];

        int position = 0;
        if(args.length >= 3) position = Integer.parseInt(args[2]) - 1;


        if(!FishType.IdExists(typeId)){
            return Formatting.GetLanguageString("PAPI.Fish.notFound");
        }

        List<FishObject> caughtFish = Database.Fish.GetAllCaughtOfType(typeId);
        if(position < 0 || caughtFish.size() - 1 < position) return "N/A";
        FishObject fish = caughtFish.get(position);

        if(fish == null) returnString = "Not Caught";
        else {
            String type = "";
            if(args.length >= 4) type = args[3];

            returnString = getFishInfo(fish, type);

        }
        return returnString;
    }

    private String getTournament(String[] args){
        if(!ConfigHandler.instance.tourneyConfig.Enabled()){
            return "Tournaments Disabled";
        }

        if(args.length < 2) return "You need to provide the ID of a tournament.";

        String tourneyId = args[1];

        var tournamentType = TournamentType.FromId(tourneyId);
        if(tournamentType == null){
            return "No tournament found with that ID";
        }

        TournamentObject activeTournament = Database.Tournaments.GetActiveOfType(tourneyId);

        String type = "";
        if(args.length >= 3) type = args[2];

        switch (type.toUpperCase()) {
            case "NAME" -> {
                return Formatting.formatColor(tournamentType.Name);
            }
            case "TIME" ->{
                if(activeTournament != null)
                    return Formatting.asTime(activeTournament.getTimeRemaining(), ChatColor.WHITE);
                else
                    return Formatting.GetLanguageString("PAPI.Tournament.notRunning");
            }
            case "FISH_TYPE" -> {
                return tournamentType.FishTypeIds.toString();
            }
            case "PROGRESS" -> {
                if(activeTournament != null)
                    return Formatting.DoubleFormat(activeTournament.getProgress() * 100);
                else
                    return Formatting.GetLanguageString("PAPI.Tournament.notRunning");
            }
            case "WINNER" -> {
                if(activeTournament != null){
                    var winningFish = activeTournament.getWinningFish();
                    if(!winningFish.isEmpty()){
                        return winningFish.get(0).getCatchingPlayer().getName();
                    }else
                        return Formatting.GetLanguageString("PAPI.Tournament.noneCaught");
                }
            }
            default -> {
                if(activeTournament != null)
                    return Formatting.GetLanguageString("PAPI.Tournament.default")
                            .replace("{time}", Formatting.asTime(activeTournament.getTimeRemaining()));
                else
                    return Formatting.GetLanguageString("PAPI.Tournament.notRunning");
            }
        }

        return "N/A";
    }

    private String getFish(String[] args){
        if(args.length < 3){
            return "Invalid Arguments";
        }

        String response = "";

        FishObject fish = Database.Fish.Get(Integer.parseInt(args[2]));

        if(fish == null){
            return Formatting.GetLanguageString("PAPI.Fish.notFound");
        }

        String type = args[1];

        response = getFishInfo(fish, type);

        return Formatting.formatColor(response);
    }

    private String getFishInfo(FishObject fish, String property){
        return switch (property.toUpperCase()) {
            case "PLAYER" -> fish.getCatchingPlayer().getName();
            case "NAME" -> fish.getType().Name;
            case "SIZE" -> Formatting.DoubleFormat(fish.Length);
            case "COST" -> Formatting.DoubleFormat(fish.Value);
            case "RARITY" -> fish.getRarity().Name;
            case "DATE" -> fish.DateCaught.toString();
            case "SCORE" -> fish.Score + "";
            default -> //Returns Collection of Default Info
                    fish.getCatchingPlayer().getName() + " : "
                            + fish.getType().Name + " : "
                            + fish.getRarity().Name + " : "
                            + Formatting.DoubleFormat(fish.Length) + "\"";
        };
    }
}