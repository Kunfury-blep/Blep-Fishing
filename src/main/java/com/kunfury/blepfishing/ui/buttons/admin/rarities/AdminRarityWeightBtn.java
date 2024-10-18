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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class AdminRarityWeightBtn extends AdminRarityMenuButton {

    public AdminRarityWeightBtn(Rarity rarity) {
        super(rarity);
    }

    @Override
    public ItemStack buildItemStack() {
        ItemStack item = new ItemStack(Material.ANVIL);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName("Weight");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE.toString() + rarity.Weight);
        lore.add("");
        String desc = "The weight determines how likely it is to catch a fish of this rarity. The lower the weight, the less likely it is to be caught. ";
        lore.addAll(Formatting.ToLoreList(desc));

        m.setLore(lore);

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
                .withFirstPrompt(new WeightPrompt())
                .withModality(true)
                .withTimeout(60)
                .thatExcludesNonPlayersWithMessage("This Conversation Factory is Player Only");
    }

    private class WeightPrompt extends NumericPrompt {

        @NotNull
        @Override
        public String getPromptText(@NotNull ConversationContext context) {
            return "What should the Rarity weight be? Current: " + getRarity().Weight;
        }

        @Override
        protected boolean isNumberValid(@NotNull ConversationContext context, @NotNull Number input) {
            return input.doubleValue() > 0;
        }

        @Nullable
        @Override
        protected Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull Number number) {
            Rarity rarity = getRarity();

            if(rarity.Weight == number.intValue()){
                new AdminRarityEditPanel(rarity).Show(player);
                return END_OF_CONVERSATION;
            }

            rarity.Weight = number.intValue();
            ConfigHandler.instance.rarityConfig.Save();
            new AdminRarityEditPanel(rarity).Show(player);

            return END_OF_CONVERSATION;
        }
    }



}
