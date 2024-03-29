package com.kunfury.blepFishing.Miscellaneous;

import com.kunfury.blepFishing.BlepFishing;
import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Tournament.Rewards;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.*;
import java.util.UUID;

public class ItemHandler {
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
                        Material type = Material.getMaterial(infoArray[0]);

                        if(type != null){
                            item.setType(Material.valueOf(infoArray[0]));
                            item.setAmount(Integer.parseInt(infoArray[1]));
                        }else{
                            Bukkit.getLogger().warning("Error creating Blep Fishing casket reward from " + infoArray[0] + ". Defaulting to air.");
                            item.setType(Material.AIR);
                            item.setAmount(0);
                        }


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

    public static void parseReward(String data, UUID uuid){
        String dataType;

        if(data.length() > 6){
            dataType = data.substring(0, 6);
            String itemStr = data.replace(dataType, "");
            String[] infoArray = itemStr.split(" ");

            try{
                switch (dataType) {
                    case "ITEM: " -> {
                        if(Material.matchMaterial(infoArray[0]) != null) {
                            ItemStack item = new ItemStack(Material.valueOf(infoArray[0]));
                            if (infoArray.length >= 2) {
                                item.setAmount(Integer.parseInt(infoArray[1]));
                            }
                            GivePlayer(uuid, item);
                        }else{
                            Variables.AddError("Tried to give invalid item: " + infoArray[0]);
                        }


                    }
                    case "CASH: " -> {
                        if (BlepFishing.hasEconomy()) {
                            OfflinePlayer p = Bukkit.getOfflinePlayer(uuid);
                            int amount = Integer.parseInt(itemStr);
                            BlepFishing.getEconomy().depositPlayer(p, amount);
                            if (p.isOnline())
                                p.getPlayer().sendMessage(Formatting.getFormattedMesage("Economy.received")
                                        .replace("{value}", BlepFishing.getEconomy().format(amount)));
                        } else {
                            Bukkit.getLogger().warning("A player would have received currency but no economy was found. Please update the Blep Fishing config files.");
                        }
                    }
                    case "BYTE: " -> {
                        try {
                            GivePlayer(uuid, itemStackFromBase64(itemStr));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    case "TEXT: " -> {
                        OfflinePlayer p = Bukkit.getOfflinePlayer(uuid);
                        if (p.isOnline()) p.getPlayer().sendMessage(Formatting.formatColor(itemStr));
                    }
                }
            }catch(Exception e){
                Variables.AddError("Error generating item. Please check the server console for more details.");
                e.printStackTrace();
            }

        }
    }
    /**
     * Gives the item directly to the player or stores to file if no space in inventory
     * @param item - The item to be given
     * @param uuid - UUID of player
     */
    public static void GivePlayer(UUID uuid, ItemStack item){
        OfflinePlayer p = Bukkit.getOfflinePlayer(uuid);

        boolean isOnline = false;

        for(var s : Bukkit.getOnlinePlayers()){
            if(s.getUniqueId().equals(uuid)){
                p = s;
                isOnline = true;
                break;
            }
        }

        if(isOnline){
            Player player = p.getPlayer();
            assert player != null;
            if(Utilities.getFreeSlots(player.getInventory()) > 0){
                player.getInventory().addItem(item);
                player.sendMessage(Formatting.getFormattedMesage("System.itemReceive")
                                .replace("{amount}", String.valueOf(item.getAmount()))
                                .replace("{item}", item.getType().toString()));
            }else Rewards.AddReward(uuid, item);
        }else{
            Rewards.AddReward(uuid, item);
        }
    }
}
