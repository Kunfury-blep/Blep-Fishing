package Objects;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class FishObject implements Serializable, Comparable<FishObject>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2959331831404886148L;
	public String Name;
	public String Lore;
	public double MinSize;
	public double MaxSize;
	public double AvgSize;
	public int ModelData;
	public boolean IsRaining;
	public int Weight = 100;
	public double BaseCost = 1;
	public String Area;
	
	
	public List<String> OldBiomes;  //Depreciated, leaving in just until everyone gets moved over to new areas
	
	
	
	//Leaderboards Info
	public Double Score;
	public String PlayerName;
	public LocalDateTime DateCaught;
	public double RealSize;
	public String Rarity;
	public double RealCost;
	
	public static boolean isMade = true;
	
	
	public FishObject(String name, String lore, double minSize, double maxSize, 
			int modelData, List<String> biomes, boolean isRaining, double baseCost, String area){
		Name = name;
		Lore = lore;
		MinSize = minSize;
		MaxSize = maxSize;
		AvgSize = (minSize + maxSize)/2;
		ModelData = modelData;		
		IsRaining = isRaining;
		BaseCost = baseCost;
		Area = area;
		
		
		OldBiomes  = biomes;
	}
	
	//Figure out what this dose
	public FishObject weight(int _weight) {
		if(_weight > 0)
			Weight = _weight;
		else
			Weight = 100;
		return this;
	}
	
	@Override
    public int compareTo(FishObject r) {
        return this.Score.compareTo(r.Score);
    }
	
	///
	//Recalculates the score of the fish
	//Needed in order to rescore all fish after a config change
	///
	public double CalcScore(double adjWeight) {
		double _score = ((RealSize / MaxSize)/adjWeight) * 100;	
		
		return _score;
	}
	
}
