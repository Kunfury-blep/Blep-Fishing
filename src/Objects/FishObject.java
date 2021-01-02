package Objects;

import java.io.Serializable;
import java.time.LocalDateTime;

import Miscellaneous.Variables;

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
	
	@Override
    public int compareTo(FishObject r) {
        return this.Score.compareTo(r.Score);
    }
	
	///
	//Recalculates the score of the fish
	//Needed in order to rescore all fish after a config change
	///
	private double CalcScore(BaseFishObject base, RarityObject rarity) {
		double adjWeight = rarity.Weight;
        if(Variables.RarityList.get(0).Weight != 1)
        	adjWeight = adjWeight / Variables.RarityList.get(0).Weight;
		
		double _score = ((RealSize / base.MaxSize)/adjWeight) * 100;	
		
		return _score;
	}
	
	private double CalcPrice(BaseFishObject base, RarityObject rarity) {
		double sizeMod = RealSize/base.AvgSize;
		
		double realCost = (base.BaseCost * sizeMod) * rarity.PriceMod;
		
		return realCost;
	}

	
}
