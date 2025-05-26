package com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTournamentMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentEditPanel;
import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.objects.TournamentType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class TournamentEditDurationBtn extends AdminTournamentMenuButton {
    public TournamentEditDurationBtn(TournamentType tournamentType) {
        super(tournamentType);
    }

    @Override
    public ItemStack buildItemStack(Player player) {
        ItemStack item = new ItemStack(Material.CLOCK);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName("Duration");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(Formatting.asTime(tournament.Duration, ChatColor.BLUE));
        m.setLore(lore);
        m = setButtonId(m, getId());

        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        dataContainer.set(ItemHandler.TourneyTypeId, PersistentDataType.STRING, tournament.Id);


        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        player.closeInventory();
        getConversation(player, new DurationPrompt()).begin();
    }

    private class DurationPrompt extends NumericPrompt {

        @NotNull
        @Override
        public String getPromptText(@NotNull ConversationContext context) {
            TournamentType type = getTournamentType();
            return Formatting.GetMessagePrefix() + "How long should the " + type.Name + " tournament last, in hours? Current: " + Formatting.asTime(type.Duration, ChatColor.BLUE);
        }

        @Override
        protected boolean isNumberValid(@NotNull ConversationContext context, @NotNull Number input) {
            return input.doubleValue() > 0;
        }

        @Nullable
        @Override
        protected Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull Number number) {
            TournamentType type = getTournamentType();

            type.Duration = number.doubleValue();

            ConfigHandler.instance.tourneyConfig.Save();
            new AdminTournamentEditPanel(type).Show(player);

            return END_OF_CONVERSATION;
        }
    }


}
