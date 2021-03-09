package com.kunfury.blepFishing.Commands;

import Objects.BaseFishObject;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandSender;

import static Miscellaneous.Variables.*;



public class ListFish {
    /**
     * Lists all the fishes
     * @param sender
     * @return
     */
    public ListFish(CommandSender sender) {
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
}
