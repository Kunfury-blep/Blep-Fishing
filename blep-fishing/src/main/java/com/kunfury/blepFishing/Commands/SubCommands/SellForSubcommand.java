package com.kunfury.blepFishing.Commands.SubCommands;

import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Crafting.Equipment.FishBag.BagInfo;
import com.kunfury.blepFishing.Miscellaneous.FishEconomy;
import com.kunfury.blepFishing.Commands.SubCommand;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SellForSubcommand extends SubCommand {
    @Override
    public String getName() {
        return "SellFor";
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
        if(args.length > 1){
            Player p = Bukkit.getPlayer(args[1]);
            if(p != null && p.isOnline()){

                ItemStack item = p.getInventory().getItemInMainHand();

                if(BagInfo.IsBag(item)){
                    FishEconomy.SellBag(p, item, 1);
                    return;
                }

                if(p.isSneaking()){ //Note: Citizens does not support shift-right click so it will never trigger this
                    FishEconomy.SellFish(p, true, 1);
                }
                else{
                    FishEconomy.SellFish(p, 1);
                }
            }
            else
                sender.sendMessage(Formatting.getFormattedMesage("Economy.noPlayer"));
        }else
            sender.sendMessage(Formatting.getFormattedMesage("Economy.noPlayer"));

    }

    @Override
    public List<String> getArguments(@NotNull CommandSender sender, String[] args) {
        List<String> optionList = new ArrayList<>();
        if (args.length == 2) {
            for(Player p : Bukkit.getServer().getOnlinePlayers()){
                optionList.add(p.getName());
            }
            optionList.add("<Player>");
        }
        return optionList;
    }

    @Override
    public String getPermissions() {
        return "bf.sellfor";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }
}
