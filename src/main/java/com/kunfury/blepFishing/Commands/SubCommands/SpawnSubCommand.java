package com.kunfury.blepFishing.Commands.SubCommands;

import com.kunfury.blepFishing.Endgame.CompassHandler;
import com.kunfury.blepFishing.Endgame.TreasureHandler;
import com.kunfury.blepFishing.Commands.CommandManager;
import com.kunfury.blepFishing.Commands.SubCommand;
import com.kunfury.blepFishing.Miscellaneous.Variables;
import com.kunfury.blepFishing.Setup;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class SpawnSubCommand extends SubCommand {
    @Override
    public String getName() {return "Spawn";}

    @Override
    public String getDescription() {
        return "Used by admins to spawn in items for debug purposes.";
    }

    @Override
    public String getSyntax() {
        return null;
    }

    @Override
    public void perform(@NotNull CommandSender sender, String[] args) {
        Player p = (Player) sender;

        if(args.length <= 1){
            p.sendMessage(Variables.Prefix + ChatColor.RED + "Please include what you wish to spawn");
            return;
        }

        if(args[1].equalsIgnoreCase("TREASURE")){
            if(args.length > 2){
                int amt = 1;
                if(args.length >= 4 && CommandManager.isNumeric(args[3])) amt = Integer.parseInt(args[3]);
                SpawnTreasure(p, args[2], amt);
                return;
            }
            else p.sendMessage(Variables.Prefix + ChatColor.RED + "Please include the treasure type you wish to spawn.");
        }
    }

    @Override
    public List<String> getArguments(@NotNull CommandSender sender, String[] args) {
        List<String> optionList = new ArrayList<>();
        switch(args.length){
            case 2:
                optionList.add("Treasure");
                break;
            case 3:
                if(args[1].equalsIgnoreCase("TREASURE")){
                    optionList.add("CASKET");
                    optionList.add("MESSAGE_BOTTLE");
                    optionList.add("COMPASS_PIECE");
                    break;
                }
            case 4:
                if(args[1].equalsIgnoreCase("TREASURE")){
                    optionList.add("<amount>");
                    break;
                }
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

    private void SpawnTreasure(Player p, String type, int amount){
        if(amount <= 0) p.sendMessage(Variables.Prefix + ChatColor.RED + "Please enter a valid amount.");
        List<ItemStack> items = new ArrayList<>();
        if(type.equalsIgnoreCase("CASKET")){
            for (int i = 0; i < amount; i++) {
                items.add(new TreasureHandler().GetTreasureCasket());
            }
        }


        if(type.equalsIgnoreCase("MESSAGE_BOTTLE")){
            for (int i = 0; i < amount; i++) {
                items.add(new TreasureHandler().GetMessageBottle());
            }
        }

        if(type.equalsIgnoreCase("COMPASS_PIECE")){
            for (int i = 0; i < amount; i++) {
                items.add(new CompassHandler().GenerateCompassPiece(p, p.getLocation()));
            }
        }

        if(items.size() > 0){
            for(var i : items)
                p.getWorld().dropItem(p.getLocation(), i);
            p.sendMessage(Variables.Prefix +  "Spawned " + amount + " " + type);
            Setup.getPlugin().getLogger().log(Level.FINE, Variables.Prefix + ChatColor.GRAY + "Spawned " + amount + " " + type + " for " + p.getName());
        }else p.sendMessage(Variables.Prefix + ChatColor.RED + "Error generating treasure: " + ChatColor.YELLOW + type.toUpperCase());
    }
}
