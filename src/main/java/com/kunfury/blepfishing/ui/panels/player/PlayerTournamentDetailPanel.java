package com.kunfury.blepfishing.ui.panels.player;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.objects.FishObject;
import com.kunfury.blepfishing.objects.FishType;
import com.kunfury.blepfishing.objects.TournamentObject;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.ui.buttons.admin.fishEdit.AdminFishButton;
import com.kunfury.blepfishing.ui.buttons.player.tournament.PlayerTournamentButton;
import com.kunfury.blepfishing.ui.buttons.player.tournament.PlayerTournamentFishBtn;
import com.kunfury.blepfishing.ui.buttons.player.tournament.PlayerTournamentPanelButton;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.objects.Panel;
import com.kunfury.blepfishing.ui.objects.panels.PaginationPanel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PlayerTournamentDetailPanel extends PaginationPanel<FishObject> {

    private final TournamentObject tournament;

    public PlayerTournamentDetailPanel(TournamentObject tourney, int page) {
        super(Formatting.GetLanguageString("UI.Player.Panels.tournamentDetail")
                        .replace("{name}", tourney.getType().Name)
                        .replace("{amount}", String.valueOf(tourney.getWinningFish().size())),
                tourney.getWinningFish().size() + 9, page, new PlayerTournamentPanelButton());
        tournament = tourney;
    }

    @Override
    public void BuildInventory(Player player) {
        super.BuildInventory(player);

        AddFooter(new PlayerTournamentPanelButton(), null, null);
    }

    @Override
    protected List<FishObject> loadContents() {
        return tournament.getWinningFish();
    }

    @Override
    protected MenuButton getButton(FishObject object)  {
        return new PlayerTournamentFishBtn(object, tournament.getWinningFish().indexOf(object) + 1);
    }
}