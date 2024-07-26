package com.kunfury.blepfishing.config;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.objects.TournamentType;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TournamentConfig {

    private final FileConfiguration tournamentConfig;

    public TournamentConfig(){
        File tournamentConfigFile = new File(BlepFishing.instance.getDataFolder(), "tournaments.yml");


//        //TODO: Remove below. Only for testing
//        BlepFishing.getPlugin().saveResource("tournaments.yml", true);
//        tournamentConfigFile = new File(BlepFishing.instance.getDataFolder(), "tournaments.yml");

        if(!tournamentConfigFile.exists()){
            BlepFishing.getPlugin().saveResource("tournaments.yml", false);
            tournamentConfigFile = new File(BlepFishing.instance.getDataFolder(), "tournaments.yml");
        }

        tournamentConfig = YamlConfiguration.loadConfiguration(tournamentConfigFile);

        LoadTournaments();
    }

    private void LoadTournaments(){
        for(final String key : tournamentConfig.getValues(false).keySet()){
            String name = tournamentConfig.getString(key + ".Name");
            List<String> fishTypes = tournamentConfig.getStringList(key + ".Fish Types");
            double duration = tournamentConfig.getDouble(key + ".Duration");
            boolean villagerHorn = tournamentConfig.getBoolean(key + ".Villager Horn");
            int hornLevel = 1;
            if(villagerHorn && tournamentConfig.contains(key + ".Villager Horn Level"))
                hornLevel = tournamentConfig.getInt(key + ".Villager Horn Level");


            HashMap<TournamentType.TournamentDay, List<String>> startTimes = new HashMap<>();
            //List<String> everyday = tournamentConfig.getStringList(key + ".Start Times.EVERYDAY");

            var sortedStartTimes = Arrays.stream(TournamentType.TournamentDay.values()).toList().stream()
                    .sorted(Enum::compareTo).toList();
            for(var d : sortedStartTimes){
                if(!tournamentConfig.contains(key + ".Start Times." + d))
                    continue;

                List<String> times = tournamentConfig.getStringList(key + ".Start Times." + d);

                //Bukkit.getLogger().warning(d + " Loaded Times: " + times);

                startTimes.put(d, times);
            }


            var rewardsConfig = tournamentConfig.getConfigurationSection(key + ".Rewards");

            HashMap<Integer, Double> cashRewards = new HashMap<>();
            HashMap<Integer, List<ItemStack>> itemRewards = new HashMap<>();

            if(rewardsConfig != null){
                for(var rKey : rewardsConfig.getKeys(false)){
                    int place = Integer.parseInt(rKey);

                    //Get all cash rewards
                    if(rewardsConfig.contains(rKey + ".Cash")){
                        cashRewards.put(place, rewardsConfig.getDouble(rKey + ".Cash"));
                    }

                    //Get all basic item rewards
                    if(rewardsConfig.contains(rKey + ".Items")){
                        var configList = rewardsConfig.getList(rKey + ".Items");
                        List<ItemStack> itemList = new ArrayList<>();
                        assert configList != null;
                        for(var i : configList){
                            if(!(i instanceof ItemStack)){
                                Bukkit.getLogger().severe("BF: Tried to load invalid Itemstack from tournament: " + key);
                                continue;
                            }
                            itemList.add((ItemStack) i);
                        }
                        itemRewards.put(place, itemList);
                    }
                }
            }





            TournamentType tournamentType = new TournamentType(
                    key, name, duration, fishTypes, startTimes, cashRewards,
                    itemRewards, villagerHorn, hornLevel);
            TournamentType.AddNew(tournamentType);
        }
    }

    public void Save(){
        FileConfiguration newTourneyConfig = new YamlConfiguration();
        for(var type : TournamentType.GetTournaments()){
            String key = type.Id;
            newTourneyConfig.set(key + ".Name", type.Name);
            newTourneyConfig.set(key + ".Fish Types", type.FishTypeIds);
            newTourneyConfig.set(key + ".Duration", type.Duration);
            newTourneyConfig.set(key + ".Villager Horn", type.VillagerHorn);
            if(type.VillagerHorn)
                newTourneyConfig.set(key + ".Villager Horn Level", type.HornLevel);

            var sortedStartTimes = Arrays.stream(TournamentType.TournamentDay.values()).toList().stream()
                    .sorted(Enum::compareTo).toList();
            for(var d : sortedStartTimes){
                newTourneyConfig.set(key + ".Start Times." + d, type.StartTimes.get(d));
                //Bukkit.broadcastMessage("Start Times: " + type.StartTimes.get(d));
            }

            for(var i : type.CashRewards.keySet()){
                newTourneyConfig.set(key + ".Rewards." + i + ".Cash", type.CashRewards.get(i));
            }

            for(var i : type.ItemRewards.keySet()){
                newTourneyConfig.set(key + ".Rewards." + i + ".Items", type.ItemRewards.get(i));
            }
        }
        try {
            FileWriter fishWriter = new FileWriter(BlepFishing.instance.getDataFolder() + "/tournaments.yml");
            fishWriter.write(newTourneyConfig.saveToString());
            fishWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
