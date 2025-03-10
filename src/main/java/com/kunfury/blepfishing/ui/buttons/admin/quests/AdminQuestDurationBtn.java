package com.kunfury.blepfishing.ui.buttons.admin.quests;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.objects.quests.QuestType;
import com.kunfury.blepfishing.ui.objects.buttons.AdminQuestMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.quests.AdminQuestEditPanel;
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

public class AdminQuestDurationBtn extends AdminQuestMenuButton {
    public AdminQuestDurationBtn(QuestType questType) {
        super(questType);
    }

    @Override
    public ItemStack buildItemStack(Player player) {
        ItemStack item = new ItemStack(Material.CLOCK);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        setButtonTitle(m, "Duration");

        ArrayList<String> lore = new ArrayList<>();
        lore.add(Formatting.asTime(questType.Duration, ChatColor.BLUE));
        m.setLore(lore);

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
                .withFirstPrompt(new DurationPrompt())
                .withModality(true)
                .withTimeout(60)
                .thatExcludesNonPlayersWithMessage("This Conversation Factory is Player Only");
    }

    private class DurationPrompt extends NumericPrompt {

        @NotNull
        @Override
        public String getPromptText(@NotNull ConversationContext context) {
            QuestType type = getQuestType();
            return "How long should the " + type.Name + " quest last, in hours? Current: " + Formatting.asTime(type.Duration, ChatColor.BLUE);
        }

        @Override
        protected boolean isNumberValid(@NotNull ConversationContext context, @NotNull Number input) {
            return input.doubleValue() > 0;
        }

        @Nullable
        @Override
        protected Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull Number number) {
            QuestType type = getQuestType();

            type.Duration = number.doubleValue();

            ConfigHandler.instance.questConfig.Save();
            new AdminQuestEditPanel(type).Show(player);

            return END_OF_CONVERSATION;
        }
    }


}
