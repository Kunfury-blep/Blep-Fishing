package com.kunfury.blepFishing.Tournament.Old;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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

import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Objects.BaseFishObject;
import com.kunfury.blepFishing.Objects.TournamentObjectOld;
import net.md_5.bungee.api.ChatColor;

public class Tournament implements Listener {
	private static final HashMap<Player, Inventory> viewMap = new HashMap<>();

	 public static Inventory tourneyInv = null;
	 public static List<TournamentObjectOld> tourneys;

	 public void Initialize(){
		 Bukkit.getServer().getScheduler().runTaskLater(Setup.getPlugin(), () ->
				 tourneyInv = Bukkit.createInventory(null, 54, Variables.getMessage("tourneyInvTitle")), 30);
	 }

	/**
	 * Shows all active tournaments to the command sender
	 * @param sender the command sender
	 */
	public void ShowTourney(CommandSender sender) {
		tourneyInv.clear();
		Player player = (Player)sender;
		
		tourneys = new SortTournaments().Sort();
		
		for(TournamentObjectOld tourney : tourneys) {
			ItemStack item;
			ItemMeta meta;
			if(tourney.EndDate.isAfter(LocalDateTime.now())) { //Checks if the tournament has ended yet or not
				item = new ItemStack(Material.FISHING_ROD, 1);
				
				meta = item.getItemMeta();
				assert meta != null;
				if(!tourney.FishName.equalsIgnoreCase("ALL")) {
					item.setType(Material.SALMON);
					meta.setCustomModelData(tourney.Fish.ModelData);
				}
				meta.setDisplayName(tourney.FishName);
				
				List<String> lore = new ArrayList<>() {{
					add("Time Left: " + tourney.GetRemainingTime());
					add("End Date: " + tourney.GetFormattedEndDate());
				}};
				meta.setLore(lore);
			}else { //If the tournament has already expired
				item = new ItemStack(Material.COOKED_COD, 1);
				meta = item.getItemMeta();
				Objects.requireNonNull(meta).setDisplayName(tourney.FishName + ChatColor.DARK_RED + " - Expired");
				List<String> lore = new ArrayList<>() {{
					add(Variables.getMessage("endDate") + " " + tourney.GetFormattedEndDate());
					add(Variables.getMessage("winner") + " " + tourney.Winner);
				}};
				meta.setLore(lore);
			}

			item.setItemMeta(meta);
			tourneyInv.addItem(item);
		}
		
		viewMap.put(player, tourneyInv);
		player.openInventory(tourneyInv);
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
			sender.sendMessage(Variables.Prefix + Variables.getMessage("fishNotFound"));
			return;
		}
			
		
		List<ItemStack> items = new ArrayList<>();
		try{
			items.add(new ItemStack(Objects.requireNonNull(Material.getMaterial(itemName.toUpperCase())), itemCount));
		}
		catch(Exception e){
			sender.sendMessage(Variables.Prefix + Variables.getMessage("invalidItem"));
			return;
		}
			
		TournamentObjectOld tourney = new TournamentObjectOld(duration, fishName, items, cashPrize);
		Variables.AddTournament(tourney);
    }

	public void StartTimer(long duration, TournamentObjectOld tourney) {
		Bukkit.getServer().getScheduler().runTaskLater(Setup.getPlugin(), () ->
				new TournamentFinish(tourney), (duration / 1000 * 20));
	}	
	
	public void DelayedWinnings(TournamentObjectOld tourney) {
		Bukkit.getServer().getScheduler().runTaskLater(Setup.getPlugin(), () ->
				new TournamentFinish(tourney), 600);
	}


	@EventHandler()
	public void TourneyClickListener(InventoryClickEvent e) { //Handles Interaction with the panel
		Player p = (Player)e.getWhoClicked();
		Inventory inv = viewMap.get(p);
		if(e.getInventory() == inv){
			e.setCancelled(true);
		}
	}

	public void CheckActiveTournaments(){
		boolean running = false;
		for (TournamentObjectOld var : Variables.Tournaments)
		{
			if (!var.HasFinished) {
				running = true;
				break;
			}
		}
		Variables.TournamentRunning = running;
	}

}
