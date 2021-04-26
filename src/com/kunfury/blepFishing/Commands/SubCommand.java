package com.kunfury.blepFishing.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class SubCommand {

    public abstract String getName();

    public abstract String getDescription();

    public abstract String getSyntax();

    public abstract void perform(@NotNull CommandSender sender, String[] args);

    public abstract List<String> getArguments(@NotNull CommandSender sender, String[] args);

    public abstract String getPermissions();

    public abstract List<String> getAliases();

}
