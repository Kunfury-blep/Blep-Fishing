package com.kunfury.blepfishing.objects;

import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.items.ItemHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class TreasureType {
    public final String Id;
    public final String Name;
    public final int Weight;
    public final List<ItemStack> Rewards;
    public final double CashReward;

    public TreasureType(String id, String name, int weight, List<ItemStack> rewards, double cashReward){
        Id = id;
        Name = name;
        Weight = weight;
        Rewards = rewards;
        CashReward = cashReward;
    }

    public ItemStack GetItem(){
        ItemStack item = new ItemStack(Material.CHEST);
        ItemMeta itemMeta = item.getItemMeta();

        assert itemMeta != null;

        itemMeta.getPersistentDataContainer().set(ItemHandler.TreasureTypeId, PersistentDataType.STRING, Id);
        itemMeta.setDisplayName(Formatting.formatColor(Name));

        List<String> lore = new ArrayList<>();

        lore.add("");
        lore.add(Formatting.formatColor("&bRight-Click to &o&eOpen"));

        itemMeta.setLore(lore);

        item.setItemMeta(itemMeta);

        return  item;
    }



    private static final HashMap<String, TreasureType> ActiveTypes = new HashMap<>();

    public static void AddNew(TreasureType treasureType) {
        if(ActiveTypes.containsKey(treasureType.Id)){
            Bukkit.getLogger().warning("Attempted to create duplicate Treasure Type with ID: " + treasureType.Id);
            return;
        }

        Bukkit.getLogger().warning("Adding new treasure: " + treasureType.Name + ". Item Count: " + treasureType.Rewards.size());

        ActiveTypes.put(treasureType.Id, treasureType);
    }

    public static Collection<TreasureType> GetAll(){
        return ActiveTypes.values();
    }
}
