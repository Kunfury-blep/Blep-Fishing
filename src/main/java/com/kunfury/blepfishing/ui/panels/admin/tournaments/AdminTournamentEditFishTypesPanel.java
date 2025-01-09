package com.kunfury.blepfishing.ui.panels.admin.tournaments;

import com.kunfury.blepfishing.ui.objects.Panel;
import com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit.AdminTournamentButton;
import com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit.TournamentEditFishTypeChoiceBtn;
import com.kunfury.blepfishing.objects.FishType;
import com.kunfury.blepfishing.objects.TournamentType;
import org.bukkit.entity.Player;

import java.util.Comparator;

public class AdminTournamentEditFishTypesPanel extends Panel {

    TournamentType type;
    public AdminTournamentEditFishTypesPanel(TournamentType type){
        super(type.Name + " Fish Types", FishType.GetAll().size() + 1);
        this.type = type;
    }

    @Override
    public void BuildInventory(Player player) {

        var sortedFishList = FishType.GetAll().stream()
                .sorted(Comparator.comparing(fish -> fish.Name)).toList();

        for(var fishType : sortedFishList){
            inv.addItem(new TournamentEditFishTypeChoiceBtn(type, fishType).getItemStack(player));
        }

        AddFooter(new AdminTournamentButton(type), null, null, player);
    }
}
