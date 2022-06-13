package com.kunfury.blepFishing;

import java.io.File;
import java.util.Objects;
import java.util.logging.Logger;

import com.kunfury.blepFishing.Crafting.CraftingManager;
import com.kunfury.blepFishing.Crafting.Equipment.FishBag.UseFishBag;
import com.kunfury.blepFishing.Tournament.TournamentClickListener;
import com.kunfury.blepFishing.Tournament.Tournament;
import com.kunfury.blepFishing.Commands.*;
import com.kunfury.blepFishing.Plugins.PluginHandler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.kunfury.blepFishing.Admin.AdminMenu;
import com.kunfury.blepFishing.Signs.FishSign;

import com.kunfury.blepFishing.Miscellaneous.CreateConfig;
import com.kunfury.blepFishing.Miscellaneous.Reload;
import com.kunfury.blepFishing.Miscellaneous.Villagers;
import com.kunfury.blepFishing.Tournament.TournamentRewards;
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

    	PluginManager pm = getServer().getPluginManager();
    	pm.registerEvents(new FishListener(), this);
		pm.registerEvents(new FishSign(), this);
		pm.registerEvents(new AdminMenu(), this);
		pm.registerEvents(new Villagers(), this);
		pm.registerEvents(new TournamentRewards(), this);
		pm.registerEvents(new Tournament(), this);
		pm.registerEvents(new TournamentClickListener(), this);
		pm.registerEvents(new UseFishBag(), this);
		pm.registerEvents(new CraftingManager(), this);
		//getServer().getPluginManager().registerEvents(new SmithingTableHandler(), this);


		new PluginHandler().InitializePlugins();

    	SetupCommands();
    	this.saveConfig();
    	saveConfig();

    	new Reload().ReloadPlugin(Bukkit.getConsoleSender());
    	
    	
    	new AdminMenu().CreateStacks(); //Creates the icons for the admin panel

		new CraftingManager().InitItems();
		//new NetheriteRod().CreateRecipe();


    }

	@Override
	public void onLoad() {

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
        return true;
    }
    
    
    public static Plugin getPlugin() {
        return plugin;
      }
    
    public static Economy getEconomy() {
        return econ;
    }

    private void SetupCommands(){
		Objects.requireNonNull(this.getCommand("bf")).setExecutor(new CommandManager());
	}
}
