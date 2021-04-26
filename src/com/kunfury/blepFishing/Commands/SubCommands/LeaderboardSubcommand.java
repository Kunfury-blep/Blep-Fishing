package com.kunfury.blepFishing.Commands.SubCommands;

import Miscellaneous.Formatting;
import Miscellaneous.Variables;
import Objects.BaseFishObject;
import Objects.FishObject;
import com.kunfury.blepFishing.Commands.SubCommand;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LeaderboardSubcommand extends SubCommand {
    @Override
    public String getName() {
        return "Leaderboard";
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
        if(args.length == 1)
            sender.sendMessage("/bf lb <Fish Name> <Leaderboard Number>");
        else {
            String fishName = args[1].toUpperCase();
            String formattedName = StringUtils.capitalize(fishName.toLowerCase());

            int startVal = 0;
            if(args.length == 3) {
                try {
                    startVal = Integer.parseInt(args[2]) - 1;
                }catch(Exception ignored){}

            }



            if(!fishName.equals("ALL") && !Variables.FishDict.containsKey(fishName)){
                sender.sendMessage("None of that fish has been caught.");
                return;
            }


            ///
            //If the fish exissts, and at least one has been caught, runs the below section
            ///
            List<FishObject> caughtFishList = Variables.GetFishList(fishName); //Gets the caught fish
            if(caughtFishList != null){
                if( caughtFishList.size() > startVal) {
                    caughtFishList.subList(0, startVal).clear();
                }else {
                    startVal = 0;
                    sender.sendMessage("There aren't that many fish available.");
                }

                if(caughtFishList.size() > 5)
                    caughtFishList.subList(5, caughtFishList.size()).clear();
            }

            //Initializes the size of the chatbox
            int pLength = 15;

            String fPlayer = Formatting.FixFontSize("Player Name", pLength);
            String fullString = ChatColor.translateAlternateColorCodes('&', "&b" + fPlayer + " Fish");

            sender.sendMessage(ChatColor.BOLD + ("--" + formattedName + " Leaderboard - Hover For Info--"));
            sender.sendMessage(fullString);

            int i = startVal;

            for (FishObject fish : caughtFishList) {
                fPlayer = Formatting.FixFontSize(fish.PlayerName, pLength);
                String lbString = ChatColor.translateAlternateColorCodes('&' ,
                        Formatting.FixFontSize((i+1) + ".", 4)
                                + fPlayer + fish.Rarity + " " + fish.Name);


                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                TextComponent mainComponent = new TextComponent (lbString);
                mainComponent.setHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT,
                        new Text(ChatColor.translateAlternateColorCodes('&' ,(fish.Rarity + " " + fish.Name +
                                "&f\nFish Size: " + Formatting.DoubleFormat(fish.RealSize) +
                                "\nRank: " + (i + 1) +
                                "\nCaught On: " +  formatter.format(fish.DateCaught)  +
                                "\nScore: " + Formatting.DoubleFormat(fish.Score)
                        )))));

                sender.spigot().sendMessage(mainComponent);
                i++;
            }
        }
    }

    @Override
    public List<String> getArguments(@NotNull CommandSender sender, String[] args) { //bf lb <Fish Name> <Leaderboard Number>
        List<String> optionList = new ArrayList<>();
        if (args.length == 2) {
            for(BaseFishObject fish : Variables.BaseFishList){
                optionList.add(fish.Name);
            }
            optionList.add("<fish name>");
        }
        if(args.length == 3){
            optionList.add("<Leaderboard Number>");
        }
        return optionList;
    }

    @Override
    public String getPermissions() {
        return null;
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList( "lb");
    }
}
