package com.kunfury.blepFishing.Commands.SubCommands;

import com.kunfury.blepFishing.Commands.CommandManager;
import com.kunfury.blepFishing.Commands.SubCommand;
import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Interfaces.Player.QuestPanel;
import com.kunfury.blepFishing.Interfaces.Player.TournamentPanel;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Quests.QuestHandler;
import com.kunfury.blepFishing.Tournament.TournamentHandler;
import com.kunfury.blepFishing.Tournament.TournamentObject;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestSubcommand extends SubCommand {
    @Override
    public String getName() {return "Quests";}

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
                new QuestPanel().ClickBase((Player)sender);
            }else{
                sender.sendMessage(Variables.Prefix + Formatting.getMessage("Quests.noAction"));
            }
            return;
        }

        if(!sender.hasPermission("bf.admin")){
            new CommandManager().NoPermission(sender);
            return;
        }

        if(!QuestHandler.isActive){
            sender.sendMessage(Variables.Prefix +  Formatting.getMessage("Quests.inactive"));
            return;
        }

        switch(args[1]){
            case "NEW_DAY" -> {
                new QuestHandler().NewDay();
            }
        }
    }

    @Override
    public List<String> getArguments(@NotNull CommandSender sender, String[] args) {
        List<String> optionList = new ArrayList<>();
        if(args.length == 2){
            if(sender.hasPermission("bf.admin")){
                optionList.add("NEW_DAY");
            }
        }

        if (args.length == 3) {
            if(args[1].equalsIgnoreCase("CANCEL"))
                optionList.add("ALL");
            for(var q : QuestHandler.QuestList){
                optionList.add(q.getName());
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
        return Arrays.asList( "QUEST", "Q");
    }

}
