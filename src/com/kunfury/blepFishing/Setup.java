package com.kunfury.blepFishing;

import java.io.File;
import java.util.logging.Logger;

import Tournament.Tournament;
import com.kunfury.blepFishing.Commands.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.kunfury.blepFishing.Admin.AdminMenu;
import com.kunfury.blepFishing.Signs.FishSign;

import Miscellaneous.CreateConfig;
import Miscellaneous.Reload;
import Miscellaneous.Villagers;
import Tournament.TournamentRewards;
import net.milkbowl.vault.economy.Economy;

public class Setup extends JavaPlugin {
	public static FileConfiguration config;
	public static Setup setup;
	public static File dataFolder;
	public static boolean hasEcon = true;
	
	private static Economy econ = null;
	private static final Logger log = Logger.getLogger("Minecraft");
	
	private static Plugin plugin;
	 
    // Fired when plugin is first enabled
    @Override
    public void onEnable() {
    	plugin = this;
    	config = this.getConfig();
    	setup = this;
    	dataFolder = getDataFolder();
    	
    	if(!setupEconomy()) {
    		log.warning(String.format("[%s] - Economy support disabled due to no Vault dependency found!", getDescription().getName()));
    		hasEcon = false;
    	}
    	
    	File configFile;
    	configFile = new File(getDataFolder(), "config.yml");
    	 
    	if(!configFile.exists()){
            new CreateConfig();
	        }
    	
    	new FishSign().LoadSigns();
    	
    	getServer().getPluginManager().registerEvents(new FishSwitch(), this);
    	getServer().getPluginManager().registerEvents(new FishSign(), this);
    	getServer().getPluginManager().registerEvents(new AdminMenu(), this);
    	getServer().getPluginManager().registerEvents(new Villagers(), this);
    	getServer().getPluginManager().registerEvents(new TournamentRewards(), this);
		getServer().getPluginManager().registerEvents(new Tournament(), this);

    	SetupCommands();
    	this.saveConfig();
    	saveConfig();

    	new Reload().ReloadPlugin(Bukkit.getConsoleSender());
    	
    	
    	new AdminMenu().CreateStacks(); //Creates the icons for the admin panel
    	
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    
    
    public static Plugin getPlugin() {
        return plugin;
      }
    
    public static Economy getEconomy() {
        return econ;
    }

    private void SetupCommands(){
		this.getCommand("bf").setExecutor(new CommandManager());
		//this.getCommand("bf").setTabCompleter(new BaseTabCompletion());

		//this.getCommand("bf StartTourney").setExecutor(new StartTourneyCommand());
		//this.getCommand("bf StartTourney").setTabCompleter(new StartTourneyTabCompletion());
	}
}
