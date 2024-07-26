package com.kunfury.blepfishing.ui.buttons.equipment;

import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.items.ItemHandler;
import com.kunfury.blepfishing.objects.FishBag;
import com.kunfury.blepfishing.objects.FishObject;
import com.kunfury.blepfishing.objects.FishType;
import com.kunfury.blepfishing.ui.objects.buttons.AdminFishMenuButton;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FishBagFishButton extends MenuButton {


    @Override
    public ItemStack buildItemStack() {
        return null;
    }

    public ItemStack buildItemStack(FishBag fishBag, FishType type, List<FishObject> availFish){
        ItemStack fish = new ItemStack(ItemHandler.FishMat, 1);

        ItemMeta m = fish.getItemMeta();
        assert m != null;
        m = setButtonId(m, getId());

        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        dataContainer.set(ItemHandler.FishBagId, PersistentDataType.INTEGER, fishBag.Id);
        dataContainer.set(ItemHandler.FishTypeId, PersistentDataType.STRING, type.Id);

        m.setDisplayName(ChatColor.AQUA + type.Name);

        ArrayList<String> lore = new ArrayList<>();
        if (type.Lore != null && !type.Lore.isEmpty())
            lore.add(type.Lore);

        lore.add("");

        FishObject biggestFish = availFish.get(availFish.size() - 1);

        lore.add(Formatting.getMessage("Equipment.Fish Bag.stored")
                .replace("{amount}", String.valueOf(availFish.size())));
        lore.add(Formatting.getMessage("Equipment.Fish Bag.largest")
                .replace("{size}", String.valueOf(biggestFish.Length)));

        lore.add("");
        lore.add(Formatting.getMessage("Equipment.Fish Bag.withdrawSmall"));
        lore.add(Formatting.getMessage("Equipment.Fish Bag.withdrawLarge"));
        lore.add("");
        lore.add(Formatting.getMessage("Equipment.Fish Bag.shift"));
        m.setLore(lore);

        m.setCustomModelData(type.ModelData);
        fish.setItemMeta(m);

        return fish;
    }

    @Override
    protected void click_left() {
        getBag().Withdraw(player, getFishType(), getBagItem(), false, true);
    }

    @Override
    protected void click_right() {
        getBag().Withdraw(player, getFishType(), getBagItem(), true, true);
    }

    @Override
    protected void click_left_shift() {
        getBag().Withdraw(player, getFishType(), getBagItem(), false, false);
    }

    @Override
    protected void click_right_shift() {
        getBag().Withdraw(player, getFishType(), getBagItem(), true, false);
    }

    private FishBag getBag(){

        int bagId = ItemHandler.getTagInt(ClickedItem, ItemHandler.FishBagId);
        return FishBag.GetBag(bagId);
    }

    private ItemStack getBagItem(){
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        if(heldItem.getType() != ItemHandler.BagMat){
            player.sendMessage("Bag not held in hand");
            return null;
        }
        return heldItem;
    }
}
