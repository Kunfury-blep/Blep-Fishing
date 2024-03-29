package com.kunfury.blepFishing.Commands.SubCommands;

import com.kunfury.blepFishing.Commands.CommandManager;
import com.kunfury.blepFishing.Commands.SubCommand;
import com.kunfury.blepFishing.Config.Variables;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ConfigSubcommand extends SubCommand {
    @Override
    public String getName() {
        return "Config";
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
            sender.sendMessage(Variables.getPrefix() + "The Web Config Panel has been discontinued. Sorry for the ");
//            TextComponent message = new TextComponent(Variables.getPrefix() + "Click here to open the configuration web panel." );
//            message.setClickEvent( new ClickEvent( ClickEvent.Action.OPEN_URL, "https://kunfury-blep.github.io/Config.html" ) );
//
//            if(sender instanceof Player player){
//                player.spigot().sendMessage(message);
//            }else
//                sender.sendMessage(Variables.getPrefix() + "https://kunfury-blep.github.io/Config.html");
            //sender.sendMessage(Variables.Prefix + component);
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
    public List<String> getAliases() {
        return null;
    }
}
