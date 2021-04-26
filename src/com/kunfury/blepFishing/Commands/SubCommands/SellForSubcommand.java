package com.kunfury.blepFishing.Commands.SubCommands;

import Miscellaneous.FishEconomy;
import com.kunfury.blepFishing.Commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SellForSubcommand extends SubCommand {
    @Override
    public String getName() {
        return "SellFor";
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
        Player p = Bukkit.getPlayer(args[1]);
        if(p != null && p.isOnline())
            FishEconomy.SellFish(p);
        else
            sender.sendMessage("That player could not be found.");
    }

    @Override
    public List<String> getArguments(@NotNull CommandSender sender, String[] args) {
        List<String> optionList = new ArrayList<>();
        if (args.length == 2) {
            for(Player p : Bukkit.getServer().getOnlinePlayers()){
                optionList.add(p.getName());
            }
            optionList.add("<Player>");
        }
        return optionList;
    }

    @Override
    public String getPermissions() {
        return "bf.sellfor";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }
}
