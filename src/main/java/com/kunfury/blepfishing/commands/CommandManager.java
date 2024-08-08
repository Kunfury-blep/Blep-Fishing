package com.kunfury.blepfishing.commands;

import com.kunfury.blepfishing.commands.subCommands.AdminSubcommand;
import com.kunfury.blepfishing.ui.panels.player.PlayerPanel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandManager implements TabExecutor {
    private final ArrayList<SubCommand> subCommands = new ArrayList<>();

    public CommandManager(){
//        subCommands.add(new LeaderboardSubcommand());
//        subCommands.add(new ReloadSubcommand());
        subCommands.add(new AdminSubcommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(args.length > 0){
            for(SubCommand subCommand : subCommands){
                if(subCommand.getName().equalsIgnoreCase(args[0]) || (subCommand.getAliases() != null && subCommand.getAliases().contains(args[0].toUpperCase()))){
                    if(CheckPermissions(sender, subCommand.getPermissions())) subCommand.perform(sender, args);
                    else NoPermission(sender);
                    return true;
                }
            }
           // sender.sendMessage(Formatting.getFormattedMesage("System.noComm"));
        }else BaseCommand(sender);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        List<String> optionList = new ArrayList<>();

        if(args.length == 1){
            for(SubCommand subCommand : subCommands)
                if(CheckPermissions(sender, subCommand.getPermissions())
                    && subCommand.getName().toUpperCase().contains((args[0].toUpperCase()))){
                    optionList.add(subCommand.getName());
                }
            return optionList;
        }else if(args.length > 1){
            for(SubCommand subCommand : subCommands)
                if(subCommand.getName().equalsIgnoreCase(args[0]) || (subCommand.getAliases() != null && subCommand.getAliases().contains(args[0].toUpperCase())))
                    if(subCommand.getArguments(sender, args) != null){
                        for(var a : subCommand.getArguments(sender, args)){
                            if(a.toUpperCase().contains((args[args.length - 1].toUpperCase()))) optionList.add(a);
                        }
                    }
        }

        return optionList;
    }

    public void NoPermission(CommandSender sender) {
       // sender.sendMessage(Formatting.getFormattedMesage("System.noPerm"));
    }

    private boolean CheckPermissions(CommandSender sender, String permission){
        return (permission == null || sender.hasPermission("bf.admin") || sender.hasPermission(permission));
    }

    private void BaseCommand(CommandSender sender){
//        ConfigHandler.instance.fishConfig.SaveFishTypes();
//

        if(sender instanceof ConsoleCommandSender)
            return;
        else
            new PlayerPanel().Show((Player) sender);
    }

}
