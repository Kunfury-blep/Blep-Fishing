package com.kunfury.blepFishing.Commands.SubCommands;

import com.kunfury.blepFishing.Miscellaneous.Variables;
import com.kunfury.blepFishing.Objects.BaseFishObject;
import com.kunfury.blepFishing.Commands.SubCommand;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static com.kunfury.blepFishing.Miscellaneous.Variables.BaseFishList;

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
        String str = String.format(Variables.getMessage("listFish"), BaseFishList.size());
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', str));
        TextComponent mainComponent = new TextComponent();
        for (BaseFishObject fish : BaseFishList) {
            TextComponent subComponent = new TextComponent (ChatColor.AQUA +  " " + fish.Name + ChatColor.WHITE + " -");
            subComponent.setHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT,
                    new Text(ChatColor.translateAlternateColorCodes('&' ,
                            fish.Name +
                                    "\n" + Variables.getMessage("lore") + fish.Lore +
                                    "\n" + Variables.getMessage("area") + fish.Areas.get(0) +
                                    "\n" + Variables.getMessage("minSize") + fish.MinSize +
                                    "\n" + Variables.getMessage("maxSize") + fish.MaxSize +
                                    "\n" + Variables.getMessage("baseCost") + fish.BaseCost
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
