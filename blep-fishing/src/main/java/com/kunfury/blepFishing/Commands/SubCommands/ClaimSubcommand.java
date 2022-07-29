package com.kunfury.blepFishing.Commands.SubCommands;

import com.kunfury.blepFishing.Commands.SubCommand;
import com.kunfury.blepFishing.Tournament.Rewards;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ClaimSubcommand extends SubCommand {
    @Override
    public String getName() {
        return "Claim";
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
        new Rewards().Claim((Player) sender);
        //new TournamentRewards().GetRewards(sender);
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
        return null;
    }
}
