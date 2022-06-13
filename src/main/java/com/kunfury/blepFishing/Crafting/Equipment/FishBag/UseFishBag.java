package com.kunfury.blepFishing.Crafting.Equipment.FishBag;

import com.kunfury.blepFishing.Miscellaneous.Variables;
import com.kunfury.blepFishing.Objects.BaseFishObject;
import com.kunfury.blepFishing.Objects.FishObject;
import com.kunfury.blepFishing.Setup;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class UseFishBag implements Listener {

    private Inventory BagInv;
    @EventHandler
    public void FishBagInteract(PlayerInteractEvent e){

        Player p = e.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();
        Action a = e.getAction();

        if(item != null && item.getType() == Material.HEART_OF_THE_SEA && a != Action.PHYSICAL){
            e.setCancelled(true);


            boolean nbt = NBTEditor.contains(item, "blep", "item", "fishBagId");

            if(nbt){
                if(a == Action.LEFT_CLICK_AIR || a == Action.LEFT_CLICK_BLOCK){
                    TogglePickup(item, p);
                    //TODO: Shift-Right to fill bag from inventory
//                    if(p.isSneaking()) FillBag(item, p);
//                    else TogglePickup(item, p);
                }else if(a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK){
                    UseBag(item, p);
                }
            }
        }
    }


    @EventHandler
    public void inventoryClick(InventoryClickEvent e) {
        ClickType a = e.getClick();
        ItemStack item = e.getCurrentItem();
        if(item != null && e.getInventory() == BagInv){
            e.setCancelled(true);
            Player p = (Player) e.getWhoClicked();
            ItemStack bag = p.getInventory().getItemInMainHand();

            if(!e.getView().getTitle().equals(bag.getItemMeta().getDisplayName())) return; //Ensures the user still has the bag selected


            String fishName = item.getItemMeta().getDisplayName();
            String bagId = NBTEditor.getString(item,"blep", "item", "fishBagId" );


            final BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
            //Grabs the collection Asynchronously
            scheduler.runTaskAsynchronously(Setup.getPlugin(), () -> {
                final List<FishObject> tempFish = new ParseFish().RetrieveFish(bagId, fishName);
                scheduler.runTask(Setup.getPlugin(), () -> {
                    boolean largeChoice = true;
                    boolean singleChoice = true;
                    switch(a){
                        case LEFT:
                            largeChoice = false;
                            singleChoice = true;
                            break;
                        case SHIFT_LEFT:
                            largeChoice = false;
                            singleChoice = false;
                            break;
                        case RIGHT:
                            largeChoice = true;
                            singleChoice = true;
                            break;
                        case SHIFT_RIGHT:
                            largeChoice = true;
                            singleChoice = false;
                            break;
                        default:
                            break;
                    }
                    new FishBagWithdraw(tempFish, largeChoice, singleChoice, p, bag);
                });
            });

            //Inventory is the player inv
            if(e.getClickedInventory() == p.getInventory() && item.getType() == Material.SALMON && NBTEditor.contains( item,"blep", "item", "fishValue" )){
                AddFish(p.getInventory().getItemInMainHand(), p, item);
            }
        }
    }

    private void UseBag(ItemStack bag, Player player){
        String bagId = NBTEditor.getString(bag, "blep", "item", "fishBagId");
        //p.sendMessage("Bag UUID: " + bagId);

        final BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

        //Grabs the collection Asynchronously
        scheduler.runTaskAsynchronously(Setup.getPlugin(), () -> {
            final List<FishObject> tempFish = new ParseFish().RetrieveFish(bagId, "ALL");


            scheduler.runTask(Setup.getPlugin(), () -> {
                ReturnBagContents(tempFish, player, bagId, bag);
            });
        });
    }

    public void ReturnBagContents(List<FishObject> fishObjectList, Player p, String bagId, @NotNull ItemStack bag){
        int fishTypes = (int) (Math.ceil(Variables.BaseFishList.size()/9.0) * 9); ; //Makes as many slots as needed for generated fish, rounded up to the nearest multiple of 9
        BagInv = Bukkit.createInventory(null, fishTypes, bag.getItemMeta().getDisplayName());

        for (int i=0; i<Variables.BaseFishList.size(); i++)
        {
            BaseFishObject bFIsh = Variables.BaseFishList.get(i);

            List<FishObject> availFish = fishObjectList.stream()
                    .filter(f -> f.Name.equalsIgnoreCase(bFIsh.Name))
                    .collect(Collectors.toList());

            //Fills the inventory with the fish in the bag
            if(availFish != null && availFish.size() > 0){
                ItemStack fish = new ItemStack(Material.SALMON, 1);

                fish = NBTEditor.set(fish, bagId, "blep", "item", "fishBagId" ); //Adds the current bag id to the fish objects. Likely inefficient, change in future
                ItemMeta m = fish.getItemMeta();

                //TODO: Set color of display name to the rarity of the largest one caught
                m.setDisplayName(ChatColor.AQUA +  bFIsh.Name);

                ArrayList<String> lore = new ArrayList<String>();
                if(bFIsh.Lore != null && !bFIsh.Lore.isEmpty()) lore.add(bFIsh.Lore);
                lore.add("");

                FishObject biggestFish = availFish.get(availFish.size() - 1);

                lore.add(ChatColor.AQUA + "Amount Stored: " + ChatColor.WHITE + availFish.size());
                lore.add(ChatColor.AQUA + "Largest Fish: " + ChatColor.WHITE + biggestFish.GetSize() + Variables.SizeSym);
                lore.add("");
                lore.add(ChatColor.RED + "Left-Click to Withdraw " + ChatColor.YELLOW + ChatColor.ITALIC + "Smallest");
                lore.add(ChatColor.RED + "Right-Click to Withdraw "  + ChatColor.YELLOW + ChatColor.ITALIC + "Largest");
                lore.add("");
                lore.add(ChatColor.RED + "" + ChatColor.ITALIC + "Hold Shift to Withdraw " + ChatColor.YELLOW + "All");
                m.setLore(lore);

                m.setCustomModelData(bFIsh.ModelData);
                fish.setItemMeta(m);

                BagInv.addItem(fish);
            }

            p.openInventory(BagInv);
        }
    }


    private void FillBag(ItemStack bag, Player p){
        p.sendMessage("In Progress: Fill Bag. Pulls all fish from inventory into bag");
    }

    private void TogglePickup(ItemStack bag, Player p){
        boolean enabled = NBTEditor.getBoolean(bag, "blep", "item", "fishBagAutoPickup" );

        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);

        if(enabled){
            bag = NBTEditor.set(bag, false, "blep", "item", "fishBagAutoPickup" );
            bag.removeEnchantment(Enchantment.DURABILITY);
            p.sendMessage(Variables.Prefix + "Auto Pickup Disabled.");
        }else{
            bag = NBTEditor.set(bag, true, "blep", "item", "fishBagAutoPickup" );
            bag.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
            p.sendMessage(Variables.Prefix + "Auto Pickup Enabled.");
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
    private void AddFish(ItemStack bag, Player p, ItemStack fish){
        if(BagInfo.IsFull(bag)){
            p.sendMessage(Variables.Prefix + "There is no more space in that bag.");
            return;
        }
        String bagId = NBTEditor.getString(bag, "blep", "item", "fishBagId");
        if(bagId == null || bagId.isEmpty()) return;

        String fishId = NBTEditor.getString(fish, "blep", "item", "fishId");
        FishObject fishObj = new ParseFish().FishFromId(fishId);

        if(fishObj == null){
            p.sendMessage(Variables.Prefix + ChatColor.RED + "That fish is from a previous version and cannot be stored in the bag. Sorry.");
            return;
        }

        if(fishObj.BagID != null){
            p.sendMessage(Variables.Prefix + ChatColor.RED + "That fish is already stored in anothe bag somewhere. This shouldn't happen.");
            return;
        }
        fishObj.BagID = bagId;
        fish.setAmount(0);
        Variables.UpdateFishData();
        new UpdateBag(bag, p);
        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, .33f, 1f);
    }
}