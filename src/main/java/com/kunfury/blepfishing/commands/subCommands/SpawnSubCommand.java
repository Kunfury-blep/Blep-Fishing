package com.kunfury.blepfishing.commands.subCommands;

import com.kunfury.blepfishing.commands.SubCommand;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.helpers.Utilities;
import com.kunfury.blepfishing.objects.FishType;
import com.kunfury.blepfishing.objects.FishingArea;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public void perform(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if(args.length <= 1){
            Utilities.SendPlayerMessage(player, Formatting.GetLanguageString("Admin.Spawn.noName"));
            return;
        }

        switch(args[1].toUpperCase()){
            case "TREASURE":
                if(args.length > 2){
                    int amt = 1;
                    if(args.length >= 4 && Formatting.isNumeric(args[3])) amt = Integer.parseInt(args[3]);
                    //SpawnTreasure(player, args[2], amt);
                    break;
                }
                else Utilities.SendPlayerMessage(player, Formatting.GetLanguageString("Admin.Spawn.validTreasure"));
                break;
            case "FISH":
                StringBuilder name;
                if(args.length == 2){
                    name = new StringBuilder("RANDOM");
                }else name = new StringBuilder(args[2]);

                if(name.toString().contains("\"")){
                    List<String> argsList = Arrays.asList(args);

                    for(int i = 3; i < args.length; i++){
                        name.append(' ').append(args[i]);
                        argsList.remove(args[i]);
                        if(args[i].contains("\"")){
                            break;
                        }
                    }
                    args = argsList.toArray(new String[0]);

                    name = new StringBuilder(name.toString().replace("\"", ""));
                }

                int amt = 1;
                if(args.length >= 4 && Formatting.isNumeric(args[3]))
                    amt = Integer.parseInt(args[3]);
                //SpawnFish(player, name.toString(), amt);
                break;

            case "ALL_BLUE":
                Bukkit.broadcastMessage("Spawning All Blue");
                //SpawnAllBlue(p);
                break;

            default:
                Utilities.SendPlayerMessage(player, Formatting.GetLanguageString("Admin.Spawn.noName"));
        }

    }

    @Override
    public List<String> getArguments(CommandSender sender, String[] args) {
        List<String> optionList = new ArrayList<>();
        switch(args.length){
            case 2:
                optionList.add("Fish");
                optionList.add("Treasure");
                optionList.add("Equipment");
                break;
            case 3:
                if(args[1].equalsIgnoreCase("TREASURE")){
                    optionList.add("Casket");
                    optionList.add("Compass");
                    optionList.add("Compass_Piece");
                    break;
                }
                if(args[1].equalsIgnoreCase("FISH")){
                    optionList.add("<fish_name>");
                    optionList.add("RANDOM");
                    for(var fishType : FishType.GetAll()){
                        optionList.add(fishType.Id);
                    }
                    break;
                }
                if(args[1].equalsIgnoreCase("Equipment")){
                    optionList.add("Fish_Bag");
                }
            case 4:
                if(args[2].equalsIgnoreCase("FISH")){
                    optionList.add("<amount>");
                    break;
                }
                if(args[2].equalsIgnoreCase("Compass_Piece")){
                    for(var area : FishingArea.GetAll()){
                        if(area.HasCompassPiece)
                            optionList.add(area.Id);
                    }
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

//    private void SpawnTreasure(Player p, String type, int amount){
//        if(amount <= 0){
//            Util
//            p.sendMessage(Formatting.getFormattedMesage("Admin.Spawn.validAmount"));
//            return;
//        }
//
//
//        if(amount > 100){
//            amount = 100;
//            p.sendMessage(Formatting.getFormattedMesage("Admin.Spawn.limit"));
//        }
//
//
//        List<ItemStack> items = new ArrayList<>();
//
//        switch(type.toUpperCase()){
//            case "CASKET":
//                for (int i = 0; i < amount; i++) {
//                    items.add(new TreasureHandler().GetTreasureCasket());
//                }
//                break;
//            case "MESSAGE_BOTTLE":
//                for (int i = 0; i < amount; i++) {
//                    items.add(new TreasureHandler().GetMessageBottle());
//                }
//                break;
//
//            case "COMPASS_PIECE":
//                for (int i = 0; i < amount; i++) {
//                    items.add(new CompassHandler().GenerateCompassPiece(p, p.getLocation(), true));
//                }
//                break;
//
//            case "COMPASS":
//                items.add(new AllBlueGeneration().CreateCompass(p));
//                break;
//        }
//
//
//        if(items.size() > 0){
//            for(var i : items){
//                if(i != null){
//                    p.getWorld().dropItem(p.getLocation(), i);
//                }else p.sendMessage(Variables.getPrefix() + ChatColor.RED + "Error generating treasure: " + ChatColor.YELLOW + type.toUpperCase());
//            }
//            p.sendMessage(Formatting.getFormattedMesage("Admin.Spawn.success")
//                    .replace("{amount}", String.valueOf(amount))
//                    .replace("{type}", type));
//            BlepFishing.getPlugin().getLogger().log(Level.FINE, Variables.getPrefix() + ChatColor.GRAY + "Spawned " + amount + " " + type + " for " + p.getName()); //TODO: Add config option for treasure reporting
//        }else p.sendMessage(Variables.getPrefix() + ChatColor.RED + "Error generating treasure: " + ChatColor.YELLOW + type.toUpperCase());
//    }
//
//    private void SpawnFish(Player p, String name, int amount){
//        if(amount <= 0){
//            p.sendMessage(Formatting.getFormattedMesage("Admin.Spawn.validAmount"));
//            return;
//        }
//
//        if(amount > 1000){
//            amount = 1000;
//            p.sendMessage(Formatting.getFormattedMesage("Admin.Spawn.limit"));
//        }
//
//        List<FishObject> fish = new ArrayList<>();
//
//
//        BaseFishObject base = BaseFishObject.getBase(name);
//        boolean random = name.equalsIgnoreCase("RANDOM") || name.equalsIgnoreCase("ALL") || name.equalsIgnoreCase("<fish_name>");
//
//        if(!random && base == null){
//            p.sendMessage(Formatting.getFormattedMesage("Admin.Spawn.validFish"));
//            return;
//        }
//
//        Random rand = new Random();
//
//        for (int i = 0; i < amount; i++) {
//            if(random) //Changes the fish for each spawn if it is random
//                base = Variables.BaseFishList.get(rand.nextInt(Variables.BaseFishList.size()));
//
//            fish.add(new FishObject(base, p));
//        }
//
//        if(fish.size() > 0){
//            for(var f : fish){
//                if(f != null){
//                    p.getWorld().dropItem(p.getLocation(), f.GenerateItemStack());
//                    Variables.AddToFishDict(f);
//                }else p.sendMessage(Variables.getPrefix() + ChatColor.RED + "Error generating fish.");
//            }
//
//            if(random){
//                p.sendMessage(Formatting.getFormattedMesage("Admin.Spawn.success")
//                        .replace("{amount}", String.valueOf(amount))
//                        .replace("{type}", "fish"));
//            }else
//                p.sendMessage(Formatting.getFormattedMesage("Admin.Spawn.success")
//                        .replace("{amount}", String.valueOf(amount))
//                        .replace("{type}", name));
//        }else p.sendMessage(Variables.getPrefix() + ChatColor.RED + "Error generating fish.");
//
//
//    }
//
//    private void SpawnAllBlue(Player p){
//        Location loc = p.getLocation();
//        new AllBlueGeneration().Create(loc);
//
//        p.sendMessage(Formatting.getMessage("Admin.Spawn.endgame")
//                .replace("{x}", Formatting.DoubleFormat(loc.getX()))
//                .replace("{y}", Formatting.DoubleFormat(loc.getY()))
//                .replace("{z}", Formatting.DoubleFormat(loc.getZ())));
//    }
}
