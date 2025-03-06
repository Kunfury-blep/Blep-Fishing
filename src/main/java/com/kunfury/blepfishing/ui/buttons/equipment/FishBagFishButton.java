package com.kunfury.blepfishing.ui.buttons.equipment;

import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.objects.equipment.FishBag;
import com.kunfury.blepfishing.objects.FishObject;
import com.kunfury.blepfishing.objects.FishType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class FishBagFishButton extends MenuButton {

    private final int page;
    public FishBagFishButton(int page){
        this.page = page;
    }

    @Override
    public ItemStack buildItemStack(Player player) {
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
        dataContainer.set(pageKey, PersistentDataType.INTEGER, page);

        m.setDisplayName(ChatColor.AQUA + type.Name);

        ArrayList<String> lore = new ArrayList<>();
        if (type.Lore != null && !type.Lore.isEmpty())
            lore.add(type.Lore);

        lore.add("");

        FishObject biggestFish = availFish.get(availFish.size() - 1);

        lore.add(Formatting.GetLanguageString("UI.Player.Buttons.Fish Bag.stored")
                .replace("{amount}", String.valueOf(availFish.size())));
        lore.add(Formatting.GetLanguageString("UI.Player.Buttons.Fish Bag.largest")
                .replace("{size}", String.valueOf(biggestFish.Length)));

        lore.add("");
        lore.add(Formatting.GetLanguageString("UI.Player.Buttons.Fish Bag.withdrawSmall"));
        lore.add(Formatting.GetLanguageString("UI.Player.Buttons.Fish Bag.withdrawLarge"));
        lore.add("");
        lore.add(Formatting.GetLanguageString("UI.Player.Buttons.Fish Bag.shift"));
        m.setLore(lore);

        m.setCustomModelData(type.ModelData);
        fish.setItemMeta(m);

        return fish;
    }

    @Override
    protected void click_left() {
        getBag().Withdraw(player, getFishType(), false, true, getPage());
    }

    @Override
    protected void click_right() {
        getBag().Withdraw(player, getFishType(), true, true, getPage());
    }

    @Override
    protected void click_left_shift() {
        getBag().Withdraw(player, getFishType(), false, false, getPage());
    }

    @Override
    protected void click_right_shift() {
        getBag().Withdraw(player, getFishType(), true, false, getPage());
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
