package com.kunfury.blepFishing.Crafting.Equipment.FishBag;

import com.kunfury.blepFishing.Crafting.Equipment.Update;
import com.kunfury.blepFishing.Miscellaneous.Variables;
import com.kunfury.blepFishing.Objects.FishObject;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public class FishBagWithdraw {

    /**
     * @param fishObjectList = List containing all the fish to withdraw
     * @param large = If true, get the largest fish. Get the smallest if not
     * @param single = If true, withdraw a single fish, withdraw as many as possible if not
     */
    public FishBagWithdraw(List<FishObject> fishObjectList, boolean large, boolean single, Player p, ItemStack bag){
        if(fishObjectList.size() > 0){

            int freeSlots = 0;
            for (ItemStack it : p.getInventory().getStorageContents()) {
                if (it == null || it.getType() == Material.AIR) {
                    freeSlots++;
                }
            }

            if(single && freeSlots > 1) freeSlots = 1;
            else if(freeSlots > fishObjectList.size()) freeSlots = fishObjectList.size();
            if(large) Collections.reverse(fishObjectList);

            for(int i = 0; i < freeSlots; i++){
                FishObject fish = fishObjectList.get(i);
                fish.BagID = null;
                p.getInventory().addItem(fish.GenerateItemStack());
            }
            p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, .33f, 1f);
            Variables.UpdateFishData();

            new UpdateBag().Update(bag, p);
        }

    }


}
