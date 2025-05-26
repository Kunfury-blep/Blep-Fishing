package com.kunfury.blepfishing.ui.buttons.admin.areas;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.objects.FishingArea;
import com.kunfury.blepfishing.ui.buttons.admin.treasure.CasketEditIdBtn;
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

public class AdminAreaCompassHintBtn extends AdminAreaMenuButton {

    public AdminAreaCompassHintBtn(FishingArea area) {
        super(area);
    }

    @Override
    public ItemStack buildItemStack(Player player) {
        ItemStack item = new ItemStack(Material.COMPASS);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName(ChatColor.AQUA + "Compass Hint");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE + area.CompassHint);
        m.setLore(lore);

        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        player.closeInventory();
        getConversation(player, new HintPrompt()).begin();
    }

    private class HintPrompt extends StringPrompt {

        @NotNull
        @Override
        public String getPromptText(@NotNull ConversationContext context) {
            return Formatting.GetMessagePrefix() + "What should the compass hint be? Current: " + getArea().CompassHint;
        }

        @Nullable
        @Override
        public Prompt acceptInput(@NotNull ConversationContext conversationContext, @Nullable String s) {
            FishingArea area = getArea();

            if(Objects.equals(area.CompassHint, s)){
                new AdminAreasEditPanel(area).Show(player);
                return END_OF_CONVERSATION; //If the hint wasn't changed, no need to save
            }


            area.CompassHint = s;

            ConfigHandler.instance.areaConfig.Save();
            new AdminAreasEditPanel(area).Show(player);

            return END_OF_CONVERSATION;
        }
    }


}
