package com.kunfury.blepFishing.Crafting.Equipment.FishBag;

import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Miscellaneous.Utilities;
import com.kunfury.blepFishing.Objects.FishObject;
import com.kunfury.blepFishing.Setup;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.*;

public class UseFishBag {
    public void UseBag(ItemStack bag, Player p){
        String bagId = BagInfo.getId(bag);
        new UpdateBag().ShowBagInv(p, bagId, bag);
        p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, .3f, 1f);

        //TODO: Remove from async
//        final BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
//
//        //Grabs the collection Asynchronously
//        scheduler.runTaskAsynchronously(Setup.getPlugin(), () -> {
//            scheduler.runTask(Setup.getPlugin(), () -> {
//
//            });
//        });
    }


    public void FillBag(ItemStack bag, Player p){
        int amount = 0;

        int bagAmt = BagInfo.getAmount(bag);
        int bagMax = BagInfo.getMax(bag);

        for(var item : p.getInventory().getStorageContents()){
            if(bagAmt >= bagMax) break;
            if(item != null && item.getType() == Material.SALMON && NBTEditor.contains( item,"blep", "item", "fishValue" )){
                AddFish(bag, p, item, false);
                amount++;
                bagAmt++;
            }
        }

        if(amount > 0){
            p.sendMessage(Variables.Prefix + Formatting.getMessage("Equipment.Fish Bag.addFish")
                            .replace("{amount}", String.valueOf(amount)));
        }else{
            if(bagAmt >= bagMax)
                p.sendMessage(Variables.Prefix + Formatting.getMessage("Equipment.Fish Bag.noSpace"));
            else
                p.sendMessage(Variables.Prefix + Formatting.getMessage("Equipment.Fish Bag.noFish"));
        }

    }

    public void TogglePickup(ItemStack bag, Player p){
        boolean enabled = NBTEditor.getBoolean(bag, "blep", "item", "fishBagAutoPickup" );

        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);

        if(enabled){
            bag = NBTEditor.set(bag, false, "blep", "item", "fishBagAutoPickup" );
            bag.removeEnchantment(Enchantment.DURABILITY);
            p.sendMessage(Variables.Prefix + Formatting.getMessage("Equipment.Fish Bag.disabled"));
        }else{
            bag = NBTEditor.set(bag, true, "blep", "item", "fishBagAutoPickup" );
            bag.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
            p.sendMessage(Variables.Prefix + Formatting.getMessage("Equipment.Fish Bag.enabled"));
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
            p.sendMessage(Variables.Prefix + Formatting.getMessage("Equipment.Fish Bag.noSpace"));
            return;
        }
        String bagId = BagInfo.getId(bag);
        if(bagId == null || bagId.isEmpty()) return;

        String fishId = NBTEditor.getString(fish, "blep", "item", "fishId");
        FishObject fishObj = new ParseFish().FishFromId(fishId);

        if(fishObj == null){
            p.sendMessage(Variables.Prefix + Formatting.getMessage("Equipment.Fish Bag.oldVersion"));
            return;
        }

        if(fishObj.getBagID() != null){
            p.sendMessage(Variables.Prefix + ChatColor.RED + "That fish is already stored in another bag somewhere. This shouldn't happen.");
            return;
        }
        fishObj.setBagID(bagId);
        fish.setAmount(0);
        Variables.UpdateFishData();
        new UpdateBag().Update(bag, p, bagOpen);
        p.playSound(p.getLocation(), Sound.ENTITY_PUFFER_FISH_FLOP, .5f, 1f);
    }

    public void FishBagWithdraw(ClickType click, String fishName, Player p, ItemStack bag){

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
            Variables.UpdateFishData();

            new UpdateBag().Update(bag, p, true);
        }


        //TODO: Ensure async is needed
//        final BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
//        scheduler.runTaskAsynchronously(Setup.getPlugin(), () -> {
//            final List<FishObject> fishObjectList = new ParseFish().RetrieveFish(bagId, fishName);
//
//            boolean large = true;
//            boolean single = true;
//            switch(click){
//                case LEFT:
//                    large = false;
//                    single = true;
//                    break;
//                case SHIFT_LEFT:
//                    large = false;
//                    single = false;
//                    break;
//                case RIGHT:
//                    large = true;
//                    single = true;
//                    break;
//                case SHIFT_RIGHT:
//                    large = true;
//                    single = false;
//                    break;
//                default:
//                    break;
//            }
//
//            if(fishObjectList.size() > 0){
//
//                int freeSlots = Utilities.getFreeSlots(p.getInventory());
//
//                if(single && freeSlots > 1) freeSlots = 1;
//                else if(freeSlots > fishObjectList.size()) freeSlots = fishObjectList.size();
//                if(large)  Collections.reverse(fishObjectList);
//
//                for(int i = 0; i < freeSlots; i++){
//                    FishObject fish = fishObjectList.get(i);
//                    fish.setBagID(null);
//                    p.getInventory().addItem(fish.GenerateItemStack());
//                }
//                p.playSound(p.getLocation(), Sound.ENTITY_SALMON_FLOP, .5f, 1f);
//                Variables.UpdateFishData();
//
//                new UpdateBag().Update(bag, p, true);
//            }
//        });
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

}