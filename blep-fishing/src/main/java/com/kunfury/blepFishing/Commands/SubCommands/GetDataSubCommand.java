package com.kunfury.blepFishing.Commands.SubCommands;

import com.kunfury.blepFishing.Commands.SubCommand;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Miscellaneous.ItemHandler;
import com.kunfury.blepFishing.Config.Variables;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

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
        if(!(sender instanceof Player)){
            sender.sendMessage(Variables.Prefix + Formatting.getMessage("System.playerOnly"));
            return;
        }
        Player p = (Player) sender;

        ItemStack item = p.getInventory().getItemInMainHand();

        String data = ItemHandler.itemStackToBase64(item);

        String dataStr = "BYTE: " + data;
        TextComponent message = new TextComponent(Variables.Prefix +  Formatting.getMessage("Admin.getDataClick"));
        message.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, dataStr));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Use Ctrl-V to paste!").create()));
        p.spigot().sendMessage(message);


        //p.sendMessage(Variables.Prefix + "Item Data has been copied to your clipboard.");
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
