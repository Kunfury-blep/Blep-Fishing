package com.kunfury.blepfishing.plugins;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.objects.FishObject;
import com.kunfury.blepfishing.objects.FishType;
import com.kunfury.blepfishing.objects.TournamentObject;
import com.kunfury.blepfishing.objects.TournamentType;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
            case "TOURNAMENT" -> getTournament(args); //bf_top {tournamentId} {data}
            case "FISH" -> getFish(args); //bf_top {typeId} {playerId} {position} {data}
            case "TOTAL_CAUGHT" -> getTotalFish(args);
            default -> Formatting.GetMessagePrefix() + "The input of: %bf_" + params + "% is not recognized";
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

        if(args.length < 3) return "You need to provide the player ID who caught. 'All' to get All Fish";
        String playerId = args[2];

        int position = 0;
        if(args.length >= 4 && Formatting.isNumeric(args[3])){
            position = Integer.parseInt(args[3]) - 1;
        }

        if(typeId.equalsIgnoreCase("ALL")){
            typeId = null;
        }else{
            if(!FishType.IdExists(typeId)){
                return Formatting.GetLanguageString("PAPI.Fish.notFound");
            }
        }

        if(playerId.equalsIgnoreCase("ALL"))
            playerId = null;
        else if(!playerId.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")){
            var player = Bukkit.getPlayer(playerId);
            if(player != null)
                playerId = player.getUniqueId().toString();
            else
                return "Invalid Player ID";
        }

        List<FishObject> caughtFish;
        caughtFish = Database.Fish.GetAllCaughtOfType(typeId, playerId);


        if(position < 0 || caughtFish.size() - 1 < position) return "N/A";
        FishObject fish = caughtFish.get(position);

        if(fish == null) returnString = "Not Caught";
        else {
            String type = "";
            if(position == 0 && args.length == 3)
                type = args[2];
            else if(args.length >= 5) type = args[4];

            returnString = getFishInfo(fish, type);

        }
        return returnString;
    }

    private String getTournament(String[] args){
        if(!ConfigHandler.instance.tourneyConfig.Enabled()){
            return "Tournaments Disabled";
        }

        if(args.length < 2) return "You need to provide the ID of a tournament. 'Active' to get all running.";

        String tourneyTypeId = args[1];

        String property = "";
        if(args.length >= 3) property = args[2];

        if(tourneyTypeId.equalsIgnoreCase("ACTIVE")) {
            var activeTournaments = Database.Tournaments.GetActive();
            StringBuilder activeString = new StringBuilder();
            int i = 0;
            for(var t : activeTournaments){
                if(i > 0)
                    activeString.append(ChatColor.BLUE).append(" | ");
                activeString.append(ChatColor.WHITE).append(getTournamentInfo(t, property));

                i++;
            }

            return activeString.toString();
        }else{
            if(!TournamentType.IdExists(tourneyTypeId)){
                return Formatting.GetLanguageString("Tournament Not Found");
            }
        }

        var tournamentType = TournamentType.FromId(tourneyTypeId);
        assert tournamentType != null;
        return getTournamentTypeInfo(tournamentType, property);
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
            case "PLAYER" -> Formatting.GetLanguageString("PAPI.Fish.player")
                            .replace("{player}", Objects.requireNonNullElse(fish.getCatchingPlayerName(), "Not Found"));
            case "NAME" -> Formatting.GetLanguageString("PAPI.Fish.name")
                            .replace("{type}", fish.getType().Name);
            case "SIZE" -> Formatting.GetLanguageString("PAPI.Fish.size")
                    .replace("{size}", Formatting.DoubleFormat(fish.Length));
            case "COST" -> Formatting.GetLanguageString("PAPI.Fish.cost")
                    .replace("{cost}", Formatting.DoubleFormat(fish.Value));
            case "RARITY" -> Formatting.GetLanguageString("PAPI.Fish.rarity")
                    .replace("{rarity}", fish.getRarity().getFormattedName());
            case "DATE" -> Formatting.GetLanguageString("PAPI.Fish.date")
                    .replace("{date}", Formatting.dateToString(fish.DateCaught));
            case "SCORE" -> Formatting.GetLanguageString("PAPI.Fish.score")
                    .replace("{score}", String.valueOf(fish.Score));
            default -> Formatting.GetLanguageString("PAPI.Fish.default")
                    .replace("{player}", Objects.requireNonNullElse(fish.getCatchingPlayerName(), "Not Found"))
                    .replace("{type}", fish.getType().Name)
                    .replace("{size}", Formatting.DoubleFormat(fish.Length))
                    .replace("{cost}", Formatting.DoubleFormat(fish.Value))
                    .replace("{rarity}", fish.getRarity().getFormattedName())
                    .replace("{date}", Formatting.dateToString(fish.DateCaught))
                    .replace("{score}", String.valueOf(fish.Score));
        };
    }

    private String getTournamentInfo(TournamentObject tournament, String property) {
        return switch (property.toUpperCase()) {
            case "NAME" -> Formatting.GetLanguageString("PAPI.Tournament.name")
                    .replace("{name}", Formatting.formatColor(tournament.getType().Name));
            case "TIME" -> Formatting.GetLanguageString("PAPI.Tournament.time")
                    .replace("{time}", Formatting.asTime(tournament.getTimeRemaining()));
            case "FISH_TYPE" -> Formatting.GetLanguageString("PAPI.Tournament.fish_type")
                    .replace("{fish}", Formatting.ToCommaList(tournament.getType().getFormattedCatchList(), ChatColor.BLUE, ChatColor.WHITE));
            case "PROGRESS" -> Formatting.GetLanguageString("PAPI.Tournament.progress")
                    .replace("{progress}", Formatting.DoubleFormat(tournament.getProgress() * 100));
            case "WINNER" -> {
                var winningFish = tournament.getWinningFish();
                if (!winningFish.isEmpty()) {
                    yield Formatting.GetLanguageString("PAPI.Tournament.winner")
                            .replace("{player}", Objects.requireNonNullElse(winningFish.get(0).getCatchingPlayerName(), "Not Found"));

                } else
                    yield Formatting.GetLanguageString("PAPI.Tournament.noneCaught");
            }
            default -> Formatting.GetLanguageString("PAPI.Tournament.default")
                    .replace("{name}", tournament.getType().Name)
                    .replace("{fish}", Formatting.ToCommaList(tournament.getType().getFormattedCatchList(), ChatColor.BLUE, ChatColor.WHITE))
                    .replace("{time}", Formatting.asTime(tournament.getTimeRemaining()))
                    .replace("{progress}", Formatting.DoubleFormat(tournament.getProgress() * 100));
        };
    }

    private String getTournamentTypeInfo(TournamentType tournamentType, String property){
        TournamentObject activeTournament = Database.Tournaments.GetActiveOfType(tournamentType.Id);

        if(activeTournament != null)
            return getTournamentInfo(activeTournament, property);

        return switch (property.toUpperCase()){
            case "NAME" -> Formatting.GetLanguageString("PAPI.Tournament.name")
                    .replace("{name}", Formatting.formatColor(tournamentType.Name));
            case "FISH_TYPE" -> Formatting.GetLanguageString("PAPI.Tournament.fish_type")
                    .replace("{fish}", Formatting.ToCommaList(tournamentType.getFormattedCatchList(), ChatColor.BLUE, ChatColor.WHITE));
            default -> Formatting.GetLanguageString("PAPI.Tournament.default")
                    .replace("{tournament}", tournamentType.Name)
                    .replace("{fish}", Formatting.ToCommaList(tournamentType.getFormattedCatchList(), ChatColor.BLUE, ChatColor.WHITE))
                    .replace("{time}", Formatting.GetLanguageString("PAPI.Tournament.notRunning"));
        };

    }
}