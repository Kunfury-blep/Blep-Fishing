package com.kunfury.blepfishing.plugins.placeholders;

import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.objects.FishObject;
import com.kunfury.blepfishing.objects.FishType;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.Objects;

public class TopPlaceholder extends Placeholder{
    @Override
    public String getName() {
        return "TOP";
    }

    @Override
    public String getValue(String[] args) {
        String returnString = "";

        if(args.length < 2) return "You need to provide the type of fish";
        String typeId = args[1];

        int position = 0;
        if(args.length >= 3 && Formatting.isNumeric(args[2])){
            position = Integer.parseInt(args[2]) - 1;
        }

        String playerId = "ALL";
        if(args.length >= 4)
            playerId = args[3];



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

    @Override
    public String getDefault() {
        return "%bf_Top <Fish Type ID} <Position> {PLAYER, FISH, SIZE, COST, RARITY, DATE, SCORE}%";
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
}
