package com.kunfury.blepFishing.Commands.SubCommands;

import com.kunfury.blepFishing.BlepFishing;
import com.kunfury.blepFishing.Commands.CommandManager;
import com.kunfury.blepFishing.Commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class IgnoreSubCommand extends SubCommand {
    @Override
    public String getName() {
        return "Ignore";
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
        Player p = (Player)sender;

        HashMap<UUID, PermissionAttachment> perms = new HashMap<>();

        PermissionAttachment attachment = sender.addAttachment(BlepFishing.getPlugin());
        perms.put(p.getUniqueId(), attachment);

        if(sender.hasPermission("bf.ignore")){
            PermissionAttachment pperms = perms.get(p.getUniqueId());
            pperms.unsetPermission("bf.ignore");
            sender.sendMessage("You will now catch Blep Fish");
        }
        else{
            PermissionAttachment pperms = perms.get(p.getUniqueId());
            pperms.setPermission("bf.ignore", true);
            sender.sendMessage("You will now only catch regular fish");
        }

    }

    @Override
    public List<String> getArguments(@NotNull CommandSender sender, String[] args) {
        return null;
    }

    @Override
    public String getPermissions() { return null;  }

    @Override
    public List<String> getAliases() {
        return null;
    }
}
