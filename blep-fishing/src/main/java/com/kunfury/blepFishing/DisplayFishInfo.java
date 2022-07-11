package com.kunfury.blepFishing;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Objects.FishObject;

public class DisplayFishInfo {


	public static boolean InfoScoreboard, InfoChat;





	public void InitialDisplay(Player p, FishObject fish){
		if(InfoScoreboard) ShowScoreboard(p, fish);
		if(InfoChat) ShowChat(p, fish);
	}

	/**
	 * Displays a scoreboard when a fish is caught
	 * @param p the player to show the scoreboard to
	 * @param fish the fish that got catched
	 */
	private void ShowScoreboard(Player p, FishObject fish) {
		
		Bukkit.getServer().getScheduler().runTaskLater(Setup.getPlugin(), new Runnable() {
        	@Override
        	  public void run() {
        		String formattedName = fish.Name;
        		String fishName = fish.Name.toUpperCase();
        		
        		ScoreboardManager manager = Bukkit.getScoreboardManager();
                final Scoreboard board = Objects.requireNonNull(manager).getNewScoreboard();
                final Objective objective = board.registerNewObjective("test", "dummy", "blep");     
                objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                objective.setDisplayName(ChatColor.RED + "--" + formattedName + "--");
                
                Score score2 = objective.getScore(ChatColor.translateAlternateColorCodes('&', fish.Rarity));
                score2.setScore(4);  
                
                Score score = objective.getScore(ChatColor.AQUA + "" + Formatting.DoubleFormat(fish.RealSize) + "\"" );
                score.setScore(3);            
                
                if(Setup.econEnabled) { //Checks that an economy is installed
                	Score score1 = objective.getScore(ChatColor.GREEN + Variables.CurrSym + Formatting.DoubleFormat(fish.RealCost));
                	score1.setScore(2);  
                }      
                
                List<FishObject> caughtFishList = Variables.FishDict.get(fishName);
        		caughtFishList.sort(Collections.reverseOrder());
        		
        		Score score3 = objective.getScore(ChatColor.AQUA + "Rank #" + (caughtFishList.indexOf(fish) + 1));
                score3.setScore(3);
                
        		
        		p.setScoreboard(board);
                Bukkit.getServer().getScheduler().runTaskLater(Setup.getPlugin(), () -> {
					for (String s : board.getEntries()) {
						board.resetScores(s);
					}
				  }, 200);
        	    }
        }, 2);
		
        
	}

	private void ShowChat(Player p, FishObject fish){
		String lbString = Variables.Prefix + "You just caught a " +
				net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&' , fish.Rarity + " " + fish.Name) + ChatColor.WHITE +  "!";
		TextComponent mainComponent = new TextComponent (lbString);
		mainComponent.setHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT, fish.GetHoverText()));
		p.spigot().sendMessage(mainComponent);
	}
}
