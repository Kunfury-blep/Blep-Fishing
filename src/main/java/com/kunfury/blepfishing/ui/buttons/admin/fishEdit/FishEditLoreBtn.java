package com.kunfury.blepfishing.ui.buttons.admin.fishEdit;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.ui.objects.buttons.AdminFishMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.fish.AdminFishEditPanel;
import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.objects.FishType;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Objects;

public class FishEditLoreBtn extends AdminFishMenuButton {


    public FishEditLoreBtn(FishType fishType) {
        super(fishType);
    }

    @Override
    public ItemStack buildItemStack() {

        ItemStack item = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName("Lore");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(String.valueOf(fishType.Lore));
        m.setLore(lore);
        m = setButtonId(m, getId());

        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        dataContainer.set(ItemHandler.FishTypeId, PersistentDataType.STRING, fishType.Id);


        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        Conversation convo = getFactory().buildConversation(player);
        player.closeInventory();
        convo.begin();
    }

    private ConversationFactory getFactory(){

        return new ConversationFactory(BlepFishing.getPlugin())
                .withFirstPrompt(new DurationPrompt())
                .withModality(true)
                .withTimeout(60)
                .thatExcludesNonPlayersWithMessage("This Conversation Factory is Player Only");
    }

    private class DurationPrompt extends StringPrompt {

        @NotNull
        @Override
        public String getPromptText(@NotNull ConversationContext context) {
            return "What should the fish lore be? Current: " + getFishType().Lore;
        }

        @Nullable
        @Override
        public Prompt acceptInput(@NotNull ConversationContext conversationContext, @Nullable String s) {
            FishType type = getFishType();


            if(Objects.equals(type.Lore, s))
                return END_OF_CONVERSATION; //If the name wasn't changed, no need to save

            type.Lore = s;

            ConfigHandler.instance.fishConfig.Save();
            new AdminFishEditPanel(type).Show(player);

            return END_OF_CONVERSATION;
        }
    }


}
