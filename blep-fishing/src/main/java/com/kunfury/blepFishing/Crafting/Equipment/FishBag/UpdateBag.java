package com.kunfury.blepFishing.Crafting.Equipment.FishBag;

import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Objects.BaseFishObject;
import com.kunfury.blepFishing.Objects.FishObject;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UpdateBag {

    /**
     * @param bag : The ItemStack of the bag being updated
     * @param p   : The player holding the bag with the inventory open
     */
    public void Update(ItemStack bag, Player p, boolean bagOpen) {
        String bagId = BagInfo.getId(bag);
        final List<FishObject> tempFish = new ParseFish().RetrieveFish(bagId, "ALL");
        FinalizeUpdate(bag, p, tempFish.size());
        if(bagOpen) ShowBagInv(p, bagId, bag);

        //TODO: Check if async is needed
        //Grabs the collection Asynchronously
//        final BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
//        scheduler.runTaskAsynchronously(Setup.getPlugin(), () -> {
//            final List<FishObject> tempFish = new ParseFish().RetrieveFish(bagId, "ALL");
//
//            scheduler.runTask(Setup.getPlugin(), () -> {
//                FinalizeUpdate(bag, p, tempFish.size());
//                if(bagOpen) ShowBagInv(p, bagId, bag);
//            });
//        });
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
        double fishCount = BagInfo.getAmount(bag);
        double maxSize = BagInfo.getMax(bag);
        int tier = BagInfo.getTier(bag);


        ArrayList<String> lore = new ArrayList<>();

        lore.add(Formatting.getMessage("Equipment.Fish Bag.descSmall")); //TODO: Change to dynamic based on size of bag
        lore.add("");

        if(Variables.DebugMode){
            lore.add("ID: " + BagInfo.getId(bag));
            lore.add("");
        }

        double barScore = 10 * (fishCount / maxSize);

        String progressBar = "";

        for (int i = 1; i <= barScore; i++) {
            progressBar += ChatColor.GREEN + "|";
        }
        for (int i = 1; i < 10 - barScore; i++) {
            progressBar += ChatColor.WHITE + "|";
        }

        lore.add(progressBar + " " + formatter.format(fishCount) + "/" + formatter.format(maxSize));

        if(tier < BagInfo.UpgradeItems.length){
            lore.add("");
            if (BagInfo.IsFull(bag)) {
                lore.add(Formatting.getMessage("Equipment.Fish Bag.upgradeReady")
                        .replace("{upgrade}", BagInfo.getUpgradeComp(bag).getType().name().replace("_", " ")));
            } else{
                lore.add(Formatting.getMessage("Equipment.Fish Bag.upgradeHint"));
            }
        }

        lore.add("");
        lore.add(Formatting.getMessage("Equipment.Fish Bag.autoPickup"));
        lore.add(Formatting.getMessage("Equipment.Fish Bag.depositAll"));

        return lore;
    }

    public void ShowBagInv(Player p, String bagId, @NotNull ItemStack bag) {
        //int fishTypes = (int) (Math.ceil(Variables.BaseFishList.size()/9.0) * 9); ; //Makes as many slots as needed for generated fish, rounded up to the nearest multiple of 9+
        final List<FishObject> parsedFish = new ParseFish().RetrieveFish(bagId, "ALL");
        List<BaseFishObject> baseFishList = Variables.BaseFishList;


        int caughtAmount = 0;

        List<BaseFishObject> caughtTypes = new ArrayList<>();

        for(var b : baseFishList){
            if(parsedFish.stream().anyMatch(f -> f.Name.equals(b.Name))){
                caughtTypes.add(b);
                caughtAmount++;
            }
        }

        if(baseFishList.size() > 45) { //If base list is larger than what can be shown, trims it down to page
            if (caughtTypes.size() > 45) {
                int page = BagInfo.getPage(bag);

                if (page > caughtTypes.size() / 45) {
                    page = 0;
                    BagInfo.setPage(bag, page, p);
                }
                int startPoint = page * 45;
                int endPoint = startPoint + 45;

                if (endPoint > caughtTypes.size()) endPoint = caughtTypes.size();

                caughtTypes = caughtTypes.subList(startPoint, endPoint);
            }
            baseFishList = caughtTypes;
        }

        List<ItemStack> bagItems = new ArrayList<>();
        for (BaseFishObject bFish : baseFishList) {
            List<FishObject> availFish = parsedFish.stream()
                    .filter(f -> f.Name.equalsIgnoreCase(bFish.Name))
                    .collect(Collectors.toList());

            if (availFish.size() > 0) {
                bagItems.add(new ParseFish().UpdateSlot(bagId, bFish, availFish));
            }
        }

        String bagName = Objects.requireNonNull(bag.getItemMeta()).getDisplayName() + " - " + caughtAmount + "/" + Variables.BaseFishList.size();
        Inventory bagInv = Bukkit.createInventory(null, 54, bagName);

        if(Variables.BaseFishList.size() > 45){
            bagInv.setItem(53, nextButton());
            bagInv.setItem(45, backButton());
        }

        for(var i : bagItems){
            bagInv.addItem(i);
        }

        BagInfo.Inventories.put(p, bagInv);
        p.openInventory(bagInv);
    }

    public void IncreaseAmount(ItemStack bag, Player p){
        FinalizeUpdate(bag, p, 1 + NBTEditor.getInt(bag, "blep", "item", "fishBagAmount"));
    }


    private ItemStack nextButton(){
        ItemStack item = new ItemStack(Material.WARPED_SIGN);
        ItemMeta m = item.getItemMeta();
        assert m != null;
        m.setDisplayName("Next Page");

        item.setItemMeta(m);

        return item;
    }

    private ItemStack backButton(){
        ItemStack item = new ItemStack(Material.CRIMSON_SIGN);
        ItemMeta m = item.getItemMeta();
        assert m != null;
        m.setDisplayName("Previous Page");

        item.setItemMeta(m);

        return item;
    }
}
