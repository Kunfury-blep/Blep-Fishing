package com.kunfury.blepfishing.commands.subCommands;

import com.kunfury.blepfishing.commands.SubCommand;
import com.kunfury.blepfishing.database.Database;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ToggleBossBarSubCommand extends SubCommand {
    @Override
    public String getName() {
        return "ToggleBossBar";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getSyntax() {
        return "";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        Player player = (Player)sender;
        for(var t : Database.Tournaments.GetActive()){
            t.ToggleBossBar(player);
        }
    }

    @Override
    public List<String> getArguments(CommandSender sender, String[] args) {
        return List.of();
    }

    @Override
    public String getPermissions() {
        return "";
    }

    @Override
    public List<String> getAliases() {
        return List.of("bossbar");
    }
}
