package com.kunfury.blepfishing.ui.buttons.admin.treasure;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.objects.treasure.Casket;
import com.kunfury.blepfishing.objects.treasure.TreasureType;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTreasureMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.treasure.AdminTreasureEditPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class CasketEditWeightBtn extends AdminTreasureMenuButton {
    public CasketEditWeightBtn(Casket casket) {
        super(casket);
    }

    @Override
    public ItemStack buildItemStack(Player player) {
        ItemStack item = new ItemStack(Material.ANVIL);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName(Formatting.GetLanguageString("UI.Admin.Buttons.Treasure.Weight.name"));
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE.toString() + treasureType.Weight);
        lore.add("");
        lore.add(Formatting.GetLanguageString("UI.Admin.Buttons.Treasure.Weight.lore"));
        m.setLore(lore);
        m = setButtonId(m, getId());

        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        player.closeInventory();
        getConversation(player, new WeightPrompt()).begin();
    }

    private class WeightPrompt extends NumericPrompt {

        @NotNull
        @Override
        public String getPromptText(@NotNull ConversationContext context) {
            TreasureType type = getTreasureType();
            return Formatting.GetFormattedMessage("UI.Admin.Buttons.Treasure.Weight.prompt")
                    .replace("{amount}", String.valueOf(type.Weight));
        }

        @Override
        protected boolean isNumberValid(@NotNull ConversationContext context, @NotNull Number input) {
            return input.intValue() > 0;
        }

        @Nullable
        @Override
        protected Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull Number number) {
            Casket casket = (Casket) getTreasureType();
            casket.Weight = number.intValue();

            ConfigHandler.instance.treasureConfig.Save();
            new AdminTreasureEditPanel(casket).Show(player);

            return END_OF_CONVERSATION;
        }
    }


}
