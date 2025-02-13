package com.kunfury.blepfishing;

import com.kunfury.blepfishing.commands.CommandManager;
import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.AllBlueHandler;
import com.kunfury.blepfishing.helpers.ItemParser;
import com.kunfury.blepfishing.helpers.TreasureHandler;
import com.kunfury.blepfishing.helpers.Utilities;
import com.kunfury.blepfishing.helpers.CraftingHandler;
import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.listeners.*;
import com.kunfury.blepfishing.objects.TournamentObject;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.plugins.Metrics;
import com.kunfury.blepfishing.plugins.PluginHandler;
import com.kunfury.blepfishing.plugins.WorldGuardHandler;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import java.sql.SQLException;

public final class BlepFishing extends JavaPlugin {

    public static BlepFishing instance;
    private static Plugin plugin;
    public ConfigHandler configHandler;
    public boolean DebugMode;

    public BlepFishing(){
        instance = this;
    }

    public Database Database;
    private static Economy econ = null;

    @Override
    public void onLoad() {
        Plugin worldGuardPlugin = Bukkit.getPluginManager().getPlugin("WorldGuard");

        if((worldGuardPlugin instanceof WorldGuardPlugin))
            new WorldGuardHandler().Load();
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        loadMetrics();
        setupEconomy();

        configHandler = new ConfigHandler();
        configHandler.Initialize();

        SetupEvents();
        ItemHandler.Initialize();
        new CraftingHandler().Initialize();

        try{
            Database = new Database(getDataFolder().getAbsolutePath() + "/data.db");
        }
        catch (SQLException e){
            e.printStackTrace();
            Bukkit.getLogger().severe("Failed to connect to database");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        this.getCommand("bf").setExecutor(new CommandManager());

        Utilities.RunTimers();
        new ItemParser();

        new TreasureHandler();
        new AllBlueHandler();

        new PluginHandler().InitializePlugins();
    }

    @Override
    public void onDisable() {
        try{
            if(Database != null)
                Database.CloseConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void SetupEvents(){
        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new FishingListener(), plugin);
        pm.registerEvents(new PlayerInteractListener(), plugin);
        pm.registerEvents(new InventoryClickListener(), plugin);
        pm.registerEvents(new PlayerJoinListener(), plugin);
        pm.registerEvents(new CraftItemListener(), plugin);
        pm.registerEvents(new InventoryEventListener(), plugin);
    }


    public static Plugin getPlugin() {
        return plugin;
    }

    public static Database getDatabase(){
        return instance.Database;
    }

    public static int stats_FishCaught;
    private void loadMetrics() {
        int pluginId = 18201;
        Metrics metrics = new Metrics(this, pluginId);

        metrics.addCustomChart(new Metrics.SingleLineChart("fish_caught", () -> {
            int stat = stats_FishCaught;
            stats_FishCaught = 0;
            return stat;
        }));

        var tournamentTable = com.kunfury.blepfishing.database.Database.Tournaments;

        if(tournamentTable != null){
            int tournamentCount = tournamentTable.GetActive().size();
            metrics.addCustomChart(new Metrics.SingleLineChart("active_tournaments", () -> tournamentCount));
        }

//        metrics.addCustomChart(new Metrics.SingleLineChart("active_quests", () -> QuestHandler.getActiveQuests().size()));
//
    }

    public static boolean hasEconomy(){
        return Bukkit.getPluginManager().getPlugin("Vault") != null && econ != null;
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

    public static Economy getEconomy() {
        return econ;
    }

}
