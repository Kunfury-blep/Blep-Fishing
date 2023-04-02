package com.kunfury.blepFishing.Commands.SubCommands;

import com.kunfury.blepFishing.Commands.CommandManager;
import com.kunfury.blepFishing.Commands.SubCommand;
import com.kunfury.blepFishing.Config.CacheHandler;
import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Miscellaneous.BiomeHandler;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DebugSubcommand extends SubCommand {
    @Override
    public String getName() {
        return "Debug";
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
        if(sender.hasPermission("bf.admin")) {
            Variables.DebugMode = !Variables.DebugMode;
            if(Variables.DebugMode) sender.sendMessage(Formatting.getFormattedMesage("System.debugEnabled"));
            else sender.sendMessage(Formatting.getFormattedMesage("System.debugDisabled"));
            sender.sendMessage(Formatting.getFormattedMesage("System.debugFish")
                            .replace("{amount}", String.valueOf(Variables.getFishList("ALL").size())));

            Player p = (Player)sender;

            sender.sendMessage("Current Biome: " + new BiomeHandler().getBiomeName(p.getLocation()));
            new CacheHandler().SaveCache();

        }else new CommandManager().NoPermission(sender);
    }

    @Override
    public List<String> getArguments(@NotNull CommandSender sender, String[] args) {
        return null;
    }

    @Override
    public String getPermissions() {
        return "bf.admin";
    }

    @Override
    public List<String> getAliases() {return null;}
}
