package com.kunfury.blepfishing.ui.buttons.admin.rarities;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.objects.Rarity;
import com.kunfury.blepfishing.ui.objects.buttons.AdminRarityMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.rarities.AdminRarityEditPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Objects;

public class AdminRarityNameBtn extends AdminRarityMenuButton {

    public AdminRarityNameBtn(Rarity rarity) {
        super(rarity);
    }

    @Override
    public ItemStack buildItemStack(Player player) {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName("Name");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE + rarity.Name);
        m.setLore(lore);

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
            return Formatting.GetMessagePrefix() + "What should the Rarity name be? Current: " + getRarity().Name;
        }

        @Nullable
        @Override
        public Prompt acceptInput(@NotNull ConversationContext conversationContext, @Nullable String s) {
            Rarity rarity = getRarity();
            String oldName = rarity.Name;

            if(Objects.equals(oldName, s)){
                new AdminRarityEditPanel(rarity).Show(player);
                return END_OF_CONVERSATION; //If the name wasn't changed, no need to save
            }

            rarity.Name = s;

            ConfigHandler.instance.rarityConfig.Save();
            new AdminRarityEditPanel(rarity).Show(player);

            return END_OF_CONVERSATION;
        }
    }


}
