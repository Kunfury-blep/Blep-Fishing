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
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UpdateBag {

    //TODO: Update the inventory being viewed if it is open.

    /**
     * @param bag : The ItemStack of the bag being updated
     * @param p   : The player holding the bag with the inventory open
     */
    public void Update(ItemStack bag, Player p) {
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

    private void FinalizeUpdate(ItemStack bag, Player p, int fishCount) {
        PlayerInventory inv = p.getInventory();
        if (!inv.contains(bag)) return;

        ItemStack heldBag = null;

        ItemStack[] items = inv.getStorageContents();

        int heldSlot = 0;
        for (int i = 0; i < inv.getStorageContents().length; ++i) {
            if (bag.equals(items[i])) {
                heldBag = items[i];
                heldSlot = i;
                break;
            }
        }

        if (heldBag == null) return;

        bag = NBTEditor.set(bag, fishCount, "blep", "item", "fishBagAmount"); //Updates the amount of fish in the bag
        ItemMeta m = bag.getItemMeta();


        m.setLore(GenerateLore(bag));
        bag.setItemMeta(m);

        inv.setItem(heldSlot, bag);

        p.updateInventory();

    }


    public ArrayList<String> GenerateLore(ItemStack bag) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        double fishCount = BagInfo.GetAmount(bag);
        double maxSize = BagInfo.GetMax(bag);
        int tier = BagInfo.GetTier(bag);

        ArrayList<String> lore = new ArrayList<>();

        lore.add("Holds a small amount of fish"); //TODO: Change to dynamic based on size of bag
        lore.add("");


        double barScore = 10 * (fishCount / maxSize);

        String progressBar = "";

        for (int i = 1; i <= barScore; i++) {
            progressBar += ChatColor.GREEN + "|";
        }
        for (int i = 1; i < 10 - barScore; i++) {
            progressBar += ChatColor.WHITE + "|";
        }

        lore.add(progressBar + " " + formatter.format(fishCount) + "/" + formatter.format(maxSize));

        if (BagInfo.IsFull(bag)) {
            lore.add("");
            lore.add(ChatColor.WHITE + "Combine with a " + ChatColor.YELLOW + ChatColor.ITALIC + BagInfo.GetUpgradeComp(bag).getType().name().replace("_", " ") + ChatColor.WHITE + " at a smithing tableto upgrade!");
        }


        lore.add("");
        lore.add(ChatColor.RED + "Left-Click While Holding to Toggle " + ChatColor.YELLOW + ChatColor.ITALIC + "Auto-Pickup");

        return lore;
    }

    public void ShowBagInv(List<FishObject> fishObjectList, Player p, String bagId, @NotNull ItemStack bag) {
        int fishTypes = (int) (Math.ceil(Variables.BaseFishList.size()/9.0) * 9); ; //Makes as many slots as needed for generated fish, rounded up to the nearest multiple of 9

        Inventory BagInv = Bukkit.createInventory(null, fishTypes, bag.getItemMeta().getDisplayName());

        for (int i = 0; i < Variables.BaseFishList.size(); i++) {
            BaseFishObject bFIsh = Variables.BaseFishList.get(i);

            List<FishObject> availFish = fishObjectList.stream()
                    .filter(f -> f.Name.equalsIgnoreCase(bFIsh.Name))
                    .collect(Collectors.toList());

            //Fills the inventory with the fish in the bag
            if (availFish != null && availFish.size() > 0) {
                ItemStack fish = new ItemStack(Material.SALMON, 1);

                fish = NBTEditor.set(fish, bagId, "blep", "item", "fishBagId"); //Adds the current bag id to the fish objects. Likely inefficient, change in future
                ItemMeta m = fish.getItemMeta();

                //TODO: Set color of display name to the rarity of the largest one caught
                m.setDisplayName(ChatColor.AQUA + bFIsh.Name);

                ArrayList<String> lore = new ArrayList<String>();
                if (bFIsh.Lore != null && !bFIsh.Lore.isEmpty()) lore.add(bFIsh.Lore);
                lore.add("");

                FishObject biggestFish = availFish.get(availFish.size() - 1);

                lore.add(ChatColor.AQUA + "Amount Stored: " + ChatColor.WHITE + availFish.size());
                lore.add(ChatColor.AQUA + "Largest Fish: " + ChatColor.WHITE + biggestFish.GetSize() + Variables.SizeSym);
                lore.add("");
                lore.add(ChatColor.RED + "Left-Click to Withdraw " + ChatColor.YELLOW + ChatColor.ITALIC + "Smallest");
                lore.add(ChatColor.RED + "Right-Click to Withdraw " + ChatColor.YELLOW + ChatColor.ITALIC + "Largest");
                lore.add("");
                lore.add(ChatColor.RED + "" + ChatColor.ITALIC + "Hold Shift to Withdraw " + ChatColor.YELLOW + "All");
                m.setLore(lore);

                m.setCustomModelData(bFIsh.ModelData);
                fish.setItemMeta(m);

                BagInv.addItem(fish);
            }

            p.openInventory(BagInv);
            p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, .05f, 1f);
        }
    }
}
