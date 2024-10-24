package com.kunfury.blepfishing.ui.panels.player;

import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.objects.FishObject;
import com.kunfury.blepfishing.objects.FishType;
import com.kunfury.blepfishing.ui.buttons.player.fish.PlayerFishBtn;
import com.kunfury.blepfishing.ui.buttons.player.PlayerPanelBtn;
import com.kunfury.blepfishing.ui.buttons.player.fish.PlayerFishMissingBtn;
import com.kunfury.blepfishing.ui.buttons.player.fish.PlayerFishPanelBtn;
import com.kunfury.blepfishing.ui.objects.Panel;
import org.bukkit.entity.Player;

import java.util.List;

public class PlayerFishPanel extends Panel {

    public PlayerFishPanel() {
        super(Formatting.GetLanguageString("UI.Player.Panels.fish"), FishType.GetAll().size() + 9);
    }

    @Override
    public void BuildInventory(Player player) {
        List<FishObject> caughtFish = Database.Fish.GetCaught(player.getUniqueId().toString());

        for(var type : FishType.GetAll()){
            List<FishObject> filteredFish = caughtFish.stream()
                            .filter(f -> f.getType() == type).toList();

            if(filteredFish.isEmpty()){
                AddButton(new PlayerFishMissingBtn(type));
                continue;
            }

            AddButton(new PlayerFishBtn(type, filteredFish));
        }
//
        AddFooter(new PlayerPanelBtn(), null, null);
    }
}