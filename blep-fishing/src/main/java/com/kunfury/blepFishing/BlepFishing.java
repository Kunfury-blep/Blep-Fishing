package com.kunfury.blepFishing;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import com.kunfury.blepFishing.Config.*;
import com.kunfury.blepFishing.Conversations.ConversationHandler;
import com.kunfury.blepFishing.Crafting.CraftingManager;
import com.kunfury.blepFishing.Crafting.SmithingTableHandler;
import com.kunfury.blepFishing.Events.EventHandler;
import com.kunfury.blepFishing.Commands.*;
import com.kunfury.blepFishing.Miscellaneous.Utilities;
import com.kunfury.blepFishing.Plugins.DiscordSRVHandler;
import com.kunfury.blepFishing.Plugins.Metrics;
import com.kunfury.blepFishing.Plugins.PluginHandler;
import com.kunfury.blepFishing.Plugins.WorldGuardHandler;
import com.kunfury.blepFishing.Quests.QuestHandler;
import com.kunfury.blepFishing.Tournament.TournamentHandler;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bstats.charts.MultiLineChart;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.kunfury.blepFishing.Interfaces.Admin.AdminMenu;
import com.kunfury.blepFishing.Signs.FishSign;

import net.milkbowl.vault.economy.Economy;

public class BlepFishing extends JavaPlugin {
	public static ConfigBase configBase;


	//public static FileConfiguration config;
	public static BlepFishing blepFishing;
	public static File dataFolder;
	public static boolean econEnabled = true;

	private static Economy econ = null;
	private static final Logger log = Logger.getLogger("Minecraft");

	private static Plugin plugin;

	public static int stats_FishCaught;
	public static int stats_QuestsFinished;
	public static int stats_TournamentsActive;

	@Override
	public void onLoad(){

		Plugin wgPlugin = Bukkit.getPluginManager().getPlugin("WorldGuard");

		if((wgPlugin instanceof WorldGuardPlugin))
			new WorldGuardHandler().Load();
	}

    // Fired when plugin is first enabled
    @Override
    public void onEnable() {
		plugin = this;
		blepFishing = this;
		dataFolder = getDataFolder();

		loadMetrics();

		if(!setupEconomy()) {
			log.warning(String.format("[%s] - Economy support disabled due to no Vault dependency found!", getDescription().getName()));
			econEnabled = false;
		}

		plugin.saveDefaultConfig();

		new FishSign().LoadSigns();

		new EventHandler().SetupEvents(getServer().getPluginManager());

		new SmithingTableHandler().InitializeSmithRecipes();

		new PluginHandler().InitializePlugins();

		new ConfigHandler().Initialize(this);

		SetupCommands();

		new AdminMenu().CreateStacks(); //Creates the icons for the admin panel
		new CraftingManager().InitItems();
		Utilities.RunTimers();

		if(Bukkit.getPluginManager().getPlugin("DiscordSRV") != null){
			DiscordSRVHandler.Load();
		}
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

	private void loadMetrics() {
		int pluginId = 18201; // <-- Replace with the id of your plugin!
		Metrics metrics = new Metrics(this, pluginId);

		metrics.addCustomChart(new Metrics.SingleLineChart("fish_caught", () -> {
			int stat = stats_FishCaught;
			stats_FishCaught = 0;
			return stat;
		}));

		metrics.addCustomChart(new Metrics.SingleLineChart("active_quests", () -> QuestHandler.getActiveQuests().size()));

		metrics.addCustomChart(new Metrics.SingleLineChart("active_tournaments", () -> TournamentHandler.ActiveTournaments.size()));
	}
}
