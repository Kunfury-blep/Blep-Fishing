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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class TreasureEditWeightBtn extends AdminTreasureMenuButton {
    public TreasureEditWeightBtn(Casket casket) {
        super(casket);
    }

    @Override
    public ItemStack buildItemStack() {
        ItemStack item = new ItemStack(Material.ANVIL);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName(Formatting.GetLanguageString("UI.Admin.Buttons.Treasure.Weight.name"));
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE.toString() + casket.Weight);
        lore.add("");
        lore.add(Formatting.GetLanguageString("UI.Admin.Buttons.Treasure.Weight.lore"));
        m.setLore(lore);
        m = setButtonId(m, getId());

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

    private class DurationPrompt extends NumericPrompt {

        @NotNull
        @Override
        public String getPromptText(@NotNull ConversationContext context) {
            TreasureType type = getCasket();
            return Formatting.GetLanguageString("UI.Admin.Buttons.Treasure.Weight.prompt")
                    .replace("{amount}", String.valueOf(type.Weight));
        }

        @Override
        protected boolean isNumberValid(@NotNull ConversationContext context, @NotNull Number input) {
            return input.intValue() > 0;
        }

        @Nullable
        @Override
        protected Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull Number number) {
            Casket casket = getCasket();
            casket.Weight = number.intValue();

            ConfigHandler.instance.treasureConfig.Save();
            new AdminTreasureEditPanel(casket).Show(player);

            return END_OF_CONVERSATION;
        }
    }


}
