package com.kunfury.blepFishing.Commands.SubCommands;

import com.kunfury.blepFishing.Endgame.AllBlueGeneration;
import com.kunfury.blepFishing.Endgame.CompassHandler;
import com.kunfury.blepFishing.Endgame.TreasureHandler;
import com.kunfury.blepFishing.Commands.CommandManager;
import com.kunfury.blepFishing.Commands.SubCommand;
import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Objects.BaseFishObject;
import com.kunfury.blepFishing.Objects.FishObject;
import com.kunfury.blepFishing.Setup;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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

        switch(args[1].toUpperCase()){
            case "TREASURE":
                if(args.length > 2){
                    int amt = 1;
                    if(args.length >= 4 && Formatting.isNumeric(args[3])) amt = Integer.parseInt(args[3]);
                    SpawnTreasure(p, args[2], amt);
                    break;
                }
                else p.sendMessage(Variables.Prefix + ChatColor.RED + "Please include the treasure type you wish to spawn.");
            case "FISH":
                String name = "";
                if(args.length <= 2){
                    name = "RANDOM";
                }else name = args[2];

                int amt = 1;
                if(args.length >= 4 && Formatting.isNumeric(args[3]))
                    amt = Integer.parseInt(args[3]);
                SpawnFish(p, name, amt);
                break;
        }

    }

    @Override
    public List<String> getArguments(@NotNull CommandSender sender, String[] args) {
        List<String> optionList = new ArrayList<>();
        switch(args.length){
            case 2:
                optionList.add("FISH");
                optionList.add("TREASURE");
                break;
            case 3:
                if(args[1].equalsIgnoreCase("TREASURE")){
                    optionList.add("CASKET");
                    optionList.add("MESSAGE_BOTTLE");
                    optionList.add("COMPASS_PIECE");
                    optionList.add("COMPASS");
                    break;
                }
                if(args[1].equalsIgnoreCase("FISH")){
                    optionList.add("<fish_name>");
                    optionList.add("RANDOM");
                    for(BaseFishObject fish : Variables.BaseFishList){
                        optionList.add(fish.Name);
                    }
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

        switch(type.toUpperCase()){
            case "CASKET":
                for (int i = 0; i < amount; i++) {
                    items.add(new TreasureHandler().GetTreasureCasket());
                }
                break;
            case "MESSAGE_BOTTLE":
                for (int i = 0; i < amount; i++) {
                    items.add(new TreasureHandler().GetMessageBottle());
                }
                break;

            case "COMPASS_PIECE":
                for (int i = 0; i < amount; i++) {
                    items.add(new CompassHandler().GenerateCompassPiece(p, p.getLocation(), true));
                }
                break;

            case "COMPASS":
                items.add(new AllBlueGeneration().CreateCompass(p));
                break;
        }


        if(items.size() > 0){
            for(var i : items){
                if(i != null){
                    p.getWorld().dropItem(p.getLocation(), i);
                }else p.sendMessage(Variables.Prefix + ChatColor.RED + "Error generating treasure: " + ChatColor.YELLOW + type.toUpperCase());
            }
            p.sendMessage(Variables.Prefix +  "Finished spawning " + amount + " " + type);
            Setup.getPlugin().getLogger().log(Level.FINE, Variables.Prefix + ChatColor.GRAY + "Spawned " + amount + " " + type + " for " + p.getName());
        }else p.sendMessage(Variables.Prefix + ChatColor.RED + "Error generating treasure: " + ChatColor.YELLOW + type.toUpperCase());
    }

    private void SpawnFish(Player p, String name, int amount){
        if(amount <= 0){
            p.sendMessage(Variables.Prefix + ChatColor.RED + "Please enter a valid amount.");
            return;
        }

        if(amount > 100){
            amount = 100;
            p.sendMessage(Variables.Prefix + ChatColor.YELLOW + "Amount set to 100 to prevent crashing.");
        }

        List<FishObject> fish = new ArrayList<>();


        BaseFishObject base = BaseFishObject.GetBase(name);
        boolean random = name.equalsIgnoreCase("RANDOM") || name.equalsIgnoreCase("ALL") || name.equalsIgnoreCase("<fish_name>");

        if(!random && base == null){
            p.sendMessage(Variables.Prefix + ChatColor.RED + "Please enter a valid fish type.");
            return;
        }

        Random rand = new Random();

        for (int i = 0; i < amount; i++) {
            if(random) //Changes the fish for each spawn if it is random
                base = Variables.BaseFishList.get(rand.nextInt(Variables.BaseFishList.size()));

            fish.add(new FishObject(base, p));
        }

        if(fish.size() > 0){
            for(var f : fish){
                if(f != null){
                    p.getWorld().dropItem(p.getLocation(), f.GenerateItemStack());
                    Variables.AddToFishDict(f);
                }else p.sendMessage(Variables.Prefix + ChatColor.RED + "Error generating fish.");
            }


            p.sendMessage(Variables.Prefix +  "Finished spawning " + amount + " fish.");
            Setup.getPlugin().getLogger().log(Level.FINE, Variables.Prefix + ChatColor.GRAY + "Spawned " + amount + " fish for " + p.getName());
        }else p.sendMessage(Variables.Prefix + ChatColor.RED + "Error generating fish.");


    }

    private void SpawnCompass(Player p){

    }


}
