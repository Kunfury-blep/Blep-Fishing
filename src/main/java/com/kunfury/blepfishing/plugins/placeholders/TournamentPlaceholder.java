package com.kunfury.blepfishing.plugins.placeholders;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.objects.TournamentObject;
import com.kunfury.blepfishing.objects.TournamentType;
import org.bukkit.ChatColor;

import java.util.Objects;

public class TournamentPlaceholder extends  Placeholder{
    @Override
    public String getName() {
        return "Tournament";
    }

    @Override
    public String getValue(String[] args) {
        if(!ConfigHandler.instance.tourneyConfig.Enabled()){
            return "Tournaments Disabled";
        }

        if(args.length < 2) return getDefault();

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
                activeString.append(ChatColor.WHITE).append(getTournamentInfo(t, args));

                i++;
            }

            return activeString.toString();
        }else{
            if(!TournamentType.IdExists(tourneyTypeId)){
                return Formatting.GetLanguageString("Papi.Tournament.notFound");
            }
        }

        var tournamentType = TournamentType.FromId(tourneyTypeId);
        assert tournamentType != null;
        return getTournamentTypeInfo(tournamentType, args);
    }

    @Override
    public String getDefault() {
        return "%bf_tournament <Tournament ID> {NAME, TIME, FISH_TYPE, PROGRESS, WINNER}%";
    }

    private String getTournamentInfo(TournamentObject tournament, String[] args) {

        String property = args[2];

        if(args.length >= 3) property = args[2];


        return switch (property.toUpperCase()) {
            case "NAME" -> Formatting.GetLanguageString("PAPI.Tournament.name")
                    .replace("{tournament}", Formatting.formatColor(tournament.getType().Name));
            case "TIME" -> Formatting.GetLanguageString("PAPI.Tournament.time")
                    .replace("{time}", Formatting.asTime(tournament.getTimeRemaining()));
            case "FISH_TYPE" -> Formatting.GetLanguageString("PAPI.Tournament.fish_type")
                    .replace("{fish}", Formatting.ToCommaList(tournament.getType().getFormattedCatchList(), ChatColor.BLUE, ChatColor.WHITE));
            case "PROGRESS" -> Formatting.GetLanguageString("PAPI.Tournament.progress")
                    .replace("{progress}", Formatting.DoubleFormat(tournament.getProgress() * 100));
            case "WINNER" -> {
                var winningFish = tournament.getWinningFish();

                int position = 0;
                if(args.length >= 4 && Formatting.isNumeric(args[3])){
                    position = Integer.parseInt(args[3]) - 1;

                    if(position < 0)
                        position = 0;
                }

                if (winningFish.size() > position) {
                    yield Formatting.GetLanguageString("PAPI.Tournament.winner")
                            .replace("{player}", Objects.requireNonNullElse(winningFish.get(position).getCatchingPlayerName(), "Not Found"));

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

    private String getTournamentTypeInfo(TournamentType tournamentType, String[] args){

        String property = args[2];

        TournamentObject activeTournament = Database.Tournaments.GetActiveOfType(tournamentType.Id);

        if(activeTournament != null)
            return getTournamentInfo(activeTournament, args);

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
