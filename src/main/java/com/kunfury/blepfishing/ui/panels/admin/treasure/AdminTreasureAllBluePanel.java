package com.kunfury.blepfishing.ui.panels.admin.treasure;

import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.helpers.TreasureHandler;
import com.kunfury.blepfishing.objects.treasure.Casket;
import com.kunfury.blepfishing.objects.treasure.CompassPiece;
import com.kunfury.blepfishing.objects.treasure.TreasureType;
import com.kunfury.blepfishing.ui.buttons.admin.treasure.*;
import com.kunfury.blepfishing.ui.objects.Panel;
import org.bukkit.entity.Player;

import java.util.Comparator;

public class AdminTreasureAllBluePanel extends Panel {
    public AdminTreasureAllBluePanel() {
        super(Formatting.GetLanguageString("UI.Admin.Panels.Treasure.allBlue"), 10);
    }

    @Override
    public void BuildInventory(Player player) {

        CompassPiece compassPiece = (CompassPiece) CompassPiece.FromId("compassPiece");
        AddButton(new AllBlueEditWeightBtn(compassPiece), player);
        AddButton(new AllBlueEditAnnounceBtn(compassPiece), player);

        AddFooter(new AdminTreasurePanelButton(), null, null, player);
    }
}