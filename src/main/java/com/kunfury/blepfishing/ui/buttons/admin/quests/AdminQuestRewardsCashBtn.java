package com.kunfury.blepfishing.ui.buttons.admin.quests;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.objects.quests.QuestType;
import com.kunfury.blepfishing.ui.objects.buttons.AdminQuestMenuButton;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTournamentRewardsMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.quests.AdminQuestRewardsPanel;
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

public class AdminQuestRewardsCashBtn extends AdminQuestMenuButton {


    public AdminQuestRewardsCashBtn(QuestType type){
        super(type);
    }

    @Override
    public ItemStack buildItemStack(Player player) {
        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName("Currency Reward");
        ArrayList<String> lore = new ArrayList<>();
        lore.add("Current: " + ChatColor.WHITE + "$" + ChatColor.GREEN + questType.CashReward);
        lore.add("");
        lore.add("Left-Click to Edit");
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
                .withFirstPrompt(new CashPrompt())
                .withModality(true)
                .withTimeout(60)
                .thatExcludesNonPlayersWithMessage("This Conversation Factory is Player Only");
    }

    private class CashPrompt extends NumericPrompt {

        @NotNull
        @Override
        public String getPromptText(@NotNull ConversationContext context) {
            questType = getQuestType();
            return "What should the reward for the tournament be? Current: " + questType.CashReward;
        }

        @Override
        protected boolean isNumberValid(@NotNull ConversationContext context, @NotNull Number input) {
            return input.doubleValue() >= 0;
        }

        @Nullable
        @Override
        protected Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull Number number) {
            questType = getQuestType();

            questType.CashReward = number.doubleValue();

            ConfigHandler.instance.questConfig.Save();
            new AdminQuestRewardsPanel(questType).Show(player);

            return END_OF_CONVERSATION;
        }
    }


}
