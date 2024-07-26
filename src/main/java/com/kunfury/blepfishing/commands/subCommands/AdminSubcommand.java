package com.kunfury.blepfishing.commands.subCommands;

import com.kunfury.blepfishing.commands.CommandManager;
import com.kunfury.blepfishing.commands.SubCommand;
import com.kunfury.blepfishing.ui.panels.admin.AdminPanel;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdminSubcommand extends SubCommand {
    @Override
    public String getName() {
        return "Admin";
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
        if(!sender.hasPermission("bf.admin")){
            new CommandManager().NoPermission(sender);
            return;
        }

        if(!(sender instanceof Player)){
            return;
        }
        AdminPanel menu = new AdminPanel();
        menu.Show(sender);
    }

    @Override
    public List<String> getArguments(@NotNull CommandSender sender, String[] args) {
        return null;
    }

    @Override
    public String getPermissions() {
        return "bf.admin";
    }

    @Override
    public List<String> getAliases() {return List.of("A");}

}
