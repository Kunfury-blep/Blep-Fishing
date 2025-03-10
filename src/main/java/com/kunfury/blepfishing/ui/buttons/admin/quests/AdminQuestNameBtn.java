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
import java.util.Objects;

public class AdminQuestNameBtn extends AdminQuestMenuButton {
    public AdminQuestNameBtn(QuestType questType) {
        super(questType);
    }

    @Override
    public ItemStack buildItemStack(Player player) {
        ItemStack item = new ItemStack(Material.NAME_TAG);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        setButtonTitle(m, "Name");

        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE + questType.Name);
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

    private class DurationPrompt extends ValidatingPrompt {

        @NotNull
        @Override
        public String getPromptText(@NotNull ConversationContext context) {
            return "What should the quest name be? Current: " + getQuestType().Name;
        }

        @Override
        protected boolean isInputValid(@NotNull ConversationContext conversationContext, @NotNull String s) {
            if(getQuestType().Name.equals(s)) return true;
            return !QuestType.IdExists(Formatting.GetIdFromNames(s));
        }

        @Nullable
        @Override
        protected Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull String s) {
            QuestType type = getQuestType();
            String oldId = type.Id;
            String oldName = type.Name;

            if(Objects.equals(oldName, s))
                return END_OF_CONVERSATION; //If the name wasn't changed, no need to save

            type.Name = s;

            type.Id = Formatting.GetIdFromNames(s);

            QuestType.UpdateId(oldId, type);

            ConfigHandler.instance.questConfig.Save();
            new AdminQuestEditPanel(type).Show(player);

            return END_OF_CONVERSATION;
        }
    }


}
