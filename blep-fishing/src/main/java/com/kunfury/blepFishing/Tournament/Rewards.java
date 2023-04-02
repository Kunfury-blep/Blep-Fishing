package com.kunfury.blepFishing.Tournament;

import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Miscellaneous.ItemHandler;
import com.kunfury.blepFishing.Miscellaneous.Utilities;
import com.kunfury.blepFishing.BlepFishing;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static com.kunfury.blepFishing.Config.Variables.Prefix;

public class Rewards {

    public static HashMap<UUID, List<ItemStack>> UnsavedRewards = new HashMap<>();
    List<UUID> rewardedPlayers = new ArrayList<>();
    public void Generate(TournamentObject t){


        HashMap<UUID, List<String>> winners;

        if(t.Type == TournamentType.AMOUNT)
            winners = getAmountRewards(t);
        else
            winners = getRewards(t);

        if(!winners.isEmpty()){
            winners.forEach((uuid, reward) -> {
                giveRewards(reward, uuid);
            });
        }

        List<String> defaultRewards = t.Rewards.get("DEFAULT");
        if(defaultRewards != null && defaultRewards.size() > 0){
            for(var p : t.getParticipants()){
                UUID uuid = p.getUniqueId();
                for(var r : defaultRewards){
                    ItemHandler.parseReward(r, uuid);
                }
            }
        }

        SaveRewards();
    }

    private HashMap<UUID, List<String>> getAmountRewards(TournamentObject t){
        HashMap<UUID, List<String>> rewards = new HashMap<>();

        HashMap<String, Integer> winners = t.CaughtMap;


        Object[] a = winners.entrySet().toArray();

        Arrays.sort(a, (Comparator) (o1, o2) -> ((Map.Entry<OfflinePlayer, Integer>) o2).getValue()
                .compareTo(((Map.Entry<OfflinePlayer, Integer>) o1).getValue()));

        for(int i = 0; i < a.length; i++){
            Map.Entry<String, Integer> winnerEntry = (Map.Entry<String, Integer>) a[i];

            if(t.Rewards.containsKey(String.valueOf(i + 1))){
                List<String> reward = t.Rewards.get(String.valueOf(i + 1));
                UUID uuid = UUID.fromString(winnerEntry.getKey());
                rewards.put(uuid, reward);
            }


        }

        return rewards;
    }

    private HashMap<UUID, List<String>> getRewards(TournamentObject t){
        HashMap<UUID, List<String>> winners = new HashMap<>();

        t.getWinners().forEach((rank, fish) -> {
            winners.put(fish.getPlayerUUID(), t.Rewards.get(String.valueOf(rank)));
        });

        return winners;
    }

    private void giveRewards(List<String> rewardStrs, UUID uuid){
        if(uuid != null && rewardStrs != null){
            rewardedPlayers.add(uuid);
            for(var r : rewardStrs){
                ItemHandler.parseReward(r, uuid);
            }
        }
    }

    public static void AddReward(UUID uuid, ItemStack item){
        if(item == null) return;

        List<ItemStack> items = UnsavedRewards.get(uuid);
        if(items == null) items = new ArrayList<>();
        items.add(item);
        UnsavedRewards.put(uuid, items);
    }

    public static void AddRewards(UUID uuid, List<ItemStack> items){
        if(items == null || items.size() <= 0) return;

        if(UnsavedRewards.get(uuid) != null) items.addAll(UnsavedRewards.get(uuid));
        UnsavedRewards.put(uuid, items);
    }

    public void SaveRewards(){
        if(UnsavedRewards.size() <= 0) return;

        final BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        //Grabs the collection Asynchronously
        scheduler.runTaskAsynchronously(BlepFishing.getPlugin(), () -> {
            UnsavedRewards.forEach((uuid, rewards) -> {
                try {
                Files.createDirectories(Paths.get(BlepFishing.dataFolder + "/Rewards"));
                String fileName = BlepFishing.dataFolder + "/Rewards/" + uuid + ".json";
                File myObj = new File(fileName);
                myObj.createNewFile();
                FileWriter myWriter = new FileWriter(fileName, true);
                Variables.SerializeItemList(rewards).forEach(serializedItem ->{
                    String message = serializedItem + System.lineSeparator();
                    try {
                        myWriter.write(message);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                    myWriter.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
            scheduler.runTask(BlepFishing.getPlugin(), () -> {
                UnsavedRewards.forEach((uuid, rewards) -> {
                    Player p = Bukkit.getPlayer(uuid);
                    if(p != null){
                        p.sendMessage(Formatting.getMessage("Tournament.claimRewards")
                                .replace("{amount}", String.valueOf(rewards.size())));
                        p.sendMessage(Variables.Prefix + Formatting.getMessage("Tournament.claimRewards2"));
                    }
                });
                UnsavedRewards.clear();
            });
        });
    }

    public List<ItemStack> LoadRewards(UUID uuid){
        List<ItemStack> items = new ArrayList<>();

        String fileName = BlepFishing.dataFolder + "/Rewards/" + uuid + ".json";
        File file = new File(fileName);

        if(file.exists()) {
            try {
                FileReader fr = new FileReader(fileName);
                BufferedReader in = new BufferedReader(fr);
                String str;
                List<String> list = new ArrayList<>();
                while ((str = in.readLine()) != null) {
                    list.add(str);
                }
                in.close();
                items.addAll(Variables.DeserializeItemList(list));

                if(!file.delete())
                {
                    String msg = Prefix + "Failed to delete the rewards file for " + uuid;
                    Bukkit.getLogger().warning(msg);
                }

                //TODO: Delete the file afterwards
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return items;
    }

    public void Claim(Player p){
        UUID uuid = p.getUniqueId();
        List<ItemStack> rewards = LoadRewards(uuid);

        if(Utilities.getFreeSlots(p.getInventory()) <= 0){
            p.sendMessage(Prefix + Formatting.getMessage("System.inventoryFull"));
            AddRewards(uuid, rewards);
            SaveRewards();
            return;
        }

        if(rewards.size() > 0){
            for(var i : rewards){
                ItemHandler.GivePlayer(uuid, i);
            }

            SaveRewards();
        }
        else p.sendMessage(Prefix + Formatting.getMessage("Tournament.noRewards"));



        //TODO: After claiming, tell player amount left
        //TODO: Possibly add on
    }

}
