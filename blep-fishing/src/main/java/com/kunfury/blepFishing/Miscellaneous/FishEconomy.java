package com.kunfury.blepFishing.Miscellaneous;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.kunfury.blepFishing.Config.FileHandler;
import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Crafting.Equipment.FishBag.BagInfo;
import com.kunfury.blepFishing.Crafting.Equipment.FishBag.ParseFish;
import com.kunfury.blepFishing.Crafting.Equipment.FishBag.UpdateBag;
import com.kunfury.blepFishing.Objects.FishObject;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.kunfury.blepFishing.BlepFishing;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class FishEconomy {

	protected static List<String> PlayerWaitList = new ArrayList<>();
	//Handles the selling of fish
	public static void SellFish(Player p, double priceMod) {
		if(p.isSneaking()) {
			if(PlayerWaitList.contains(p.toString())) {
				SellFish(p, true, priceMod);
				PlayerWaitList.remove(p.toString());
			}else {
				PlayerWaitList.add(p.toString());
				p.sendMessage(Formatting.getFormattedMesage("Economy.confirmSellAll"));
			}
		}else
			SellFish(p, false, priceMod);
	}


	public static void SellFish(Player p, boolean sellAll, double priceMod) {
		Economy econ = BlepFishing.getEconomy();
		if(econ != null){
			List<ItemStack> itemList = new ArrayList<>();
			if(sellAll) { //Runs if the player is wanting to sell all fish
				for (ItemStack item : p.getInventory().getContents()) {
					if(item != null && item.getType() == Material.SALMON) {
						itemList.add(item);
					}
				}
			}else
				itemList.add(p.getInventory().getItemInMainHand());

			double total = 0;

			//Adds the cost of all the fish together.
			for (ItemStack item : itemList)
			{

				double value = NBTEditor.getDouble( item, "blep", "item", "fishValue" );
				value *= priceMod;
				total += value;
			}

			if(total > 0) {
				EconomyResponse r = econ.depositPlayer(p, total);
				if(r.transactionSuccess()) {
					if(itemList.size() == 1){
						ItemStack item = itemList.get(0);

						p.sendMessage(Formatting.getFormattedMesage("Economy.singleSale")
								.replace("{fish}", Objects.requireNonNull(item.getItemMeta()).getDisplayName())
								.replace("{total}", BlepFishing.getEconomy().format(total)));
						item.setAmount(item.getAmount() - 1);
					}else{
						p.sendMessage(Formatting.getFormattedMesage("Economy.finishSale")
								.replace("{amount}", String.valueOf(itemList.size()))
								.replace("{total}", BlepFishing.getEconomy().format(total)));
						for(var i : itemList){
							i.setAmount(0);
						}
					}
				} else {
					p.sendMessage(String.format("An error occured: %s", r.errorMessage));
				}
			}else p.sendMessage(Formatting.getFormattedMesage("Economy.noValue"));
		}
	}

	public static void SellFish(List<FishObject> fishList, Player p, double priceMod, ItemStack bag){
		Economy econ = BlepFishing.getEconomy();
		if(econ == null) return;
		double total = 0;

		//Adds the cost of all the fish together.
		for (var f : fishList)
		{
			total += f.RealCost * priceMod;
		}

		if(total > 0) {
			EconomyResponse r = econ.depositPlayer(p, total);
			if(r.transactionSuccess()) {
				p.sendMessage(Formatting.getFormattedMesage("Economy.finishSale")
						.replace("{amount}", String.valueOf(fishList.size()))
						.replace("{total}", BlepFishing.getEconomy().format(total)));
				for(var f : fishList){
					f.setBagID(null);
				}
				new UpdateBag().Update(bag, p, false);
				//Variables.UpdateFishData();
				FileHandler.FishData = true;
			} else {
				p.sendMessage(String.format("An error occured: %s", r.errorMessage));
			}
		}else p.sendMessage(Formatting.getFormattedMesage("Economy.noValue"));
	}

	public static void SellBag(Player p, ItemStack bag, double priceMod){
		if(!p.isSneaking()){
			p.sendMessage(Formatting.getFormattedMesage("Economy.sellBagHint"));
			return;
		}

		if(!PlayerWaitList.contains(p.toString())) {
			PlayerWaitList.add(p.toString());
			p.sendMessage(Formatting.getFormattedMesage("Economy.sellBagConfirm"));
			return;
		}

		String bagId = BagInfo.getId(bag);
		final List<FishObject> tempFish = new ParseFish().RetrieveFish(bagId, "ALL");
		SellFish(tempFish, p, priceMod, bag);
		PlayerWaitList.remove(p.toString());
	}

}



