package com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTournamentRewardsMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentEditRewardsPlacementPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class TournamentEditRewardsCashBtn extends AdminTournamentRewardsMenuButton {


    public TournamentEditRewardsCashBtn(TournamentType type, int placement){
        super(type, placement);
    }

    @Override
    public ItemStack buildItemStack(Player player) {
        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName("#" + Placement + " Cash Rewards");
        ArrayList<String> lore = new ArrayList<>();
        lore.add("Current: " + ChatColor.WHITE + "$" + ChatColor.GREEN + GetCash());
        lore.add("");
        lore.add("Left-Click to Edit");
        m.setLore(lore);

        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        player.closeInventory();
        getConversation(player, new CashPrompt()).begin();
    }

    private class CashPrompt extends NumericPrompt {

        @NotNull
        @Override
        public String getPromptText(@NotNull ConversationContext context) {
            TournamentType type = getTournamentType();
            int placement = getPlacement();
            return Formatting.GetMessagePrefix() + "What should the reward for #" + placement + " in the tournament be? Current: " + type.CashRewards.get(placement);
        }

        @Override
        protected boolean isNumberValid(@NotNull ConversationContext context, @NotNull Number input) {
            return input.doubleValue() >= 0;
        }

        @Nullable
        @Override
        protected Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull Number number) {
            TournamentType type = getTournamentType();
            int placement = getPlacement();

            type.CashRewards.put(placement, number.doubleValue());

            ConfigHandler.instance.tourneyConfig.Save();
            new AdminTournamentEditRewardsPlacementPanel(type, placement).Show(player);

            return END_OF_CONVERSATION;
        }
    }


}
