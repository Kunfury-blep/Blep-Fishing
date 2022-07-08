package com.kunfury.blepFishing.Commands.SubCommands;

import com.kunfury.blepFishing.Miscellaneous.Variables;
import com.kunfury.blepFishing.Objects.BaseFishObject;
import com.kunfury.blepFishing.Tournament.Tournament;
import com.kunfury.blepFishing.Commands.SubCommand;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.kunfury.blepFishing.Miscellaneous.Variables.Prefix;

public class StartTourney extends SubCommand {
    @Override
    public String getName() {return "StartTourney";}

    @Override
    public String getDescription() {
        return "Used by admins to start a tournament";
    }

    @Override
    public String getSyntax() {
        return "/bf StartTourney <fishName> <duration> <cash prize> <item name> <item count>";
    }

    @Override
    public void perform(@NotNull CommandSender sender, String[] args) {
        try {
            new Tournament().CreateTourny(sender, args[1], Double.parseDouble(args[2]), Integer.parseInt(args[3]),
                    args[4], Integer.parseInt(args[5]));
            //Oh boi why did I ever write it like this
            //This is impractical as all hell
            //Definitely need to fix this in the future.
        } catch (Exception e) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    Prefix + "/bf StartTourney <fishName> <duration> <cash prize> <item name> <item count>"));
        }
    }

    @Override
    public List<String> getArguments(@NotNull CommandSender sender, String[] args) {
        List<String> optionList = new ArrayList<>();
        if (args.length == 2) {
            optionList.add("<fish_name>");
            optionList.add("All");
            for(BaseFishObject fish : Variables.BaseFishList){
                optionList.add(fish.Name);
            }

        }
        if(args.length == 3){
            optionList.add("<duration>");
        }
        if(args.length == 4){
            optionList.add("<cash prize>");
        }
        if(args.length == 5){
            optionList.add("<item name>");
        }
        if(args.length == 6){
            optionList.add("<item count>");
        }
        return optionList;
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
