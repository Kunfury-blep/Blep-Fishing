package Miscellaneous;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import Objects.AreaObject;
import Objects.FishObject;
import Objects.RarityObject;

public class Variables {

	public static List<FishObject> FishList = new ArrayList<>();
	public static List<RarityObject> RarityList = new ArrayList<>();
	public static List<AreaObject> AreaList = new ArrayList<>();
	
	public static List<FishObject> CaughtFish = new ArrayList<>();
	
	public static Hashtable<String, List<FishObject>> FishDict = new Hashtable<String,  List<FishObject>>(); 
	
	public static int RarityTotalWeight;
	public static int FishTotalWeight;
	
	
	public static void AddToFishDict(FishObject f) {
		if(FishDict.get(f.Name) == null) {
    		List<FishObject> temp = new ArrayList<>();
    		FishDict.put(f.Name, temp);
    	}
    	FishDict.get(f.Name).add(f);
	}
	
}
