package com.kunfury.blepfishing.ui.panels.admin.treasure;

import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.objects.treasure.Casket;
import com.kunfury.blepfishing.objects.treasure.TreasureType;
import com.kunfury.blepfishing.ui.buttons.admin.treasure.*;
import com.kunfury.blepfishing.ui.objects.Panel;
import org.bukkit.entity.Player;

public class AdminTreasureEditPanel extends Panel {

    Casket casket;
    public AdminTreasureEditPanel(Casket casket){
        super(Formatting.GetLanguageString("UI.Admin.Panels.Treasure.editCasket")
                .replace("{casket}", casket.Name), 18);
        this.casket = casket;
    }

    @Override
    public void BuildInventory(Player player) {
        AddButton(new TreasureEditNameBtn(casket), player);
        AddButton(new TreasureEditWeightBtn(casket), player);
        AddButton(new TreasureEditAnnounceBtn(casket), player);
        AddButton(new TreasureEditRewardsBtn(casket), player);

        inv.setItem(17, new AdminTreasurePanelButton().getBackButton(player));
    }
}
