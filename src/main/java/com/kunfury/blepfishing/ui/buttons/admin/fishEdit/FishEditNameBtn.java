package com.kunfury.blepfishing.ui.buttons.admin.fishEdit;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.ui.objects.buttons.AdminFishMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.fish.AdminFishEditPanel;
import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.objects.FishType;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Objects;

public class FishEditNameBtn extends AdminFishMenuButton {


    public FishEditNameBtn(FishType fishType) {
        super(fishType);
    }

    @Override
    public ItemStack buildItemStack(Player player) {

        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName("Name");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(String.valueOf(fishType.Name));
        m.setLore(lore);
        m = setButtonId(m, getId());

        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        dataContainer.set(ItemHandler.FishTypeId, PersistentDataType.STRING, fishType.Id);


        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        player.closeInventory();
        getConversation(player, new NamePrompt()).begin();
    }

    private class NamePrompt extends StringPrompt {

        @NotNull
        @Override
        public String getPromptText(@NotNull ConversationContext context) {
            return Formatting.GetMessagePrefix() + "What should the fish name be? Current: " + getFishType().Name;
        }

        @Nullable
        @Override
        public Prompt acceptInput(@NotNull ConversationContext conversationContext, @Nullable String s) {
            FishType type = getFishType();
            String oldName = type.Name;

            if(Objects.equals(oldName, s))
                return END_OF_CONVERSATION; //If the name wasn't changed, no need to save

            type.Name = s;

            ConfigHandler.instance.fishConfig.Save();
            new AdminFishEditPanel(type).Show(player);

            return END_OF_CONVERSATION;
        }
    }


}
