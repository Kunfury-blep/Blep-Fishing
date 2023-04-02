package com.kunfury.blepFishing.Plugins;

import com.kunfury.blepFishing.BlepFishing;
import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Objects.FishObject;
import com.kunfury.blepFishing.Tournament.TournamentHandler;
import com.kunfury.blepFishing.Tournament.TournamentObject;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

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
        return "1.0";
    }

    @Override
    public String onPlaceholderRequest(Player p, String params){
        if(p == null) return "";

        if(params.length() == 0) return "";

        String[] args = params.split(" ");

        return switch (args[0].toUpperCase()) {
            case "INFO" -> "Blep Fishing was made by Kunfury!";
            case "TOP" -> getTopFish(args);
            case "TOURNAMENT" -> getTournament(args);
            case "FISH" -> getFish(args);
            default -> "The input of: " + params + " is not recognized";
        };
    }

    private String getTopFish(String[] args){
        String fishName = "";
        int position = 0;
        String returnString = "";

        if(args.length < 2) return "You need to provide the type of fish";
        if(args.length >= 3) position = Integer.parseInt(args[2]) - 1;

        fishName = args[1];
        if(Variables.GetFishNames().stream().noneMatch(fishName::equalsIgnoreCase)){
            return "That type of fish does not exist";
        }

        List<FishObject> fishList = Objects.requireNonNull(Variables.getFishList((fishName)));
        if(position < 0 || fishList.size() - 1 < position) return "N/A";
        FishObject fish = fishList.get(position);

        if(fish == null) returnString = "Not Caught";
        else {
            String type = "";
            if(args.length >= 4) type = args[3];

            returnString = switch (type.toUpperCase()) {
                case "PLAYER" -> fish.PlayerName;
                case "FISH" -> fish.Name;
                case "SIZE" -> Formatting.DoubleFormat(fish.RealSize) + "";
                case "COST" -> fish.RealCost + "";
                case "RARITY" -> fish.Rarity;
                case "DATE" -> fish.DateCaught.toString();
                case "SCORE" -> Formatting.DoubleFormat(fish.Score) + "";
                default -> fish.PlayerName + " : " + fish.Name + " : " + fish.RealSize;
            };

        }
        return returnString;
    }

    private String getTournament(String[] args){
        if(!BlepFishing.configBase.getEnableTournaments()){
            return Formatting.getMessage("Tournament.inactive");
        }

        if(args.length < 2) return "You need to provide the name of the tournament.";

        StringBuilder tourneyName = new StringBuilder();
        for(int i = 2; i < args.length; i++){
            if(tourneyName.length() > 0)
                tourneyName.append(" ");
            tourneyName.append(args[i]);
        }
        //Color Formatting required for compatibility with plugins using placeholderAPI that automatically color translate
        tourneyName = new StringBuilder(Formatting.formatColor(tourneyName.toString()));
        TournamentObject t = null;

        for(var f : TournamentHandler.TournamentList){
            if (Formatting.formatColor(f.getName()).equalsIgnoreCase(tourneyName.toString())){
                t = f;
                break;
            }
        }

        if(t == null)
            return "No tournament found with that name.";

        String response = "";

        String type = "";
        if(args.length >= 4) type = args[1];

        switch (type.toUpperCase()) {
            case "NAME" -> response = Formatting.formatColor(t.getName());
            case "TIME" ->{
                if(t.isRunning())
                    response = Formatting.asTime(t.getTimeRemaining());
                else
                    response = Formatting.getMessage("PAPI.Tournament.notRunning");


            }
            case "FISH_TYPE" -> response = t.getFishType();
            case "PROGRESS" -> response = Formatting.DoubleFormat(t.getProgress() * 100);
            case "WINNER" -> {
                if(t.isRunning()){
                    if(t.getBestFish() != null){
                        response = t.getBestFish().getPlayer().getName();
                    }else
                        response = Formatting.getMessage("PAPI.Tournament.noneCaught");
                }
            }
            default -> {
                if(t.isRunning())
                    response = Formatting.getMessage("PAPI.Tournament.default")
                            .replace("{time}", Formatting.asTime(t.getTimeRemaining()));
                else
                    response = Formatting.getMessage("PAPI.Tournament.notRunning");
            }
        }


        response = response.replace("{tournament}", tourneyName.toString())
                            .replace("{fish}", t.getFishType());


        return Formatting.formatColor(response);
    }

    private String getFish(String[] args){
        if(args.length < 3){
            return "Invalid Arguments";
        }

        String response = "";

        FishObject fish = FishObject.getById(args[2]);

        if(fish == null){
            return Formatting.getMessage("PAPI.Fish.notFound");
        }

        String type = args[1];

        response = switch (type.toUpperCase()) {
            case "NAME" -> fish.getName();
            case "RARITY" -> fish.getRarity();
            case "SIZE" -> fish.getFormattedSize();
            case "VALUE" -> fish.getValue();
            case "DATE" -> fish.DateCaught.toString();
            case "PLAYER" -> fish.getPlayer().getName();
            case "SCORE" -> Formatting.DoubleFormat(fish.getScore());
            default -> "Invalid Type";
        };

        return Formatting.formatColor(response);
    }

}
