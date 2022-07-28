package com.kunfury.blepFishing.Commands.SubCommands;

import com.kunfury.blepFishing.Commands.CommandManager;
import com.kunfury.blepFishing.Commands.SubCommand;
import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Objects.BaseFishObject;
import com.kunfury.blepFishing.Tournament.Old.Tournament;
import com.kunfury.blepFishing.Tournament.TournamentHandler;
import com.kunfury.blepFishing.Tournament.TournamentObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TournamentSubcommand extends SubCommand {
    @Override
    public String getName() {return "Tournament";}

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

        if(args.length == 1){
            sender.sendMessage(Variables.Prefix + "Please provide the type of tournament action you would like to perform.");
            return;
        }

        if(args.length == 2){
            sender.sendMessage(Variables.Prefix + "Please provide the name of the tournament you would like to " + args[1].toLowerCase());
            return;
        }

        TournamentObject t = null;
        StringBuilder tourneyName = new StringBuilder();

        for(int i = 2; i < args.length; i++){
            if(tourneyName.length() > 0) tourneyName.append(" ");
            tourneyName.append(args[i]);
        }

        for(var f : TournamentHandler.TournamentList){
            if (f.getName().equals(tourneyName.toString())){
                t = f;
                break;
            }
        }

        if(t == null){
            sender.sendMessage(Variables.Prefix + "Please provide a valid tournament name.");
            return;
        }


        Bukkit.broadcastMessage("Reached the switch");
        switch(args[1]){
            case "CANCEL" -> {
                if(!sender.hasPermission("bf.admin")){
                    new CommandManager().NoPermission(sender);
                    return;
                }
                if(TournamentHandler.ActiveTournaments.contains(t))
                    new TournamentHandler().Cancel(t);
                else sender.sendMessage(Variables.Prefix + Formatting.formatColor(t.getName()) + ChatColor.WHITE + " is not currently running.");
            }
            case "START" -> {
                if(!sender.hasPermission("bf.admin")){
                    new CommandManager().NoPermission(sender);
                    return;
                }
                sender.sendMessage(Variables.Prefix + "Starting " + Formatting.formatColor(t.getName()));
                new TournamentHandler().Start(t);
            }
            case "FINISH" -> {
                if(!sender.hasPermission("bf.admin")){
                    new CommandManager().NoPermission(sender);
                    return;
                }
                new TournamentHandler().Finish(t);
            }
        }
    }

    @Override
    public List<String> getArguments(@NotNull CommandSender sender, String[] args) {
        List<String> optionList = new ArrayList<>();
        if(args.length == 2){
            if(sender.hasPermission("bf.admin")){
                optionList.add("CANCEL");
                optionList.add("START");
            }
        }

        if (args.length == 3) {
            for(var t : TournamentHandler.TournamentList){
                optionList.add(t.getName());
            }

        }
        return optionList;
    }

    @Override
    public String getPermissions() {
        return null;
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList( "TOURNAMENT", "TOURNEY", "TOURNEYS", "TOURNAMENTS", "T");
    }

}
