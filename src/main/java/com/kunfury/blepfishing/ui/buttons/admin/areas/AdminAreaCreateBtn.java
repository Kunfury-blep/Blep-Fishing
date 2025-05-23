package com.kunfury.blepfishing.ui.buttons.admin.areas;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.objects.FishType;
import com.kunfury.blepfishing.objects.FishingArea;
import com.kunfury.blepfishing.ui.buttons.admin.fishEdit.AdminFishCreateButton;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.panels.admin.areas.AdminAreasEditPanel;
import com.kunfury.blepfishing.ui.panels.admin.areas.AdminAreasPanel;
import com.kunfury.blepfishing.ui.panels.admin.fish.AdminFishEditPanel;
import com.kunfury.blepfishing.ui.panels.admin.fish.AdminFishPanel;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class AdminAreaCreateBtn extends MenuButton {
    @Override
    public ItemStack buildItemStack(Player player) {
        Material mat = Material.TURTLE_SCUTE;

        ItemStack item = new ItemStack(mat);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName("Create New Area");
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
                .withFirstPrompt(new NewAreaPrompt())
                .withModality(true)
                .withTimeout(60)
                .thatExcludesNonPlayersWithMessage("This Conversation Factory is Player Only");
    }

    private class NewAreaPrompt extends StringPrompt {

        @NotNull
        @Override
        public String getPromptText(@NotNull ConversationContext context) {
            return Formatting.GetMessagePrefix() +  "What Should The New Fishing Area Be Named?";
        }

        @Nullable
        @Override
        public Prompt acceptInput(@NotNull ConversationContext conversationContext, @Nullable String areaName) {
            if(areaName == null){
                new AdminAreasPanel().Show(player);
                return END_OF_CONVERSATION;
            }

            String initialId = Formatting.GetIdFromNames(areaName);

            int i = 0;
            String areaId = initialId;
            while(FishingArea.IdExists(areaId)){
                conversationContext.getForWhom().sendRawMessage(Formatting.GetMessagePrefix() + "Area with ID of " + areaId + " already exists. Changing to " + initialId + i);
                areaId = initialId + i;
                i++;
            }

            FishingArea area = new FishingArea(
                    areaId, areaName, new ArrayList<>(), false,null);
            FishingArea.AddNew(area);

            ConfigHandler.instance.areaConfig.Save();
            new AdminAreasEditPanel(area).Show(player);

            return END_OF_CONVERSATION;
        }
    }



}
