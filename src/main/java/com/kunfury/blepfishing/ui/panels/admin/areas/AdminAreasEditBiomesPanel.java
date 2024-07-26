package com.kunfury.blepfishing.ui.panels.admin.areas;

import com.kunfury.blepfishing.objects.FishingArea;
import com.kunfury.blepfishing.ui.buttons.admin.areas.*;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.objects.PaginationPanel;
import com.kunfury.blepfishing.ui.objects.Panel;
import org.bukkit.Bukkit;
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
    protected List<Biome> getContents() {
        return sortedBiomes;
    }

    @Override
    protected MenuButton getButton(Biome object) {
        return new AdminAreaBiomeChoiceBtn(area, object, Page);
    }
}
