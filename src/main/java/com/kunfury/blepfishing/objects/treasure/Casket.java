package com.kunfury.blepfishing.objects.treasure;

import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.helpers.TreasureHandler;
import com.kunfury.blepfishing.helpers.Utilities;
import com.kunfury.blepfishing.helpers.ItemHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Casket extends TreasureType {

    public List<TreasureReward> Rewards;
    public final double CashReward;
    public String Name;

    public Casket(String id, String name, int weight, boolean announce, List<TreasureReward> rewards, double cashReward) {
        super(id, weight, announce);
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

        lore.add(Formatting.GetLanguageString("Treasure.Casket.use"));

        itemMeta.setLore(lore);

        item.setItemMeta(itemMeta);

        return  item;
    }

    @Override
    public ItemStack GetItem(PlayerFishEvent e) {

        Database.TreasureDrops.Add(new TreasureDrop
                (Id, e.getPlayer().getUniqueId().toString(), LocalDateTime.now()));

        return GetItem();
    }

    @Override
    public boolean CanGenerate(Player player) {
        return true;
    }

    @Override
    protected void Use(ItemStack item, Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, .30f, 1f);

        if(Rewards.isEmpty()){
            Utilities.Severe(player.getDisplayName() + " tried to open " + Id + " Casket which contained no drops.");
        }

        List<ItemStack> treasureDrops = new ArrayList<>();
        int i = 0;
        while(treasureDrops.isEmpty()){ //Ensures at least one item drops
            i++;
            if(i >= 1000){
                Utilities.Severe("No Casket Rewards found after 1,000 iterations on " + Id);
                return;
            }
            for(var reward : Rewards){
                if(!reward.Drops())
                    continue;

                treasureDrops.add(reward.Item);
            }
        }

        Inventory inv = Bukkit.createInventory(player, Utilities.getInventorySize(treasureDrops.size()), Formatting.formatColor(Name));

        for(var itemDrop : treasureDrops){
            inv.addItem(itemDrop);

            player.sendMessage(Formatting.GetLanguageString("Treasure.Casket.result")
                    .replace("{item}", Formatting.GetItemName(itemDrop) + " x" + itemDrop.getAmount())
                    .replace("{casket}", getFormattedName()));
        }

        item.setAmount(item.getAmount() - 1);

        player.openInventory(inv);
        TreasureHandler.OpenInventories.put(player, inv);
    }

    @Override
    public String getFormattedName() {
        return Formatting.formatColor(Name);
    }

    private static List<Casket> Caskets;
    private static int TotalCasketWeight = 0;

    public static List<Casket> GetAll(){
        if(Caskets != null)
            return Caskets;

        Caskets = new ArrayList<>();
        TotalCasketWeight = 0;
        for(var t : ActiveTreasureTypes.values()){
            if(!(t instanceof Casket))
                continue;

            Caskets.add((Casket)t);
            TotalCasketWeight += t.Weight;
        }

        return Caskets;
    }

    public static Casket RollCasket(){
        int randR = ThreadLocalRandom.current().nextInt(0, TotalCasketWeight);

        var treasures = new ArrayList<>(TreasureType.ActiveTreasureTypes.values());

        treasures.sort(Comparator.comparingInt((TreasureType t) -> t.Weight));

        for(var casket : GetAll()){
            if(randR <= casket.Weight) {
                return casket;
            }else
                randR -= casket.Weight;
        }
        Utilities.Severe("Error when rolling Treasure Casket");
        return null;
    }

    public static Casket FromId(String typeId){
        var treasureType = TreasureType.FromId(typeId);

        if(treasureType instanceof Casket)
            return (Casket)treasureType;

        Bukkit.getLogger().warning("Tried to get invalid Casket with ID: " + typeId);
        return null;
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
