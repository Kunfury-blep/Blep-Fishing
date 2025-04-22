package com.kunfury.blepfishing.plugins;

import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.plugins.placeholders.*;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ExpandPlaceholder extends PlaceholderExpansion {

    private final List<Placeholder> placeholders;

    public ExpandPlaceholder(){
        placeholders = List.of(
                new TournamentPlaceholder(),
                new TopPlaceholder(),
                new FishPlaceholder(),
                new TotalCaughtPlaceholder()
        );
    }


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
    public String onPlaceholderRequest(Player player, @NotNull String params){
        if(player == null) return "";

        if(params.isEmpty()) return "";

        String[] args = params.split(" ");

        for(var p : placeholders){
            if(p.Matches(args[0]))
                return p.getValue(args);
        }

        return Formatting.GetMessagePrefix() + "%bf_" + params + "% is not recognized";
    }
}