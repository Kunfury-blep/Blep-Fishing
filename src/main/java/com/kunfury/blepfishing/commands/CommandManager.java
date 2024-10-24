package com.kunfury.blepfishing.commands;

import com.kunfury.blepfishing.commands.subCommands.*;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.helpers.Utilities;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.panels.player.PlayerPanel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager implements TabExecutor {
    private final List<SubCommand> subCommands;

    public CommandManager(){
        subCommands = Arrays.asList(
                new AdminSubcommand(),
                new TournamentsSubCommand(),
                new FishSubCommand(),
                new ReloadSubCommand(),
                new SellSubCommand(),
                new SellAllSubCommand(),
                new SellForSubCommand(),
                new SpawnSubCommand(),
                new ToggleBossBarSubCommand()
        );
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
            Utilities.SendPlayerMessage(sender, Formatting.GetLanguageString("System.noCommand"));
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
                if(subCommand.getName().equalsIgnoreCase(args[0])
                        || (subCommand.getAliases() != null && subCommand.getAliases().contains(args[0].toUpperCase()))
                        && subCommand.getArguments(sender, args) != null)

                    for(var a : subCommand.getArguments(sender, args)){
                        if(a.toUpperCase().contains((args[args.length - 1].toUpperCase()))) optionList.add(a);
                    }
        }

        return optionList;
    }

    public void NoPermission(CommandSender sender) {
        Utilities.SendPlayerMessage(sender, Formatting.GetLanguageString("System.noPermission"));
    }

    private boolean CheckPermissions(CommandSender sender, String permission){
        return (permission.isEmpty() || sender.hasPermission("bf.admin") || sender.hasPermission(permission));
    }

    private void BaseCommand(CommandSender sender){
//        ConfigHandler.instance.fishConfig.SaveFishTypes();
//

        if(sender instanceof ConsoleCommandSender)
            return;
        else{
            new PlayerPanel().Show((Player) sender);
//            Player player = (Player) sender;
//            Utilities.GiveItem(player, new FishingJournal(player.getUniqueId()).GetItemStack(), true);
        }

            //
    }

}
