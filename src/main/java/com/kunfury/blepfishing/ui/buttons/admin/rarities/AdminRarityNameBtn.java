package com.kunfury.blepfishing.ui.buttons.admin.rarities;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.objects.Rarity;
import com.kunfury.blepfishing.ui.objects.buttons.AdminRarityMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.rarities.AdminRarityEditPanel;
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

public class AdminRarityNameBtn extends AdminRarityMenuButton {

    public AdminRarityNameBtn(Rarity rarity) {
        super(rarity);
    }

    @Override
    public ItemStack buildItemStack(Player player) {
        ItemStack item = new ItemStack(Material.NAME_TAG);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName("Name");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE + rarity.Name);
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

    private class NamePrompt extends ValidatingPrompt {

        @NotNull
        @Override
        public String getPromptText(@NotNull ConversationContext context) {
            return "What should the Rarity name be? Current: " + getRarity().Name;
        }

        @Override
        protected boolean isInputValid(@NotNull ConversationContext conversationContext, @NotNull String s) {
            if(getRarity().Name.equals(s)) return true;
            return !Rarity.IdExists(Formatting.GetIdFromNames(s));
        }

        @Nullable
        @Override
        protected Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull String s) {
            Rarity rarity = getRarity();
            String oldId = rarity.Id;
            String oldName = rarity.Name;

            if(Objects.equals(oldName, s)){
                new AdminRarityEditPanel(rarity).Show(player);
                return END_OF_CONVERSATION; //If the name wasn't changed, no need to save
            }


            rarity.Name = s;
            rarity.Id = Formatting.GetIdFromNames(s);

            Rarity.UpdateId(oldId, rarity);

            ConfigHandler.instance.rarityConfig.Save();
            new AdminRarityEditPanel(rarity).Show(player);

            return END_OF_CONVERSATION;
        }
    }


}
