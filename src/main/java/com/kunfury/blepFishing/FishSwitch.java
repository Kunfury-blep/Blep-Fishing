package com.kunfury.blepFishing;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import com.kunfury.blepFishing.AllBlue.AllBlueInfo;
import com.kunfury.blepFishing.AllBlue.AllBlueVars;
import com.kunfury.blepFishing.AllBlue.DangerEvents;
import com.kunfury.blepFishing.AllBlue.TreasureHandler;
import com.kunfury.blepFishing.Crafting.Equipment.FishBag.BagInfo;
import com.kunfury.blepFishing.Crafting.Equipment.FishBag.UpdateBag;
import com.kunfury.blepFishing.Objects.*;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Miscellaneous.Variables;
//import io.netty.util.internal.ThreadLocalRandom;
import org.jetbrains.annotations.NotNull;

public class FishSwitch{
	private static final List<Material> itemList = Arrays.asList(Material.SALMON, Material.COD, Material.TROPICAL_FISH);


	public void FishHandler(@NotNull PlayerFishEvent e) {
		if(e.getState() != PlayerFishEvent.State.CAUGHT_FISH) return;
	    if(e.getCaught() instanceof Item){
			Item item = (Item) e.getCaught();
			Player player = e.getPlayer();

			if(!CanFish(item, player, e )) return;
			AllBlueObject allBlueObj = AllBlueInfo.GetAllBlue(item.getLocation());
			boolean allBlue = (allBlueObj != null);

			if(new TreasureHandler().TreasureCheck()){
				ItemStack treasure = new TreasureHandler().GenerateTreasure(player, item.getLocation());
				if(treasure != null && treasure.getType() != Material.AIR){
					item.setItemStack(treasure);
					return;
				}
			}

			BaseFishObject base = GetCaughtFish(item);
			if(base == null || base.Name == null) return;

			//Rarity Selection
			int randR = ThreadLocalRandom.current().nextInt(0, Variables.RarityTotalWeight);
			RarityObject chosenRarity = Variables.RarityList.get(0);
			for(final RarityObject rarity : Variables.RarityList) {
				if(randR <= rarity.Weight) {
					chosenRarity = rarity;
					break;
				}else
					randR -= rarity.Weight;
			}



			FishObject fish = new FishObject(base, chosenRarity, e.getPlayer().getName(), base.getSize(allBlue));

			item.setItemStack(fish.GenerateItemStack());

			//Checks if the player has a fishing bag. Automatically adds the fish to it if so
			for (var slot : player.getInventory())
			{
				if (slot != null && slot.getType().equals(Material.HEART_OF_THE_SEA)
						&& NBTEditor.getBoolean(slot, "blep", "item", "fishBagAutoPickup" ) && !BagInfo.IsFull(slot))
				{
					String bagId = NBTEditor.getString(slot, "blep", "item", "fishBagId" );
					fish.BagID = bagId;
					item.remove();
					player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, .33f, 1f);
					new UpdateBag().Update(slot, player);
					break;
				}
			}

			//Broadcasts if the player catches the rarest fish possible
			if(chosenRarity.Weight <= Variables.RarityList.get(0).Weight) {
				if(Variables.LegendaryFishAnnounce){
					Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
							player.getDisplayName() + " just caught a " + fish.Rarity + " "
									+ fish.Name + " &fthat was " + Formatting.DoubleFormat(fish.RealSize) + "\" long!"));
				}
				Firework fw = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
				fw.detonate();
			}
			new DisplayFishInfo().InitialDisplay(player, fish);

			CheckAgainstTournaments(fish);
			Variables.AddToFishDict(fish);

			if(allBlue) allBlueObj.RemoveFish(1, player);
			new DangerEvents().Trigger(player, item.getLocation()); //TODO: Pass if in All Blue or not
		}

	}


	/**
	 * Returns the fish to be caught. Checks against the possible areas
	 * @param item The item that was fished up
	 * @return
	 */
	private BaseFishObject GetCaughtFish(Item item) {
		Location iLoc = item.getLocation();

		List<BaseFishObject> availFish = Variables.BaseFishList; //Available fish to choose from


		if(!AllBlueInfo.InAllBlue(iLoc)) { //If in All Blue, skips the below testing and instead just returns whole list

			List<AreaObject> areas = AreaObject.GetArea(iLoc); //Available areas to pull fish from
			int height = iLoc.getBlockY();
			Iterator iter = availFish.iterator();

			if (!Bukkit.getWorlds().get(0).hasStorm()) {
				while (iter.hasNext()) {
					BaseFishObject bFish = (BaseFishObject) iter.next();
					if (bFish.RequiresRain) iter.remove();
					else if (bFish.MaxHeight >= height && bFish.MinHeight <= height) {
						areas.forEach(a -> {
							if (a.Name.equals(bFish.Area)) iter.remove();
						});
					}
				}
			}
		}
		availFish.sort((o1, o2) -> {
			Integer newWeight1 = o1.Weight;
			Integer newWeight2 = o2.Weight;
			return (newWeight1).compareTo(newWeight2);
		});


		//Get fish where height matches.

		BaseFishObject base = null;
		if (availFish.size() > 0) {
			int rand = ThreadLocalRandom.current().nextInt(0, availFish.size());
			base = availFish.get(rand);
		}
		return base;
	}

	private void CheckAgainstTournaments(FishObject fish){
		boolean isTop = false;
		if(!Variables.TournamentRunning) return;
		for (TournamentObject t : Variables.Tournaments){
			if(!t.HasFinished
			&& (t.FishName.equalsIgnoreCase("ALL") || t.FishName.equalsIgnoreCase(fish.Name))){
				List<FishObject> winners = t.GetWinners();
				if(winners != null && winners.size() > 0){
					FishObject winner = winners.get(0);
					if(winner != null && fish.Score > winner.Score) isTop = true;
				}else{
					isTop = true;
				}

				if(isTop){
					String lbString = net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&' ,
									Variables.Prefix +
									fish.PlayerName + " took the top spot of the tournament with a " + fish.Rarity + " " + fish.Name + "!");
					TextComponent mainComponent = new TextComponent (lbString);
					mainComponent.setHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT, fish.GetHoverText()));

					for(Player p : Bukkit.getOnlinePlayers()) {
						p.spigot().sendMessage(mainComponent);
					}
				}
			}
		}
	}

	private boolean CanFish(Item item, Player player, PlayerFishEvent e)
	{
		String world = e.getPlayer().getWorld().getName().toUpperCase();

		//Check Area
		String biomeName = item.getLocation().getBlock().getBiome().name(); //change to location of hook
		List<AreaObject> areas = new ArrayList<>();
		for(AreaObject a : Variables.AreaList) {
			if (a.Biomes.contains(biomeName)) areas.add(a);
		}

		if(areas.size() <= 0) return false; //Makes sure the area exists
		else if(Variables.RequireAreaPerm){ //Checks for area permissions
			for(AreaObject a : areas){
				if(!(player.hasPermission("bf.area.*") || player.hasPermission("bf.area." + a.Name)))
					return false;
			}
		}

		//&& (player.hasPermission("bf.area.*") || player.hasPermission("bf.area." + biomeName)))

		//Ensures the item caught is a fish
		if (!itemList.contains(item.getItemStack().getType()) || Objects.requireNonNull(item.getItemStack().getItemMeta()).hasCustomModelData()) return false;

		//Checks if the server is tournament only
		if(Variables.TournamentOnly && !Variables.TournamentRunning) return false;

		//Check for world permissions
		if(Variables.WorldsWhitelist && !Variables.AllowedWorlds.contains(world)) return false;

		var l = item.getLocation();

		//if(PluginHandler.hasWorldGuard) PluginHandler.CheckWorldGuard(new Location(BukkitAdapter.adapt(l.getWorld()), l.getX(), l.getY(), l.getZ()), player);

		return true;
	}

}
