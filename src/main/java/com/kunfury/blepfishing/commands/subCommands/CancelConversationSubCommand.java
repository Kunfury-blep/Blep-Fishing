package com.kunfury.blepfishing.commands.subCommands;

import com.kunfury.blepfishing.commands.SubCommand;
import com.kunfury.blepfishing.helpers.Utilities;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CancelConversationSubCommand extends SubCommand {
    @Override
    public String getName() {
        return "CancelConversation";
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
        if(sender instanceof Player)
            MenuButton.CancelConversation((Player) sender);
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
        return List.of();
    }

    @Override
    public boolean showTabComplete() {
        return false;
    }
}
