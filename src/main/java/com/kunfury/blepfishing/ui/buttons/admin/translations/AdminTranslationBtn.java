package com.kunfury.blepfishing.ui.buttons.admin.translations;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.items.ItemHandler;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AdminTranslationBtn extends MenuButton {

    private final static NamespacedKey TranslationKey = new NamespacedKey(BlepFishing.getPlugin(), "blep.translationName");

    private final String TranslationName;

    public AdminTranslationBtn(String translationName){
        super();

        TranslationName = translationName;
    }


    @Override
    protected ItemStack buildItemStack() {
        ItemStack item = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta m = item.getItemMeta();

        var title = TranslationName.replace("translations/", "").replace(".yml", "");

        m.setDisplayName(ChatColor.AQUA + title);

        List<String> lore = new ArrayList<>();





        var testStr = getTranslationYaml(TranslationName).getString("Test");

        lore.add(ChatColor.WHITE + testStr);

        lore.add("");

        lore.add(ChatColor.YELLOW + "Left-Click to Select");


        m.setLore(lore);

        m.getPersistentDataContainer().set(TranslationKey, PersistentDataType.STRING, TranslationName);

        item.setItemMeta(m);

        return item;
    }

    @Override
    protected void click_left() {
        String translationName = ItemHandler.getTagString(ClickedItem, TranslationKey);
        var yaml = getTranslationYaml(translationName);

        Bukkit.broadcastMessage(yaml.getKeys(true).toString());
    }

    private YamlConfiguration getTranslationYaml(String path){

        var resourceStream = BlepFishing.instance.getResource(path);
        File translationFile = new File(path);

        try {
            FileUtils.copyInputStreamToFile(resourceStream, translationFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //File translationFile = new File(BlepFishing.instance.getDataFolder(), TranslationName);
        return YamlConfiguration.loadConfiguration(translationFile);
    }
}
