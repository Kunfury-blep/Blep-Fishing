package com.kunfury.blepFishing.Admin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.kunfury.blepFishing.Conversations.GetCashPrompt;
import com.kunfury.blepFishing.Conversations.GetFishTypePrompt;
import com.kunfury.blepFishing.Conversations.GetTourneyTimePrompt;
import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Objects.TournamentObjectOld;
import com.kunfury.blepFishing.Objects.TourneyAdminObject;

public class TourneyAdmin {

	/**
	 * Handles the click events in the TourneyGUI
	 * @param e
	 * @param item
	 */
	public void TourneyClicked(InventoryClickEvent e, ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		if(e.getRawSlot() > 17 && e.getRawSlot() < 27) { //If the clicked button is one of the bottom row of GUI
			e.setCancelled(true);
			Player p = (Player)e.getWhoClicked();
			//Gets items in top window
			List<ItemStack> items = new ArrayList<>();
			for (int i = 0; i < 17; i++) {
				ItemStack bItem = e.getInventory().getItem(i);
				if(bItem != null)
					items.add(bItem);
			}
			UpdateTourney(p, null, null, items, null);
			
			switch(meta.getDisplayName()){ //Checks the name of what in the settings bar was clicked
				case "Cash Prize": 
					p.closeInventory();
					Variables.ConFactory.withFirstPrompt(new GetCashPrompt()
							.new InitialPrompt()).buildConversation(p).begin();
					return;
				case "com.kunfury.blepFishing.Tournament Time":
					p.closeInventory();
					Variables.ConFactory.withFirstPrompt(new GetTourneyTimePrompt()
							.new InitialPrompt()).buildConversation(p).begin();
					return;
				case "Choose Fish": 
					p.closeInventory();
					Variables.ConFactory.withFirstPrompt(new GetFishTypePrompt()
							.new InitialPrompt()).buildConversation(p).begin();
					return;
				case "Create com.kunfury.blepFishing.Tournament":
					TourneyAdminObject aObj = AdminMenu.tourneyMap.get(p);
					TournamentObjectOld tourney = new TournamentObjectOld
							(aObj.Duration, aObj.FishName, aObj.Rewards, aObj.Cash);
					Variables.AddTournament(tourney);
					p.closeInventory();
					//p.sendMessage(Variables.Prefix + "com.kunfury.blepFishing.Tournament Created!");
					AdminMenu.tourneyMap.remove(p);
			}

		}
	}

	/**
	 * Updates the TourneyGUI
	 * @param p the admin that modifies the menu
	 * @param duration the duration of the tourney
	 * @param fishName the fish name the tournament is currently running on
	 * @param rewards the rewards of the tourney
	 * @param cash the cash rewards of the tourney
	 *
	 */
	public void UpdateTourney(Player p, Number duration, String fishName, List<ItemStack> rewards, Integer cash) {
		TourneyAdminObject tObj = AdminMenu.tourneyMap.get(p);
		
		if(duration != null)
			tObj.Duration = duration;
		if(fishName != null)
			tObj.FishName = fishName;
		if(rewards != null)
			tObj.Rewards = rewards;
		if(cash != null)
			tObj.Cash = cash;
		
		AdminMenu.tourneyMap.put(p, tObj);
		
	}
	
}
