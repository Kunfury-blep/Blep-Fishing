package com.kunfury.blepFishing.Commands;

import com.kunfury.blepFishing.Commands.SubCommands.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.kunfury.blepFishing.Miscellaneous.Variables.Messages;
import static com.kunfury.blepFishing.Miscellaneous.Variables.Prefix;

public class CommandManager implements TabExecutor {
    private final ArrayList<SubCommand> subCommands = new ArrayList<>();

    public CommandManager(){
        subCommands.add(new StartTourney());
        subCommands.add(new LeaderboardSubcommand());
        subCommands.add(new ReloadSubcommand());
        subCommands.add(new AdminSubcommand());
        subCommands.add(new SellSubcommand());
        subCommands.add(new SellForSubcommand());
        subCommands.add(new SellAllSubcommand());
        subCommands.add(new TournamentSubcommand());
        subCommands.add(new ClaimSubcommand());
        subCommands.add(new ListFishSubcommand());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(args.length > 0){
            if(args[0].equalsIgnoreCase("HELP") || args[0].equalsIgnoreCase("?")) BaseCommand(sender);
            else {
                for(SubCommand subCommand : subCommands){
                    if(subCommand.getName().equalsIgnoreCase(args[0]) || (subCommand.getAliases() != null && subCommand.getAliases().contains(args[0]))){
                        if(CheckPermissions(sender, subCommand.getPermissions())) subCommand.perform(sender, args);
                        else NoPermission(sender);
                    }
                }
            }

        }else BaseCommand(sender);




        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(args.length == 1){
            ArrayList<String> subCommandArgs = new ArrayList<>();
            for(SubCommand subCommand : subCommands)
                if(CheckPermissions(sender, subCommand.getPermissions())) subCommandArgs.add(subCommand.getName());
            return subCommandArgs;
        }else if(args.length > 1){
            for(SubCommand subCommand : subCommands)
                if(subCommand.getName().equalsIgnoreCase(args[0]))
                    return subCommand.getArguments(sender, args);
        }


        return null;
    }


    public ArrayList<SubCommand> getSubCommands(){
        return subCommands;
    }

    public void NoPermission(CommandSender sender) {
        sender.sendMessage(Prefix + Messages.getString("noPermissions"));
    }

    private boolean CheckPermissions(CommandSender sender, String permission){
        return (permission == null || sender.hasPermission("bf.admin") || sender.hasPermission(permission));
    }

    private void BaseCommand(CommandSender sender){
        Player player = (Player)sender;
        sender.sendMessage("Current Biome: " + player.getLocation().getBlock().getBiome().name());


//        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
//                "                     " + Messages.getString("helpTitle") + "\n"
//                        + Prefix + "/bf lb <fishname> - " + Messages.getString("leaderboardHelp") + "\n"
//                        + Prefix + "/bf reload - " + Messages.getString("reloadHelp") + "\n"
//                        + Prefix + "/bf fish - " + Messages.getString("fishHelp") + "\n"
//                        + Prefix + "/bf claim - " + Messages.getString("claimHelp") + "\n"
//                        + Prefix + "/bf tourney - " + Messages.getString("tourneyHelp") + "\n"
//                        + Prefix + "/bf admin - " + Messages.getString("adminHelp") + "\n"
//        ));
    }

}
