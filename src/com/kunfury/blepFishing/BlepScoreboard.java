package com.kunfury.blepFishing;

import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import Miscellaneous.Variables;
import Objects.FishObject;

public class BlepScoreboard {

	public static void FishInfo(Player p, FishObject fish) {
		
		Bukkit.getServer().getScheduler().runTaskLater(Setup.getPlugin(), new Runnable() {
        	@Override
        	  public void run() {
        		ScoreboardManager manager = Bukkit.getScoreboardManager();
                final Scoreboard board = manager.getNewScoreboard();
                final Objective objective = board.registerNewObjective("test", "dummy", "blep");     
                objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                objective.setDisplayName(ChatColor.RED + "--" + fish.Name + "--");
                
                Score score2 = objective.getScore(ChatColor.translateAlternateColorCodes('&', fish.Rarity));
                score2.setScore(4);  
                
                Score score = objective.getScore(ChatColor.AQUA + "" + fish.RealSize + "\"" );
                score.setScore(3);            
                
                if(Setup.hasEcon) { //Checks that an economy is installed
                	Score score1 = objective.getScore(ChatColor.GREEN + "$" + fish.RealCost);
                	score1.setScore(2);  
                }      
                
                List<FishObject> caughtFishList = Variables.FishDict.get(fish.Name);
        		Collections.sort(caughtFishList, Collections.reverseOrder());
        		
        		Score score3 = objective.getScore(ChatColor.AQUA + "Rank #" + (caughtFishList.indexOf(fish) + 1));
                score3.setScore(3);
                
        		
        		p.setScoreboard(board);
                Bukkit.getServer().getScheduler().runTaskLater(Setup.getPlugin(), new Runnable() {
                	@Override
                	  public void run() {
                	    for (String s : board.getEntries()) {
                	        board.resetScores(s);
                	    }
                	  }
                }, 200);
        	    }
        }, 2);
		
        
	}
	
}
