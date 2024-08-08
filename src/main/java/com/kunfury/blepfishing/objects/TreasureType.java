package com.kunfury.blepfishing.objects;

import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.items.ItemHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class TreasureType {
    public String Id;
    public String Name;
    public int Weight;
//    public final List<ItemStack> OldRewards;
    public List<TreasureReward> Rewards;
    public final double CashReward;

    public boolean ConfirmedDelete;
    public boolean Announce;

    public TreasureType(String id, String name, int weight, boolean announce, List<TreasureReward> rewards, double cashReward){
        Id = id;
        Name = name;
        Weight = weight;
        Rewards = rewards;
        CashReward = cashReward;
        Announce = announce;
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

    public static boolean IsTreasure(ItemStack item){
        return ItemHandler.hasTag(item, ItemHandler.TreasureTypeId);
    }



    private static final HashMap<String, TreasureType> ActiveTypes = new HashMap<>();
    private static int totalWeight = 0;

    public static void AddNew(TreasureType treasureType) {
        if(ActiveTypes.containsKey(treasureType.Id)){
            Bukkit.getLogger().warning("Attempted to create duplicate Treasure Type with ID: " + treasureType.Id);
            return;
        }

        //Bukkit.getLogger().warning("Adding new treasure: " + treasureType.Name + ". Item Count: " + treasureType.Rewards.size());

        ActiveTypes.put(treasureType.Id, treasureType);
        totalWeight += treasureType.Weight;
    }

    public static int GetTotalWeight(){
        return totalWeight;
    }

    public static Collection<TreasureType> GetAll(){
        return ActiveTypes.values();
    }

    public static TreasureType FromId(String typeId){
        if(ActiveTypes.containsKey(typeId)){
            return ActiveTypes.get(typeId);
        }

        Bukkit.getLogger().warning("Tried to get invalid Treasure with ID: " + typeId);
        return null;
    }

    public static boolean IdExists(String id){
        return ActiveTypes.containsKey(id);
    }

    public static void UpdateId(String oldId, TreasureType type){
        ActiveTypes.remove(oldId);
        ActiveTypes.put(type.Id, type);
    }

    public static void Delete(TreasureType type){
        ActiveTypes.remove(type.Id);
    }


    public static class TreasureReward{
        public double DropChance;
        public ItemStack Item;
        public boolean Announce;
        public final double Cash;
        public boolean ConfirmedDelete;

        public TreasureReward(double dropChance, ItemStack item, boolean announce, double cash){
            DropChance = dropChance;
            Item = item;
            Announce = announce;
            Cash = cash;
        }

        public boolean Drops(){
            Random rand = new Random();
            var roll = 100 * rand.nextDouble();

            return roll < DropChance;
        }
    }
}
