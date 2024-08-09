package com.kunfury.blepfishing.objects;


import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.helpers.Utilities;
import com.kunfury.blepfishing.ui.panels.FishBagPanel;
import com.kunfury.blepfishing.items.ItemHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.*;

public class FishBag {
    public int Id = -1;
    public boolean Pickup = true;
    private int amount;
    private int tier;

    public FishBag(){}

    public FishBag(int id, int amount, int tier, boolean pickup){
        Id = id;
        this.amount = amount;
        this.tier = tier;
        Pickup = pickup;
    }

    public FishBag(ResultSet rs, int _amount) throws SQLException {
        Id = rs.getInt("id");
        tier = rs.getInt("tier");
        Pickup = rs.getBoolean("pickup");
        amount = _amount;
    }

    public void AssignId(int id){
        if(Id != -1){
            Bukkit.getLogger().severe("Tried to overwrite fishing bag of id " + Id);
            return;
        }
        Id = id;
    }

    public void Use(Player p){
        new FishBagPanel(this, 1).Show(p);
        p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, .3f, 1f);
    }


    public void UpdateBagItem(ItemStack bagItem){

        RequestUpdate();
        ItemMeta m = bagItem.getItemMeta();
        assert m != null;
        m.setLore(GenerateLore());
        m.getPersistentDataContainer().set(ItemHandler.FishBagId, PersistentDataType.INTEGER, Id);

        bagItem.setItemMeta(m);
    }

    public int getAmount(){ return amount; }
    public int getTier() { return tier; }

    public void TogglePickup(ItemStack bag, Player player){
        Pickup = !Pickup;

        Database.FishBags.Update(Id, "pickup", Pickup);

        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);

        if(Pickup){
            if(Bukkit.getServer().getVersion().contains("1.20.6"))
                bag.addUnsafeEnchantment(Enchantment.UNBREAKING, 1);

            player.sendMessage(Formatting.getFormattedMessage("Equipment.Fish Bag.pickupEnabled"));
        }else{
            if(Bukkit.getServer().getVersion().contains("1.20.6"))
                bag.removeEnchantment(Enchantment.UNBREAKING);
            player.sendMessage(Formatting.getFormattedMessage("Equipment.Fish Bag.pickupDisabled"));
        }

        player.getInventory().setItemInMainHand(bag);
    }

    public void FillFromInventory(ItemStack bag, Player player){
        int fillAmt = 0;


        for(var item : player.getInventory().getStorageContents()){
            if(getAmount() + fillAmt >= getMax()) break;
            if(item != null && item.getType() == ItemHandler.FishMat && ItemHandler.hasTag(item, ItemHandler.FishIdKey)){
                Deposit(item, player);
                fillAmt++;
            }
        }

        if(fillAmt > 0){
            player.sendMessage(Formatting.getFormattedMessage("Equipment.Fish Bag.addFish")
                    .replace("{amount}", String.valueOf(fillAmt)));
        }else{
            if(getAmount() >= getMax())
                player.sendMessage(Formatting.getFormattedMessage("Equipment.Fish Bag.noSpace"));
            else
                player.sendMessage(Formatting.getFormattedMessage("Equipment.Fish Bag.noFish"));
        }
        UpdateBagItem(bag);
    }

    public void Deposit(ItemStack item, Player player){
        FishObject fish = FishObject.GetCaughtFish(ItemHandler.getTagInt(item, ItemHandler.FishIdKey));
        if(fish == null){
            Bukkit.getLogger().warning("Null fish found");
            return;
        }
        AddFish(fish);

        player.getInventory().remove(item);
        player.playSound(player.getLocation(), Sound.ENTITY_SALMON_FLOP, .25f, .25f);
        amount++;
    }

    public ArrayList<String> GenerateLore() {
        DecimalFormat formatter = new DecimalFormat("#,###");
        double maxSize = getMax();

        ArrayList<String> lore = new ArrayList<>();

        lore.add(Formatting.getMessage("Equipment.Fish Bag.descSmall")); //TODO: Change to dynamic based on size of bag
        lore.add("");


        double barScore = 0;

        if(amount != 0 || maxSize != 0){
            barScore = 10 * (amount / maxSize);
        }

        StringBuilder progressBar = new StringBuilder();

        for (int i = 1; i <= barScore; i++) {
            progressBar.append(ChatColor.GREEN + "|");
        }
        for (int i = 1; i < 10 - barScore; i++) {
            progressBar.append(ChatColor.WHITE + "|");
        }

        lore.add(progressBar + " " + formatter.format(amount) + "/" + formatter.format(maxSize));

//        if(tier < CraftingHandler.UpgradeItems.length){
//            lore.add("");
//            if (isFull()) {
//                lore.add(Formatting.getMessage("Equipment.Fish Bag.upgradeReady")
//                        .replace("{upgrade}", getUpgradeComp(bag).getType().name().replace("_", " ")));
//            } else{
//                lore.add(Formatting.getMessage("Equipment.Fish Bag.upgradeHint"));
//            }
//        }

        lore.add("");
        lore.add(Formatting.getMessage("Equipment.Fish Bag.autoPickup"));
        lore.add(Formatting.getMessage("Equipment.Fish Bag.depositAll"));
        lore.add(Formatting.getMessage("Equipment.Fish Bag.openBag"));
        lore.add(Formatting.getMessage("Equipment.Fish Bag.openPanel"));

        return lore;
    }

    public int getMax(){
        //return (10 * tier); //This is for testing purposes to be able to easily upgrade the bag
        return (int) (256 * Math.pow(tier, 4 ));
    }

    public boolean isFull(){
        return getMax() <= amount;
    }

    public void AddFish(FishObject fish){
        fish.setFishBagId(Id);
        NeedsRefresh = true;
//        FileHandler.Equipment = true;
    }
    public void RemoveFish(FishObject fish){
        fish.setFishBagId(null);
        NeedsRefresh = true;
    //    FileHandler.Equipment = true;
    }
    private List<FishObject> fishList = new ArrayList<>();
    public List<FishObject> getFish(){
        RequestUpdate();
        return fishList;
    }

    public boolean NeedsRefresh = true;
    private void RequestUpdate(){
        if(NeedsRefresh){
            fishList = Database.FishBags.GetAllFish(Id).stream().sorted(Comparator.comparingDouble(FishObject::getScore)).toList();

            NeedsRefresh = false;
            amount = fishList.size();
        }
    }

    public void Withdraw(Player player, FishType type, ItemStack bagItem, boolean large, boolean single, int page){
        var filteredFishList = new ArrayList<>(getFish().stream().filter(f -> Objects.equals(f.TypeId, type.Id)).toList());

        if(!filteredFishList.isEmpty()){
            int freeSlots = Utilities.getFreeSlots(player.getInventory());

            if(single && freeSlots > 1) freeSlots = 1;
            else if(freeSlots > filteredFishList.size()) freeSlots = filteredFishList.size();
            if(large)  Collections.reverse(filteredFishList);

            for(int i = 0; i < freeSlots; i++){
                FishObject fish = filteredFishList.get(i);
                RemoveFish(fish);
                player.getInventory().addItem(fish.CreateItemStack());
                player.playSound(player.getLocation(), Sound.ENTITY_SALMON_FLOP, .5f, 1f);
            }
            UpdateBagItem(bagItem);

            new FishBagPanel(this, page).Show(player);
        }
    }





    ///
    //Static Methods
    ///


