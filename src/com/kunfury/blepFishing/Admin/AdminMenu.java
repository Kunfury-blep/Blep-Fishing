package com.kunfury.blepFishing.Admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.kunfury.blepFishing.Setup;

import Miscellaneous.Variables;
import Objects.BaseFishObject;
import Objects.RarityObject;
import Objects.TourneyAdminObject;

public class AdminMenu implements Listener {
	
	public static ItemStack Background, FishEdit, RarityEdit, BiomeEdit, BackButton, 
		TourneyGUI, TourneyCreate, TourneyHelp, TourneyCash, TourneyTime, TourneyFish; 
	
	//static Inventory inv;
	static enum Window {
		BASE,
		FISH,
		RARITIES,
		BIOMES,
		FISH_EDIT,
		TOURNEY
	};
	
	public static HashMap<Player, Inventory> invMap = new HashMap<Player, Inventory>();
	public static HashMap<Player, Window> winMap = new HashMap<Player, Window>();
	public static HashMap<Player, TourneyAdminObject> tourneyMap = new HashMap<>();
	
	public void ShowInventory(CommandSender sender) {		
		Player p = (Player)sender;
		Inventory inv = Bukkit.createInventory(null, 27, " ---Blep Fishing Admin Panel---");		
		invMap.put(p, inv);
		winMap.put(p, Window.BASE);
		
		
		for(int i = 0; i < 27; i++) {
			inv.setItem(i, Background);
		}
		
		inv.setItem(12, RarityEdit);
		inv.setItem(13, FishEdit);
		inv.setItem(14, BiomeEdit);		
		inv.setItem(4, TourneyGUI);		
		p.openInventory(inv);
	}
	
	///
	// Handles when the inventory is clicked
	// Decides which events to trigger based on the currently active INV enum
	//
	@EventHandler()
    public void clickEvent(InventoryClickEvent e) { //Handles Interaction with the panel
		Player p = (Player)e.getWhoClicked();
		Window activeWindow = winMap.get(p);
		Inventory inv = invMap.get(p);
		if(e.getInventory().equals(inv) && e.getClickedInventory() != null && e.getCurrentItem() != null) {		
			if(!activeWindow.equals(Window.TOURNEY))
				e.setCancelled(true);
			ItemStack item =e.getCurrentItem();

			if(item.equals(BackButton)) {
				if(activeWindow.equals(Window.BASE) || activeWindow.equals(Window.FISH) || activeWindow.equals(Window.BIOMES) || activeWindow.equals(Window.RARITIES))
					ShowInventory(e.getWhoClicked());
				if(activeWindow.equals(Window.FISH_EDIT))
					FishEdit(p);
			}
				
			
			if(activeWindow.equals(Window.BASE)) {
				if(item.equals(FishEdit))
					FishEdit(p);
				if(item.equals(RarityEdit))
					RarityEdit(p);
				if(item.equals(BiomeEdit))
					BiomeEdit(p);	
				if(item.equals(TourneyGUI))
					CreateTourneyGUI((Player)e.getWhoClicked());
				return;
			}
			if(activeWindow.equals(Window.FISH)) {

			}
			if(activeWindow.equals(Window.BASE)) {
				
			}
			
			//Runs if the currently showing window is the tourney
			if(activeWindow.equals(Window.TOURNEY)) {
				new TourneyAdmin().TourneyClicked(e, item);
			}
		}
	}
	
	public String getPromptText(ConversationContext context) {
        return "Please enter the cash reward amount.";
    }
	
	@SuppressWarnings("serial")
	private void FishEdit(Player p) {
		Inventory inv = invMap.get(p);
		winMap.put(p, Window.FISH);
		inv.clear();
		
		for(int i = 0; i < 27; i++) {
			inv.setItem(i, Background);
		}
		
		ItemStack fishIcon = new ItemStack(Material.SALMON, 1);
		ItemMeta meta = null;
		
		int i = 0;
		//Needs to be changed to the new BaseObject/FishObject system
		for(final BaseFishObject fish : Variables.BaseFishList) {
			meta = fishIcon.getItemMeta();
			meta.setDisplayName(fish.Name);
			meta.setCustomModelData(fish.ModelData);
			List<String> lore = new ArrayList<String>() {{
				add("Min Size: " + fish.MinSize);
				add("Max Size: " + fish.MaxSize);
				add("Base Price: " + fish.BaseCost);
			}};
			meta.setLore(lore);
			fishIcon.setItemMeta(meta);
			inv.setItem(i, fishIcon);
			i++;
		}
		inv.setItem(inv.getSize() - 1, BackButton);
		
		
	}
	
