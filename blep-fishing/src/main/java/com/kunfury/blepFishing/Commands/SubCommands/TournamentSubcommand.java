package com.kunfury.blepFishing.Commands.SubCommands;

import com.kunfury.blepFishing.Commands.CommandManager;
import com.kunfury.blepFishing.Commands.SubCommand;
import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Interfaces.Player.TournamentPanel;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Tournament.TournamentHandler;
import com.kunfury.blepFishing.Tournament.TournamentObject;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
            if(sender instanceof Player){
                new TournamentPanel().ClickBase((Player) sender);
            }else{
                sender.sendMessage(Variables.Prefix + Formatting.getMessage("Tournament.noAction"));
            }

            return;
        }

        if(!sender.hasPermission("bf.admin")){
            new CommandManager().NoPermission(sender);
            return;
        }

        if(!TournamentHandler.isActive){
            sender.sendMessage(Variables.Prefix +  Formatting.getMessage("Tournament.inactive"));
            return;
        }

        if(args.length == 2){
            sender.sendMessage(Variables.Prefix + Formatting.getMessage("Tournament.noName")
                    .replace("{action}", args[1].toLowerCase()));
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

        if(!args[2].equalsIgnoreCase("ALL") && t == null){
            sender.sendMessage(Variables.Prefix + Formatting.getMessage("Tournament.noTournament"));
            return;
        }

        switch(args[1]){
            case "CANCEL" -> {
                if(!sender.hasPermission("bf.admin")){
                    new CommandManager().NoPermission(sender);
                    return;
                }

                if(args[2].equalsIgnoreCase("ALL")){
                    if(TournamentHandler.ActiveTournaments.size() > 0){
                        for(var a : new ArrayList<>(TournamentHandler.ActiveTournaments)){
                            new TournamentHandler().Cancel(a);
                            sender.sendMessage(Variables.Prefix + Formatting.formatColor(Formatting.getMessage("Tournament.cancel")
                                    .replace("{tournament}", a.getName())));
                        }
                    } else sender.sendMessage(Variables.Prefix + Formatting.getMessage("Tournament.empty"));
                    return;
                }

                if(TournamentHandler.ActiveTournaments.contains(t)){
                    new TournamentHandler().Cancel(t);
                    sender.sendMessage(Variables.Prefix + Formatting.formatColor(Formatting.getMessage("Tournament.cancel")
                            .replace("{tournament}", t.getName())));
                }

                else sender.sendMessage(Variables.Prefix + Formatting.formatColor(Formatting.getMessage("Tournament.notRunning")
                        .replace("{tournament}", t.getName())));
            }
            case "START" -> {
                if(!sender.hasPermission("bf.admin")){
                    new CommandManager().NoPermission(sender);
                    return;
                }
                assert t != null;
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
                optionList.add("FINISH");
            }
        }

        if (args.length == 3) {
            if(args[1].equalsIgnoreCase("CANCEL"))
                optionList.add("ALL");
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
