package com.kunfury.blepFishing;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

import com.kunfury.blepFishing.AllBlue.EventListener;
import com.kunfury.blepFishing.Crafting.CraftingManager;
import com.kunfury.blepFishing.Crafting.Equipment.FishBag.UseFishBag;
import com.kunfury.blepFishing.Crafting.SmithingTableHandler;
import com.kunfury.blepFishing.Miscellaneous.*;
import com.kunfury.blepFishing.Tournament.TournamentClickListener;
import com.kunfury.blepFishing.Tournament.Tournament;
import com.kunfury.blepFishing.Commands.*;
import com.kunfury.blepFishing.Plugins.PluginHandler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.kunfury.blepFishing.Admin.AdminMenu;
import com.kunfury.blepFishing.Signs.FishSign;

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
    	setup = this;
    	dataFolder = getDataFolder();
    	
    	if(!setupEconomy()) {
    		log.warning(String.format("[%s] - Economy support disabled due to no Vault dependency found!", getDescription().getName()));
    		hasEcon = false;
    	}

		File configFile = new File(getDataFolder(), "config.yml"); //Massive Thank You to YourPalJake for their spigot thread containing this config loading
		if(!configFile.exists()){
			new FileCopy().copy(getResource("config.yml"), configFile);
		}
		config = new YamlConfiguration().loadConfiguration(configFile);

		try{
			config.save(configFile);
		}catch(IOException e){
			e.printStackTrace();
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
		pm.registerEvents(new SmithingTableHandler(), this);

		pm.registerEvents(new EventListener(), this);

		new SmithingTableHandler().InitializeSmithRecipes();

		new PluginHandler().InitializePlugins();

    	SetupCommands();
    	this.saveConfig();
    	saveConfig();

    	new Reload().ReloadPlugin(Bukkit.getConsoleSender());
    	
    	
    	new AdminMenu().CreateStacks(); //Creates the icons for the admin panel

		new CraftingManager().InitItems();


    }

    private boolean setupEconomy() {
        if (Variables.UseEconomy && getServer().getPluginManager().getPlugin("Vault") == null) {
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
