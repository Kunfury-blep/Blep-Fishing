package com.kunfury.blepFishing.Commands.SubCommands;

import com.kunfury.blepFishing.Admin.AdminMenu;
import com.kunfury.blepFishing.Commands.CommandManager;
import com.kunfury.blepFishing.Commands.SubCommand;
import org.bukkit.command.CommandSender;
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
        if(sender.hasPermission("bf.admin")) {
            AdminMenu menu = new AdminMenu();
            menu.ShowInventory(sender);
        }else new CommandManager().NoPermission(sender);
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
    public List<String> getAliases() {return null;}
}
