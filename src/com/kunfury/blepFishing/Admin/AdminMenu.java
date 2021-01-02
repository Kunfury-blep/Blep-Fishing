package com.kunfury.blepFishing.Admin;

import java.util.ArrayList;
import java.util.List;

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

import Miscellaneous.Variables;
import Objects.BaseFishObject;
import Objects.RarityObject;

public class AdminMenu implements Listener {
	
	public static ItemStack Background, FishEdit, RarityEdit, BiomeEdit, BackButton; 
	
	static Inventory inv;
	static enum Inv {
		BASE,
		FISH,
		RARITIES,
		BIOMES,
		FISH_EDIT
	};
	
	static Inv openInv = Inv.BASE;
	
	public void ShowInventory(CommandSender sender) {		
		inv = Bukkit.createInventory(null, 27, " ---Blep Fishing Admin Panel---");
		openInv = Inv.BASE;
		Player player = (Player)sender;
		
		for(int i = 0; i < 27; i++) {
			inv.setItem(i, Background);
		}
		
		inv.setItem(12, RarityEdit);
		inv.setItem(13, FishEdit);
		inv.setItem(14, BiomeEdit);		

		player.openInventory(inv);
	}
	
	@EventHandler()
    public void clickEvent(InventoryClickEvent e) { //Handles Interaction with the panel
		if(e.getInventory().equals(inv) && e.getClickedInventory() != null ) {			
			e.setCancelled(true);
			ItemStack item =e.getCurrentItem();
			
			
			if(item.equals(BackButton)) {
				if(openInv.equals(Inv.BASE) || openInv.equals(Inv.FISH) || openInv.equals(Inv.BIOMES) || openInv.equals(Inv.RARITIES))
					ShowInventory(e.getWhoClicked());
				if(openInv.equals(Inv.FISH_EDIT))
					FishEdit();
			}
				
			
			if(openInv.equals(Inv.BASE)) {
				if(item.equals(FishEdit))
					FishEdit();
				if(item.equals(RarityEdit))
					RarityEdit();
				if(item.equals(BiomeEdit))
					BiomeEdit();	
				
				return;
			}
			if(openInv.equals(Inv.FISH)) {
				//Bukkit.broadcastMessage(item.getItemMeta().getDisplayName());
			}
			if(openInv.equals(Inv.BASE)) {
				
			}
			
			
			}
	}
	
	@SuppressWarnings("serial")
	private void FishEdit() {
		openInv = Inv.FISH;
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
	private void RarityEdit() {
		openInv = Inv.RARITIES;
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
	
	private void BiomeEdit() {
		//openInv = Inv.BIOMES;
		//Bukkit.broadcastMessage("Biome Edit Clicked");
	}
	
	
	public static void CreateStacks() {
		Background = new ItemStack(Material.PINK_STAINED_GLASS_PANE, 1);
		ItemMeta meta = Background.getItemMeta();
		meta.setDisplayName(" ");
		Background.setItemMeta(meta);
		
		FishEdit = new ItemStack(Material.SALMON, 1);
		meta = FishEdit.getItemMeta();
		meta.setDisplayName("Edit Fish");
		FishEdit.setItemMeta(meta);
		
		RarityEdit = new ItemStack(Material.EMERALD, 1);
		meta = RarityEdit.getItemMeta();
		meta.setDisplayName("Edit Rarity");
		RarityEdit.setItemMeta(meta);
		
		BiomeEdit = new ItemStack(Material.COMPASS, 1);
		meta = BiomeEdit.getItemMeta();
		meta.setDisplayName("Edit Biome");
		BiomeEdit.setItemMeta(meta);
		
		BackButton = new ItemStack(Material.BARRIER, 1);
		meta = BackButton.getItemMeta();
		meta.setDisplayName("Go Back");
		BackButton.setItemMeta(meta);
	}

}
