package com.kunfury.blepfishing.ui.panels.admin.fish;

import com.kunfury.blepfishing.ui.buttons.admin.areas.AdminAreaBiomeChoiceBtn;
import com.kunfury.blepfishing.ui.buttons.admin.fishEdit.AdminFishPanelButton;
import com.kunfury.blepfishing.ui.buttons.footer.PageChangeBtn;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.objects.PaginationPanel;
import com.kunfury.blepfishing.ui.objects.Panel;
import com.kunfury.blepfishing.ui.buttons.admin.fishEdit.AdminFishButton;
import com.kunfury.blepfishing.ui.buttons.admin.AdminPanelButton;
import com.kunfury.blepfishing.ui.buttons.admin.fishEdit.AdminFishCreateButton;
import com.kunfury.blepfishing.objects.FishType;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AdminFishPanel extends PaginationPanel<FishType> {

    public AdminFishPanel(int page) {
        super("Fish Admin Panel", FishType.GetAll().size() + 9, page, new AdminPanelButton());
    }

    @Override
    public void BuildInventory(Player player) {
        super.BuildInventory(player);

        inv.setItem(InventorySize - 9, new AdminFishCreateButton().getItemStack());
    }

    @Override
    protected List<FishType> getContents() {
        return FishType.GetAll().stream()
                .sorted(Comparator.comparing(FishType::getId, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    @Override
    protected MenuButton getButton(FishType type) {
        return new AdminFishButton(type, Page);
    }
}