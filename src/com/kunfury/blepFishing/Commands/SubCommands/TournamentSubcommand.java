package com.kunfury.blepFishing.Commands.SubCommands;

import Tournament.Tournament;
import com.kunfury.blepFishing.Commands.SubCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class TournamentSubcommand extends SubCommand {
    @Override
    public String getName() {
        return "Tournament";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getSyntax() {
        return null;
    }

    @Override
    public void perform(@NotNull CommandSender sender, String[] args) {
        new Tournament().ShowTourney(sender);
    }

    @Override
    public List<String> getArguments(@NotNull CommandSender sender, String[] args) {
        return null;
    }

    @Override
    public String getPermissions() {
        return null;
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList( "TOURNAMENT", "TOURNEY", "TOURNEYS", "TOURNAMENTS", "T");
    }
}
