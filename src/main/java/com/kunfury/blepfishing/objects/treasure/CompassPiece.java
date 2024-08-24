package com.kunfury.blepfishing.objects.treasure;

import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.items.ItemHandler;
import com.kunfury.blepfishing.objects.FishingArea;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class CompassPiece extends TreasureType{

    public CompassPiece(String id, int weight, boolean announce) {
        super(id, weight, announce);
    }

    @Override
    public ItemStack GetItem() {
        ItemStack item = new ItemStack(Material.PRISMARINE_SHARD);
        ItemMeta itemMeta = item.getItemMeta();

        assert itemMeta != null;

        itemMeta.getPersistentDataContainer().set(ItemHandler.TreasureTypeId, PersistentDataType.STRING, Id);
        itemMeta.setDisplayName(Formatting.formatColor("Compass Piece"));

        List<String> lore = new ArrayList<>();

        lore.add("");
        lore.add(Formatting.formatColor("&bRight-Click to &o&eFocus"));

        itemMeta.setLore(lore);

        item.setItemMeta(itemMeta);

        return  item;
    }

    @Override
    public boolean CanGenerate(Player player){
        return true;
    }
}
