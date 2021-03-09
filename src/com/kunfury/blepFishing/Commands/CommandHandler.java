package com.kunfury.blepFishing.Commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.kunfury.blepFishing.Setup;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kunfury.blepFishing.Admin.AdminMenu;

import Miscellaneous.FishEconomy;
import Miscellaneous.Formatting;
import Miscellaneous.Reload;
import Miscellaneous.Variables;
import Objects.FishObject;
import Objects.RarityObject;
import Tournament.Tournament;
import Tournament.TournamentRewards;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

import static Miscellaneous.Variables.Messages;
import static Miscellaneous.Variables.Prefix;

public class CommandHandler implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if(args.length == 0 || args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")) { //Runs when base command is sent   		
    		sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
    				"                     " + Messages.getString("helpTitle") + "\n"
    				+ Prefix + "/bf lb <fishname> - " + Messages.getString("leaderboardHelp") + "\n"
    				+ Prefix + "/bf reload - " + Messages.getString("reloadHelp") + "\n"
    				+ Prefix + "/bf fish - " + Messages.getString("fishHelp") + "\n"
    				+ Prefix + "/bf claim - " + Messages.getString("claimHelp") + "\n"
    				+ Prefix + "/bf tourney - " + Messages.getString("tourneyHelp") + "\n"
    				+ Prefix + "/bf admin - " + Messages.getString("adminHelp") + "\n"
    				));
    		return true;
    	}
    	
    	if(args.length <= 3 && (args[0].equalsIgnoreCase("leader")
    			|| args[0].equalsIgnoreCase("lb") || args[0].equalsIgnoreCase("leaderboard"))) {
    		return showLeaderboard(args, sender);
    	}
    	
    	if(args.length == 1 && args[0].equalsIgnoreCase("reload")) {
    		if(sender.hasPermission("bf.reload"))
    			return new Reload().ReloadPlugin(sender);
    		else
    			return NoPermission(sender);
    	}
    	
    	if(args.length == 1 && args[0].equalsIgnoreCase("fish")) {
    		if(sender.hasPermission("bf.listFish")){
				new ListFish(sender);
				return true;
			}
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
    	
    	if(args.length == 1 && args[0].equalsIgnoreCase("sell")){
    		if(sender.hasPermission("bf.sell")) {
    			FishEconomy.SellFish((Player)sender, false);
    			return true;
    		}else
    			return NoPermission(sender);
    	}
    	
    	if(args.length == 1 && args[0].equalsIgnoreCase("sellall")){
    		if(sender.hasPermission("bf.sellall")) {
    			FishEconomy.SellFish((Player)sender, true);
    			return true;
    		}else
    			return NoPermission(sender);
    	}
    	
    	if(args.length == 2 && args[0].equalsIgnoreCase("sellfor")){
    		if(sender.hasPermission("bf.sellfor")) {
    			Player p = Bukkit.getPlayer(args[1]);
    			if(p != null && p.isOnline())
    				FishEconomy.SellFish(p);
    			else
    				sender.sendMessage("That player could not be found.");
    			return true;
    		}else
    			return NoPermission(sender);
    	}
    	
    	List<String> tourneyList = Arrays.asList( "TOURNAMENT", "TOURNEY", "TOURNEYS", "TOURNAMENTS", "T"); 
    	
    	if(args.length == 1 && tourneyList.contains(args[0].toUpperCase())) {
    		new Tournament().ShowTourney(sender);
    		return true;
    	}
    	
    	if(args[0].equalsIgnoreCase("StartTourney")) {
    		if(sender.hasPermission("bf.admin")) {
    			try {
        			new Tournament().CreateTourny(sender, args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]), 
        					args[4], Integer.parseInt(args[5]));
        			//Oh boi why did I ever write it like this
        			//This is impractical as all hell
        			//Definitely need to fix this in the future.
    			}catch(Exception e) { 
        			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
        					Prefix + "/bf StartTourney <fishName> <duration> <cash prize> <item name> <item count>"));
        		}
    		}    		
    		return true;
    	}
    	
    	if(args.length == 1 && args[0].equalsIgnoreCase("Claim")) {
    		new TournamentRewards().GetRewards(sender);
    		return true;
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
    
	private boolean showLeaderboard(String[] args, CommandSender sender){
    	if(args.length == 1)
    		sender.sendMessage("/bf lb <Fish Name> <Leaderboard Number>");
		else {			           	
        	String fishName = args[1].toUpperCase();
        	String formattedName = StringUtils.capitalize(fishName.toLowerCase());
        	
        	int startVal = 0;
        	if(args.length == 3) {
        		try {
        			startVal = Integer.parseInt(args[2]) - 1;
        		}catch(Exception ignored){}
        		
        	}
        	
        	
        	
        	if(!fishName.equals("ALL") && !Variables.FishDict.containsKey(fishName)){
        		sender.sendMessage("None of that fish has been caught.");
        		return true;
	        }
        	
        	
        	///
        	//If the fish exissts, and at least one has been caught, runs the below section
        	///
        	List<FishObject> caughtFishList = Variables.GetFishList(fishName); //Gets the caught fish
			if(caughtFishList != null){
				if( caughtFishList.size() > startVal) {
					caughtFishList.subList(0, startVal).clear();
				}else {
					startVal = 0;
					sender.sendMessage("There aren't that many fish available.");
				}

				if(caughtFishList.size() > 5)
					caughtFishList.subList(5, caughtFishList.size()).clear();
			}

        	//Initializes the size of the chatbox
        	int pLength = 15;

        	String fPlayer = Formatting.FixFontSize("Player Name", pLength);
    		String fullString = ChatColor.translateAlternateColorCodes('&', "&b" + fPlayer + " Fish");
    		
    		sender.sendMessage(ChatColor.BOLD + ("--" + formattedName + " Leaderboard - Hover For Info--"));
    		sender.sendMessage(fullString);	

    			int i = startVal;

			for (FishObject fish : caughtFishList) {
    				fPlayer = Formatting.FixFontSize(fish.PlayerName, pLength);
            		String lbString = ChatColor.translateAlternateColorCodes('&' ,
            				Formatting.FixFontSize((i+1) + ".", 4)
            				+ fPlayer + fish.Rarity + " " + fish.Name);
            		
            		
            		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            		TextComponent mainComponent = new TextComponent (lbString);
            		mainComponent.setHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT, 
            				new Text(ChatColor.translateAlternateColorCodes('&' ,(fish.Rarity + " " + fish.Name +
            						"&f\nFish Size: " + Formatting.DoubleFormat(fish.RealSize) +
            						"\nRank: " + (i + 1) +
            						"\nCaught On: " +  formatter.format(fish.DateCaught)  +
            						"\nScore: " + Formatting.DoubleFormat(fish.Score)
            						)))));
            		
            		sender.spigot().sendMessage(mainComponent);
            		i++;
            	}    		
			return true; 
		}
    	return true;
    }

	/**
	 *
	 * @param sender
	 * @return if the player has permission to execute a command
	 */
	public boolean NoPermission(CommandSender sender) {
    	sender.sendMessage(Prefix + Messages.getString("noPermissions"));
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
    			sender.sendMessage("Loading failed. Report this to your admin.");
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
                    //Fix this to use the new system
                    //fish.CalcScore(adjWeight);
                    break;
        		}
        	}
    	}
    	sender.sendMessage(fishList.size() +  " Fish Scores Calculated");
    	return true;
    }
    
    
    
}
