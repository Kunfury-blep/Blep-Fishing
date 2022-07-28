package com.kunfury.blepFishing.Tournament;

import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Miscellaneous.ItemHandler;
import com.kunfury.blepFishing.Miscellaneous.Utilities;
import com.kunfury.blepFishing.Objects.FishObject;
import com.kunfury.blepFishing.Setup;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.kunfury.blepFishing.Config.Variables.Prefix;

public class Rewards {

    public static HashMap<UUID, List<ItemStack>> UnsavedRewards = new HashMap<>();
    List<OfflinePlayer> rewardedPlayers = new ArrayList<>();
    public void Generate(TournamentObject t){

        t.getWinners().forEach((rank, fish) -> {
            giveRewards(t.Rewards.get(String.valueOf(rank)), fish);
        });
        SaveRewards();

        //TODO: Ensure the default winners are rewarded
        List<String> defaultRewards = t.Rewards.get("DEFAULT");
        if(defaultRewards != null && defaultRewards.size() > 0){
            for(var p : t.getParticipants()){
                UUID uuid = p.getUniqueId();
                for(var r : defaultRewards){
                    ItemHandler.parseReward(r, uuid);
                }
            }
        }


//        if ("DEFAULT".equalsIgnoreCase(key)){
//            for(var f : caughtFish){
//                Player p = f.getPlayer();
//                if(rewardedPlayers.contains(p)) continue;
//                rewardedPlayers.add(p);
//                giveRewards(t.Rewards.get(key));
//
//            }
//        }
    }

    private void giveRewards(List<String> rewardStrs, FishObject fish){
        if(fish != null){
            rewardedPlayers.add(fish.getPlayer());
            UUID uuid = fish.getPlayerUUID();
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
        scheduler.runTaskAsynchronously(Setup.getPlugin(), () -> {
            UnsavedRewards.forEach((uuid, rewards) -> {
                try {
                Files.createDirectories(Paths.get(Setup.dataFolder + "/Rewards"));
                String fileName = Setup.dataFolder + "/Rewards/" + uuid + ".json";
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
            scheduler.runTask(Setup.getPlugin(), () -> {
                UnsavedRewards.forEach((uuid, rewards) -> {
                    Player p = Bukkit.getPlayer(uuid);
                    if(p != null){
                        p.sendMessage(Prefix + String.format(Variables.getMessage("claimRewards"), rewards.size()));
                        p.sendMessage(Variables.Prefix + Variables.getMessage("claimRewards2"));
                    }
                });
                UnsavedRewards.clear();
            });
        });
    }

    public List<ItemStack> LoadRewards(UUID uuid){
        List<ItemStack> items = new ArrayList<>();

        String fileName = Setup.dataFolder + "/Rewards/" + uuid + ".json";
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
            p.sendMessage(Prefix + Variables.getMessage("inventoryFull"));
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
        else p.sendMessage(Prefix + Variables.getMessage("noRewards"));



        //TODO: After claiming, tell player amount left
        //TODO: Possibly add on
    }

}
