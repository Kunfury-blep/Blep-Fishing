package com.kunfury.blepfishing.ui.buttons.admin.areas;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.objects.FishingArea;
import com.kunfury.blepfishing.ui.objects.buttons.AdminAreaMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.areas.AdminAreasEditPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.Format;
import java.util.ArrayList;
import java.util.Objects;

public class AdminAreaIdBtn extends AdminAreaMenuButton {

    public AdminAreaIdBtn(FishingArea area) {
        super(area);
    }

    @Override
    public ItemStack buildItemStack(Player player) {
        ItemStack item = new ItemStack(Material.NAME_TAG);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName(ChatColor.AQUA + "Area ID");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE + area.Id);
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
                .withFirstPrompt(new IdPrompt())
                .withModality(true)
                .withTimeout(60)
                .thatExcludesNonPlayersWithMessage("This Conversation Factory is Player Only");
    }

    private class IdPrompt extends ValidatingPrompt {

        @NotNull
        @Override
        public String getPromptText(@NotNull ConversationContext context) {
            return "What should the Area ID be? Current: " + getArea().Id;
        }

        @Override
        protected boolean isInputValid(@NotNull ConversationContext conversationContext, @NotNull String s) {
            s = Formatting.FormatId(s);

            if(getArea().Id.equals(s)) return true;
            return !FishingArea.IdExists(s);
        }

        @Nullable
        @Override
        protected Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull String s) {
            FishingArea area = getArea();
            String oldId = area.Id;
            s = Formatting.FormatId(s);

            if(Objects.equals(oldId, s)){
                new AdminAreasEditPanel(area).Show(player);
                return END_OF_CONVERSATION; //If the name wasn't changed, no need to save
            }

            area.Id = s;

            FishingArea.UpdateId(oldId, area);

            ConfigHandler.instance.areaConfig.Save();
            new AdminAreasEditPanel(area).Show(player);

            return END_OF_CONVERSATION;
        }
    }


}
