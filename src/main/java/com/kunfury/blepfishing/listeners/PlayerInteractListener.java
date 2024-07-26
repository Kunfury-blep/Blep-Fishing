package com.kunfury.blepfishing.listeners;

import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.ui.panels.player.PlayerPanel;
import com.kunfury.blepfishing.items.ItemHandler;
import com.kunfury.blepfishing.items.recipes.TournamentHornRecipe;
import com.kunfury.blepfishing.objects.FishBag;
import com.kunfury.blepfishing.objects.FishingRod;
import com.kunfury.blepfishing.objects.TournamentType;
import org.bukkit.Bukkit;
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

        if(FishBag.IsBag(item)){
            FishBag fishBag = FishBag.GetBag(item);
            if(fishBag == null) return;
            fishBag.Use(player);
            return;
        }

        if(item.getType() == Material.GOAT_HORN && dataContainer.has(ItemHandler.TourneyTypeId)){
            var typeId = dataContainer.get(ItemHandler.TourneyTypeId, PersistentDataType.STRING);
            TournamentType type = TournamentType.FromId(typeId);
            if(type.Start()){
                player.playSound(player.getLocation(), Sound.BLOCK_SCULK_BREAK, 1f, 1);
                item.setAmount(item.getAmount() - 1);
                return;
            }

            var running = Formatting.getFormattedMessage("Tournament.alreadyRunning");
            running = running.replace("{tournament}", type.Name);
            player.sendMessage(running);
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

        //Fishing rod triggers both left and right click on cast
//        if(p.isSneaking() && FishingRod.IsRod(item)){
//            e.setCancelled(true);
//            new PlayerPanel().Show(p);
//        }

    }

    @EventHandler
    public void OpenVillager(PlayerInteractEntityEvent e){
        if(e.getRightClicked().getType() != EntityType.VILLAGER)
            return;

        Villager villager = (Villager) e.getRightClicked();

        if(villager.getProfession() != Villager.Profession.FISHERMAN)
            return;

        TournamentHornRecipe.UpdateFishermanTrades(villager);
    }
}