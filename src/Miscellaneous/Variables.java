package Miscellaneous;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.kunfury.blepFishing.Setup;
import com.kunfury.blepFishing.Signs.FishSign;

import Objects.AreaObject;
import Objects.BaseFishObject;
import Objects.FishObject;
import Objects.RarityObject;

public class Variables {

	public static List<BaseFishObject> BaseFishList = new ArrayList<>(); //Available fish to catch
	public static List<RarityObject> RarityList = new ArrayList<>(); //Available rarities
	public static List<AreaObject> AreaList = new ArrayList<>(); //Available Areas
	
	public static List<FishObject> CaughtFish = new ArrayList<>(); //Fish that have been caught, needs to be depreciated
	
	public static HashMap<String, List<FishObject>> FishDict = new HashMap<>(); //Dictionary containing lists of all caught fish
	
	public static int RarityTotalWeight;
	public static int FishTotalWeight;
	
	//Handles saving the fish to the local dictionary
	public static void AddToFishDict(FishObject f) {
		String fishName = f.Name.toUpperCase(); //Gets the name of the fish to be saved
		
		List<FishObject> list = new ArrayList<>();
		
		if(FishDict.get(fishName) != null) {
			list = FishDict.get(fishName);
		}
		
		
		list.add(f);
		
		FishDict.put(fishName, list);
    	try {
			String dictPath = Setup.dataFolder + "/fish.data";   
		    ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(dictPath));
		    
		    output.writeObject(Variables.FishDict);
		    output.close();
		} catch (IOException ex) {
		    ex.printStackTrace();
		}

		FishSign.UpdateSigns();
	}
	
	public static List<FishObject> GetFishList(String fishName) {
		fishName = fishName.toUpperCase();
		boolean fishFound = false;
		
		//Ensures the fish exists, else returns null
		for (BaseFishObject f : BaseFishList) {
    		if(fishName.equals(f.Name.toUpperCase())) {
    			fishFound = true;
    			break;
    		}
		}
		
		if(fishFound) { //If the fish is found, get all caught
			List<FishObject> fishList = new ArrayList<>();
			
			if(!fishName.equals("ALL")) {
				fishList = FishDict.get(fishName);
			}else {
				 for (Entry<String, List<FishObject>> entry : FishDict.entrySet()) {
					fishList.addAll(entry.getValue());				 
				 }
			}		
			if(fishList != null && fishList.size() > 0)
				Collections.sort(fishList, Collections.reverseOrder());
			else
				fishList = new ArrayList<>();
			return fishList;
		}else
			return null;
		
	}
	
}
