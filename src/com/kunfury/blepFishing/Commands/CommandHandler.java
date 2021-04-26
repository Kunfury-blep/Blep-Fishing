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
import Miscellaneous.Reload;
import Miscellaneous.Variables;
import Objects.FishObject;
import Objects.RarityObject;
import Tournament.Tournament;
import Tournament.TournamentRewards;

import static Miscellaneous.Variables.Messages;
import static Miscellaneous.Variables.Prefix;

public class CommandHandler implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if(args.length == 0 || args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")) { //Runs when base command is sent   		

    		return true;
    	}
    	
    	if(args.length == 1 && args[0].equalsIgnoreCase("score")) {
    		sender.sendMessage("Scoring Fish");
			return ScoreFish(sender);
    	}
    	sender.sendMessage("That command is not recognized.");
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
