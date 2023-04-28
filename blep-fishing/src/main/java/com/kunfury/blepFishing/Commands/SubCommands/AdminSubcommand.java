package com.kunfury.blepFishing.Commands.SubCommands;

import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Interfaces.Admin.AdminMenu;
import com.kunfury.blepFishing.Commands.CommandManager;
import com.kunfury.blepFishing.Commands.SubCommand;
import com.kunfury.blepFishing.Objects.RarityObject;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
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
        AdminMenu menu = new AdminMenu();
        menu.ShowMenu(sender);
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
