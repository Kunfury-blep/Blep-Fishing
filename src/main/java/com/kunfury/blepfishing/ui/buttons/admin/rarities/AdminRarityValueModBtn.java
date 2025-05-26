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

public class AdminRarityValueModBtn extends AdminRarityMenuButton {

    public AdminRarityValueModBtn(Rarity rarity) {
        super(rarity);
    }

    @Override
    public ItemStack buildItemStack(Player player) {
        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName("Value Mod");
        ArrayList<String> lore = new ArrayList<>();
        String valueMod = Formatting.DoubleFormat(rarity.ValueMod);
        lore.add(ChatColor.BLUE + valueMod);
        lore.add("");

        String desc = "The value of caught " + rarity.Name + " Fish will be modified by this value.";
        lore.addAll(Formatting.ToLoreList(desc));

        m.setLore(lore);

        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        player.closeInventory();
        getConversation(player, new ValueModPrompt()).begin();
    }

    private class ValueModPrompt extends NumericPrompt {

        @NotNull
        @Override
        public String getPromptText(@NotNull ConversationContext context) {
            return Formatting.GetMessagePrefix() + "What should the Value Mod be? Current: " + Formatting.DoubleFormat(getRarity().ValueMod);
        }

        @Override
        protected boolean isNumberValid(@NotNull ConversationContext context, @NotNull Number input) {
            return input.doubleValue() > 0;
        }

        @Nullable
        @Override
        protected Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull Number number) {
            Rarity rarity = getRarity();
            double newValue = number.doubleValue();

            if(rarity.ValueMod == newValue){
                new AdminRarityEditPanel(rarity).Show(player);
                return END_OF_CONVERSATION;
            }

            rarity.ValueMod = newValue;
            ConfigHandler.instance.rarityConfig.Save();
            new AdminRarityEditPanel(rarity).Show(player);

            return END_OF_CONVERSATION;
        }
    }



}
