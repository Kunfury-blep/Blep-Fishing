package com.kunfury.blepFishing.Commands;

import com.kunfury.blepFishing.Commands.SubCommands.*;
import com.kunfury.blepFishing.Miscellaneous.Variables;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
        subCommands.add(new ConfigSubcommand());
        subCommands.add(new GetDataSubCommand());
        subCommands.add(new SpawnSubCommand());
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
                if(subCommand.getName().equalsIgnoreCase(args[0]))
                    for(var a : subCommand.getArguments(sender, args)){
                        if(a.toUpperCase().contains((args[args.length - 1].toUpperCase()))) optionList.add(a);
                    }
        }

        return optionList;
    }


    public ArrayList<SubCommand> getSubCommands(){
        return subCommands;
    }

    public void NoPermission(CommandSender sender) {
        sender.sendMessage(Prefix + Variables.getMessage("noPermissions"));
    }

    private boolean CheckPermissions(CommandSender sender, String permission){
        return (permission == null || sender.hasPermission("bf.admin") || sender.hasPermission(permission));
    }

    private void BaseCommand(CommandSender sender){
        String helpMessage = ChatColor.translateAlternateColorCodes('&',
                "                     " + Variables.getMessage("helpTitle") + "\n"
                        + Prefix + "/bf lb <fishname> - " + Variables.getMessage("leaderboardHelp") + "\n"
                        + Prefix + "/bf reload - " + Variables.getMessage("reloadHelp") + "\n"
                        + Prefix + "/bf fish - " + Variables.getMessage("fishHelp") + "\n"
                        + Prefix + "/bf claim - " + Variables.getMessage("claimHelp") + "\n"
                        + Prefix + "/bf tourney - " + Variables.getMessage("tourneyHelp") + "\n"
                        + Prefix + "/bf admin - " + Variables.getMessage("adminHelp") + "\n");
        if(sender.hasPermission("bf.admin")) helpMessage += Prefix + "/bf config - " + Variables.getMessage("configHelp") + "\n";


        sender.sendMessage(helpMessage);
    }

    private static Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

}
