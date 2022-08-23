package com.kunfury.blepFishing.Signs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.kunfury.blepFishing.Crafting.Equipment.FishBag.BagInfo;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.kunfury.blepFishing.Setup;

import com.kunfury.blepFishing.Miscellaneous.FishEconomy;
import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Objects.MarketObject;

import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;


public class FishSign implements Listener {

	public static List<SignObject> rankSigns = new ArrayList<>();
	public static List<MarketObject> marketSigns = new ArrayList<>();
	static String signFilePath = Setup.dataFolder + "/Data" + "/signs.data";
	static String marketFilePath = Setup.dataFolder + "/Data" + "/markets.data";

	/**
	 * Signs for the Fishmarket
	 * @param e event variable
	 */
	@EventHandler
	public void onSignChange(SignChangeEvent e){
		String[] lines = e.getLines();
		Player player = e.getPlayer();

		Sign sign = (Sign)e.getBlock().getState();
		boolean fishExists = false;
		//Beginning of new sign creation
		if(lines[0].equals("[bf]")) { //Checks that the sign is a Blep Fishing sign
			if(lines[1].equalsIgnoreCase(Formatting.getMessage("Signs.marketTitle"))) {
				if(player.hasPermission("bf.admin")) MarketCreate(sign, player.getWorld());
				else player.sendMessage(Variables.Prefix + Formatting.getMessage("System.adminReq"));
			}else {
				//Checks if fish exist in the main list in FishSwitch
				int level = 0;
				if(!lines[2].isEmpty() && isNumeric(lines[2])) { //Gets the provided leaderboard level
					level = Integer.parseInt(lines[2]) - 1;
					if(level < 0) level = 0;
				}else player.sendMessage(Variables.Prefix + Formatting.getMessage("Signs.noRank"));

				SignObject signObj = new SignObject((Sign)e.getBlock().getState(), lines[1], level, player.getWorld());

				if(signObj.FishName != null && !signObj.FishName.isEmpty()){
					rankSigns.add(signObj);
					UpdateSignsFile();
				} else e.setLine(3, Formatting.getMessage("Signs.noFish"));
			}
		}
	}

	/**
	 * Triggers when a Sign gets broken
	 * @param e event variable
	 * @throws Exception
	 */
	@EventHandler
	public void onSignBreak(BlockBreakEvent e) {
		if (e.getBlock().getState() instanceof org.bukkit.block.Sign) { //Checks if the block is a sign
			Sign sign = (Sign) e.getBlock().getState();

			if (rankSigns != null && rankSigns.size() > 0) {
				for (SignObject signObj : rankSigns) {
					if (signObj != null && signObj.GetSign() != null && signObj.GetSign().equals(sign)) {
						rankSigns.remove(signObj);
						UpdateSignsFile();
						break;
					}}}

			if (marketSigns != null  && marketSigns.size() > 0) {
				for (MarketObject marketObj : marketSigns) {
					if (marketObj != null && marketObj.GetSign() != null  && marketObj.GetSign().equals(sign)) {
						marketSigns.remove(marketObj);
						UpdateSignsFile();
						break;
					}}}
		}
	}

	/**
	 * Triggers when a sign gets rightclickked
	 * @param e event variable
	 */
	@EventHandler
	public void onSignInteract(PlayerInteractEvent e) {
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock().getState() instanceof  Sign) {
			Sign sign = (Sign) e.getClickedBlock().getState();

			if(Setup.econEnabled && e.getItem() != null){
				ItemStack item = e.getItem();
				if(item.getType().equals(Material.SALMON)){
					for(MarketObject market : marketSigns) {
						if(market.CheckBool(sign)){
							e.setCancelled(true);
							FishEconomy.SellFish(e.getPlayer(), 1);
							return;
						}
					}
				}
				if(BagInfo.IsBag(item)){

				}


			}
		}

	}


	/**
	 * Updates the sign
	 */
	public void UpdateSigns() {
		for (SignObject signObj : rankSigns) {
			if(signObj.GetSign() != null) {
				RefreshSign.Refresh(signObj);
			}
		}
	}

	/**
	 * Loads all the signs from the tempfile
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public void LoadSigns() {
		//Load Leaderboard Signs
		try
		{
			rankSigns.clear();
			ObjectInputStream input = null;
			File tempFile = new File(signFilePath);
			if(tempFile.exists()) {
				input = new ObjectInputStream(new FileInputStream (signFilePath));
				rankSigns = (List<SignObject>)input.readObject();
			}
			if(input != null)
				input.close();
		} catch (IOException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		//
		//Load Market Signs
		//
		try
		{
			marketSigns.clear();
			ObjectInputStream input = null;
			File tempFile = new File(marketFilePath);
			if(tempFile.exists()) {
				input = new ObjectInputStream(new FileInputStream (marketFilePath));
				marketSigns = (List<MarketObject>)input.readObject();
			}
			if(input != null)
				input.close();
		} catch (IOException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}

	private void MarketCreate(Sign sign, World world) {

		marketSigns.add(new MarketObject(sign, world));

		//Save Fish
		try {
			ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(marketFilePath));
			output.writeObject(marketSigns);
			output.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		Bukkit.getScheduler().runTaskLater(Setup.getPlugin(), () -> {
			sign.setLine(0, "-------------");
			sign.setLine(1, Formatting.getMessage("Signs.fish"));
			sign.setLine(2, Formatting.getMessage("Signs.market"));
			sign.setLine(3, "-------------");
			sign.update();
			UpdateSigns();
		}, 1L);
	}

	public void UpdateSignsFile(){
		try {
			ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(signFilePath));
			output.writeObject(rankSigns);
			output.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		Bukkit.getScheduler().runTaskLater(Setup.getPlugin(), () -> UpdateSigns(), 1L);

		//TODO: Possibly delete file if empty
		//TODO: Combine MarketObject and SignObject
	}

	public static boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			double d = Double.parseDouble(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}


}
