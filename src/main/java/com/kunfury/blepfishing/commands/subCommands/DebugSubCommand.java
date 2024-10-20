package com.kunfury.blepfishing.commands.subCommands;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.commands.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

public class DebugSubCommand extends SubCommand {
    @Override
    public String getName() {
        return "debug";
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
        BlepFishing.instance.DebugMode = !BlepFishing.instance.DebugMode;
    }

    @Override
    public List<String> getArguments(CommandSender sender, String[] args) {
        return null;
    }

    @Override
    public String getPermissions() {
        return "bf.admin";
    }

    @Override
    public List<String> getAliases() {
        return List.of();
    }
}
