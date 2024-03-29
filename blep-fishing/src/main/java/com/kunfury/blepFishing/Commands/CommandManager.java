package com.kunfury.blepFishing.Commands;

import com.kunfury.blepFishing.Commands.SubCommands.*;
import com.kunfury.blepFishing.Interfaces.Player.PlayerPanel;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CommandManager implements TabExecutor {
    private final ArrayList<SubCommand> subCommands = new ArrayList<>();

    public CommandManager(){
        subCommands.add(new LeaderboardSubcommand());
        subCommands.add(new ReloadSubcommand());
        subCommands.add(new AdminSubcommand());
        subCommands.add(new SellSubcommand());
        subCommands.add(new SellForSubcommand());
        subCommands.add(new SellAllSubcommand());
        subCommands.add(new TournamentSubcommand());
        subCommands.add(new ClaimSubcommand());
        //subCommands.add(new ConfigSubcommand());
        subCommands.add(new GetDataSubCommand());
        subCommands.add(new SpawnSubCommand());
        subCommands.add(new DebugSubcommand());
        subCommands.add(new HelpSubcommand());
        subCommands.add(new QuestSubcommand());
        subCommands.add(new IgnoreSubCommand());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(args.length > 0){
            for(SubCommand subCommand : subCommands){
                if(subCommand.getName().equalsIgnoreCase(args[0]) || (subCommand.getAliases() != null && subCommand.getAliases().contains(args[0].toUpperCase()))){
                    if(CheckPermissions(sender, subCommand.getPermissions())) subCommand.perform(sender, args);
                    else NoPermission(sender);
                    return true;
                }
            }
            sender.sendMessage(Formatting.getFormattedMesage("System.noComm"));
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
        sender.sendMessage(Formatting.getFormattedMesage("System.noPerm"));
    }

    private boolean CheckPermissions(CommandSender sender, String permission){
        return (permission == null || sender.hasPermission("bf.admin") || sender.hasPermission(permission));
    }

    private void BaseCommand(CommandSender sender){
        ItemStack item = new ItemStack( Material.DIAMOND_HOE );
        System.out.println( "Setting value..." );
        item = NBTEditor.set( item, "Hello, world!", "io", "github", "bananapuncher714", "nbteditor", "test" );
        System.out.println( "Getting value..." );
        System.out.println( NBTEditor.getString( item, "io", "github", "bananapuncher714", "nbteditor", "test" ) );
        System.out.println( NBTEditor.getNBTCompound( item ) );

        if(sender instanceof ConsoleCommandSender)
            new HelpSubcommand().perform(sender, null);
        else
            new PlayerPanel().Show(sender);
    }

}
