package com.kunfury.blepfishing.commands.subCommands;

import com.kunfury.blepfishing.commands.SubCommand;
import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.helpers.Utilities;
import com.kunfury.blepfishing.objects.equipment.FishBag;
import com.kunfury.blepfishing.objects.FishType;
import com.kunfury.blepfishing.objects.FishingArea;
import com.kunfury.blepfishing.objects.treasure.Casket;
import com.kunfury.blepfishing.objects.treasure.CompassPiece;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
                SpawnTreasure(args, player);
                return;
            case "FISH":
                SpawnFish(args, player);
                return;
            case "EQUIPMENT":
                SpawnEquipment(args, player);
                return;

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
                switch(args[1].toUpperCase()){
                    case "TREASURE":
                        optionList.add("Casket");
                        optionList.add("Compass");
                        optionList.add("Compass_Piece");
                        break;
                    case "FISH":
                        optionList.add("<Random>");
                        for(var fishType : FishType.GetAll()){
                            optionList.add(fishType.Id);
                        }
                        break;
                    case "EQUIPMENT":
                        optionList.add("Fish_Bag");

                }
                break;
            case 4:
                switch(args[2].toUpperCase()){
                    case "FISH":
                        optionList.add("<amount>");
                        for(var fishType : FishType.GetAll()){
                            optionList.add(fishType.Id);
                        }
                        break;
                    case "CASKET":
                        optionList.add("<Random>");
                        for(var casket : Casket.GetAll()){
                            optionList.add(casket.Id);
                        }
                        break;
                    case "COMPASS_PIECE":
                        optionList.add("<Random>");
                        for(var area : FishingArea.GetAll()){
                            if(area.HasCompassPiece)
                                optionList.add(area.Id);
                        }
                        break;
                }
                break;
            case 5:
                switch(args[2].toUpperCase()){
                    case "FISH", "CASKET":
                        optionList.add("<amount>");
                        break;
                }
                break;
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

    private void SpawnEquipment(String[] args, Player player) {
        if(args.length == 2){
            Utilities.SendPlayerMessage(player, Formatting.GetLanguageString("Admin.Spawn.validEquipment"));
            return;
        }

        switch(args[2].toUpperCase()){
            case "FISH_BAG":
                FishBag fishBag = new FishBag();
                Utilities.GiveItem(player, fishBag.GetItem(), true);
                return;
            default:
                return;
        }
    }

    private void SpawnFish(String[] args, Player player){
        String typeId;
        if(args.length == 2)
            typeId = "RANDOM";
        else
            typeId = args[2];
        int amt = 1;
        if(args.length >= 4 && Formatting.isNumeric(args[3]))
            amt = Integer.parseInt(args[3]);

        if(Objects.equals(typeId, "<Random>")){
            for(int i = 0; i < amt; i++){
                FishType.GetRandom().Spawn(player);
            }
            return;
        }

        FishType fishType = FishType.FromId(typeId);
        if(fishType == null){
            Utilities.SendPlayerMessage(player, Formatting.GetLanguageString("Admin.Spawn.noName"));
            return;
        }

        for(int i = 0; i < amt; i++){
            fishType.Spawn(player);
        }
    }


    private void SpawnTreasure(String[] args, Player player){
        if(args.length <= 2){
            Utilities.SendPlayerMessage(player, Formatting.GetLanguageString("Admin.Spawn.validTreasure"));
            return;
        }


//        int amount = 1;
//        if(args.length >= 4 && Formatting.isNumeric(args[3])) amount = Integer.parseInt(args[3]);
//
//
//        if(amount <= 0){
//            Utilities.SendPlayerMessage(player, Formatting.GetLanguageString("Admin.Spawn.validAmount"));
//            return;
//        }


        int amount = 1;
        switch(args[2].toUpperCase()){
            case "CASKET":
                String casketTypeId;
                if(args.length == 3)
                    casketTypeId = "<RANDOM>";
                else{
                    casketTypeId = args[3];
                    if(args.length > 4 && Formatting.isNumeric(args[4]))
                        amount = Integer.parseInt(args[4]);
                }

                if(casketTypeId.equalsIgnoreCase("<RANDOM>")){
                    for (int i = 0; i < amount; i++) {
                        Casket randomCasket = Casket.RollCasket();
                        if(randomCasket == null){
                            Utilities.Severe("Error generating random casket to spawn");
                            return;
                        }
                        Utilities.GiveItem(player, randomCasket.GetItem(), true);
                    }
                    return;
                }

                if(!Casket.IdExists(casketTypeId)){
                    Utilities.SendPlayerMessage(player, Formatting.GetLanguageString("Admin.Spawn.validTreasure"));
                    return;
                }
                Casket casket = Casket.FromId(casketTypeId);
                if(casket == null){
                    Utilities.Severe("Error generating casket to spawn with id: ");
                    return;
                }

                for (int i = 0; i < amount; i++) {
                    Utilities.GiveItem(player, casket.GetItem(), true);
                }
                return;
            case "COMPASS_PIECE":
                if(!ConfigHandler.instance.treasureConfig.getCompassEnabled()){
                    return;
                }

                if(args.length == 3){
                    Utilities.SendPlayerMessage(player, Formatting.GetLanguageString("Admin.Spawn.validTreasure"));
                    return;
                }

                for (int i = 0; i < amount; i++) {
                    FishingArea area = FishingArea.GetRandom();
                    while(!area.HasCompassPiece)
                        area = FishingArea.GetRandom();

                    Utilities.GiveItem(player, CompassPiece.GeneratePiece(new FishingArea[]{area}), true);
                }
                return;
            case "COMPASS":
                Utilities.GiveItem(player, CompassPiece.GenerateCompass(), true);
                return;
            default:
                Utilities.SendPlayerMessage(player, Formatting.GetLanguageString("Admin.Spawn.invalidTreasureType"));
                return;
        }
    }
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
