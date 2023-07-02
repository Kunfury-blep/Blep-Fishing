package com.kunfury.blepFishing.Crafting.Equipment.FishBag;

import com.kunfury.blepFishing.Config.FileHandler;
import com.kunfury.blepFishing.Config.ItemsConfig;
import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Miscellaneous.NBTEditor;
import com.kunfury.blepFishing.Miscellaneous.Utilities;
import com.kunfury.blepFishing.Objects.FishObject;
import net.minecraft.world.item.Items;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.kunfury.blepFishing.Crafting.CraftingManager.BagSetup;

public class UseFishBag {
    public void UseBag(ItemStack bag, Player p){
        String bagId = BagInfo.getId(bag);
        new UpdateBag().ShowBagInv(p, bagId, bag);
        p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, .3f, 1f);
    }


    public void FillBag(ItemStack bag, Player p){
        int amount = 0;

        int bagAmt = BagInfo.getAmount(bag);
        int bagMax = BagInfo.getMax(bag);

        for(var item : p.getInventory().getStorageContents()){
            if(bagAmt >= bagMax) break;
            if(item != null && item.getType() == ItemsConfig.FishMat && NBTEditor.contains( item,"blep", "item", "fishId" )){
                AddFish(bag, p, item, false);
                amount++;
                bagAmt++;
            }
        }

        if(amount > 0){
            p.sendMessage(Formatting.getFormattedMesage("Equipment.Fish Bag.addFish")
                            .replace("{amount}", String.valueOf(amount)));
        }else{
            if(bagAmt >= bagMax)
                p.sendMessage(Formatting.getFormattedMesage("Equipment.Fish Bag.noSpace"));
            else
                p.sendMessage(Formatting.getFormattedMesage("Equipment.Fish Bag.noFish"));
        }

        new UpdateBag().Update(bag, p, false);
    }

    public void TogglePickup(ItemStack bag, Player p){
        boolean enabled = NBTEditor.getBoolean(bag, "blep", "item", "fishBagAutoPickup" );

        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);

        if(enabled){
            bag = NBTEditor.set(bag, false, "blep", "item", "fishBagAutoPickup" );
            bag.removeEnchantment(Enchantment.DURABILITY);
            p.sendMessage(Formatting.getFormattedMesage("Equipment.Fish Bag.pickupDisabled"));
        }else{
            bag = NBTEditor.set(bag, true, "blep", "item", "fishBagAutoPickup" );
            bag.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
            p.sendMessage(Formatting.getFormattedMesage("Equipment.Fish Bag.pickupEnabled"));
        }

        ItemMeta m = bag.getItemMeta();
        bag.setItemMeta(m);

        p.getInventory().setItemInMainHand(bag);
    }

    /**
     * Moves fish from inventory to bag
     * @param bag
     * @param p
     * @param fish
     */
    public void AddFish(ItemStack bag, Player p, ItemStack fish, boolean bagOpen){
        if(BagInfo.IsFull(bag)){
            p.sendMessage(Formatting.getFormattedMesage("Equipment.Fish Bag.noSpace"));
            return;
        }
        String bagId = BagInfo.getId(bag);
        if(bagId == null || bagId.isEmpty()) return;

        String fishId = NBTEditor.getString(fish, "blep", "item", "fishId");
        FishObject fishObj = new ParseFish().FishFromId(fishId);

        if(fishObj == null){
            p.sendMessage(Formatting.getFormattedMesage("Equipment.Fish Bag.oldVersion"));
            return;
        }

        if(fishObj.getBagID() != null){
            p.sendMessage(Formatting.getFormattedMesage("Equipment.Fish Bag.corruptFish"));
            return;
        }
        fishObj.setBagID(bagId);
        fish.setAmount(0);

        //Variables.UpdateFishData();
        FileHandler.FishData = true;

        new UpdateBag().Update(bag, p, bagOpen);
        p.playSound(p.getLocation(), Sound.ENTITY_PUFFER_FISH_FLOP, .5f, 1f);
    }

    public void FishBagWithdraw(@NotNull ClickType click, String fishName, Player p, ItemStack bag){

        String bagId = BagInfo.getId(bag);

        final List<FishObject> fishObjectList = new ParseFish().RetrieveFish(bagId, fishName);

        boolean large = true;
        boolean single = true;
        switch(click){
            case LEFT:
                large = false;
                single = true;
                break;
            case SHIFT_LEFT:
                large = false;
                single = false;
                break;
            case RIGHT:
                large = true;
                single = true;
                break;
            case SHIFT_RIGHT:
                large = true;
                single = false;
                break;
            default:
                break;
        }

        if(fishObjectList.size() > 0){

            int freeSlots = Utilities.getFreeSlots(p.getInventory());

            if(single && freeSlots > 1) freeSlots = 1;
            else if(freeSlots > fishObjectList.size()) freeSlots = fishObjectList.size();
            if(large)  Collections.reverse(fishObjectList);

            for(int i = 0; i < freeSlots; i++){
                FishObject fish = fishObjectList.get(i);
                fish.setBagID(null);
                p.getInventory().addItem(fish.GenerateItemStack());
            }
            p.playSound(p.getLocation(), Sound.ENTITY_SALMON_FLOP, .5f, 1f);
            FileHandler.FishData = true;

            new UpdateBag().Update(bag, p, true);
        }
    }

    public void ChangePage(boolean next, ItemStack bag, Player p){
        int page = BagInfo.getPage(bag);

        int newPage;

        if(next) newPage = ++page;
        else newPage = --page;

        if(newPage >= Variables.BaseFishList.size() / 45)
            newPage = 0;

        if(!next && newPage <= 0)
            newPage = Variables.BaseFishList.size() /45;

        bag = BagInfo.setPage(bag, newPage, p);
        new UpdateBag().Update(bag, p, true);

        p.updateInventory();
    }

    private static final List<Player> convertPlayers = new ArrayList<>();
    public void ConvertFish(ItemStack bag, Player p, Block barrel, PlayerInteractEvent e){
        if(!convertPlayers.contains(p)){
            e.setCancelled(true);
            convertPlayers.add(p);
            p.sendMessage(Formatting.getFormattedMesage("Equipment.Fish Bag.convert"));
            return;
        }
        convertPlayers.remove(p);

        String bagId = BagInfo.getId(bag);

        final List<FishObject> tempFish = new ParseFish().RetrieveFish(bagId, "ALL");
        if(tempFish.size() == 0){
            p.sendMessage(Formatting.getFormattedMesage("Equipment.Fish Bag.empty"));
            return;
        }


        BlockState state = barrel.getState();

        if (!(state instanceof Container container)) {
            return;
        }

        Inventory inv = container.getInventory();

        for(var f: tempFish){
            if(inv.firstEmpty() == -1) break;
            inv.addItem(new ItemStack(Material.SALMON));
            f.setBagID(null);
            p.playSound(p.getLocation(), Sound.ENTITY_PUFFER_FISH_FLOP, .5f, 1f);
        }

        new UpdateBag().Update(bag, p, false);

    }

    public void UpgradeBag(ItemStack bag, ItemStack upgrade, Player p){

        ItemStack newBag = null;
        switch (upgrade.getType()) {
            case IRON_BLOCK -> newBag = BagSetup(bag, 2);
            case DIAMOND_BLOCK -> newBag = BagSetup(bag, 3);
            case NETHERITE_BLOCK -> newBag = BagSetup(bag, 4);
        }

        if(newBag == null){
            Bukkit.getLogger().warning(Variables.getPrefix() + "Error creating new bag.");
            return;
        }

        if(!BagInfo.IsBag(p.getInventory().getItemInMainHand())){
            Bukkit.getLogger().warning("Player tried to upgrade bag without the bag in main hand.");
            return;
        }

        upgrade.setAmount(upgrade.getAmount() - 1);

        p.getInventory().setItemInMainHand(newBag);

        UseBag(newBag, p);
    }


}