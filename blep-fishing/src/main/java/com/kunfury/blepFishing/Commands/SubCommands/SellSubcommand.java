package com.kunfury.blepFishing.Commands.SubCommands;

import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Miscellaneous.FishEconomy;
import com.kunfury.blepFishing.Commands.SubCommand;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.BlepFishing;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SellSubcommand extends SubCommand {
    @Override
    public String getName() {
        return "Sell";
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
        if(BlepFishing.getEconomy() != null){
            FishEconomy.SellFish((Player)sender, false, 1);
        }else sender.sendMessage(Formatting.getFormattedMesage("Economy.noEconomy"));

    }

    @Override
    public List<String> getArguments(@NotNull CommandSender sender, String[] args) {
        return null;
    }

    @Override
    public String getPermissions() {
        return "bf.sell";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }
}
