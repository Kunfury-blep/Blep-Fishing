package com.kunfury.blepfishing.ui.panels.admin.treasure;

import com.kunfury.blepfishing.objects.treasure.Casket;
import com.kunfury.blepfishing.objects.treasure.TreasureType;
import com.kunfury.blepfishing.ui.buttons.admin.treasure.*;
import com.kunfury.blepfishing.ui.objects.Panel;
import org.bukkit.entity.Player;

public class AdminTreasureEditPanel extends Panel {

    Casket casket;
    public AdminTreasureEditPanel(Casket casket){
        super("Edit " + casket.Name, 18);
        this.casket = casket;
    }

    @Override
    public void BuildInventory(Player player) {
        AddButton(new TreasureEditNameBtn(casket));
        AddButton(new TreasureEditWeightBtn(casket));
        AddButton(new TreasureEditAnnounceBtn(casket));
        AddButton(new TreasureEditRewardsBtn(casket));

        inv.setItem(17, new AdminTreasurePanelButton().getBackButton());
    }
}
