package com.kunfury.blepfishing.ui.buttons.admin.treasure;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.items.ItemHandler;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.objects.TreasureType;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTournamentMenuButton;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTreasureMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentEditPanel;
import com.kunfury.blepfishing.ui.panels.admin.treasure.AdminTreasureEditPanel;
import org.bukkit.ChatColor;
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

public class TreasureEditNameBtn extends AdminTreasureMenuButton {
    public TreasureEditNameBtn(TreasureType type) {
        super(type);
    }

    @Override
    public ItemStack buildItemStack() {
        ItemStack item = new ItemStack(Material.NAME_TAG);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName("Name");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE + treasureType.Name);
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
            return "What should the name of the treasure be? Current: " + getTreasureType().Name;
        }

        @Override
        protected boolean isInputValid(@NotNull ConversationContext conversationContext, @NotNull String s) {
            if(getTreasureType().Name.equals(s)) return true;
            return !TreasureType.IdExists(Formatting.getIdFromName(s));
        }

        @Nullable
        @Override
        protected Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull String s) {
            TreasureType type = getTreasureType();
            String oldId = type.Id;
            String oldName = type.Name;

            if(Objects.equals(oldName, s))
                return END_OF_CONVERSATION; //If the name wasn't changed, no need to save

            type.Name = s;

            type.Id = Formatting.getIdFromName(s);

            TreasureType.UpdateId(oldId, type);

            ConfigHandler.instance.treasureConfig.Save();
            new AdminTreasureEditPanel(type).Show(player);

            return END_OF_CONVERSATION;
        }
    }


}
