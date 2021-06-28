package com.kunfury.blepFishing.Tournament;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mysql.jdbc.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.kunfury.blepFishing.Setup;

import com.kunfury.blepFishing.Miscellaneous.Variables;
import com.kunfury.blepFishing.Objects.BaseFishObject;
import com.kunfury.blepFishing.Objects.TournamentObject;
import net.md_5.bungee.api.ChatColor;

public class Tournament implements Listener {
	private static HashMap<Player, Inventory> viewMap = new HashMap<Player, Inventory>();


	/**
	 * Shows all active tournaments to the command sender
	 * @param sender the command sender
	 */
	public void ShowTourney(CommandSender sender) {
		
		final Inventory inv = Bukkit.createInventory(null, 54, Variables.Messages.getString("tourneyInvTitle"));
		Player player = (Player)sender;
		
		List<TournamentObject> tourneys = new SortTournaments().Sort();
		
		for(TournamentObject tourney : tourneys) {
			ItemStack item = null;
			ItemMeta meta = null;
			if(tourney.EndDate.isAfter(LocalDateTime.now())) { //Checks if the tournament has ended yet or not
				item = new ItemStack(Material.FISHING_ROD, 1);
				
				meta = item.getItemMeta();
				
				if(!tourney.FishName.equalsIgnoreCase("ALL")) {
					item.setType(Material.SALMON);
					meta.setCustomModelData(tourney.Fish.ModelData);
				}
				meta.setDisplayName(tourney.FishName);
				
				List<String> lore = new ArrayList<String>() {{
					add("Time Left: " + tourney.GetRemainingTime());
					add("End Date: " + tourney.GetFormattedEndDate());
				}};
				meta.setLore(lore);
			}else { //If the tournament has already expired
				item = new ItemStack(Material.COOKED_COD, 1);
				meta = item.getItemMeta();
				meta.setDisplayName(tourney.FishName + ChatColor.DARK_RED + " - Expired");
				List<String> lore = new ArrayList<String>() {{
					add(Variables.Messages.getString("endDate") + tourney.GetFormattedEndDate());
					add(Variables.Messages.getString("winner") + tourney.Winner);
				}};
				meta.setLore(lore);
			}

			item.setItemMeta(meta);
			inv.addItem(item);
		}
		
		viewMap.put(player, inv);
		player.openInventory(inv);
	}

	/**
	 * Creates a new tournament
	 * @param sender the command sender
	 * @param fishName The fish name to create the tournament for
	 * @param duration the duration of the tournament
	 * @param cashPrize the cash reward of the tournament
	 * @param itemName ?
	 * @param itemCount ?
	 */
	public void CreateTourny(CommandSender sender, String fishName, Number duration, int cashPrize, String itemName, int itemCount) {
		
		boolean fishFound = false;
		if(fishName.equalsIgnoreCase("ALL"))
			fishFound = true;
		else {
			for(BaseFishObject fish : Variables.BaseFishList) {
				if(fishName.equalsIgnoreCase(fish.Name)) {
					fishName = fish.Name;
					fishFound = true;
					break;
				}
			}
		}
		
		if(!fishFound) {
			sender.sendMessage(Variables.Prefix + Messages.getString("fishNotFound"));
			return;
		}
			
		
		List<ItemStack> items = new ArrayList<>();
		try{
			items.add(new ItemStack(Material.getMaterial(itemName.toUpperCase()), itemCount));
		}
		catch(Exception e){
			sender.sendMessage(Variables.Prefix + Messages.getString("invalidItem"));
			return;
		}
			
		TournamentObject tourney = new TournamentObject(duration, fishName, items, cashPrize);
		Variables.AddTournament(tourney);
    }

	public void StartTimer(long duration, TournamentObject tourney) {
		Bukkit.getServer().getScheduler().runTaskLater(Setup.getPlugin(), new Runnable(){
            @Override
            public void run() {
                new TournamentFinish(tourney);
            }
            
        }, (duration / 1000 * 20));
	}	
	
	public void DelayedWinnings(TournamentObject tourney) {
		Bukkit.getServer().getScheduler().runTaskLater(Setup.getPlugin(), new Runnable() {
        	@Override
        	  public void run() {
        		new TournamentFinish(tourney);
        	    }
        }, 600);
	}


	@EventHandler()
	public void clickEvent(InventoryClickEvent e) { //Handles Interaction with the panel
		Player p = (Player)e.getWhoClicked();
		Inventory inv = viewMap.get(p);
		if(e.getInventory() == inv){
			e.setCancelled(true);
		}
	}

	public void CheckActiveTournaments(){
		boolean running = false;
		for (TournamentObject var : Variables.Tournaments)
		{
			if (!var.HasFinished) {
				running = true;
				break;
			}
		}
		Variables.TournamentRunning = running;
	}

}
