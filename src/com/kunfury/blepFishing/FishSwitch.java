package com.kunfury.blepFishing;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.kunfury.blepFishing.Signs.FishSign;

import Miscellaneous.Variables;
import Objects.FishObject;
import Objects.RarityObject;
import io.netty.util.internal.ThreadLocalRandom;

public class FishSwitch implements Listener {
	private Player player;
	
	@EventHandler
	public void onFish(PlayerFishEvent e) {				
		player = e.getPlayer();
		
		List<Material> itemList = new ArrayList<Material>();
		itemList.add(Material.SALMON);
		itemList.add(Material.COD);
		itemList.add(Material.TROPICAL_FISH);
	    if(e.getCaught() instanceof Item){   	
	        Item item = (Item) e.getCaught();
	    	Material t = item.getItemStack().getType();
	    	List<FishObject> wFish = new ArrayList<>();
	    	
	    	
	        if(itemList.contains(t) && !item.getItemStack().getItemMeta().hasCustomModelData()){
	        	//Checks For Weather
	        	if(!Bukkit.getWorlds().get(0).hasStorm()) {
	        		for(final FishObject fish : Variables.FishList) {
	        			if(!fish.IsRaining)
	        				wFish.add(fish);
	        		}
	        	}else
	        		wFish = Variables.FishList;
	        	
	        	List<FishObject> availFish = new ArrayList<>();
	        	
	        	//Checks For Biome
        		for(final FishObject fish : wFish) {
        			for(final String biome : fish.OldBiomes) 
        			{
        				if(biome.equalsIgnoreCase((item.getLocation().getBlock().getBiome().name())) || biome.toUpperCase().equals("ALL")){
        					availFish.add(fish);
        				}
        			}
        		}
	        	
        		//Checking if there are available fish, otherwise give vanilla
        		if(availFish.size() > 0) {	
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
    		    	
    	        	//New Fish Selection - Based On Weight
    		    	FishObject fish = availFish.get(0);
    		    	
    		    	Collections.sort(Variables.FishList, new Comparator<FishObject>() {
    		    	    @Override
    		    	    public int compare(FishObject o1, FishObject o2) {
    		    	    	Integer newWeight1 = o1.Weight;
    		    	    	Integer newWeight2 = o2.Weight;
    		    	        return (newWeight1).compareTo(newWeight2);
    		    	    }
    		    	});

    		    	
    		    	
    	        	int randF = ThreadLocalRandom.current().nextInt(0, Variables.FishTotalWeight);
    	        	for(final FishObject sort : Variables.FishList) {
    		    		if(randF <= sort.Weight) {
    		    			fish = sort;
    		    			break;
    		    		}else
    		    			randF -= fish.Weight;
    		    	}
    	        	
    		    	
    	            ItemStack is = item.getItemStack();
    	            is.setType(Material.SALMON);
    	            
    	           
    	            ItemMeta m = is.getItemMeta();
    	            m.setDisplayName(ChatColor.translateAlternateColorCodes('&', '&' + chosenRarity.Prefix + fish.Name));
    	            m.setCustomModelData(fish.ModelData);
    	            
    		    	
    	            
    	            double adjWeight = chosenRarity.Weight;
    	            if(Variables.RarityList.get(0).Weight != 1)
    	            	adjWeight = adjWeight / Variables.RarityList.get(0).Weight;
    	            double size = ThreadLocalRandom.current().nextDouble(fish.MinSize, fish.MaxSize);
    	            
    	            fish.RealSize = Double.parseDouble(Setup.df.format(size).replace(',', '.')); //Bandaid fix. Figure out why commas sometimes appear
    	            fish.Score = fish.CalcScore(adjWeight);
    	            fish.PlayerName = e.getPlayer().getName();
    	            fish.DateCaught = LocalDateTime.now();
    	            fish.Rarity = chosenRarity.Name;
    	            fish.RealCost = Double.parseDouble(Setup.df.format(CalcPrice(fish, chosenRarity)).replace(',', '.'));
    	            
    	            m.setLore(CreateLore(fish));
    	            is.setItemMeta(m);
    	            is = NBTEditor.set( is, fish.RealCost, "blep", "item", "fishValue" );

				    item.setItemStack(is);
				    
				    //Broadcasts if the player catches the rarest fish possible
				    if(chosenRarity.Weight <= Variables.RarityList.get(0).Weight) {
				    	Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', 
					    		player.getDisplayName() + " just caught a " + fish.Rarity + " " 
		    					+ fish.Name + " &fthat was " + fish.RealSize + "\" long!"));
					    Firework fw = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
					    fw.detonate();
				    }
				    
				    BlepScoreboard.FishInfo(player, fish);
    	            SaveFish(fish);
        		}
        		
	            

	        }
	    }
	}

	private double CalcPrice(FishObject fish, RarityObject rarity) {
		double sizeMod = fish.RealSize/fish.AvgSize;
		
		double realCost = (fish.BaseCost * sizeMod) * rarity.PriceMod;
		
		return realCost;
	}

	@SuppressWarnings("unchecked")
	private void SaveFish(FishObject gFish) {
		
		//Temporary (Maybe)
		Variables.AddToFishDict(gFish);
		
		String filePath = Setup.dataFolder + "/fish data/" + gFish.Name  + ".data";    	                	            
        List<FishObject> savedFishList = new ArrayList<>();
		
		//Load Fish
        try {
        	ObjectInputStream input = null;
		    File tempFile = new File(filePath);
		    if(tempFile.exists()) {
    		    input = new ObjectInputStream(new FileInputStream (filePath));
		    	savedFishList.addAll((List<FishObject>)input.readObject());	
		    }
		    if(input != null)
		    	input.close();
		} catch (IOException | ClassNotFoundException ex) {
			player.sendMessage("Loading Failed");
			ex.printStackTrace();
		}    	            
        
        savedFishList.add(gFish);
        
        //Save Fish
		try {
			
			File tmpDir = new File(Setup.dataFolder + "/fish data/");
	    	if(!Files.exists(tmpDir.toPath()))
	    		tmpDir.mkdir();
			
		    ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(filePath));
		    
		    output.writeObject(savedFishList);
		    output.close();
		} catch (IOException ex) {
			player.sendMessage("Saving Failed");
		    ex.printStackTrace();
		}  
		FishSign fishSign = new FishSign();
		fishSign.UpdateSigns();
	}

	private List<String> CreateLore(FishObject fish){
		List<String> Lore = new ArrayList<>();   		
		//Lore.add(fish.Rarity);
		if(Setup.hasEcon) //Checks that an economy is installed
			Lore.add("&2Value: $" + Setup.df.format(fish.RealCost));
		Lore.add(fish.Lore);
		 
		Lore.add("&8Length: " + Setup.df.format(fish.RealSize) + "in.");
			 
		LocalDateTime now = LocalDateTime.now();
		String details = ("&8Caught By: " + fish.PlayerName + " on " + now.toLocalDate());
		Lore.add(details);
		
		List<String> colorLore = new ArrayList<>();
		for (String line : Lore) colorLore.add(ChatColor.translateAlternateColorCodes('&', line));
		
		return colorLore;
	}

}
