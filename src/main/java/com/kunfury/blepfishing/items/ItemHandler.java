package com.kunfury.blepfishing.items;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.objects.FishObject;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.units.qual.N;

import javax.naming.Name;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemHandler {

    public static NamespacedKey FishIdKey, ButtonIdKey, FishTypeId,
            FishBagId, FishRodId, FishAreaId, TourneyTypeId, TourneyId, RarityId;

    public static Material FishMat = Material.SALMON;
    public static Material BagMat = Material.HEART_OF_THE_SEA;
    public static int BagModelData = 1;

    public static void Initialize(){
        FishIdKey = new NamespacedKey(BlepFishing.getPlugin(), "blep.fishId");
        ButtonIdKey = new NamespacedKey(BlepFishing.getPlugin(), "blep.buttonId");

        FishBagId = new NamespacedKey(BlepFishing.getPlugin(), "blep.fishBagId");
        FishTypeId = new NamespacedKey(BlepFishing.getPlugin(), "blep.fishTypeId");
        FishRodId = new NamespacedKey(BlepFishing.getPlugin(), "blep.fishRodId");
        FishAreaId = new NamespacedKey(BlepFishing.getPlugin(), "blep.fishAreaId");

        TourneyTypeId = new NamespacedKey(BlepFishing.getPlugin(), "blep.tourneyTypeId");
        TourneyId = new NamespacedKey(BlepFishing.getPlugin(), "blep.tourneyId");

        RarityId = new NamespacedKey(BlepFishing.getPlugin(), "blep.rarityId");
    }

    public static boolean hasTag(ItemStack item, NamespacedKey key){
        if(item == null || !item.hasItemMeta()) return false;

        ItemMeta m = item.getItemMeta();
        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        if(dataContainer.isEmpty()) return false;


        return dataContainer.has(key, PersistentDataType.STRING) || dataContainer.has(key, PersistentDataType.INTEGER)
                || dataContainer.has(key, PersistentDataType.BOOLEAN);
    }

    public static String getTagString(ItemStack item, NamespacedKey key){
        ItemMeta m = item.getItemMeta();
        if(m == null) return "";
        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        if(dataContainer.isEmpty()) return "";
        if(!dataContainer.has(key, PersistentDataType.STRING)) return "";

        return dataContainer.get(key, PersistentDataType.STRING);
    }

    public static int getTagInt(ItemStack item, NamespacedKey key){
        ItemMeta m = item.getItemMeta();
        if(m == null) return 0;
        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        if(dataContainer.isEmpty()) return 0;
        if(!dataContainer.has(key, PersistentDataType.INTEGER)) return 0;

        return dataContainer.get(key, PersistentDataType.INTEGER);
    }

    public static boolean getTagBool(ItemStack item, NamespacedKey key){
        ItemMeta m = item.getItemMeta();
        if(m == null) return false;
        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        if(dataContainer.isEmpty()) return false;
        if(!dataContainer.has(key, PersistentDataType.BOOLEAN)) return false;

        return dataContainer.get(key, PersistentDataType.BOOLEAN);
    }
}
