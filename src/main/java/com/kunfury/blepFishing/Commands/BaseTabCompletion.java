package com.kunfury.blepFishing.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BaseTabCompletion implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> optionList = new ArrayList<>();
        if (args.length == 1) {
            optionList = new ArrayList<>(Arrays.asList("reload", "fish", "claim", "tourney", "admin", "lb"));

            if(sender.hasPermission("bf.admin")){
                optionList.add("StartTourney");
            }
        }
        return optionList;
    }
}