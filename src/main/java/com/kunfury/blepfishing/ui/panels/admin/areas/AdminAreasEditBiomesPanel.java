package com.kunfury.blepfishing.ui.panels.admin.areas;

import com.kunfury.blepfishing.objects.FishingArea;
import com.kunfury.blepfishing.ui.buttons.admin.areas.*;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.objects.panels.PaginationPanel;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class AdminAreasEditBiomesPanel extends PaginationPanel<Biome> {

    private static final List<Biome> sortedBiomes = Arrays.stream(Biome.values())
            .sorted(Comparator.comparing(Enum::name)).toList();

    FishingArea area;
    public AdminAreasEditBiomesPanel(FishingArea area, int page){
        super("Edit " + area.Name + " Biomes", Biome.values().length, page, new AdminAreaBtn(area));
        this.area = area;
    }

    @Override
    protected List<Biome> loadContents() {
        return sortedBiomes;
    }

    @Override
    protected MenuButton getButton(Biome object, Player player) {
        return new AdminAreaBiomeChoiceBtn(area, object, Page);
    }
}
