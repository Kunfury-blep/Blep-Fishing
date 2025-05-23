package com.kunfury.blepfishing.ui.panels.admin.treasure;

import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.objects.treasure.Casket;
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
        AddButton(new CasketEditIdBtn(casket), player);
        AddButton(new CasketEditNameBtn(casket), player);
        AddButton(new CasketEditWeightBtn(casket), player);
        AddButton(new CasketEditAnnounceBtn(casket), player);
        AddButton(new CasketEditRewardsBtn(casket), player);

        AddFooter(new AdminTreasureCasketsPanelBtn(), null, null, player);
    }
}
