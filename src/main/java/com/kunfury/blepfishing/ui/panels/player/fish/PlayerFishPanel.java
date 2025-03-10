package com.kunfury.blepfishing.ui.panels.player.fish;

import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.objects.FishObject;
import com.kunfury.blepfishing.objects.FishType;
import com.kunfury.blepfishing.ui.buttons.player.fish.PlayerFishBtn;
import com.kunfury.blepfishing.ui.buttons.player.PlayerPanelBtn;
import com.kunfury.blepfishing.ui.buttons.player.fish.PlayerFishMissingBtn;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.objects.panels.PaginationPanel;
import org.bukkit.entity.Player;

import java.util.List;

public class PlayerFishPanel extends PaginationPanel<FishType> {

    private List<FishObject> caughtFish;

    public PlayerFishPanel(int page) {
        super(Formatting.GetLanguageString("UI.Player.Panels.fish"),
                FishType.GetAll().size() + 9, page, new PlayerPanelBtn());
    }

    @Override
    public void BuildInventory(Player player) {
        caughtFish = Database.Fish.GetCaught(player.getUniqueId().toString());
        super.BuildInventory(player);
    }

    @Override
    protected List<FishType> loadContents() {
        return FishType.GetAll().stream().toList();
    }

    @Override
    protected MenuButton getButton(FishType fishType, Player player) {
        List<FishObject> filteredFish = caughtFish.stream()
                .filter(f -> f.getType().Id.equals(fishType.Id)).toList();

        if(filteredFish.isEmpty())
            return new PlayerFishMissingBtn(fishType);
        else
            return new PlayerFishBtn(fishType, filteredFish);
    }
}