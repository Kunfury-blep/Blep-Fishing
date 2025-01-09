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

import java.util.ArrayList;
import java.util.Objects;

public class AdminAreaNameBtn extends AdminAreaMenuButton {

    public AdminAreaNameBtn(FishingArea area) {
        super(area);
    }

    @Override
    public ItemStack buildItemStack(Player player) {
        ItemStack item = new ItemStack(Material.NAME_TAG);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName(ChatColor.AQUA + "Name");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE + area.Name);
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
            return "What should the area name be? Current: " + getArea().Name;
        }

        @Override
        protected boolean isInputValid(@NotNull ConversationContext conversationContext, @NotNull String s) {
            if(getArea().Name.equals(s)) return true;
            return !FishingArea.IdExists(Formatting.GetIdFromNames(s));
        }

        @Nullable
        @Override
        protected Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull String s) {
            FishingArea area = getArea();
            String oldId = area.Id;
            String oldName = area.Name;

            if(Objects.equals(oldName, s)){
                new AdminAreasEditPanel(area).Show(player);
                return END_OF_CONVERSATION; //If the name wasn't changed, no need to save
            }


            area.Name = s;
            area.Id = Formatting.GetIdFromNames(s);

            FishingArea.UpdateId(oldId, area);

            ConfigHandler.instance.areaConfig.Save();
            new AdminAreasEditPanel(area).Show(player);

            return END_OF_CONVERSATION;
        }
    }


}
