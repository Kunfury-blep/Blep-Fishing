package com.kunfury.blepFishing.Crafting.Equipment.FishBag;

import com.kunfury.blepFishing.Objects.FishObject;
import com.kunfury.blepFishing.Setup;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;

public class UpdateBag {

    //TODO: Update the inventory being viewed if it is open.

    /**
     * @param bag : The ItemStack of the bag being updated
     * @param p : The player holding the bag with the inventory open
     */
    public UpdateBag(ItemStack bag, Player p) {
        //Grabs the amount of fish caught to display on the item
        final BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        String bagId = NBTEditor.getString(bag, "blep", "item", "fishBagId");
        //Grabs the collection Asynchronously
        scheduler.runTaskAsynchronously(Setup.getPlugin(), () -> {
            final List<FishObject> tempFish = new ParseFish().RetrieveFish(bagId, "ALL");

            scheduler.runTask(Setup.getPlugin(), () -> {
                FinalizeUpdate(bag, p, tempFish.size());
            });
        });
    }

    private void FinalizeUpdate(ItemStack bag ,Player p, int fishCount){
        PlayerInventory inv = p.getInventory();
        if(!inv.contains(bag)) return;

        ItemStack heldBag = null;

        ItemStack[] items = inv.getStorageContents();

        int heldSlot = 0;
        for (int i = 0 ; i < inv.getStorageContents().length; ++i) {
            if(bag.equals(items[i])){
                heldBag = items[i];
                heldSlot = i;
                break;
            }
        }

        if(heldBag == null) return;

        bag = NBTEditor.set(bag, fishCount, "blep", "item", "fishBagAmount" ); //Updates the amount of fish in the bag
        ItemMeta m = bag.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();

        lore.add("Holds a small amount of fish");
        lore.add("");

        int maxSize = BagInfo.GetMax(bag);
        double barScore = 10 * ((double)fishCount / maxSize);

        String progressBar = "";

        for (int i = 1 ; i < barScore; i++) {
            progressBar += ChatColor.GREEN + "|";
        }
        for (int i = 0; i < 10 - barScore; i++){
            progressBar += ChatColor.WHITE + "|";
        }

        lore.add(progressBar + " " +fishCount + "/" + maxSize);

        lore.add("");
        lore.add(ChatColor.RED + "Left-Click While Holding to Toggle " + ChatColor.YELLOW + ChatColor.ITALIC + "Auto-Pickup");



        m.setLore(lore);
        bag.setItemMeta(m);

        inv.setItem(heldSlot, bag);

        p.updateInventory();

    }
}
