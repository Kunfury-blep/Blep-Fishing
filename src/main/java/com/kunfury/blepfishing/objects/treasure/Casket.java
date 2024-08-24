package com.kunfury.blepfishing.objects.treasure;

import com.gmail.nossr50.datatypes.treasure.Treasure;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.items.ItemHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Casket extends TreasureType {

    public List<TreasureReward> Rewards;
    public final double CashReward;
    public String Name;

    public Casket(String id, String name, int weight, boolean announce, List<TreasureReward> rewards, double cashReward) {
        super(id, weight, announce);

        Bukkit.getLogger().warning(id + " - " + weight);

        Rewards = rewards;
        CashReward = cashReward;
        Name = name;
    }


    @Override
    public ItemStack GetItem() {
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

    @Override
    public boolean CanGenerate(Player player) {
        return true;
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



    public static List<Casket> GetAll(){
        List<Casket> caskets = new ArrayList<>();

        for(var t : ActiveTypes.values()){
            if(!(t instanceof Casket))
                continue;

            caskets.add((Casket)t);
        }

        return caskets;
    }

    public static Casket FromId(String typeId){
        var treasureType = TreasureType.FromId(typeId);

        if(treasureType instanceof Casket)
            return (Casket)treasureType;

        Bukkit.getLogger().warning("Tried to get invalid Casket with ID: " + typeId);
        return null;
    }

}
