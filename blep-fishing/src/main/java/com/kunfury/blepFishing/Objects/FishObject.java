package com.kunfury.blepFishing.Objects;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Setup;
//import com.mysql.fabric.xmlrpc.base.Value;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import net.md_5.bungee.api.ChatColor;

import com.kunfury.blepFishing.Config.Variables;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FishObject implements Serializable, Comparable<FishObject>{
	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = -2959331831404886148L;
	//Leaderboards Info
	public final String Name;
	public String Rarity;
	public String PlayerName;
	private String playerUUID;
	public LocalDateTime DateCaught;
	public Double RealSize;
	
	public Double Score;

	private String BagID;
	public String FishID;

	public double RealCost;


	/**
	 * Base Constructor
	 * @param base = The Base Fish to be generated from
	 * @param rarity = The rarity of the fish
	 * @param _player = The player who caught the fish
	 * @param _size = The length of the fish
	 */
	public FishObject(BaseFishObject base, RarityObject rarity, Player _player, Double _size){
		Name = base.Name;
		Rarity = rarity.Name;
		PlayerName = _player.getDisplayName();
		playerUUID = _player.getUniqueId().toString();
		DateCaught = LocalDateTime.now();
		RealSize = _size;
		
		Score = CalcScore(base, rarity);
		RealCost = CalcPrice(base, rarity);
	}


	public FishObject(BaseFishObject base, Player _player){
		Name = base.Name;
		PlayerName = _player.getDisplayName();
		playerUUID = _player.getUniqueId().toString();
		DateCaught = LocalDateTime.now();
		RealSize = base.getSize(false);

		RarityObject rarity = RarityObject.GetRandom();
		Rarity = rarity.Name;
		Score = CalcScore(base, rarity);
		RealCost = CalcPrice(base, rarity);

	}

	/**
	 * Recalculates the score of the fish
	 * Needed in order to rescore all fish after a config change
	 * @param r is the fishobject
	 * @return the new cost of the fish
	 */
	@Override
    public int compareTo(FishObject r) {
        return this.Score.compareTo(r.Score);
    }

	private double CalcScore(BaseFishObject base, RarityObject rarity) {
		double adjWeight = rarity.Weight;
        if(Variables.RarityList.get(0).Weight != 1)
        	adjWeight = adjWeight / Variables.RarityList.get(0).Weight;

		return ((RealSize / base.MaxSize)/adjWeight) * 100;
	}
	
	private double CalcPrice(BaseFishObject base, RarityObject rarity) {
		double sizeMod = RealSize/base.AvgSize;

		double realCost = (base.BaseCost * sizeMod) * rarity.PriceMod;

		return Formatting.round(realCost, 2);
	}

	public Text GetHoverText(){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

		String content = Rarity + " " + Name +
				"&f\nFish Size: " + Formatting.DoubleFormat(RealSize) +
				//"\nRank: " + (i) +
				"\nCaught On: " +  formatter.format(DateCaught)  +
				"\nScore: " + Formatting.DoubleFormat(Score);

		if(Setup.econEnabled) content += "\nValue: "  + Variables.CurrSym + Formatting.DoubleFormat(RealCost);

		return new Text(ChatColor.translateAlternateColorCodes('&' , content));
	}



	public double GetScore(){return Score;}

	public String GetFishId(){
		if(FishID == null || FishID.isEmpty()){ //Needed to make previously caught fish compatible
			FishID = UUID.randomUUID().toString();
			Variables.UpdateFishData();
		}

		return FishID;
	}

	public String GetSize(){
		return String.valueOf(Formatting.round(RealSize, 2));
	}

	public ItemStack GenerateItemStack(){
		BaseFishObject base = BaseFishObject.GetBase(Name);
		ItemStack fishItem = new ItemStack(Material.SALMON, 1);

		ItemMeta m = fishItem.getItemMeta();
		m.setLore(CreateLore(base));
		m.setCustomModelData(base.ModelData);

		RarityObject rarity = RarityObject.GetRarity(Rarity);

		String dName = base.Name;
		if(rarity != null)
			dName = '&' + rarity.Prefix + dName;

		m.setDisplayName(Formatting.formatColor(dName));
		fishItem.setItemMeta(m);

		fishItem = NBTEditor.set( fishItem, RealCost, "blep", "item", "fishValue" );
		fishItem = NBTEditor.set( fishItem, GetFishId(), "blep", "item", "fishId" );

		return fishItem;
	}

	/**
	 * Creates a lore for the fish that has been catched
	 * @param base The template of the fish
	 * @return the lore
	 */
	private List<String> CreateLore(BaseFishObject base){
		List<String> Lore = new ArrayList<>();
		Lore.add(base.Lore);

		if(Setup.econEnabled) //Checks that an economy is installed
			Lore.add("&2Value: " + Variables.CurrSym + Formatting.DoubleFormat(RealCost));
		Lore.add("&8Length: " + Formatting.DoubleFormat(RealSize) + "in.");

		LocalDateTime now = LocalDateTime.now();
		String details = ("&8Caught By: " + PlayerName + " on " + now.toLocalDate());
		Lore.add(details);

		List<String> colorLore = new ArrayList<>();
		for (String line : Lore){
			if(line != null)colorLore.add(org.bukkit.ChatColor.translateAlternateColorCodes('&', line));
		}



		return colorLore;
	}

	public OfflinePlayer getPlayer(){
		return Bukkit.getOfflinePlayer(UUID.fromString(playerUUID));
	}

	public UUID getPlayerUUID(){
		return UUID.fromString(playerUUID);
	}

	public String getBagID(){ return BagID;}

	public void setBagID(String _bagId){
		BagID = _bagId;
	}
}
