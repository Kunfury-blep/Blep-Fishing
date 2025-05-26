package com.kunfury.blepfishing.ui.buttons.admin.fishEdit;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.objects.FishType;
import com.kunfury.blepfishing.ui.objects.buttons.AdminFishMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.fish.AdminFishEditPanel;
import org.bukkit.ChatColor;
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

public class FishEditIdBtn extends AdminFishMenuButton {


    public FishEditIdBtn(FishType fishType) {
        super(fishType);
    }

    @Override
    public ItemStack buildItemStack(Player player) {

        ItemStack item = new ItemStack(Material.NAME_TAG);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName("Id");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE + String.valueOf(fishType.Id));
        m.setLore(lore);
        m = setButtonId(m, getId());

        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        dataContainer.set(ItemHandler.FishTypeId, PersistentDataType.STRING, fishType.Id);


        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        player.closeInventory();
        getConversation(player, new IdPrompt()).begin();
    }

    private class IdPrompt extends ValidatingPrompt {

        @NotNull
        @Override
        public String getPromptText(@NotNull ConversationContext context) {
            return Formatting.GetMessagePrefix() + "What should the fish ID be? Current: " + getFishType().Id;
        }

        @Override
        protected boolean isInputValid(@NotNull ConversationContext conversationContext, @NotNull String s) {
            s = Formatting.FormatId(s);
            if(getFishType().Id.equals(s)) return true;
            return !FishType.IdExists(s);
        }

        @Nullable
        @Override
        protected Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull String s) {
            FishType type = getFishType();
            String oldId = type.Id;

            s = Formatting.FormatId(s);


            if(Objects.equals(oldId, s))
                return END_OF_CONVERSATION; //If the name wasn't changed, no need to save

            type.Id = s;

            FishType.UpdateId(oldId, type);

            ConfigHandler.instance.fishConfig.Save();
            new AdminFishEditPanel(type).Show(player);

            return END_OF_CONVERSATION;
        }
    }


}
