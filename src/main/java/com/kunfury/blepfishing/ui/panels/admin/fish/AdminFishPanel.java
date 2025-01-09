package com.kunfury.blepfishing.ui.panels.admin.fish;

import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.objects.panels.PaginationPanel;
import com.kunfury.blepfishing.ui.buttons.admin.fishEdit.AdminFishButton;
import com.kunfury.blepfishing.ui.buttons.admin.AdminPanelButton;
import com.kunfury.blepfishing.ui.buttons.admin.fishEdit.AdminFishCreateButton;
import com.kunfury.blepfishing.objects.FishType;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.List;

public class AdminFishPanel extends PaginationPanel<FishType> {

    public AdminFishPanel(int page) {
        super("Fish Admin Panel", FishType.GetAll().size() + 9, page, new AdminPanelButton());
    }

    @Override
    public void BuildInventory(Player player) {
        super.BuildInventory(player);

        inv.setItem(InventorySize - 9, new AdminFishCreateButton().getItemStack(player));
    }

    @Override
    protected List<FishType> loadContents() {
        return FishType.GetAll().stream()
                .sorted(Comparator.comparing(FishType::getId, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    @Override
    protected MenuButton getButton(FishType type, Player player) {
        return new AdminFishButton(type, Page);
    }
}