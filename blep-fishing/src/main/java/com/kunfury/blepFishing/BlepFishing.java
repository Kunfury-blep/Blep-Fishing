package com.kunfury.blepFishing;

import java.io.File;
import java.util.Objects;
import java.util.logging.Logger;

import com.kunfury.blepFishing.Config.CacheHandler;
import com.kunfury.blepFishing.Config.ConfigBase;
import com.kunfury.blepFishing.Config.FileHandler;
import com.kunfury.blepFishing.Config.Reload;
import com.kunfury.blepFishing.Crafting.CraftingManager;
import com.kunfury.blepFishing.Crafting.SmithingTableHandler;
import com.kunfury.blepFishing.Events.EventHandler;
import com.kunfury.blepFishing.Events.EventListener;
import com.kunfury.blepFishing.Commands.*;
import com.kunfury.blepFishing.Interfaces.MenuHandler;
import com.kunfury.blepFishing.Miscellaneous.Utilities;
import com.kunfury.blepFishing.Plugins.PluginHandler;
import com.kunfury.blepFishing.Tournament.TournamentHandler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.kunfury.blepFishing.Interfaces.Admin.AdminMenu;
import com.kunfury.blepFishing.Signs.FishSign;

import net.milkbowl.vault.economy.Economy;

public class BlepFishing extends JavaPlugin {
	public static ConfigBase configBase;


	public static FileConfiguration config;
	public static BlepFishing blepFishing;
	public static File dataFolder;
	public static boolean econEnabled = true;
	
	private static Economy econ = null;
	private static final Logger log = Logger.getLogger("Minecraft");
	
	private static Plugin plugin;
	 
    // Fired when plugin is first enabled
    @Override
    public void onEnable() {
    	plugin = this;
    	blepFishing = this;
    	dataFolder = getDataFolder();
    	
    	if(!setupEconomy()) {
    		log.warning(String.format("[%s] - Economy support disabled due to no Vault dependency found!", getDescription().getName()));
    		econEnabled = false;
    	}

    	plugin.saveDefaultConfig();

		GenerateFiles();

    	new FishSign().LoadSigns();

		new EventHandler().SetupEvents(getServer().getPluginManager());


		new SmithingTableHandler().InitializeSmithRecipes();

		new PluginHandler().InitializePlugins();

    	SetupCommands();

		saveConfig();
		configBase = new ConfigBase(this);
		new Reload().ReloadPlugin(Bukkit.getConsoleSender());

		if(Reload.success){
			new AdminMenu().CreateStacks(); //Creates the icons for the admin panel
			new CraftingManager().InitItems();
		}

		Utilities.RunTimers();
    }

	@Override
	public void onDisable(){
		new CacheHandler().SaveCache();
		FileHandler.SaveAll();

		if(SmithingTableHandler.SmithingKeys != null){
			for(var r : SmithingTableHandler.SmithingKeys){
				Bukkit.removeRecipe(r.getKey());
			}
		}
		if(CraftingManager.ShapedRecipes != null){
			for(var r : CraftingManager.ShapedRecipes){
				Bukkit.removeRecipe(r.getKey());
			}
		}

		TournamentHandler.Reset(true);
	}

    public static boolean hasEconomy(){
		return econEnabled && BlepFishing.blepFishing.getServer().getPluginManager().getPlugin("Vault") != null && econ != null;
	}

    private boolean setupEconomy() {
        if (!econEnabled || getServer().getPluginManager().getPlugin("Vault") == null) {
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

	private void GenerateFiles(){
		File tournamentFile = new File(plugin.getDataFolder(), "tournaments.yml");
		if (!tournamentFile.exists())
			plugin.saveResource("tournaments.yml", false);

		File messageFile = new File(plugin.getDataFolder(), "messages.yml");
		if (!messageFile.exists())
			plugin.saveResource("messages.yml", false);

		File itemsFile = new File(plugin.getDataFolder(), "items.yml");
		if (!itemsFile.exists())
			plugin.saveResource("items.yml", false);

		File questFile = new File(plugin.getDataFolder(), "quests.yml");
		if (!questFile.exists())
			plugin.saveResource("quests.yml", false);
	}
}