	@SuppressWarnings("serial")
	private void RarityEdit(Player p) {
		Inventory inv = invMap.get(p);
		winMap.put(p, Window.RARITIES);
		inv.clear();
		
		for(int i = 0; i < 27; i++) {
			inv.setItem(i, Background);
		}
		
		ItemStack rarityIcon = new ItemStack(Material.EMERALD, 1);
		ItemMeta meta = null;
		
		int i = 0;
		for(final RarityObject rarity : Variables.RarityList) {
			meta = rarityIcon.getItemMeta();
			meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', rarity.Name));
			List<String> lore = new ArrayList<String>() {{
				add("Weight: " + rarity.Weight);
				add("Color Code: " + rarity.Prefix);
				add("Price Mod: " + rarity.PriceMod);
			}};
			meta.setLore(lore);
			rarityIcon.setItemMeta(meta);
			inv.setItem(i, rarityIcon);
			i++;
		}
		
		inv.setItem(inv.getSize() - 1, BackButton);
	}
	
	private void BiomeEdit(Player p) {
		//openInv = Inv.BIOMES;
	}
	
	
	@SuppressWarnings("serial")
	public void CreateStacks() {
		String WIP = ChatColor.RED + " WIP " + ChatColor.WHITE;
		
		Background = new ItemStack(Material.PINK_STAINED_GLASS_PANE, 1);
		ItemMeta meta = Background.getItemMeta();
		meta.setDisplayName(" ");
		Background.setItemMeta(meta);
		
		FishEdit = new ItemStack(Material.SALMON, 1);
		meta = FishEdit.getItemMeta();
		meta.setDisplayName("Edit Fish" + WIP);
		FishEdit.setItemMeta(meta);
		
		RarityEdit = new ItemStack(Material.EMERALD, 1);
		meta = RarityEdit.getItemMeta();
		meta.setDisplayName("Edit Rarity" + WIP);
		RarityEdit.setItemMeta(meta);
		
		BiomeEdit = new ItemStack(Material.COMPASS, 1);
		meta = BiomeEdit.getItemMeta();
		meta.setDisplayName("Edit Biome" + WIP);
		BiomeEdit.setItemMeta(meta);
		
		BackButton = new ItemStack(Material.BARRIER, 1);
		meta = BackButton.getItemMeta();
		meta.setDisplayName("Go Back");
		BackButton.setItemMeta(meta);
		
		TourneyGUI = new ItemStack(Material.FISHING_ROD, 1);
		meta = TourneyGUI.getItemMeta();
		meta.setDisplayName("Tournament Creation");
		TourneyGUI.setItemMeta(meta);
		
		TourneyCreate = new ItemStack(Material.FISHING_ROD, 1);
		meta = TourneyCreate.getItemMeta();
		meta.setDisplayName("Create Tournament");
		TourneyCreate.setItemMeta(meta);
		
		
		TourneyHelp = new ItemStack(Material.BOOK, 1);
		meta = TourneyHelp.getItemMeta();
		meta.setDisplayName("Tournament Help");
		List<String> lore = new ArrayList<String>(){{
			add("Add items to this menu to add them to the reward list.");
			add("Click on the following icons to set the tournaments values");
		}};
		meta.setLore(lore);
		TourneyHelp.setItemMeta(meta);
		
		TourneyCash = new ItemStack(Material.EMERALD, 1);
		meta = TourneyCash.getItemMeta();
		meta.setDisplayName("Cash Prize");
		TourneyCash.setItemMeta(meta);
		
		TourneyTime = new ItemStack(Material.CLOCK, 1);
		meta = TourneyTime.getItemMeta();
		meta.setDisplayName("Tournament Time");
		TourneyTime.setItemMeta(meta);
		
		TourneyFish = new ItemStack(Material.SALMON, 1);
		meta = TourneyFish.getItemMeta();
		meta.setDisplayName("Choose Fish");
		TourneyFish.setItemMeta(meta);
	}
	
	
	
	
	/**
	 * @param inv
	 * @param p
	 */
	@SuppressWarnings("serial")
	public void CreateTourneyGUI(Player p) {		
		Inventory inv = invMap.get(p);
		winMap.put(p, Window.TOURNEY);
		inv.clear();
		inv.setItem(18, TourneyHelp);
		inv.setItem(19, TourneyFish);
		inv.setItem(20, TourneyTime);
		inv.setItem(21, Background);
		if(Setup.hasEcon)
			inv.setItem(21, TourneyCash);
		inv.setItem(22, Background);
		inv.setItem(23, Background);
		inv.setItem(24, Background);
		inv.setItem(25, Background);
		
		//Below fills out the item with the currently set variables
		ItemStack item = TourneyCreate;
		
		if(tourneyMap.get(p) == null) {
			TourneyAdminObject tObj = new TourneyAdminObject(0, "ALL", null, 0);
			tourneyMap.put(p, tObj);
		}
		
		
		//Fills the inventory with items that were previously there
		TourneyAdminObject tObj = tourneyMap.get(p);
		if(tObj.Rewards != null) {
			for(int i = 0; i < tObj.Rewards.size(); i++) {
				inv.setItem(i, tObj.Rewards.get(i));
			}
		}
		
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<String>() {{
			add("Fish Type: " + tObj.FishName);
			add("Duration: " + tObj.Duration + " Hour(s)");
			if(Setup.hasEcon)
				add("Cash Prize: " + Variables.CSym + tObj.Cash);
			add("Click to finish creation.");
		}};
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		inv.setItem(26, TourneyCreate);
		p.openInventory(inv);
	}
	
	

}
