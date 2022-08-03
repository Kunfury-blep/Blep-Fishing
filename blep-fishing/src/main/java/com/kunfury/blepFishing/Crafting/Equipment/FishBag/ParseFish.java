package com.kunfury.blepFishing.Crafting.Equipment.FishBag;

import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Objects.BaseFishObject;
import com.kunfury.blepFishing.Objects.FishObject;
import com.kunfury.blepFishing.Setup;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.*;
import java.util.stream.Collectors;

public class ParseFish {
    public List<FishObject> RetrieveFish(String bagId, String fishName){
        fishName = ChatColor.stripColor(fishName);

        List<FishObject> fishList = new ArrayList<>();

        fishList.addAll(Variables.getFishList(fishName).stream()
                .filter(f -> f.getBagID() != null && f.getBagID().equals(bagId))
                .sorted(Comparator.comparingDouble(FishObject::GetScore)).toList());

        return fishList;
    }

    public FishObject FishFromId(String fishID){
        List<FishObject> fishList = Variables.getFishList("ALL").stream()
                .filter(f -> fishID.equals(f.FishID)).toList();


        if(fishList.size() <= 0) return null;
        return fishList.get(0);
    }

    public ItemStack UpdateSlot(String bagId, BaseFishObject bFish, List<FishObject> availFish){


        ItemStack fish = new ItemStack(Material.SALMON, 1);

        fish = NBTEditor.set(fish, bagId, "blep", "item", "fishBagId"); //Adds the current bag id to the fish objects. Likely inefficient, change in future
        ItemMeta m = fish.getItemMeta();

        //TODO: Set color of display name to the rarity of the largest one caught
        assert m != null;
        m.setDisplayName(ChatColor.AQUA + bFish.Name);

        ArrayList<String> lore = new ArrayList<>();
        if (bFish.Lore != null && !bFish.Lore.isEmpty()) lore.add(bFish.Lore);
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

        m.setCustomModelData(bFish.ModelData);
        fish.setItemMeta(m);

        return fish;
    }

}
