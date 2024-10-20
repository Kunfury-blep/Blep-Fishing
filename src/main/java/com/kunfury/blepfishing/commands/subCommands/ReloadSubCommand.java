package com.kunfury.blepfishing.commands.subCommands;

import com.kunfury.blepfishing.commands.SubCommand;
import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ReloadSubCommand extends SubCommand {
    @Override
    public String getName() {
        return "Reload";
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
        ConfigHandler.instance.Reload();
        sender.sendMessage(Formatting.GetMessagePrefix() + Formatting.GetLanguageString("System.reload"));
    }

    @Override
    public List<String> getArguments(CommandSender sender, String[] args) {
        return List.of();
    }

    @Override
    public String getPermissions() {
        return "bf.admin";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }
}
