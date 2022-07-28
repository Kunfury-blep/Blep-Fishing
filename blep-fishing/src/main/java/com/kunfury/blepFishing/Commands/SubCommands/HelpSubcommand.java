package com.kunfury.blepFishing.Commands.SubCommands;

import com.kunfury.blepFishing.Commands.CommandManager;
import com.kunfury.blepFishing.Commands.SubCommand;
import com.kunfury.blepFishing.Config.Variables;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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
        String helpMessage = ChatColor.translateAlternateColorCodes('&',
                "                     " + Variables.getMessage("helpTitle") + "\n"
                        + Prefix + "/bf lb <fishname> - " + Variables.getMessage("leaderboardHelp") + "\n"
                        + Prefix + "/bf reload - " + Variables.getMessage("reloadHelp") + "\n"
                        + Prefix + "/bf fish - " + Variables.getMessage("fishHelp") + "\n"
                        + Prefix + "/bf claim - " + Variables.getMessage("claimHelp") + "\n"
                        + Prefix + "/bf tourney - " + Variables.getMessage("tourneyHelp") + "\n"
                        + Prefix + "/bf admin - " + Variables.getMessage("adminHelp") + "\n");
        if(sender.hasPermission("bf.admin")) helpMessage += Prefix + "/bf config - " + Variables.getMessage("configHelp") + "\n";

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
