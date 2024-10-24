package com.kunfury.blepfishing.commands.subCommands;

import com.kunfury.blepfishing.commands.SubCommand;
import com.kunfury.blepfishing.ui.panels.admin.AdminPanel;
import com.kunfury.blepfishing.ui.panels.player.PlayerTournamentPanel;
import org.bukkit.command.CommandSender;

import java.util.List;

public class TournamentsSubCommand extends SubCommand {
    @Override
    public String getName() {
        return "Tournaments";
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
        PlayerTournamentPanel panel = new PlayerTournamentPanel();
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
        return List.of("T", "Tourney", "Tourneys", "Tournament");
    }
}
