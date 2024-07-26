package com.kunfury.blepfishing.listeners;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.helpers.TreasureHandler;
import com.kunfury.blepfishing.ui.scoreboards.DisplayFishInfo;
import com.kunfury.blepfishing.items.ItemHandler;
import com.kunfury.blepfishing.objects.*;
import com.kunfury.blepfishing.plugins.McMMO;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class FishingListener implements Listener {

    private static final List<Material> fishMats = Arrays.asList(Material.SALMON, Material.COD, Material.TROPICAL_FISH);


    @EventHandler
    public void onFish(PlayerFishEvent e) {
       if(e.getState() != PlayerFishEvent.State.CAUGHT_FISH
       || !(e.getCaught() instanceof Item)){
           return;
       }

        Bukkit.getScheduler().runTaskLater (BlepFishing.getPlugin(), () ->{
            if(McMMO.McMcMmoCanFish(e.getPlayer())) FishCaught(e);
        } , 1);
    }

    private void FishCaught(PlayerFishEvent e){
        Item item = (Item) e.getCaught();
        assert item != null;

        if(!fishMats.contains(item.getItemStack().getType()))
            return;

        Player player = e.getPlayer();

        if(TreasureHandler.instance.TreasureCaught()){
            Bukkit.broadcastMessage("Treasure caught!");

            ItemStack treasureItem = TreasureHandler.instance.GetTreasureItem();
            item.setItemStack(treasureItem);
            return;
        }

        FishType fishType = GetCaughtFishType(item.getLocation());

        Rarity rarity = Rarity.GetRandom();
        if(rarity == null) return;



        //TODO: Get Rod and Bag ID before creating fishObject
        Integer rodId = GetRodId(player);
        Integer bagId = null;
        FishBag fishBag = null;
        ItemStack bagItem = null;


        //Checks if the player has a fishing bag. Automatically adds the fish to it if so
        if(ConfigHandler.instance.baseConfig.getEnableFishBags() && player.getInventory().contains(ItemHandler.BagMat)){
            Inventory inv = player.getInventory();
            for (var slot : inv)
            {
                FishBag bag = FishBag.GetBag(slot);
                if(bag != null && bag.Pickup && !bag.isFull()){
                    fishBag = bag;
                    bagId = bag.Id;
                    bagItem = slot;
                    break;
                }
            }
        }




        FishObject caughtFish = fishType.GenerateFish(rarity, e.getPlayer().getUniqueId(), rodId, bagId);

        item.setItemStack(caughtFish.CreateItemStack());
        BlepFishing.stats_FishCaught++;
        DisplayFishInfo.ShowFish(caughtFish, player);
        if(rarity.Announce)
            AnnounceCatch(caughtFish);

        if(fishBag != null){
            item.remove();
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, .33f, 1f);
            fishBag.NeedsRefresh = true;
            fishBag.UpdateBagItem(bagItem);
        }

    }

    private FishType GetCaughtFishType(Location iLoc) {
        List<FishType> availFish = new ArrayList<>();//Available fish to choose from

        var world = iLoc.getWorld();
        assert world != null;

        var biome = iLoc.getBlock().getBiome();

        List<FishingArea> fishingAreas = FishingArea.GetAvailableAreas(biome.toString()); //Available areas to pull fish from

        int height = iLoc.getBlockY();
        boolean isRaining = world.hasStorm();
        long time = world.getTime();
        boolean isNight = !(time < 12300 || time > 23850);

        for (var type : FishType.GetAll())
        {
            if(type.canCatch(isRaining, height, isNight, fishingAreas))
                availFish.add(type);
        }

        //Get fish where height matches.

        if(availFish.isEmpty()){
            Bukkit.getLogger().warning("No fish available for biome: " + biome);
            return null;
        }

        int rand = ThreadLocalRandom.current().nextInt(0, availFish.size());
        return availFish.get(rand);
    }

    private void AnnounceCatch(FishObject fish){
        Player p = fish.getCatchingPlayer();
        Rarity rarity = fish.getRarity();
        for(var s : Bukkit.getOnlinePlayers()){
            s.sendMessage(Formatting.formatColor(
                    p.getDisplayName() + " just caught a " + rarity.Prefix + fish.getRarity().Name + " " + fish.getType().Name + ChatColor.WHITE + "!"));
        }

        if(Bukkit.getServer().getVersion().contains("1.20.6")){
            Firework fw = (Firework) p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK_ROCKET);
            fw.detonate();
        }
    }

    private Integer GetRodId(Player player){
        ItemStack rodItem;
        PlayerInventory pInv = player.getInventory();

        var mainHand = pInv.getItemInMainHand();
        var offHand = pInv.getItemInOffHand();

        if(mainHand.getType() == Material.FISHING_ROD){
            rodItem = mainHand;
        }else if(offHand.getType() == Material.FISHING_ROD){
            rodItem = offHand;
        }else{
            Bukkit.getLogger().severe(Formatting.getPrefix() + player.getDisplayName() + " is fishing without a held rod.");
            return null;
        }

        FishingRod fishingRod;
        if(ItemHandler.hasTag(rodItem, ItemHandler.FishRodId)){
            int rodId = ItemHandler.getTagInt(rodItem, ItemHandler.FishRodId);
            fishingRod = Database.Rods.Get(rodId);
        }else
            fishingRod = FishingRod.InitialSetup(rodItem, player);


        if(fishingRod == null){
            Bukkit.getLogger().severe(Formatting.getPrefix() + "Null fishing rod.");
            return null;
        }
        fishingRod.UpdateRodItem(rodItem);
        return fishingRod.Id;
    }
}