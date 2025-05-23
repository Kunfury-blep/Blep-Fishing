package com.kunfury.blepfishing.ui.buttons.admin.rarities;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.objects.FishType;
import com.kunfury.blepfishing.objects.Rarity;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.panels.admin.fish.AdminFishEditPanel;
import com.kunfury.blepfishing.ui.panels.admin.fish.AdminFishPanel;
import com.kunfury.blepfishing.ui.panels.admin.rarities.AdminRarityEditPanel;
import com.kunfury.blepfishing.ui.panels.admin.rarities.AdminRarityPanel;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class AdminRarityCreateBtn extends MenuButton {
    @Override
    public ItemStack buildItemStack(Player player) {
        Material mat = Material.TURTLE_SCUTE;

        ItemStack item = new ItemStack(mat);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName("Create New Rarity");
        ArrayList<String> lore = new ArrayList<>();
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
                .withFirstPrompt(new NewRarityPrompt())
                .withModality(true)
                .withTimeout(60)
                .thatExcludesNonPlayersWithMessage("This Conversation Factory is Player Only");
    }

    private class NewRarityPrompt extends StringPrompt {

        @NotNull
        @Override
        public String getPromptText(@NotNull ConversationContext context) {
            return Formatting.GetMessagePrefix() +  "What Should The New Rarity Be Named?";
        }

        @Nullable
        @Override
        public Prompt acceptInput(@NotNull ConversationContext conversationContext, @Nullable String rarityName) {
            if(rarityName == null){
                new AdminRarityPanel().Show(player);
                return END_OF_CONVERSATION;
            }

            String initialId = Formatting.GetIdFromNames(rarityName);

            int i = 0;
            String rarityId = initialId;
            while(Rarity.IdExists(rarityId)){
                conversationContext.getForWhom().sendRawMessage(Formatting.GetMessagePrefix() + "Rarity with ID of " + rarityId + " already exists. Changing to " + initialId + i);
                rarityId = initialId + i;
                i++;
            }

            Rarity rarity = new Rarity(
                    rarityId, rarityName, "", 0,false, 1.0);
            Rarity.AddNew(rarity);

            ConfigHandler.instance.rarityConfig.Save();
            new AdminRarityEditPanel(rarity).Show(player);

            return END_OF_CONVERSATION;
        }
    }





}
