package com.kunfury.blepFishing.Interfaces.Admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.kunfury.blepFishing.Commands.SubCommands.ConfigSubcommand;
import com.kunfury.blepFishing.Commands.SubCommands.ReloadSubcommand;
import com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.AdminQuestMenuButton;
import com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.AdminRarityMenuButton;
import com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.Tournament.AdminTourneyMenuButton;
import com.kunfury.blepFishing.Interfaces.MenuHandler;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Objects.BaseFishObject;

public class AdminMenu implements Listener {
	
	public static ItemStack FishView, ConfigEdit, BackButton, ReloadButton,
		TourneyGUI;
	
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

	/**
	 *
	 * @param sender the sender of the command
	 * Shows the inventory to the given sender.
	 */
	public void ShowMenu(CommandSender sender) {
		Player p = (Player)sender;
		Inventory inv = Bukkit.createInventory(null, 27, Formatting.getMessage("Admin.panelTitle"));
		invMap.put(p, inv);
		winMap.put(p, Window.BASE);
		
		
		for(int i = 0; i < 27; i++) {
			inv.setItem(i, MenuHandler.getBackgroundItem());
		}

		List<ItemStack> menuButtons = new ArrayList<>();
		menuButtons.add(new AdminTourneyMenuButton().getItemStack());
		menuButtons.add(new AdminQuestMenuButton().getItemStack());
		menuButtons.add(new AdminRarityMenuButton().getItemStack());

		int slot = 0;
		for (ItemStack menuButton : menuButtons) {
			inv.setItem(slot, menuButton);
			slot++;
		}

		inv.setItem(13, ConfigEdit);
		inv.setItem(14, FishView);
		inv.setItem(22, ReloadButton);
		p.openInventory(inv);
	}

	/**
	 * Decides which events to trigger based on the currently active INV enum
	 * @param e event variable
	 */

	//TODO: Move this to EventHandler
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
					ShowMenu(e.getWhoClicked());
				if(activeWindow.equals(Window.FISH_EDIT))
					FishEdit(p);
			}
				
			
			if(activeWindow.equals(Window.BASE)) {
				if(item.equals(FishView)){
					FishEdit(p);
					return;
				}
				if(item.equals(ConfigEdit)){
					ConfigEdit(p);
					return;
				}
				if(item.equals(ReloadButton)){
					new ReloadSubcommand().perform(p, null);
					return;
				}
				if(item.equals(TourneyGUI)){
					TournamentEdit(p);
					return;
				}
			}
		}
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
			inv.setItem(i, MenuHandler.getBackgroundItem());
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
		FishView = new ItemStack(Material.SALMON, 1);
		ItemMeta meta = FishView.getItemMeta();
		meta.setDisplayName("View Fish Types");
		FishView.setItemMeta(meta);
		
		ConfigEdit = new ItemStack(Material.REDSTONE_TORCH, 1);
		meta = ConfigEdit.getItemMeta();
		meta.setDisplayName("Edit Config");
		ArrayList lore = new ArrayList();
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
		lore = new ArrayList();
		lore.add("Click to recieve a link to the tournament wiki page.");
		meta.setLore(lore);
		TourneyGUI.setItemMeta(meta);

	}
	
	
	public void TournamentEdit(Player p){
		p.closeInventory();
		TextComponent message = new TextComponent(Variables.getPrefix() + "Click here to open the Tournament Wiki Page." ); //Add to Messages.yml file
		message.setClickEvent( new ClickEvent( ClickEvent.Action.OPEN_URL, "https://github.com/Kunfury-blep/Blep-Fishing/wiki/Tournaments.yml" ) );
		p.spigot().sendMessage(message);
	}
	
	

}