//    public static void AddNew(FishBag fishBag, boolean requestSave){
//        if(FishBags.containsKey(fishBag.Id)){
//            Bukkit.getLogger().warning("Attempted to create duplicate Fish Bag with ID: " + fishBag.Id);
//            return;
//        }
//
//        //FishBags.put(fishBag.Id, fishBag);
//        if(requestSave) FileHandler.Equipment = true;
//    }
//    public static Collection<FishBag> GetAll(){
//        return FishBags.values();
//    }
    public static FishBag GetBag(int bagId){
        return Database.FishBags.Get(bagId);
    }

    private static final HashMap<Integer, FishBag> FishBags = new HashMap<>();
    public static FishBag GetBag(ItemStack bagItem){
        if(!ItemHandler.hasTag(bagItem, ItemHandler.FishBagId))
            return null;

        int bagId = ItemHandler.getTagInt(bagItem, ItemHandler.FishBagId);
        if(FishBags.containsKey(bagId))
            return FishBags.get(bagId);

        var db = BlepFishing.getDatabase();
        if(!Database.FishBags.Exists(bagId))
            return null;

        var bag = Database.FishBags.Get(bagId);
        FishBags.put(bagId, bag);
        return bag;
    }

    public static boolean IsBag(ItemStack bag){

        if(bag == null || !bag.hasItemMeta())
            return false;

        return bag.getType() == ItemHandler.BagMat
                && bag.getItemMeta().getPersistentDataContainer().has(ItemHandler.FishBagId, PersistentDataType.INTEGER);
    }

    public static ItemStack GetRecipeItem(){
        ItemStack bag = new ItemStack(ItemHandler.BagMat, 1);

        ItemMeta m = bag.getItemMeta();
        assert m != null;

        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        dataContainer.set(ItemHandler.FishBagId, PersistentDataType.INTEGER, -1);
        m.setDisplayName(Formatting.getMessage("Equipment.Fish Bag.smallBag"));

        List<String> lore = new ArrayList<>();
        lore.add(Formatting.getMessage("Equipment.Fish Bag.descSmall")); //TODO: Change to dynamic based on size of bag
        lore.add("");
        lore.add(Formatting.getMessage("Equipment.Fish Bag.autoPickup"));
        lore.add(Formatting.getMessage("Equipment.Fish Bag.depositAll"));
        lore.add(Formatting.getMessage("Equipment.Fish Bag.openBag"));
        lore.add(Formatting.getMessage("Equipment.Fish Bag.openPanel"));

        m.setLore(lore);

        m.setCustomModelData(ItemHandler.BagModelData);
        m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        bag.setItemMeta(m);

        var version = Bukkit.getServer().getVersion();
        if(version.contains("1.20.6"))
            bag.addUnsafeEnchantment(Enchantment.UNBREAKING, 1);

        return bag;
    }

    public static Integer GetId(ItemStack bag){
        ItemMeta m = bag.getItemMeta();
        return ItemHandler.getTagInt(bag, ItemHandler.FishBagId);
    }

}
