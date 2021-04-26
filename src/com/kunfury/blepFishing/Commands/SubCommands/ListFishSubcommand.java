package com.kunfury.blepFishing.Commands.SubCommands;

import Objects.BaseFishObject;
import com.kunfury.blepFishing.Commands.SubCommand;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static Miscellaneous.Variables.BaseFishList;
import static Miscellaneous.Variables.Messages;

public class ListFishSubcommand extends SubCommand {
    @Override
    public String getName() {
        return "fish";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getSyntax() {
        return null;
    }

    @Override
    public void perform(@NotNull CommandSender sender, String[] args) {
        String str = String.format(Messages.getString("listFish"), BaseFishList.size());
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', str));
        TextComponent mainComponent = new TextComponent();
        for (BaseFishObject fish : BaseFishList) {
            TextComponent subComponent = new TextComponent (ChatColor.AQUA +  " " + fish.Name + ChatColor.WHITE + " -");
            subComponent.setHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT,
                    new Text(ChatColor.translateAlternateColorCodes('&' ,
                            fish.Name +
                                    "\n" + Messages.getString("lore") + fish.Lore +
                                    "\n" + Messages.getString("area") + fish.Area +
                                    "\n" + Messages.getString("minSize") + fish.MinSize +
                                    "\n" + Messages.getString("maxSize") + fish.MaxSize +
                                    "\n" + Messages.getString("baseCost") + fish.BaseCost
                    ))));
            mainComponent.addExtra(subComponent);
        }
        sender.spigot().sendMessage(mainComponent);
    }

    @Override
    public List<String> getArguments(@NotNull CommandSender sender, String[] args) {
        return null;
    }

    @Override
    public String getPermissions() {
        return "bf.listFish";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList( "listFish");
    }
}
