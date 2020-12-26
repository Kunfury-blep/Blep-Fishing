package Miscellaneous;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.kunfury.blepFishing.NBTEditor;
import com.kunfury.blepFishing.Setup;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class FishEconomy {

	protected static List<String> PlayerWaitList = new ArrayList<String>();
	//Handles the selling of fish
		public static void SellFish(Player player) {
			
			List<ItemStack> itemList = new ArrayList<ItemStack>();
			
			if(player.isSneaking()) {
				
				if(PlayerWaitList.contains(player.toString())) {
					for (ItemStack item : player.getInventory().getContents()) {
						if(item != null && item.getType() == Material.SALMON) {
							itemList.add(item);
						}						
					}
					PlayerWaitList.remove(player.toString());
				}else {
					PlayerWaitList.add(player.toString());
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b[BF] &fReally sell all fish in your inventory?"));
					return;
				}
				
				
				
			}
			
			itemList.add(player.getInventory().getItemInMainHand());
			
			double total = 0;
			for (ItemStack item : itemList) 
			{ 
				double value = NBTEditor.getDouble( item, "blep", "item", "fishValue" );
			    
			    if(value > 0) {
			    	Economy econ = Setup.getEconomy();
				    EconomyResponse r = econ.depositPlayer(player, value);
				    if(r.transactionSuccess()) {
				    	if(itemList.size() <= 1) //Only sends out a message if a single fish is sold
				    		player.sendMessage(ChatColor.translateAlternateColorCodes('&',
				    			"Sold " + item.getItemMeta().getDisplayName() 
					    		+ String.format("&f for &a%s", econ.format(r.amount))));
		                item.setAmount(item.getAmount() - 1);
		                total += value;
		            } else {
		            	player.sendMessage(String.format("An error occured: %s", r.errorMessage));
		            }
			    }
			}
			if(itemList.size() > 1)
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', 
		    			"&b[BF] &fSold " + itemList.size() + " fish"
			    		+ "&f for &a$" + Setup.df.format(total) + "." ));
		}	
}



