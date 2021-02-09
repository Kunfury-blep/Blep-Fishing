package Tournament;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import Miscellaneous.Formatting;
import Miscellaneous.Variables;
import Objects.FishObject;
import Objects.TournamentObject;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class TournamentFinish {
	/**
	 * Triggers when the Tournament is finished
	 * @param tObj the tournament object
	 */
	public TournamentFinish(TournamentObject tObj) {		
		tObj.HasFinished = true;
		List<FishObject> tourneyFish = new ArrayList<>();
		Variables.GetFishList("ALL").forEach(f -> {
			//Checks if the fish is the correct type and was caught after the start date
			if((tObj.FishName.equals("ALL") || f.Name.equals(tObj.FishName))) { 
				tourneyFish.add(f);
			}
		});
		
		if(tourneyFish.size() > 0) { //If any fish have been caught
			Collections.sort(tourneyFish, Collections.reverseOrder());
			
			if(tourneyFish.size() > 3)
				tourneyFish.subList(3, tourneyFish.size()).clear();
        	
        	//Initializes the size of the chatbox
        	int pLength = 15;

        	String fPlayer = Formatting.FixFontSize("Player Name", pLength);
    		String fullString = ChatColor.translateAlternateColorCodes('&', "&b" + fPlayer + " Fish");
    		
    		Bukkit.broadcastMessage(ChatColor.BOLD + ("-- Tournament Leaderboard - Hover For Info--"));
    		Bukkit.broadcastMessage(fullString);	
    		
			int i = 1;
			for (FishObject fish : tourneyFish) {
				fPlayer = Formatting.FixFontSize(fish.PlayerName, pLength);
        		String lbString = ChatColor.translateAlternateColorCodes('&' ,
        				Formatting.FixFontSize(i + ".", 4)
        				+ fPlayer + fish.Rarity + " " + fish.Name);
        		
        		
        		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        		TextComponent mainComponent = new TextComponent (lbString);
        		mainComponent.setHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT, 
        				new Text(ChatColor.translateAlternateColorCodes('&' ,(fish.Rarity + " " + fish.Name +
        						"&f\nFish Size: " + Formatting.DoubleFormat(fish.RealSize) +
        						"\nRank: " + (i) +
        						"\nCaught On: " +  formatter.format(fish.DateCaught)  +
        						"\nScore: " + Formatting.DoubleFormat(fish.Score)
        						)))));

        		for(Player p : Bukkit.getOnlinePlayers()) {
        			p.spigot().sendMessage(mainComponent);
        		}
        		i++;
			}
			
			for(Player p : Bukkit.getOnlinePlayers()) {
				p.sendMessage(Variables.Prefix + "Rewards will be given out to the winner!");
	    		p.sendMessage(Variables.Prefix + "Type " + ChatColor.AQUA + "/bf claim" + ChatColor.WHITE +  " to get them!");

    		}
			
			tObj.Winner = tourneyFish.get(0).PlayerName;
			
			
			
			new SaveWinner(tObj.Winner, tObj.GetRewards(), tObj.CashReward);
		}else {
			Bukkit.broadcastMessage("No " + tObj.FishName + " were caught before the tournament ended. So no rewards have been given.");
		}
		
		new SaveTournaments(); //Saves the tournaments once more to ensure the tournament does not reward twice
	}

}
