package com.kunfury.blepFishing.Commands.SubCommands;

import com.kunfury.blepFishing.Commands.CommandManager;
import com.kunfury.blepFishing.Commands.SubCommand;
import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.kunfury.blepFishing.Config.Variables.Prefix;

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
        String helpMessage = Formatting.getMessage("Player Panel.Help.title") + "\n"
                        + Prefix + Formatting.getMessage("Player Panel.Help.lb") + "\n"
                        + Prefix + Formatting.getMessage("Player Panel.Help.claim") + "\n";
        if(sender.hasPermission("bf.admin")) {
            helpMessage += Prefix + Formatting.getMessage("Player Panel.Help.config") + "\n";
            helpMessage += Prefix + Formatting.getMessage("Player Panel.Help.reload") + "\n";
            helpMessage += Prefix + Formatting.getMessage("Player Panel.Help.getData") + "\n";
            helpMessage += Prefix + Formatting.getMessage("Player Panel.Help.tourney") + "\n";
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
