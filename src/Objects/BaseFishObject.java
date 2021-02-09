package Objects;

import java.io.Serializable;

public class BaseFishObject implements Serializable{

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
	
	public BaseFishObject(String name, String lore, double minSize, double maxSize, 
			int modelData, boolean isRaining, double baseCost, String area){
		Name = name;
		Lore = lore;
		MinSize = minSize;
		MaxSize = maxSize;
		AvgSize = (minSize + maxSize)/2;
		ModelData = modelData;		
		IsRaining = isRaining;
		BaseCost = baseCost;
		Area = area;
	}
	
	//Figure out what this dose
	public BaseFishObject weight(int _weight) {
		if(_weight > 0)
			Weight = _weight;
		else
			Weight = 100;
		return this;
	}
	
}
