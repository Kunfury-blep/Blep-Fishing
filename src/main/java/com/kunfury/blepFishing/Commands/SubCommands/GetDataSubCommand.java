package com.kunfury.blepFishing.Commands.SubCommands;

import com.kunfury.blepFishing.Commands.SubCommand;
import com.kunfury.blepFishing.Miscellaneous.ItemSerializer;
import com.kunfury.blepFishing.Miscellaneous.Variables;
import com.kunfury.blepFishing.Tournament.Tournament;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class GetDataSubCommand extends SubCommand {
    @Override
    public String getName() {return "GetData";}

    @Override
    public String getDescription() {
        return "Returns the raw data of the item currently in your hand. Used for setting up custom treasure rewards.";
    }

    @Override
    public String getSyntax() {
        return null;
    }

    @Override
    public void perform(@NotNull CommandSender sender, String[] args) {
        Player p = (Player) sender;

        ItemStack item = p.getInventory().getItemInMainHand();

        String data = ItemSerializer.itemStackToBase64(item);

        StringSelection stringSelection = new StringSelection("BYTE: " + data);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Clipboard clipboard = toolkit.getSystemClipboard();

        clipboard.setContents(stringSelection, null);
        p.sendMessage(Variables.Prefix + "Item Data has been copied to your clipboard.");
    }

    @Override
    public List<String> getArguments(@NotNull CommandSender sender, String[] args) {
        return null;
    }

    @Override
    public String getPermissions() {
        return "bf.admin";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }
}
