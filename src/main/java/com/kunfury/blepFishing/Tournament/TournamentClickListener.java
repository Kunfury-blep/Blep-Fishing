package com.kunfury.blepFishing.Tournament;

import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Miscellaneous.Variables;
import com.kunfury.blepFishing.Objects.FishObject;
import com.kunfury.blepFishing.Objects.TournamentObject;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import static com.kunfury.blepFishing.Miscellaneous.Variables.Messages;
import static com.kunfury.blepFishing.Miscellaneous.Variables.Prefix;

public class TournamentClickListener implements Listener {

    @EventHandler
    public void inventoryClick(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        CommandSender sender = e.getWhoClicked();

        if(item != null && e.getClickedInventory() == Tournament.tourneyInv && sender.hasPermission("bf.admin") ){
            e.setCancelled(true);
            TournamentObject tourney = Tournament.tourneys.get(e.getSlot());
            if(item.getType() != Material.EMERALD && e.getClick() == ClickType.RIGHT ) {

                item.setType(Material.EMERALD);
                ItemMeta meta = item.getItemMeta();
                assert meta != null;
                meta.setDisplayName("Confirm Delete");

                List<String> lore = new ArrayList<String>() {{
                    add("Shift Right-Click to confirm you wish to delete the tournament");
                    add("Left Click to cancel");
                    add("Type: " + tourney.FishName);
                }};
                meta.setLore(lore);
                item.setItemMeta(meta);
            }else if(item.getType() == Material.EMERALD){
                if(e.getClick() == ClickType.SHIFT_RIGHT) DeleteConfirm(tourney, sender);
                if(e.getClick() == ClickType.LEFT) DeleteCancel(sender);
            }else if(e.getClick() == ClickType.LEFT) ShowTourneyInfo(tourney, sender);

        }
    }

    private void DeleteConfirm(TournamentObject tourney, CommandSender sender){
        Variables.Tournaments.remove(tourney);
        new SaveTournaments();

        if(!tourney.HasFinished) sender.sendMessage(Variables.Prefix + "Tournament successfully deleted, however the server will require a restart to stop the scheduled timer for that tournament.");
        else sender.sendMessage(Variables.Prefix + "Tournament successfully deleted.");


        new Tournament().ShowTourney(sender);
    }

    private void DeleteCancel(CommandSender sender){
        new Tournament().ShowTourney(sender);
    }

    private void ShowTourneyInfo(TournamentObject tourney, CommandSender sender){
        List<FishObject> fishList = new ArrayList<>();
        if(tourney.HasFinished){
            fishList = tourney.GetWinners();
        }else{
            fishList = tourney.GetTournamentFish();
        }

        if(fishList.size() > 0) { //If any fish have been caught
            //Initializes the size of the chatbox
            int pLength = 15;

            String fPlayer = Formatting.FixFontSize("Player Name", pLength);
            String fullString = ChatColor.translateAlternateColorCodes('&', "&b" + fPlayer + " Fish");

            Bukkit.broadcastMessage(ChatColor.BOLD + (Messages.getString("tournamentLeaderboard")));
            Bukkit.broadcastMessage(fullString);

            if(fishList.size() > 3){
                fishList.subList(3, fishList.size()).clear();
            }

            int i = 1;
            for (FishObject fish : fishList) {
                fPlayer = Formatting.FixFontSize(fish.PlayerName, pLength);
                String lbString = ChatColor.translateAlternateColorCodes('&' ,
                        Formatting.FixFontSize(i + ".", 4)
                                + fPlayer + fish.Rarity + " " + fish.Name);
                TextComponent mainComponent = new TextComponent (lbString);
                mainComponent.setHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT, fish.GetHoverText()));


                sender.spigot().sendMessage(mainComponent);
                i++;
            }
        }else {
            String tempName = tourney.FishName.toLowerCase();
            if(tempName.equalsIgnoreCase("ALL"))
                tempName = "fish";
            sender.sendMessage(Prefix + "No " + tempName + " have been cuahgt");
        }
    }
}
