package com.kunfury.blepFishing.Tournament;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import org.apache.commons.io.FilenameUtils;
import com.kunfury.blepFishing.Setup;

import com.kunfury.blepFishing.Miscellaneous.Variables;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

import static com.kunfury.blepFishing.Miscellaneous.Variables.Prefix;

public class TournamentRewards implements Listener {
	/**
	 * Gets the rewards of the tournament
	 * @param sender the command sender
	 */
	public void GetRewards(CommandSender sender) {
		String playerName = sender.getName();
		Player p = (Player)sender;
		List<String> list = new ArrayList<>();
		List<ItemStack> items = new ArrayList<>();
		
		String fileName = Setup.dataFolder + "/Rewards/" + playerName + ".json";
		
		File file = new File(fileName);
		
		if(file.exists()) {
			try {			
				FileReader fr = new FileReader(fileName);
				BufferedReader in = new BufferedReader(fr);
	            String str;
	            while ((str = in.readLine()) != null) {
	                list.add(str);
	            }
				in.close();

	            items = Variables.DeserializeItemList(list);


	            if(!file.delete())
	            {
	            	String msg = Prefix + "Failed to delete the rewards file for " + playerName;
					Bukkit.getLogger().warning(msg);
	            }
	            
			} catch (IOException e) {
				e.printStackTrace();
			}


			int cashVal = 0;

			if(items.size() > 0) { //Only shows the inventory if the player has rewards to claim
				final Inventory inv = Bukkit.createInventory(null, 54, Variables.getMessage("rewardInvTitle"));

				for (ItemStack item : items) {
					if(item.getType().equals(Material.COMMAND_BLOCK))cashVal += item.getAmount();
					else inv.addItem(item);
				}
				if(cashVal > 0 && Setup.econEnabled) GiveMoney(p, cashVal);
				p.openInventory(inv);
			}
			
			
		}else
			sender.sendMessage(Prefix + Variables.getMessage("noRewards"));
	}

	/**
	 * Gives money to a player
	 * @param p the player to receive money
	 * @param value how much money the player should reveive
	 */
	private void GiveMoney(Player p, int value) {
		Economy econ = Setup.getEconomy();
	    econ.depositPlayer(p, value);
	    p.sendMessage("You received " + Variables.CurrSym + value);
	}

	/**
	 * Checks if the player has any rewards to claim and alerts them if so
	 * @param e event variable
	 */
	@EventHandler
    public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		File folder = new File(Setup.dataFolder + "/Rewards/");
		if(folder.exists()) {
			File[] listOfFiles = folder.listFiles();
			for (int i = 0; i < listOfFiles.length; i++) {
				File f = listOfFiles[i];

				//TODO: Check to ensure the file is properly being read
				//TODO: Switch to using UUID instead of Player Name


				String fileName = FilenameUtils.removeExtension(f.getName());
				//String pUUID = p.getUniqueId();

				  if (f.isFile() && fileName.equals(p.getName())) {
					  Bukkit.getServer().getScheduler().runTaskLater(Setup.getPlugin(), new Runnable() {
				        	@Override
				        	  public void run() {
				        		p.sendMessage(Prefix +  Variables.getMessage("claimRewards"));
				        		p.sendMessage(Prefix + ChatColor.translateAlternateColorCodes(
				        				'&', Variables.getMessage("claimRewards2")));
				        	}
				        }, 250);
				  }
			}
		}
	}

	
	
}
