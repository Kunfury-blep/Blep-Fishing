package com.kunfury.blepfishing.ui.buttons.admin.quests;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.objects.quests.QuestType;
import com.kunfury.blepfishing.ui.objects.buttons.AdminQuestMenuButton;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTournamentMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.quests.AdminQuestEditPanel;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentEditPanel;
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

public class AdminQuestCatchAmountBtn extends AdminQuestMenuButton {

    public AdminQuestCatchAmountBtn(QuestType questType) {
        super(questType);
    }

    @Override
    public ItemStack buildItemStack(Player player) {

        ItemStack item = new ItemStack(Material.FISHING_ROD);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        setButtonTitle(m, "Catch Amount");

        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.WHITE.toString() + questType.CatchAmount);

        lore.add("");
        lore.add("Amount of Fish Needed to Complete");

        m.setLore(lore);
        m = setButtonId(m, getId());

        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        dataContainer.set(ItemHandler.QuestTypeId, PersistentDataType.STRING, questType.Id);


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
                .withFirstPrompt(new HornLevelPrompt())
                .withModality(true)
                .withTimeout(60)
                .thatExcludesNonPlayersWithMessage("This Conversation Factory is Player Only");
    }

    private class HornLevelPrompt extends NumericPrompt {

        @NotNull
        @Override
        public String getPromptText(@NotNull ConversationContext context) {
            questType = getQuestType();
            return ChatColor.AQUA + "How Many Fish are Needed to Complete for " + questType.Name
                    + "\nCurrent: " + ChatColor.WHITE + questType.CatchAmount;
        }

        @Override
        protected boolean isNumberValid(@NotNull ConversationContext context, @NotNull Number input) {
            return input.intValue() >= 1;
        }

        @Nullable
        @Override
        protected Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull Number number) {
            questType = getQuestType();

            questType.CatchAmount = number.intValue();

            ConfigHandler.instance.questConfig.Save();
            new AdminQuestEditPanel(questType).Show(player);

            return END_OF_CONVERSATION;
        }
    }


}
