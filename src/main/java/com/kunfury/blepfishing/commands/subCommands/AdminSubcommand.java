package com.kunfury.blepfishing.commands.subCommands;

import com.kunfury.blepfishing.commands.CommandManager;
import com.kunfury.blepfishing.commands.SubCommand;
import com.kunfury.blepfishing.objects.FishType;
import com.kunfury.blepfishing.objects.FishingArea;
import com.kunfury.blepfishing.objects.Rarity;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.objects.treasure.TreasureType;
import com.kunfury.blepfishing.ui.panels.admin.AdminGeneralPanel;
import com.kunfury.blepfishing.ui.panels.admin.AdminPanel;
import com.kunfury.blepfishing.ui.panels.admin.AdminTranslationsPanel;
import com.kunfury.blepfishing.ui.panels.admin.areas.AdminAreasPanel;
import com.kunfury.blepfishing.ui.panels.admin.fish.AdminFishEditPanel;
import com.kunfury.blepfishing.ui.panels.admin.fish.AdminFishPanel;
import com.kunfury.blepfishing.ui.panels.admin.rarities.AdminRarityPanel;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentEditPanel;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentPanel;
import com.kunfury.blepfishing.ui.panels.admin.treasure.AdminTreasurePanel;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AdminSubcommand extends SubCommand {
    @Override
    public String getName() {
        return "Admin";
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
        if(!sender.hasPermission("bf.admin")){
            new CommandManager().NoPermission(sender);
            return;
        }

        if(args.length <= 1){
            new AdminPanel().Show(sender);
            return;
        }

        switch(args[1].toUpperCase()){
            case "AREAS":
                if(args.length == 2){
                    new AdminAreasPanel().Show(sender);
                    return;
                }
                break;
            case "FISH":
                if(args.length == 2){
                    new AdminFishPanel(1).Show(sender);
                    return;
                }
                break;
            case "GENERAL":
                if(args.length == 2){
                    new AdminGeneralPanel().Show(sender);
                    return;
                }
                break;
            case "RARITIES":
                if(args.length == 2){
                    new AdminRarityPanel().Show(sender);
                    return;
                }
                break;
            case "TOURNAMENTS":
                if(args.length == 2){
                    new AdminTournamentPanel().Show(sender);
                    return;
                }
                if(args.length == 3){
                    if(TournamentType.IdExists(args[2])){
                        new AdminTournamentEditPanel(TournamentType.FromId(args[2])).Show(sender);
                        return;
                    }
                }
                if(args.length == 4){
                    if(!TournamentType.IdExists(args[2]))
                        return;

                    var tournamentType = TournamentType.FromId(args[2]);
                    assert tournamentType != null;

                    switch(args[3].toUpperCase()){
                        case "START":
                            tournamentType.Start();
                            return;
                        case "CANCEL":
                            tournamentType.Cancel();
                            return;
                        case "FINISH":
                            tournamentType.Finish();
                            return;
                    }
                }
                break;
            case "TREASURE":
                if(args.length == 2){
                    new AdminTreasurePanel().Show(sender);
                    return;
                }
                break;
            case "TRANSLATIONS":
                if(args.length == 2){
                    new AdminTranslationsPanel().Show(sender);
                    return;
                }
                break;
            default:
                new AdminPanel().Show(sender);
                return;
        }
    }

    @Override
    public List<String> getArguments(@NotNull CommandSender sender, String[] args) {
        List<String> optionList = new ArrayList<>();

        switch(args.length){
            case 2:
                optionList.add("Fish");
                optionList.add("Rarities");
                optionList.add("Areas");
                optionList.add("Treasure");
                optionList.add("Translations");
                optionList.add("General");
                optionList.add("Tournaments");
                break;
            case 3:
                switch(args[1].toUpperCase()){
                    case "AREAS":
                        for (var a : FishingArea.GetAll()){
                            optionList.add(a.Id);
                        }
                        break;
                    case "FISH":
                        for(var f : FishType.GetAll()){
                            optionList.add(f.Id);
                        }
                        break;
                    case "RARITIES":
                        for(var r : Rarity.GetAll()){
                            optionList.add(r.Id);
                        }
                        break;
                    case "TOURNAMENTS":
                        for(var t : TournamentType.GetTournaments()){
                            optionList.add(t.Id);
                        }
                        break;
                }
            case 4:
                switch(args[1].toUpperCase()){
                    case "TOURNAMENTS":
                        if(TournamentType.IdExists(args[2])){
                            optionList.add("Start");
                            optionList.add("Cancel");
                            optionList.add("Finish");
                        }
                }
        }

        return optionList;
    }

    @Override
    public String getPermissions() {
        return "bf.admin";
    }

    @Override
    public List<String> getAliases() {return List.of("A");}

}
