package com.kunfury.blepFishing.Crafting.Equipment.FishBag;

import com.kunfury.blepFishing.Config.ItemsConfig;
import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Objects.BaseFishObject;
import com.kunfury.blepFishing.Objects.FishObject;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ParseFish {
    public List<FishObject> RetrieveFish(String bagId, String fishName){
        fishName = ChatColor.stripColor(fishName);

        List<FishObject> fishList = new ArrayList<>();

        fishList.addAll(Variables.getFishList(fishName).stream()
                .filter(f -> f.getBagID() != null && f.getBagID().equals(bagId))
                .sorted(Comparator.comparingDouble(FishObject::getScore)).toList());

        return fishList;
    }

    public FishObject FishFromId(String fishID){
        List<FishObject> fishList = Variables.getFishList("ALL").stream()
                .filter(f -> fishID.equals(f.FishID)).toList();


        if(fishList.size() <= 0) return null;
        return fishList.get(0);
    }

    public ItemStack UpdateSlot(String bagId, BaseFishObject bFish, List<FishObject> availFish){
        ItemStack fish = new ItemStack(ItemsConfig.FishMat, 1);

        fish = NBTEditor.set(fish, bagId, "blep", "item", "fishBagId"); //Adds the current bag id to the fish objects. Likely inefficient, change in future
        ItemMeta m = fish.getItemMeta();

        //TODO: Set color of display name to the rarity of the largest one caught
        assert m != null;
        m.setDisplayName(ChatColor.AQUA + bFish.Name);

        ArrayList<String> lore = new ArrayList<>();
        if (bFish.Lore != null && !bFish.Lore.isEmpty()) lore.add(bFish.Lore);
        lore.add("");

        FishObject biggestFish = availFish.get(availFish.size() - 1);

        lore.add(Formatting.getMessage("Equipment.Fish Bag.stored")
                        .replace("{amount}", String.valueOf(availFish.size())));
        lore.add(Formatting.getMessage("Equipment.Fish Bag.largest")
                .replace("{size}", String.valueOf(biggestFish.getSize())));

        lore.add("");
        lore.add(Formatting.getMessage("Equipment.Fish Bag.withdrawSmall"));
        lore.add(Formatting.getMessage("Equipment.Fish Bag.withdrawLarge"));
        lore.add("");
        lore.add(Formatting.getMessage("Equipment.Fish Bag.shift"));
        m.setLore(lore);

        m.setCustomModelData(bFish.ModelData);
        fish.setItemMeta(m);

        return fish;
    }

}
