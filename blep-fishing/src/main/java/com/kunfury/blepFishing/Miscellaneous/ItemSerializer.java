package com.kunfury.blepFishing.Miscellaneous;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ItemSerializer {
    public static String itemStackToBase64(ItemStack item) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeObject(item);
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public static ItemStack parseItem(String data) throws IOException{
        String dataType;

        if(data.length() > 6){
            dataType = data.substring(0, 6);
            String itemStr = data.replace(dataType, "");
            ItemStack item = new ItemStack(Material.AIR, 1);
            switch(dataType){
                case "ITEM: ":
                    String[] infoArray = itemStr.split(" ");
                    if(infoArray.length >= 2){
                        item.setType(Material.valueOf(infoArray[0]));
                        item.setAmount(Integer.parseInt(infoArray[1]));
                    }
                    return item;
                case "CASH: ":
                    item.setAmount(Integer.parseInt(itemStr));
                    return item;
                case "BYTE: ":
                    return itemStackFromBase64(itemStr);
            }
        }
        return null;
    }

    public static ItemStack itemStackFromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack item = (ItemStack) dataInput.readObject();

            dataInput.close();
            return item;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }
}
