package com.kunfury.blepfishing.helpers;

import com.kunfury.blepfishing.config.ConfigHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class ItemParser {
    public static ItemStack ParseBasic(String type, int amount){
        Material material = Material.getMaterial(type);
        if(material == null){
            ConfigHandler.instance.ReportIssue("Error parsing material: " + type);
            return null;
        }

        ItemStack item = new ItemStack(material);
        item.setAmount(amount);


        return item;
    }

    public static ItemStack ParseBasic(String data){
        String[] splitStr = data.trim().split("\\s+");

        if(!splitStr[1].matches("-?(0|[1-9]\\d*)")){
            ConfigHandler.instance.ReportIssue("Error parsing basic item: " + data);
            return null;
        }

        int amt = Integer.parseInt(splitStr[1]);

        return ParseBasic(splitStr[0], amt);
    }

    public static String itemToStringBlob(ItemStack item){
        YamlConfiguration itemConfig = new YamlConfiguration();
        itemConfig.set("item", item);
        return itemConfig.saveToString();
    }

    public static ItemStack stringBlobToItem(String stringBlob) {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(stringBlob);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return config.getItemStack("item", null);
    }

}
