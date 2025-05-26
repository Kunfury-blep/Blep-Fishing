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

public class FishEditHeightMinBtn extends AdminFishMenuButton {

    public FishEditHeightMinBtn(FishType fishType) {
        super(fishType);
    }

    @Override
    public ItemStack buildItemStack(Player player) {

        ItemStack item = new ItemStack(Material.ANDESITE_STAIRS);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName("Height Min");
        ArrayList<String> lore = new ArrayList<>();

        int heightMin = fishType.HeightMin;
        if(heightMin == -1000)
            heightMin = 0;

        lore.add(String.valueOf(heightMin));
        m.setLore(lore);
        m = setButtonId(m, getId());

        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        dataContainer.set(ItemHandler.FishTypeId, PersistentDataType.STRING, fishType.Id);


        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        player.closeInventory();
        getConversation(player, new HeightPrompt()).begin();
    }

    private class HeightPrompt extends NumericPrompt {

        @NotNull
        @Override
        public String getPromptText(@NotNull ConversationContext context) {
            FishType type = getFishType();
            int heightMin = type.HeightMin;
            if(heightMin == -1000)
                heightMin = 0;
            return Formatting.GetMessagePrefix() + "What should the minimum height of a " + type.Name + " be? Current: " + heightMin + " | 0 to ignore.";
        }

        @Override
        protected boolean isNumberValid(@NotNull ConversationContext context, @NotNull Number input) {
            return input.intValue() >= -1000;
        }

        @Nullable
        @Override
        protected Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull Number number) {
            FishType type = getFishType();

            type.HeightMin = number.intValue();

            ConfigHandler.instance.fishConfig.Save();
            new AdminFishEditPanel(type).Show(player);

            return END_OF_CONVERSATION;
        }
    }


}
