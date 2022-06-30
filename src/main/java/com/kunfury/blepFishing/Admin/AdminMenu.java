package com.kunfury.blepFishing.Admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.kunfury.blepFishing.Commands.SubCommands.ConfigSubcommand;
import com.kunfury.blepFishing.Commands.SubCommands.ReloadSubcommand;
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

import com.kunfury.blepFishing.Miscellaneous.Variables;
import com.kunfury.blepFishing.Objects.BaseFishObject;
import com.kunfury.blepFishing.Objects.RarityObject;
import com.kunfury.blepFishing.Objects.TourneyAdminObject;

public class AdminMenu implements Listener {
	
	public static ItemStack Background, FishView, RarityView, ConfigEdit, BackButton, ReloadButton,
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
	
	public static HashMap<Player, Inventory> invMap = new HashMap<>();
	public static HashMap<Player, Window> winMap = new HashMap<>();
	public static HashMap<Player, TourneyAdminObject> tourneyMap = new HashMap<>();

	/**
	 *
	 * @param sender the sender of the command
	 * Shows the inventory to the given sender.
	 */
	public void ShowInventory(CommandSender sender) {		
		Player p = (Player)sender;
		Inventory inv = Bukkit.createInventory(null, 27, Variables.getMessage("adminInvTitle"));
		invMap.put(p, inv);
		winMap.put(p, Window.BASE);
		
		
		for(int i = 0; i < 27; i++) {
			inv.setItem(i, Background);
		}
		
		inv.setItem(12, RarityView);
		inv.setItem(13, ConfigEdit);
		inv.setItem(14, FishView);
		inv.setItem(22, ReloadButton);
		inv.setItem(4, TourneyGUI);		
		p.openInventory(inv);
	}

	/**
	 * Decides which events to trigger based on the currently active INV enum
	 * @param e event variable
	 */
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
				if(item.equals(FishView))
					FishEdit(p);
				if(item.equals(RarityView))
					RarityEdit(p);
				if(item.equals(ConfigEdit))
					ConfigEdit(p);
				if(item.equals(TourneyGUI))
					CreateTourneyGUI((Player)e.getWhoClicked());
				if(item.equals(ReloadButton))
					new ReloadSubcommand().perform(p, null);
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

	/**
	 * Passes a random fish with given Player argument.
	 * @param p the player to give the fish to
	 */
	@SuppressWarnings("serial")
	private void FishEdit(Player p) {
		Inventory inv = invMap.get(p);
		winMap.put(p, Window.FISH);
		inv.clear();
		
		for(int i = 0; i < inv.getSize() - 1; i++) {
			inv.setItem(i, Background);
		}
		
		ItemStack fishIcon = new ItemStack(Material.SALMON, 1);
		ItemMeta meta = null;
		
		int i = 0;
		//Needs to be changed to the new BaseObject/FishObject system
		for(final BaseFishObject fish : Variables.BaseFishList) {
			if(inv.getSize() <= i) break;
			else {
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

		}
		inv.setItem(inv.getSize() - 1, BackButton);
		
		
	}
	/**
	 * Gives the fish a rarity.
	 */
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
	
	private void ConfigEdit(Player p) {
		p.closeInventory();
		new ConfigSubcommand().perform(p, null);
	}

	/**
	 * Creates needed itemstacks
	 */
	@SuppressWarnings("serial")
	public void CreateStacks() {
		
		Background = new ItemStack(Material.PINK_STAINED_GLASS_PANE, 1);
		ItemMeta meta = Background.getItemMeta();
		meta.setDisplayName(" ");
		Background.setItemMeta(meta);
		
		FishView = new ItemStack(Material.SALMON, 1);
		meta = FishView.getItemMeta();
		meta.setDisplayName("View Fish Types");
		FishView.setItemMeta(meta);
		
		RarityView = new ItemStack(Material.EMERALD, 1);
		meta = RarityView.getItemMeta();
		meta.setDisplayName("View Rarities");
		RarityView.setItemMeta(meta);
		
		ConfigEdit = new ItemStack(Material.REDSTONE_TORCH, 1);
		meta = ConfigEdit.getItemMeta();
		meta.setDisplayName("Edit Config");
		ArrayList<String> lore = new ArrayList();
		lore.add("Runs the " + ChatColor.AQUA + "/bf Config " + ChatColor.DARK_PURPLE + "command");
		meta.setLore(lore);
		ConfigEdit.setItemMeta(meta);

		ReloadButton = new ItemStack(Material.NETHER_STAR, 1);
		meta = ReloadButton.getItemMeta();
		meta.setDisplayName("Reload Plugin");
		lore = new ArrayList();
		lore.add("Runs the " + ChatColor.AQUA + "/bf Reload " + ChatColor.DARK_PURPLE + "command");
		meta.setLore(lore);
		ReloadButton.setItemMeta(meta);
		
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
		lore = new ArrayList<>(){{
			add("Add items to this menu to add them to the reward list");
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
	 * Creates the TourneyGUI and opens it at given player
	 * @param p the player to open the invntory at
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
		if(Setup.econEnabled)
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
			if(Setup.econEnabled)
				add("Cash Prize: " + Variables.CurrSym + tObj.Cash);
			add("Click to finish creation.");
		}};
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		inv.setItem(26, TourneyCreate);
		p.openInventory(inv);
	}
	
	

}
