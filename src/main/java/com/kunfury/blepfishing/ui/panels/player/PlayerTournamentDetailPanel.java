package com.kunfury.blepfishing.ui.panels.player;

import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.objects.FishObject;
import com.kunfury.blepfishing.objects.TournamentObject;
import com.kunfury.blepfishing.ui.buttons.player.tournament.PlayerTournamentFishBtn;
import com.kunfury.blepfishing.ui.buttons.player.tournament.PlayerTournamentPanelBtn;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.objects.panels.PaginationPanel;
import org.bukkit.entity.Player;

import java.util.List;

public class PlayerTournamentDetailPanel extends PaginationPanel<FishObject> {

    private final TournamentObject tournament;

    public PlayerTournamentDetailPanel(TournamentObject tourney, int page) {
        super(Formatting.GetLanguageString("UI.Player.Panels.tournamentDetail")
                        .replace("{name}", tourney.getType().Name)
                        .replace("{amount}", String.valueOf(tourney.getWinningFish().size())),
                tourney.getWinningFish().size() + 9, page, new PlayerTournamentPanelBtn());
        tournament = tourney;
    }

    @Override
    public void BuildInventory(Player player) {
        super.BuildInventory(player);

        AddFooter(new PlayerTournamentPanelBtn(), null, null);
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