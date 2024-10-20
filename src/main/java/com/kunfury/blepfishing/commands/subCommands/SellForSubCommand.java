package com.kunfury.blepfishing.commands.subCommands;

import com.kunfury.blepfishing.commands.SubCommand;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.helpers.Utilities;
import jdk.jshell.execution.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SellForSubCommand extends SubCommand {
    @Override
    public String getName() {
        return "SellFor";
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
        if(args.length > 1) {
            Player targetPlayer = Bukkit.getPlayer(args[1]);
            if (targetPlayer != null && targetPlayer.isOnline()) {
                if (targetPlayer.isSneaking())
                    Utilities.SellAllFish(targetPlayer);
                else
                    Utilities.SellFish(targetPlayer);
                return;
            }
        }
        Utilities.SendPlayerMessage(sender, Formatting.GetLanguageString("System.noPlayer"));
    }

    @Override
    public List<String> getArguments(CommandSender sender, String[] args) {
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
        return "bf.sellFor";
    }

    @Override
    public List<String> getAliases() {
        return List.of();
    }
}
