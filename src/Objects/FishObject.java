package Objects;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import Miscellaneous.Formatting;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

import Miscellaneous.Variables;
import net.md_5.bungee.api.chat.hover.content.Text;

public class FishObject implements Serializable, Comparable<FishObject>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2959331831404886148L;
	//Leaderboards Info
	public String Name;
	public String Rarity;
	public String PlayerName;
	public LocalDateTime DateCaught;
	public double RealSize;
	
	public Double Score;
	
	
	public double RealCost;
	
	public static boolean isMade = true;
	
	
	public FishObject(BaseFishObject base, RarityObject rarity, String _playerName, Double _size){
		Name = base.Name;
		Rarity = rarity.Name;
		PlayerName = _playerName;
		DateCaught = LocalDateTime.now();
		RealSize = _size;
		
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

		return (base.BaseCost * sizeMod) * rarity.PriceMod;
	}

	public Text GetHoverText(){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		return new Text(ChatColor.translateAlternateColorCodes('&' ,(Rarity + " " + Name +
				"&f\nFish Size: " + Formatting.DoubleFormat(RealSize) +
				//"\nRank: " + (i) +
				"\nCaught On: " +  formatter.format(DateCaught)  +
				"\nScore: " + Formatting.DoubleFormat(Score)
		)));
	}
}
