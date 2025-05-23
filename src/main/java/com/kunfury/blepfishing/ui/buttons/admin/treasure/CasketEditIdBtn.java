package com.kunfury.blepfishing.ui.buttons.admin.treasure;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.objects.treasure.Casket;
import com.kunfury.blepfishing.objects.treasure.TreasureType;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTreasureMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentPanel;
import com.kunfury.blepfishing.ui.panels.admin.treasure.AdminTreasureCasketsPanel;
import com.kunfury.blepfishing.ui.panels.admin.treasure.AdminTreasureEditPanel;
import org.antlr.v4.codegen.model.chunk.SetNonLocalAttr;
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

public class CasketEditIdBtn extends AdminTreasureMenuButton {
    public CasketEditIdBtn(Casket casket) {
        super(casket);
    }

    @Override
    public ItemStack buildItemStack(Player player) {
        ItemStack item = new ItemStack(Material.NAME_TAG);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        Casket casket = (Casket)treasureType;

        m.setDisplayName("Casket ID");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE + casket.Id);

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
                .withFirstPrompt(new IdPrompt())
                .withModality(true)
                .withTimeout(60)
                .thatExcludesNonPlayersWithMessage("This Conversation Factory is Player Only");
    }

    private class IdPrompt extends ValidatingPrompt {

        @NotNull
        @Override
        public String getPromptText(@NotNull ConversationContext context) {
            Casket casket = (Casket) getTreasureType();
            return "What should the Casket ID be? Current: " + casket.Id;
        }

        @Override
        protected boolean isInputValid(@NotNull ConversationContext conversationContext, @NotNull String s) {
            s = Formatting.FormatId(s);
            if(getTreasureType().Id.equals(s)) return true;

            return !TreasureType.IdExists(s);
        }

        @Nullable
        @Override
        protected Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull String s) {
            Casket casket = (Casket) getTreasureType();
            String oldId = casket.Id;
            s = Formatting.FormatId(s);

            if(Objects.equals(oldId, s)){
                new AdminTreasureCasketsPanel().Show(player);
                return END_OF_CONVERSATION; //If the name wasn't changed, no need to save

            }

            casket.Id = s;

            TreasureType.UpdateId(oldId, casket);

            ConfigHandler.instance.treasureConfig.Save();
            new AdminTreasureEditPanel(casket).Show(player);

            return END_OF_CONVERSATION;
        }
    }


}
