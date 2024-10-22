package com.kunfury.blepfishing.listeners;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.helpers.Utilities;
import com.kunfury.blepfishing.objects.equipment.FishingJournal;
import com.kunfury.blepfishing.objects.treasure.TreasureType;
import com.kunfury.blepfishing.ui.panels.player.PlayerPanel;
import com.kunfury.blepfishing.items.ItemHandler;
import com.kunfury.blepfishing.items.recipes.TournamentHornRecipe;
import com.kunfury.blepfishing.objects.equipment.FishBag;
import com.kunfury.blepfishing.objects.TournamentType;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void playerInteractEvent(PlayerInteractEvent e){
        Player p = e.getPlayer();
        Action a = e.getAction();
        ItemStack i = e.getItem();

        if(i == null)
            return;


        if((a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK)){
            RightClickItem(e);
            return;
        }

        if((a == Action.LEFT_CLICK_AIR || a == Action.LEFT_CLICK_BLOCK)){
            LeftClickItem(e);
            return;
        }
    }

    private void RightClickItem(PlayerInteractEvent e){
        ItemStack item = e.getItem();
        assert item != null;
        Player player = e.getPlayer();

        if(!item.hasItemMeta()) return;
        ItemMeta meta = item.getItemMeta();
        assert meta != null;

        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();

        if(dataContainer.isEmpty()){
            return;
        }

//        if(CompassPiece.isCompass(item)){
//            Bukkit.broadcastMessage("Using Compass");
//
//            CompassMeta cMeta = (CompassMeta) meta;
//
//            Location cLoc = cMeta.getLodestone();
//            cLoc.setY(player.getLocation().getY());
//            Location pLoc = player.getLocation();
//
//            if(cLoc.distance(pLoc) > 500){
//                player.teleport(cLoc);
//            }else{
//                Database.AllBlues.InAllBlue(player.getLocation());
//            }
//        }

        if(FishBag.IsBag(item)){
            if(player.isSneaking()){
                new PlayerPanel().Show(player);
                player.playSound(player.getLocation(), Sound.ITEM_BUCKET_EMPTY_FISH, .3f, 1f);
                return;
            }

            FishBag fishBag = FishBag.GetBag(item);
            if(fishBag == null) return;
            fishBag.Use(player);
            return;
        }

        if(FishingJournal.IsJournal(item)){
            if(player.isSneaking()){
                e.setCancelled(true);
                new PlayerPanel().Show(player);
                player.playSound(player.getLocation(), Sound.ITEM_BUCKET_EMPTY_FISH, .3f, 1f);
                return;
            }

            FishingJournal journal = FishingJournal.Get(item);

            if(journal == null || e.getHand() == null)
                return;

            if(Objects.equals(player.getInventory().getItem(e.getHand()), item))
                player.getInventory().setItem(e.getHand(), journal.GetItemStack());
            else
                Utilities.Severe("Error confirming item in hand");
            return;
        }

        if(TreasureType.IsTreasure(item)){
            e.setCancelled(true);
            TreasureType.UseItem(item, player);
            return;
        }

        if(item.getType() == Material.GOAT_HORN && dataContainer.has(ItemHandler.TourneyTypeId)){
            var typeId = dataContainer.get(ItemHandler.TourneyTypeId, PersistentDataType.STRING);
            TournamentType type = TournamentType.FromId(typeId);
            if(type.Start() != null){
                player.playSound(player.getLocation(), Sound.BLOCK_SCULK_BREAK, 1f, 1);
                item.setAmount(item.getAmount() - 1);
                return;
            }

            player.sendMessage(Formatting.GetFormattedMessage("Tournament.alreadyRunning")
                    .replace("{tournament", type.Name));
            return;
        }
    }

    private void LeftClickItem(PlayerInteractEvent e){
        Player p = e.getPlayer();
        ItemStack item = e.getItem();
        assert item != null;

        if(FishBag.IsBag(item)){
            e.setCancelled(true);
            FishBag fishBag = FishBag.GetBag(item);
            if(fishBag == null) return;

            if(p.isSneaking())
                fishBag.FillFromInventory(e.getItem(), p);
            else
                fishBag.TogglePickup(e.getItem(), p);
            return;
        }
    }

    @EventHandler
    public void OpenVillager(PlayerInteractEntityEvent e){
        if(e.getRightClicked().getType() != EntityType.VILLAGER)
            return;

        Villager villager = (Villager) e.getRightClicked();
        Player player = e.getPlayer();

        if(villager.getProfession() != Villager.Profession.FISHERMAN)
            return;

        if(BlepFishing.hasEconomy()){
            ItemStack item = player.getInventory().getItemInMainHand();

            if(item.getType() == Material.SALMON && ItemHandler.hasTag(item, ItemHandler.FishIdKey)){
                if(player.isSneaking())
                    Utilities.SellAllFish(player);
                else
                    Utilities.SellFish(player);

                e.setCancelled(true);
                return;
            }
        }

        TournamentHornRecipe.UpdateFishermanTrades(villager);
    }
}
