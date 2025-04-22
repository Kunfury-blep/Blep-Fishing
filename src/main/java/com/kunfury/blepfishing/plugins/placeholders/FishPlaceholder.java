package com.kunfury.blepfishing.plugins.placeholders;

import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.objects.FishObject;

import java.util.Objects;

public class FishPlaceholder extends Placeholder{
    @Override
    public String getName() {
        return "TOP";
    }

    @Override
    public String getValue(String[] args) {
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

    @Override
    public String getDefault() {
        return "";
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
