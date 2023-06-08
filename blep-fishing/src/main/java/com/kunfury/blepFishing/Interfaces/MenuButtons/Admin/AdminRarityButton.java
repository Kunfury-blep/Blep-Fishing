package com.kunfury.blepFishing.Interfaces.MenuButtons.Admin;

import com.kunfury.blepFishing.Conversations.ConversationHandler;
import com.kunfury.blepFishing.Interfaces.MenuButton;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Objects.RarityObject;
import com.kunfury.blepFishing.Quests.QuestObject;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class AdminRarityButton extends MenuButton {
    @Override
    public String getId() {
        return "adminRarityButton";
    }

    @Override
    public String getPermission() {
        return "bf.admin";
    }

    @Override
    public ItemStack getItemStack(Object o) {
        if(!(o instanceof RarityObject r))
            return null;

        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta m = item.getItemMeta();

        assert m != null;
        m.setDisplayName(Formatting.formatColor(r.getName()));
        //m.setCustomModelData(t.getModelData());

        List<String> lore = new ArrayList<String>() {{
            add("Weight: " + r.getWeight());
            add("Color Code: " + r.getPrefix());
            add("Price Mod: " + r.getPriceMod());
            add("");
            add("Left-Click to Edit");
        }};

        m.setLore(lore);
        item.setItemMeta(m);

        item = NBTEditor.set(item, getId(),"blep", "item", "buttonId");
        item = NBTEditor.set(item, r.getId(),"blep", "item", "rarityID");

        return item;
    }

    @Override
    protected void click_left() {
        RarityObject rarity = getRarity();
        ConversationHandler.StartRarityConversation(player, rarity);

        player.closeInventory();
    }

    @Override
    protected void click_right() {
        RarityObject rarity = getRarity();

    }

    @Override
    protected void click_right_shift() {
        RarityObject rarity = getRarity();

    }

    private RarityObject getRarity(){

        return RarityObject.GetRarity(NBTEditor.getString(ClickedItem, "blep", "item", "rarityID"));
    }

}
