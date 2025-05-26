package com.kunfury.blepfishing.ui.buttons.admin.fishEdit;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.panels.admin.fish.AdminFishEditPanel;
import com.kunfury.blepfishing.objects.FishType;
import com.kunfury.blepfishing.ui.panels.admin.fish.AdminFishPanel;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class AdminFishCreateButton extends MenuButton {

    @Override
    public ItemStack buildItemStack(Player player) {
        Material mat = Material.TURTLE_SCUTE;

        ItemStack item = new ItemStack(mat);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName("Create New Fish");
        ArrayList<String> lore = new ArrayList<>();
        m.setLore(lore);
        m = setButtonId(m, getId());
        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        player.closeInventory();
        getConversation(player, new NewFishPrompt()).begin();
    }

    private class NewFishPrompt extends StringPrompt {

        @NotNull
        @Override
        public String getPromptText(@NotNull ConversationContext context) {
            return Formatting.GetMessagePrefix() +  "What Should The New Fish Be Named?";
        }

        @Nullable
        @Override
        public Prompt acceptInput(@NotNull ConversationContext conversationContext, @Nullable String fishName) {
            if(fishName == null){
                new AdminFishPanel(1).Show(player);
                return END_OF_CONVERSATION;
            }

            String initialId = Formatting.GetIdFromNames(fishName);

            int i = 0;
            String fishId = initialId;
            while(FishType.IdExists(fishId)){
                conversationContext.getForWhom().sendRawMessage(Formatting.GetMessagePrefix() + "Fish with ID of " + fishId + " already exists. Changing to " + initialId + i);
                fishId = initialId + i;
                i++;
            }

            FishType fishType = new FishType(fishId, fishName, "", "", 1, 100, 0, 0, new ArrayList<>(), false, 0, 0);
            FishType.AddFishType(fishType);
            ConfigHandler.instance.fishConfig.Save();
            new AdminFishEditPanel(fishType).Show(player);

            return END_OF_CONVERSATION;
        }
    }




}
