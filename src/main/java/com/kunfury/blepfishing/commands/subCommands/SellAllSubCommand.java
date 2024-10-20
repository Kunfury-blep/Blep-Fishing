package com.kunfury.blepfishing.commands.subCommands;

import com.kunfury.blepfishing.commands.SubCommand;
import com.kunfury.blepfishing.helpers.Utilities;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SellAllSubCommand extends SubCommand {
    @Override
    public String getName() {
        return "SellAll";
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
        Utilities.SellAllFish((Player) sender);
    }

    @Override
    public List<String> getArguments(CommandSender sender, String[] args) {
        return List.of();
    }

    @Override
    public String getPermissions() {
        return "bf.sellAll";
    }

    @Override
    public List<String> getAliases() {
        return List.of();
    }
}
