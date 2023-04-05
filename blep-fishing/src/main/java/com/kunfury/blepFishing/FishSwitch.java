package com.kunfury.blepFishing;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import com.kunfury.blepFishing.Config.ItemsConfig;
import com.kunfury.blepFishing.Endgame.AllBlueInfo;
import com.kunfury.blepFishing.Endgame.DangerEvents;
import com.kunfury.blepFishing.Endgame.TreasureHandler;
import com.kunfury.blepFishing.CollectionLog.CollectionHandler;
import com.kunfury.blepFishing.Crafting.Equipment.FishBag.BagInfo;
import com.kunfury.blepFishing.Crafting.Equipment.FishBag.UpdateBag;
import com.kunfury.blepFishing.Events.FishCaughtEvent;
import com.kunfury.blepFishing.Miscellaneous.BiomeHandler;
import com.kunfury.blepFishing.Objects.*;
import com.kunfury.blepFishing.Quests.QuestHandler;
import com.kunfury.blepFishing.Signs.FishSign;
import com.kunfury.blepFishing.Tournament.TournamentHandler;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Config.Variables;
//import io.netty.util.internal.ThreadLocalRandom;
import org.jetbrains.annotations.NotNull;

public class FishSwitch{
	private static final List<Material> itemList = Arrays.asList(Material.SALMON, Material.COD, Material.TROPICAL_FISH);


	public void FishHandler(@NotNull PlayerFishEvent e) {
		Player player = e.getPlayer();
		if(e.getState() != PlayerFishEvent.State.CAUGHT_FISH
				|| !(e.getCaught() instanceof Item item)
				|| !CanFish(item, player, e )) return;

		AllBlueObject allBlueObj = AllBlueInfo.GetAllBlue(item.getLocation());
		boolean allBlue = (allBlueObj != null);
		Location itemLoc = item.getLocation();

		ItemStack treasure = new TreasureHandler().Perform(player, itemLoc);
		if(treasure != null && treasure.getType() != Material.AIR){
			item.setItemStack(treasure);
			return;
		}

		BaseFishObject base = GetCaughtFish(item, allBlue);
		if(base == null || base.Name == null) return;

		//Rarity Selection
		RarityObject chosenRarity = RarityObject.GetRandom();

		FishObject fish = new FishObject(base, chosenRarity, e.getPlayer(), base.getSize(allBlue));

		//Calls the event
		FishCaughtEvent event = new FishCaughtEvent(item, fish, player);
		Bukkit.getServer().getPluginManager().callEvent(event);

		if(event.isCancelled()){
			return;
		}

		item.setItemStack(fish.GenerateItemStack());

		new DangerEvents().Trigger(player, item.getLocation());

		//Checks if the player has a fishing bag. Automatically adds the fish to it if so
		if(BlepFishing.configBase.getEnableFishBags() && player.getInventory().contains(ItemsConfig.BagMat)){
			for (var slot : player.getInventory())
			{
				if(slot != null && BagInfo.IsBag(slot) && NBTEditor.getBoolean(slot, "blep", "item", "fishBagAutoPickup") && !BagInfo.IsFull(slot)){
					fish.setBagID(BagInfo.getId(slot));
					item.remove();
					player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, .33f, 1f);
					new UpdateBag().IncreaseAmount(slot, player);
					break;
				}
			}
		}

		if(BlepFishing.configBase.getAnnounceLegendary() && chosenRarity.Weight <= Variables.RarityList.get(0).Weight){
			String message = Formatting.getMessage("System.announceCatch")
					.replace("{player}", player.getDisplayName())
					.replace("{rarity}", fish.Rarity)
					.replace("{fish}", fish.Name)
					.replace("{size}", Formatting.DoubleFormat(fish.RealSize));

			for(var s : Bukkit.getOnlinePlayers()){
				s.sendMessage(Variables.getPrefix() + Formatting.formatColor(message));
			}

			Firework fw = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
			fw.detonate();
		}

		new DisplayFishInfo().InitialDisplay(player, fish);

		Variables.AddToFishDict(fish);

		for(var a : TournamentHandler.ActiveTournaments){
			a.newCatch(fish, player);
		}

		new QuestHandler().UpdateFishQuest(player, fish);
		new CollectionHandler().CaughtFish(player, fish); //Adds the caught fish to the players collection

		new FishSign().UpdateSigns();

		if(allBlue) allBlueObj.RemoveFish(1, player);
	}

	/**
	 * Returns the fish to be caught. Checks against the possible areas
	 * @param item The item that was fished up
	 * @return
	 */
	private BaseFishObject GetCaughtFish(Item item, boolean allBlue) {
		Location iLoc = item.getLocation();

		List<BaseFishObject> availFish = new ArrayList<>(); //Available fish to choose from

		if(!allBlue) { //If in All Blue, skips the below testing and instead just returns whole list
			List<AreaObject> areas = AreaObject.GetAreas(iLoc); //Available areas to pull fish from
			int height = iLoc.getBlockY();
			boolean isRaining = Bukkit.getWorlds().get(0).hasStorm();
			long time = iLoc.getWorld().getTime();
			boolean isNight = !(time < 12300 || time > 23850);

			for (var bFish : Variables.BaseFishList)
			{
				if(bFish.canCatch(isRaining, height, isNight, areas))
					availFish.add(bFish);
			}
		} else availFish = Variables.BaseFishList;

		availFish.sort((fish1, fish2) -> {
			Integer newWeight1 = fish1.Weight;
			Integer newWeight2 = fish2.Weight;
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

	private boolean CanFish(Item item, Player player, PlayerFishEvent e) {

		if(BlepFishing.configBase.getAreaPermissions()){//Check Area
			String biomeName = new BiomeHandler().getBiomeName(e.getHook().getLocation());
			List<AreaObject> areas = new ArrayList<>();
			for(AreaObject a : Variables.AreaList) {
				if (a.Biomes.contains(biomeName)) areas.add(a);
			}

			if(areas.size() == 0) return false; //Makes sure the area exists

			for(AreaObject a : areas){
				if(!(player.hasPermission("bf.area.*") || player.hasPermission("bf.area." + a.Name)))
					return false;
			}
		}





		//&& (player.hasPermission("bf.area.*") || player.hasPermission("bf.area." + biomeName)))

		//Ensures the item caught is a fish
		if (!itemList.contains(item.getItemStack().getType()) || Objects.requireNonNull(item.getItemStack().getItemMeta()).hasCustomModelData()) return false;

		//Checks if the server is tournament only
		if(BlepFishing.configBase.getTournamentOnly() && (TournamentHandler.ActiveTournaments == null || TournamentHandler.ActiveTournaments.size() == 0)) return false;

		//Check for world permissions
		String world = e.getPlayer().getWorld().getName().toUpperCase();
		if(BlepFishing.configBase.getEnableWorldWhitelist() && !BlepFishing.configBase.getAllowedWorlds().contains(world)) return false;

		var l = item.getLocation();

		//if(PluginHandler.hasWorldGuard) PluginHandler.CheckWorldGuard(new Location(BukkitAdapter.adapt(l.getWorld()), l.getX(), l.getY(), l.getZ()), player);

		return true;
	}
}
