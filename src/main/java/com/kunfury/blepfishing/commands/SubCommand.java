package com.kunfury.blepfishing.commands;

import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class SubCommand {

    public abstract String getName();

    public abstract String getDescription();

    public abstract String getSyntax();

    public abstract void perform(CommandSender sender, String[] args);

    public abstract List<String> getArguments(CommandSender sender, String[] args);

    public abstract String getPermissions();

    public abstract List<String> getAliases();

    public boolean showTabComplete(){
        return true;
    }

}
