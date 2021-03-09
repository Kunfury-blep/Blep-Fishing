package com.kunfury.blepFishing;
import java.time.LocalDateTime;
import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import Miscellaneous.Formatting;
import Miscellaneous.NBTEditor;
import Miscellaneous.Variables;
import Objects.AreaObject;
import Objects.BaseFishObject;
import Objects.FishObject;
import Objects.RarityObject;
import io.netty.util.internal.ThreadLocalRandom;

public class FishSwitch implements Listener {
	private static final List<Material> itemList = Arrays.asList(Material.SALMON, Material.COD, Material.TROPICAL_FISH);


	/**
	 * Triggers when someone catches a fish
	 * @param e
	 */
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onFishNormal(PlayerFishEvent e) {
		if(!Variables.HighPriority)
			FishHandler(e);			
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onFishHigh(PlayerFishEvent e) {
		if(Variables.HighPriority)
			FishHandler(e);			
	}
	
	private void FishHandler(PlayerFishEvent e) {
		Player player = e.getPlayer();
		

	    if(e.getCaught() instanceof Item){   	
	        Item item = (Item) e.getCaught();
			ItemStack is = item.getItemStack();
			is.setType(Material.SALMON);
			BaseFishObject base = GetCaughtFish(item);

			if(base != null){
				//Rarity Selection
				int randR = ThreadLocalRandom.current().nextInt(0, Variables.RarityTotalWeight);
				RarityObject chosenRarity = null;
				for(final RarityObject rarity : Variables.RarityList) {
					if(randR <= rarity.Weight) {
						chosenRarity = rarity;
						break;
					}else
						randR -= rarity.Weight;
				}

				ItemMeta m = is.getItemMeta();
				m.setDisplayName(ChatColor.translateAlternateColorCodes('&', '&' + chosenRarity.Prefix + base.Name));
				m.setCustomModelData(base.ModelData);



				double size = ThreadLocalRandom.current().nextDouble(base.MinSize, base.MaxSize);

				FishObject fish = new FishObject(base, chosenRarity, e.getPlayer().getName(), size);

				m.setLore(CreateLore(fish, base));
				is.setItemMeta(m);
				is = NBTEditor.set( is, fish.RealCost, "blep", "item", "fishValue" );

				item.setItemStack(is);

				//Broadcasts if the player catches the rarest fish possible
				if(chosenRarity.Weight <= Variables.RarityList.get(0).Weight) {
					Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
							player.getDisplayName() + " just caught a " + fish.Rarity + " "
									+ fish.Name + " &fthat was " + Formatting.DoubleFormat(fish.RealSize) + "\" long!"));
					Firework fw = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
					fw.detonate();
				}
				if(Variables.ShowScoreboard)
					new BlepScoreboard().FishInfo(player, fish);
				Variables.AddToFishDict(fish);
			}
		}

	}

	/**
	 * Creates a lore for the fish that has been catched
	 * @param fish The fish that needs the lore to be created on (Is this proper english?)
	 * @param base
	 * @return the lore
	 */
	private List<String> CreateLore(FishObject fish, BaseFishObject base){
		List<String> Lore = new ArrayList<>();   		
		//Lore.add(fish.Rarity);
		if(Setup.hasEcon) //Checks that an economy is installed
			Lore.add("&2Value: " + Variables.CSym + Formatting.DoubleFormat(fish.RealCost));
		Lore.add(base.Lore);
		 
		Lore.add("&8Length: " + Formatting.DoubleFormat(fish.RealSize) + "in.");
			 
		LocalDateTime now = LocalDateTime.now();
		String details = ("&8Caught By: " + fish.PlayerName + " on " + now.toLocalDate());
		Lore.add(details);
		
		List<String> colorLore = new ArrayList<>();
		for (String line : Lore) colorLore.add(ChatColor.translateAlternateColorCodes('&', line));
		
		return colorLore;
	}


	private BaseFishObject GetCaughtFish(Item item) {
		Material t = item.getItemStack().getType();
		List<BaseFishObject> fishList = new ArrayList<>();

		List<BaseFishObject> wFish = new ArrayList<>(); //Available fish to choose from
		if (itemList.contains(t) && !Objects.requireNonNull(item.getItemStack().getItemMeta()).hasCustomModelData()) {
			//Checks For Weather
			if (!Bukkit.getWorlds().get(0).hasStorm()) {
				for (final BaseFishObject fish : Variables.BaseFishList) {
					if (!fish.IsRaining)
						wFish.add(fish);
				}
			} else
				wFish = Variables.BaseFishList;


			//Checks for active area
			List<AreaObject> areas = new ArrayList<>();
			String biomeName = item.getLocation().getBlock().getBiome().name();
			Variables.AreaList.forEach(a -> {
				if (a.Biomes.contains(biomeName)) {
					areas.add(a);
				}
			});

			//Get fish who can be caught in the area
			wFish.forEach(f -> {
				areas.forEach(a -> {
					if (a.Name.equals(f.Area))
						fishList.add(f);
				});
			});

		}

		fishList.sort(new Comparator<BaseFishObject>() {
			@Override
			public int compare(BaseFishObject o1, BaseFishObject o2) {
				Integer newWeight1 = o1.Weight;
				Integer newWeight2 = o2.Weight;
				return (newWeight1).compareTo(newWeight2);
			}
		});
		/*
		//Commented out until I figure out what it was meant to do.
		int randF = ThreadLocalRandom.current().nextInt(0, Variables.FishTotalWeight);
		for(final BaseFishObject sort : Variables.BaseFishList) {
			if(randF <= sort.Weight) {
				base = sort;
				break;
			}else
				randF -= base.Weight;
		}
	 	*/
		//The following is temporary until I get fish weights introduced

		BaseFishObject base = null;
		if(fishList != null && fishList.size() > 0){
			int rand = ThreadLocalRandom.current().nextInt(0, fishList.size());
			base = fishList.get(rand);
		}
		return base;

	}

}
