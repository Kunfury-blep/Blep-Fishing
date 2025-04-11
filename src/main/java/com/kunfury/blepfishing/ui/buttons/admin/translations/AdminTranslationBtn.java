package com.kunfury.blepfishing.ui.buttons.admin.translations;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.helpers.Utilities;
import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.panels.admin.AdminTranslationsPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class AdminTranslationBtn extends MenuButton {

    private final static NamespacedKey LanguageKey = new NamespacedKey(BlepFishing.getPlugin(), "blep.language");

    private final String LanguageName;

    public AdminTranslationBtn(String translationName){
        super();

        LanguageName = translationName;
    }


    @Override
    protected ItemStack buildItemStack(Player player) {
        ItemStack item = new ItemStack(Material.OAK_SIGN);
        ItemMeta m = item.getItemMeta();

        m.setDisplayName(ChatColor.AQUA + LanguageName);

        List<String> lore = new ArrayList<>();

        YamlConfiguration languageYaml = ConfigHandler.instance.Translations.get(LanguageName);

        if(languageYaml == null){
            Utilities.Severe("Invalid language found for " + LanguageName);
            return null;
        }

        String author = languageYaml.getString("Author");
        if(author != null){
            lore.add(Formatting.GetLanguageString("UI.Admin.Buttons.Translations.author")
                    .replace("{name}", author));
        }


        lore.add("");




        var activeLanguage = Formatting.GetLanguageString("Language");
        if(activeLanguage.equals(LanguageName)){
            item.setType(Material.WARPED_SIGN);
            lore.add(Formatting.GetLanguageString("UI.Admin.Buttons.Translations.reload"));
            lore.add("");
            lore.add(Formatting.GetLanguageString("UI.System.Buttons.enabled"));
        }else
            lore.add(Formatting.GetLanguageString("UI.Admin.Buttons.Translations.select"));

        m.setLore(lore);

        m.getPersistentDataContainer().set(LanguageKey, PersistentDataType.STRING, LanguageName);

        item.setItemMeta(m);

        return item;
    }

    @Override
    protected void click_left() {
        String language = ItemHandler.getTagString(ClickedItem, LanguageKey);
        ConfigHandler.instance.LoadLanguage(language);
        new AdminTranslationsPanel().Show(player);
        player.sendMessage(
                Formatting.GetFormattedMessage("UI.Admin.Buttons.Translations.click")
                        .replace("{language}", language));
    }
}
