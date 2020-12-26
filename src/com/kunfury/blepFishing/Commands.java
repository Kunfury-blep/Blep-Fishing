package com.kunfury.blepFishing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.kunfury.blepFishing.Admin.AdminMenu;

import Miscellaneous.Reload;
import Miscellaneous.Variables;
import Objects.FishObject;
import Objects.RarityObject;

public class Commands implements CommandExecutor {
	
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	
    	if(args.length == 0) { //Runs when base command is sent   		
    		sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
    				"                     &b[Blep Fishing Help] &b\n" 
    				+ "&b[BF]&f /bf lb <fishname> - displays the leadboard for the fish \n"
    				+ "&b[BF]&f /bf reload - reloads config \n"
    				+ "&b[BF]&f /bf fish - Lists all fish \n"
    				));
    		return true;
    	}
    	
    	if(args.length > 0 && args.length <= 2 && (args[0].equalsIgnoreCase("leader") 
    			|| args[0].equalsIgnoreCase("lb") || args[0].equalsIgnoreCase("leaderboard"))) {
    		return showLeaderboard(args, sender);
    	}
    	
    	if(args.length == 1 && args[0].equalsIgnoreCase("reload")) {
    		if(sender.hasPermission("bf.reload"))
    			return Reload.ReloadPlugin(sender);
    		else
    			return NoPermission(sender);
    	}
    	
    	if(args.length == 1 && args[0].equalsIgnoreCase("fish")) {
    		if(sender.hasPermission("bf.listFish"))
    			return ListFish(sender);
    		else
    			return NoPermission(sender);
    	}
    	
    	
    	if(args.length == 1 && args[0].equalsIgnoreCase("admin")) {
    		if(sender.hasPermission("bf.admin")) {
    			AdminMenu menu = new AdminMenu();
    			menu.ShowInventory(sender);
    			return true;
    		}else
    			return NoPermission(sender);
    	}
    	
    	if(args.length == 1 && args[0].equalsIgnoreCase("score")) {
    		sender.sendMessage("Scoring Fish");
			return ScoreFish(sender);
    	}
    		/*
    	if(args.length == 1 && args[0].equalsIgnoreCase("toggle")) {
    		sender.sendMessage("Fishing Toggled");
    		HashMap<UUID, PermissionAttachment> perms = new HashMap<UUID, PermissionAttachment>();
			Player player = (Player)sender;
			PermissionAttachment attachment = player.addAttachment(Setup.getPlugin());
			PermissionAttachment pperms = perms.get(player.getUniqueId());
			perms.put(player.getUniqueId(), attachment);
    		if(sender.hasPermission("bf.canFish")) {
    			perms.get(player.getUniqueId()).unsetPermission("bf.canFish");
    			sender.sendMessage("Fishing Disabled");
    		}else {
    			pperms.setPermission("bf.canFish", true);
    			sender.sendMessage("Fishing Enabled");
    		}    			
			return true;
    	}
    		*/
    	sender.sendMessage("That command is not recognized.");
        return true;
	
    }
    
    @SuppressWarnings("unchecked")
	private boolean showLeaderboard(String[] args, CommandSender sender){
    	if(args.length == 1)
    		sender.sendMessage("/blep lb <Fish Name>");
		else {			           	
        	String fishName = args[1];
        	
        	//New Fish Fetch
        	for (FishObject f : Variables.FishList) {
        		if(fishName.toUpperCase().equals(f.Name.toUpperCase())) {
        			//Triggers when a fish is found. Looks to see if there are any available fish in file.
        			String filePath = Setup.dataFolder + "/fish data/" + fishName  + ".data"; 
        			File fishFile = new File(filePath);
        			if(!fishFile.exists()){
                		sender.sendMessage("None of that fish has been caught.");
                		return true;
            	        }
        			
        			List<FishObject> caughtFishList = new ArrayList<>();
        			
        			try { //Finds the file        				
            		    ObjectInputStream input = new ObjectInputStream(new FileInputStream (filePath));
            		    caughtFishList.addAll((List<FishObject>)input.readObject());
            		    input.close();
            		} catch (IOException | ClassNotFoundException ex) {
            			sender.sendMessage("Loading failed. Report this to your admin.");
            			ex.printStackTrace();
            		}  
        			
        			Collections.sort(caughtFishList, Collections.reverseOrder());
        			
        			int pLength = 15;
                	int sLength = 10;
                	int dLength = 10;
                	int rLength = 20;
                	
                	String fPrefix = "&b";
                	String fPlayer = fixFontSize("Player Name", pLength);
            		String fSize = fixFontSize("  Fish Size", sLength);
            		String fDate = fixFontSize("Date Caught", dLength);
            		String fRarity = fixFontSize("Rarity", rLength);
                	
            		String fullString = ChatColor.translateAlternateColorCodes('&', fPrefix + fPlayer + fSize + fRarity + fDate);
            		
            		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', fPrefix + "--" + fishName + " Leaderboard--"));
            		sender.sendMessage(fullString);	
            		if(caughtFishList.size() > 5) {
            			for(int i = 0; i < 5; i++) {
            				FishObject fish = caughtFishList.get(i);
            				fPlayer = fixFontSize(fish.PlayerName, pLength);
                    		fSize = fixFontSize(Setup.df.format(fish.RealSize) + "in", sLength);
                    		fDate = fixFontSize(fish.DateCaught.getMonth().toString() 
                    				+ fish.DateCaught.getDayOfMonth() + ", " + fish.DateCaught.getYear(), dLength);
                    		fRarity = fixFontSize(fish.Rarity, rLength); 
                    		
                    		String lbString = ChatColor.translateAlternateColorCodes('&' ,(i+1) + ". " 
                    						+ fPlayer + fSize + fRarity + "&f" + fDate);
                    		sender.sendMessage(lbString);
            			}
            		}else
            		{
            			for (FishObject fish : caughtFishList) {
            				int i = 0;
            				fPlayer = fixFontSize(fish.PlayerName, pLength);
                    		fSize = fixFontSize(Setup.df.format(fish.RealSize) + "in", sLength);
                    		fDate = fixFontSize(fish.DateCaught.getMonth().toString() 
                    				+ fish.DateCaught.getDayOfMonth() + ", " + fish.DateCaught.getYear(), dLength);
                    		fRarity = fixFontSize(fish.Rarity, rLength); 
                    		
                    		String lbString = ChatColor.translateAlternateColorCodes('&' ,(i+1) + ". " 
                            				+ fPlayer + fSize + fRarity + "&f" + fDate);
                    		sender.sendMessage(lbString);
                    		i++;
                    	}
            		}
        			return true;
        		}
        	}
        	sender.sendMessage("That fish does not exist.");      
		}
    	return true;
    }
    
    public boolean ListFish(CommandSender sender) {
    	int i = 1;
    	sender.sendMessage("--Listing Fish--");
    	for (FishObject fish : Variables.FishList) {
    		
    		sender.sendMessage(i + ". " + fish.Name);
    		i++;
    	}
    	
    	
    	return true;
    }
    
    public boolean NoPermission(CommandSender sender) {
    	sender.sendMessage("Sorry, you don't have permission to do that."); 
    	return true;
    }
    
    @SuppressWarnings("unchecked")
	private boolean ScoreFish(CommandSender sender) {
    	
    	List<FishObject> fishList = new ArrayList<>();
    	
    	
    	for (File file : new File(Setup.dataFolder.getAbsolutePath() + File.separator + "fish data").listFiles()) {    		
    		try { //Finds the file        				
    		    ObjectInputStream input = new ObjectInputStream(new FileInputStream (file.getPath()));
    		    fishList.addAll((List<FishObject>)input.readObject());
    		    input.close();
    		} catch (IOException | ClassNotFoundException ex) {
    			//sender.sendMessage("Loading failed. Report this to your admin.");
    			ex.printStackTrace();
    		}  
    		
    	}
    	
    	int lowWeight = Variables.RarityList.get(0).Weight;
    	
    	for (FishObject fish : fishList) 
    	{ 
    		for (RarityObject rarity : Variables.RarityList) 
        	{ 
        		if(rarity.Name.equals(fish.Rarity)) {
        			double adjWeight = rarity.Weight;
                    if(lowWeight != 1 && lowWeight != 0)
                    	adjWeight = adjWeight / lowWeight;
                    fish.CalcScore(adjWeight);
                    break;
        		}
        	}
    	}
    	sender.sendMessage(fishList.size() +  " Fish Scores Calculated");
    	return true;
    }
    
    /********************************************************
    * Fix string spaces to align text in minecraft chat
    *
    * @author David Toledo ([EMAIL]david.oracle@gmail.com[/EMAIL])
    * @param String to be resized
    * @param Size to align
    * @return New aligned String
    */
    public static String fixFontSize (String s, int size) {
     
    String ret = s;
     
    if ( s != null ) {
     
    for (int i=0; i < s.length(); i++) {
    if ( s.charAt(i) == 'I' || s.charAt(i) == ' ') {
    ret += " ";
    }
    }
     
    int availLength = size - s.length();
     
    for (int i=0; i < availLength; i++) {
    ret += " ";
    }
    }
     
    return (ret);
    }
     
    
    
}
