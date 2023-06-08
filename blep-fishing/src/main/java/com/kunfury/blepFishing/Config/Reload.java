package com.kunfury.blepFishing.Config;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;

import com.kunfury.blepFishing.Objects.CollectionLogObject;
import com.kunfury.blepFishing.Endgame.EndgameVars;
import com.kunfury.blepFishing.Endgame.TreasureHandler;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Miscellaneous.ItemHandler;
import com.kunfury.blepFishing.Objects.*;
import com.kunfury.blepFishing.Quests.QuestHandler;
import com.kunfury.blepFishing.Quests.QuestObject;
import com.kunfury.blepFishing.Tournament.TournamentHandler;
import com.kunfury.blepFishing.Tournament.TournamentMode;
import com.kunfury.blepFishing.Tournament.TournamentObject;
import com.kunfury.blepFishing.Tournament.TournamentType;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.command.CommandSender;

import com.kunfury.blepFishing.BlepFishing;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;

public class Reload {

	CommandSender sender;
	public static boolean success;

	public void ReloadPlugin(CommandSender s) {
		sender = s;
		success = false;
//    	BlepFishing.config = BlepFishing.blepFishing.getConfig();

//    	if(BlepFishing.config.getKeys(false).size() == 0){
//			new ConfigHandler().SendError("No/Empty Config for Blep Fishing! Blep Fishing has been disabled.", null);
//			return;
//		}



		//LoadFish();
		//LoadRarities();




		success = true;


	}



}
