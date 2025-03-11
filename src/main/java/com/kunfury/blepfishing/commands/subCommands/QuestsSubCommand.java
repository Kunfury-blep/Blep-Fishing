package com.kunfury.blepfishing.commands.subCommands;

import com.kunfury.blepfishing.commands.SubCommand;
import com.kunfury.blepfishing.ui.panels.player.quests.PlayerQuestPanel;
import com.kunfury.blepfishing.ui.panels.player.tournaments.PlayerTournamentPanel;
import org.bukkit.command.CommandSender;

import java.util.List;

public class QuestsSubCommand extends SubCommand {
    @Override
    public String getName() {
        return "Quests";
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
        PlayerQuestPanel panel = new PlayerQuestPanel();
        panel.Show(sender);
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
        return List.of("Q", "Quest");
    }
}
