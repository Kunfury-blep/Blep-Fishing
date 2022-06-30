package com.kunfury.blepFishing.Tournament;

import java.util.List;

import com.kunfury.blepFishing.Miscellaneous.Variables;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Objects.FishObject;
import com.kunfury.blepFishing.Objects.TournamentObject;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import static com.kunfury.blepFishing.Miscellaneous.Variables.Prefix;

public class TournamentFinish {
	/**
	 * Triggers when the com.kunfury.blepFishing.Tournament is finished
	 * @param tObj the tournament object
	 */
	public TournamentFinish(TournamentObject tObj) {		
		tObj.HasFinished = true;
		List<FishObject> tourneyFish = tObj.GetWinners();
		if(tourneyFish.size() > 0) { //If any fish have been caught
        	//Initializes the size of the chatbox
        	int pLength = 15;

        	String fPlayer = Formatting.FixFontSize("Player Name", pLength);
    		String fullString = ChatColor.translateAlternateColorCodes('&', "&b" + fPlayer + " Fish");
    		
    		Bukkit.broadcastMessage(ChatColor.BOLD + (Variables.getMessage("tournamentLeaderboard")));
    		Bukkit.broadcastMessage(fullString);	
    		
			int i = 1;
			for (FishObject fish : tourneyFish) {
				fPlayer = Formatting.FixFontSize(fish.PlayerName, pLength);
        		String lbString = ChatColor.translateAlternateColorCodes('&' ,
        				Formatting.FixFontSize(i + ".", 4)
        				+ fPlayer + fish.Rarity + " " + fish.Name);
        		TextComponent mainComponent = new TextComponent (lbString);
        		mainComponent.setHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT, fish.GetHoverText()));

        		for(Player p : Bukkit.getOnlinePlayers()) {
        			p.spigot().sendMessage(mainComponent);
        		}
        		i++;
			}
			
			for(Player p : Bukkit.getOnlinePlayers()) {
				p.sendMessage(Prefix + Variables.getMessage("tournamentFinish"));
	    		p.sendMessage(Prefix + ChatColor.translateAlternateColorCodes('&', Variables.getMessage("tournamentFinish2")));

    		}
			
			tObj.Winner = tourneyFish.get(0).PlayerName;
			
			
			
			new SaveWinner(tObj.Winner, tObj.GetRewards(), tObj.CashReward);
		}else {
			String tempName = tObj.FishName.toLowerCase();
			if(tempName.equalsIgnoreCase("ALL"))
				tempName = "fish";
			Bukkit.broadcastMessage(Prefix + String.format(Variables.getMessage("tournamentNoCaught"), tempName));
		}
		
		new SaveTournaments(); //Saves the tournaments once more to ensure the tournament does not reward twice
		new Tournament().CheckActiveTournaments();
	}

}
