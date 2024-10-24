package com.kunfury.blepfishing.ui.buttons.admin.areas;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.objects.FishingArea;
import com.kunfury.blepfishing.ui.objects.buttons.AdminAreaMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.areas.AdminAreasEditBiomesPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Comparator;

public class AdminAreaBiomeChoiceBtn extends AdminAreaMenuButton {

    private final Biome biome;
    private final int page;

    public AdminAreaBiomeChoiceBtn(FishingArea area, Biome biome, int page) {
        super(area);
        this.biome = biome;
        this.page = page;
    }

    @Override
    public ItemStack buildItemStack() {
        var active = area.Biomes.contains(biome.toString());
        var activeStr = ChatColor.RED + "Disabled";

        Material mat = Material.RED_CONCRETE;
        if(active){
            mat = Material.GREEN_CONCRETE;
            activeStr = ChatColor.GREEN + "Enabled";
        }


        ItemStack item = new ItemStack(mat);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName(biome.name());
        ArrayList<String> lore = new ArrayList<>();
        lore.add(activeStr);

        lore.add("");
        lore.add(ChatColor.YELLOW + "Left-Click to Edit");
        m.setLore(lore);

        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        dataContainer.set(biomeKey, PersistentDataType.STRING, biome.toString());
        dataContainer.set(pageKey, PersistentDataType.INTEGER, page);

        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        FishingArea area = getArea();
        Biome biome = getBiome();
        var active = area.Biomes.contains(biome.toString());
        if(active)
            area.Biomes.remove(biome.toString());
        else
            area.Biomes.add(biome.toString());

        var sortedBiomes = area.Biomes.stream()
                .sorted(Comparator.comparing(b -> b)).toList();

        area.Biomes.clear();
        area.Biomes.addAll(sortedBiomes);

        ConfigHandler.instance.areaConfig.Save();
        new AdminAreasEditBiomesPanel(area, getPage()).Show(player);

    }


}
