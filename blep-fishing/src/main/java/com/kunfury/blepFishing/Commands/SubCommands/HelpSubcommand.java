package com.kunfury.blepFishing.Commands.SubCommands;

import com.kunfury.blepFishing.Commands.SubCommand;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HelpSubcommand extends SubCommand {
    @Override
    public String getName() {
        return "Help";
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
        String helpMessage = Formatting.getFormattedMesage("Player Panel.Help.title") + "\n"
                        + Formatting.getFormattedMesage("Player Panel.Help.lb") + "\n"
                        + Formatting.getFormattedMesage("Player Panel.Help.claim") + "\n";
        if(sender.hasPermission("bf.admin")) {
            helpMessage += Formatting.getFormattedMesage("Player Panel.Help.config") + "\n";
            helpMessage += Formatting.getFormattedMesage("Player Panel.Help.reload") + "\n";
            helpMessage += Formatting.getFormattedMesage("Player Panel.Help.getData") + "\n";
            helpMessage += Formatting.getFormattedMesage("Player Panel.Help.tourney") + "\n";
        }
        sender.sendMessage(helpMessage);
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
        return List.of("?");
    }
}
