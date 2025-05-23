package com.kunfury.blepfishing.ui.buttons.admin.treasure;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.objects.treasure.Casket;
import com.kunfury.blepfishing.objects.treasure.TreasureType;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTreasureMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.treasure.AdminTreasureEditPanel;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Objects;

public class CasketEditNameBtn extends AdminTreasureMenuButton {
    public CasketEditNameBtn(Casket casket) {
        super(casket);
    }

    @Override
    public ItemStack buildItemStack(Player player) {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        Casket casket = (Casket)treasureType;


        m.setDisplayName(Formatting.GetLanguageString("UI.Admin.Buttons.Treasure.Name.name"));
        ArrayList<String> lore = new ArrayList<>();
        lore.add(Formatting.GetLanguageString("UI.Admin.Buttons.Treasure.Name.lore")
                .replace("{name}", casket.Name));
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
                .withFirstPrompt(new NamePrompt())
                .withModality(true)
                .withTimeout(60)
                .thatExcludesNonPlayersWithMessage("This Conversation Factory is Player Only");
    }

    private class NamePrompt extends StringPrompt {

        @NotNull
        @Override
        public String getPromptText(@NotNull ConversationContext context) {
            Casket casket = (Casket) getTreasureType();
            return Formatting.GetLanguageString("UI.Admin.Buttons.Treasure.Name.prompt")
                    .replace("{name}", casket.Name);
        }

        @Nullable
        @Override
        public Prompt acceptInput(@NotNull ConversationContext conversationContext, @Nullable String s) {
            Casket casket = (Casket) getTreasureType();
            String oldName = casket.Name;

            if(Objects.equals(oldName, s)){
                new AdminTreasureEditPanel(casket).Show(player);
                return END_OF_CONVERSATION; //If the name wasn't changed, no need to save
            }

            casket.Name = s;

            ConfigHandler.instance.treasureConfig.Save();
            new AdminTreasureEditPanel(casket).Show(player);

            return END_OF_CONVERSATION;
        }
    }


}
