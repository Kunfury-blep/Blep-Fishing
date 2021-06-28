package com.kunfury.blepFishing.Miscellaneous;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Villagers implements Listener {
	/**
	 * checks if the entity is a villager and a fisherman
	 * @param e event variable
	 */
	@EventHandler
	public void VillagerCheck(PlayerInteractEntityEvent e) {
		Entity entity = e.getRightClicked();
		Player player = e.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();
		
		if(entity instanceof Villager) {
			Villager villager = (Villager)entity;
			if(villager.getProfession().equals(Villager.Profession.FISHERMAN) 
					&& item.getType() == Material.SALMON) {
				FishEconomy.SellFish(player, 1);
				e.setCancelled(true);
			}
			
		}
		if(Variables.AllowWanderingTraders && entity instanceof WanderingTrader && item.getType() == Material.SALMON){
			FishEconomy.SellFish(player, Variables.TraderMod);
			e.setCancelled(true);
		}
		
		
	}

}
