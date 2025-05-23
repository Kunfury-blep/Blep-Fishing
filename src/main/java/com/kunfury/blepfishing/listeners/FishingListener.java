package com.kunfury.blepfishing.listeners;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.helpers.TreasureHandler;
import com.kunfury.blepfishing.helpers.Utilities;
import com.kunfury.blepfishing.objects.equipment.FishBag;
import com.kunfury.blepfishing.objects.equipment.FishingJournal;
import com.kunfury.blepfishing.objects.equipment.FishingRod;
import com.kunfury.blepfishing.objects.treasure.TreasureType;
import com.kunfury.blepfishing.plugins.PluginHandler;
import com.kunfury.blepfishing.plugins.WorldGuardHandler;
import com.kunfury.blepfishing.ui.scoreboards.DisplayFishInfo;
import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.objects.*;
import com.kunfury.blepfishing.plugins.McMMO;
import com.sk89q.worldguard.WorldGuard;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
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
            if(McMMO.McMcMmoCanFish(e.getPlayer()))
                FishCaught(e);
        } , 1);
    }

    private void FishCaught(PlayerFishEvent e){
        Item item = (Item) e.getCaught();
        assert item != null;

        if(!fishMats.contains(item.getItemStack().getType()))
            return;

        Player player = e.getPlayer();
        Location hookLoc = item.getLocation();

        if(PluginHandler.HasWorldGuard() && !WorldGuardHandler.canFish(hookLoc))
            return;

        if(TreasureHandler.instance.TreasureCaught()){
            TreasureType treasureType = TreasureHandler.instance.GetTreasure();

            if(treasureType != null && treasureType.CanGenerate(player)){
                var treasureItem = treasureType.GetItem(e);
                if(treasureItem != null){ //Ensures a treasure item was found, gives a normal fish otherwise
                    item.setItemStack(treasureItem);
                    if(treasureType.Announce){
                        Utilities.Announce(Formatting.GetLanguageString("Treasure.announce")
                                .replace("{player}", player.getDisplayName())
                                .replace("{treasure}", treasureType.getFormattedName()));
                    }

                    return;
                }
            }
        }

        var allBlue = Database.AllBlues.InAllBlue(hookLoc);

        Rarity rarity = Rarity.GetRandom();
        if(rarity == null) return;

        FishType fishType = GetCaughtFishType(hookLoc, allBlue);

        if(fishType == null){
            Utilities.Severe("No Valid Fish Type Found");
            return;
        }

        Integer rodId = GetRodId(player);

        FishObject caughtFish = fishType.GenerateFish(rarity, e.getPlayer().getUniqueId(), rodId, allBlue);

        FishBag fishBag = FishBag.GetBag(player);
        if(fishBag != null){
            item.remove();
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, .33f, 1f);
            fishBag.AddFish(caughtFish);
            fishBag.UpdateBagItem();
        }else
            item.setItemStack(caughtFish.CreateItemStack());




        BlepFishing.stats_FishCaught++;
        DisplayFishInfo.ShowFish(caughtFish, player);
        TournamentObject.CheckWinning(caughtFish);

        if(rarity.Announce)
            AnnounceCatch(caughtFish);

        var playerId = player.getUniqueId();
        if(!Database.FishingJournals.HasJournal(playerId.toString())
                && Utilities.getFreeSlots(player.getInventory()) > 0){
            FishingJournal journal = new FishingJournal(playerId);
            Utilities.GiveItem(player, journal.GetItemStack(), false);
        }


    }

    private FishType GetCaughtFishType(Location hookLoc, boolean allBlue) {
        if(allBlue)
            return GetRandomFishType(FishType.GetAll().stream().toList(), hookLoc);

        if(PluginHandler.HasWorldGuard()){
            var fishTypes = WorldGuardHandler.GetFishTypes(hookLoc);
            if(!fishTypes.isEmpty())
                return GetRandomFishType(fishTypes, hookLoc);
        }

        var world = hookLoc.getWorld();
        assert world != null;

        List<FishType> availFish = new ArrayList<>();//Available fish to choose from
        List<FishingArea> fishingAreas = FishingArea.GetAvailableAreas(hookLoc); //Available areas to pull fish from

        int height = hookLoc.getBlockY();
        boolean isRaining = world.hasStorm();
        long time = world.getTime();
        boolean isNight = !(time < 12300 || time > 23850);

        for (var type : FishType.GetAll())
        {
            if(type.canCatch(isRaining, height, isNight, fishingAreas))
                availFish.add(type);
        }

        return GetRandomFishType(availFish, hookLoc);
    }

    private FishType GetRandomFishType(List<FishType> fishTypes, Location hookLoc){
        if(fishTypes.isEmpty()){
            Bukkit.getLogger().warning("No fish available for location: " + hookLoc);
            return null;
        }

        int rand = ThreadLocalRandom.current().nextInt(0, fishTypes.size());
        return fishTypes.get(rand);
    }

    private void AnnounceCatch(FishObject fish){
        Player player = fish.getCatchingPlayer().getPlayer();

        if(player == null)
            return;


        TextComponent textComponent = new TextComponent(Formatting.formatColor(Formatting.GetLanguageString("Fish.announce")
                .replace("{player}", player.getDisplayName())
                .replace("{rarity}", fish.getRarity().getFormattedName())
                .replace("{fish}", fish.getType().Name)));
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, fish.getHoverText()));


        Utilities.Announce(textComponent);

        Firework fw = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK_ROCKET);
        fw.detonate();
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
            Bukkit.getLogger().severe(Formatting.GetMessagePrefix() + player.getDisplayName() + " is fishing without a held rod.");
            return null;
        }

        FishingRod fishingRod;
        if(ItemHandler.hasTag(rodItem, ItemHandler.FishRodId)){
            int rodId = ItemHandler.getTagInt(rodItem, ItemHandler.FishRodId);
            fishingRod = Database.Rods.Get(rodId);
        }else
            fishingRod = FishingRod.InitialSetup(rodItem, player);


        if(fishingRod == null){
            Bukkit.getLogger().severe(Formatting.GetMessagePrefix() + "Null fishing rod.");
            return null;
        }
        fishingRod.UpdateRodItem(rodItem);
        return fishingRod.Id;
    }
}
