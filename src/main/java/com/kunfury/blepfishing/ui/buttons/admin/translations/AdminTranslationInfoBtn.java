package com.kunfury.blepfishing.ui.buttons.admin.translations;

import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.helpers.Utilities;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class AdminTranslationInfoBtn extends MenuButton {
    @Override
    protected ItemStack buildItemStack(Player player) {
        ItemStack item = new ItemStack(Material.TURTLE_EGG);
        ItemMeta m = item.getItemMeta();

        m.setDisplayName(Formatting.GetLanguageString("UI.Admin.Buttons.Translations.Info.name"));

        List<String> lore = new ArrayList<>();

        lore.addAll(Formatting.ToLoreList(
                Formatting.GetLanguageString("UI.Admin.Buttons.Translations.Info.lore")));

        m.setLore(lore);

        item.setItemMeta(m);

        return item;
    }

    @Override
    protected void click_left() {
        TextComponent message = new TextComponent(Formatting.GetLanguageString("UI.Admin.Buttons.Translations.Info.click"));
        message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.com/invite/vuBCHngkFH"));

        player.spigot().sendMessage(message);
        player.closeInventory();
    }
}
