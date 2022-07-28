package com.kunfury.blepFishing.Plugins;

import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Objects.FishObject;
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

        if(params.length() <= 0) return "";

        String[] args = params.split(" ");

        switch(args[0].toUpperCase()){
            case "INFO":
                return "Blep Fishing was made by Kunfury!";
            case "TOP":
                return GetTopFish(args);
            default:
                return Variables.Prefix + "The input of: " + params + " is not recognized";
        }
    }

    private String GetTopFish(String[] args){
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

            switch(type.toUpperCase()){
                case "PLAYER":
                    returnString = fish.PlayerName;
                    break;
                case "FISH":
                    returnString = fish.Name;
                    break;
                case "SIZE":
                    returnString = fish.RealSize + "";
                    break;
                case "COST":
                    returnString = fish.RealCost + "";
                    break;
                case "RARITY":
                    returnString = fish.Rarity;
                    break;
                case "DATE":
                    returnString = fish.DateCaught.toString();
                    break;
                case "SCORE":
                    returnString = fish.Score + "";
                    break;
                default:
                    returnString = fish.PlayerName + " : " + fish.Name + " : " + fish.RealSize;
                    break;
            }

        }


        return returnString;
    }
}
