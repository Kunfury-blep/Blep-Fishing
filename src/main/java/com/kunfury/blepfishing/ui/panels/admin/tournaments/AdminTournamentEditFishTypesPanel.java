package com.kunfury.blepfishing.ui.panels.admin.tournaments;

import com.kunfury.blepfishing.objects.FishingArea;
import com.kunfury.blepfishing.ui.buttons.admin.areas.AdminAreaBtn;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.objects.Panel;
import com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit.AdminTournamentButton;
import com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit.TournamentEditFishTypeChoiceBtn;
import com.kunfury.blepfishing.objects.FishType;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.ui.objects.panels.PaginationPanel;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.List;

public class AdminTournamentEditFishTypesPanel extends PaginationPanel<FishType> {

    TournamentType type;
    public AdminTournamentEditFishTypesPanel(TournamentType type, int page){
        super(type.Name + " Fish Types", FishType.GetAll().size() + 1, page, new AdminTournamentButton(type));
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

    @Override
    protected List<FishType> loadContents() {
        return FishType.GetAll().stream()
                .sorted(Comparator.comparing(fish -> fish.Name)).toList();
    }

    @Override
    protected MenuButton getButton(FishType object, Player player) {
        return new TournamentEditFishTypeChoiceBtn(type, object);
    }
}
