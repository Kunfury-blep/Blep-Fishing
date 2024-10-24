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
import java.util.Objects;

public class TreasureEditNameBtn extends AdminTreasureMenuButton {
    public TreasureEditNameBtn(Casket casket) {
        super(casket);
    }

    @Override
    public ItemStack buildItemStack() {
        ItemStack item = new ItemStack(Material.NAME_TAG);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName(Formatting.GetLanguageString("UI.Admin.Buttons.Treasure.Name.name"));
        ArrayList<String> lore = new ArrayList<>();
        lore.add(Formatting.GetLanguageString("UI.Admin.Buttons.Treasure.Name.lore")
                .replace("{name}", casket.Name));
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

    private class DurationPrompt extends ValidatingPrompt {

        @NotNull
        @Override
        public String getPromptText(@NotNull ConversationContext context) {
            return Formatting.GetLanguageString("UI.Admin.Buttons.Treasure.Name.prompt")
                    .replace("{name}", getCasket().Name);
        }

        @Override
        protected boolean isInputValid(@NotNull ConversationContext conversationContext, @NotNull String s) {
            if(getCasket().Name.equals(s)) return true;
            return !TreasureType.IdExists(Formatting.GetIdFromNames(s));
        }

        @Nullable
        @Override
        protected Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull String s) {
            Casket casket = getCasket();
            String oldId = casket.Id;
            String oldName = casket.Name;

            if(Objects.equals(oldName, s))
                return END_OF_CONVERSATION; //If the name wasn't changed, no need to save

            casket.Name = s;

            casket.Id = Formatting.GetIdFromNames(s);

            TreasureType.UpdateId(oldId, casket);

            ConfigHandler.instance.treasureConfig.Save();
            new AdminTreasureEditPanel(casket).Show(player);

            return END_OF_CONVERSATION;
        }
    }


}
