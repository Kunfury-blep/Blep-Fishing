package com.kunfury.blepfishing.ui.buttons.admin.treasure;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.objects.FishType;
import com.kunfury.blepfishing.objects.treasure.Casket;
import com.kunfury.blepfishing.objects.treasure.TreasureType;
import com.kunfury.blepfishing.ui.buttons.admin.fishEdit.AdminFishCreateButton;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.panels.admin.fish.AdminFishEditPanel;
import com.kunfury.blepfishing.ui.panels.admin.fish.AdminFishPanel;
import com.kunfury.blepfishing.ui.panels.admin.treasure.AdminTreasureCasketsPanel;
import com.kunfury.blepfishing.ui.panels.admin.treasure.AdminTreasureEditPanel;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class AdminCasketCreateButton extends MenuButton {
    @Override
    public ItemStack buildItemStack(Player player) {
        Material mat = Material.TURTLE_SCUTE;

        ItemStack item = new ItemStack(mat);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName(Formatting.GetLanguageString("UI.Admin.Buttons.Treasure.createNew"));
        ArrayList<String> lore = new ArrayList<>();
        m.setLore(lore);
        m = setButtonId(m, getId());
        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        player.closeInventory();
        getConversation(player, new NewCasketPrompt()).begin();
    }

    private class NewCasketPrompt extends StringPrompt {

        @NotNull
        @Override
        public String getPromptText(@NotNull ConversationContext context) {
            return Formatting.GetMessagePrefix() +  "What Should The New Casket Be Named?";
        }

        @Nullable
        @Override
        public Prompt acceptInput(@NotNull ConversationContext conversationContext, @Nullable String casketName) {
            if(casketName == null){
                new AdminTreasureCasketsPanel().Show(player);
                return END_OF_CONVERSATION;
            }

            String initialId = Formatting.GetIdFromNames(casketName);

            int i = 0;
            String casketId = initialId;
            while(TreasureType.IdExists(casketId)){
                conversationContext.getForWhom().sendRawMessage(Formatting.GetMessagePrefix() + "Casket with ID of "
                        + casketId + " already exists. Changing to " + initialId + i);
                casketId = initialId + i;
                i++;
            }

            Casket casket = new Casket(casketId, casketName, 0, false, new ArrayList<>(), 0);
            Casket.AddNew(casket);

            ConfigHandler.instance.treasureConfig.Save();
            new AdminTreasureEditPanel(casket).Show(player);

            return END_OF_CONVERSATION;
        }
    }

}
