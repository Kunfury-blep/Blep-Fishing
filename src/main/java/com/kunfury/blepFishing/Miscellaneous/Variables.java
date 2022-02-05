package com.kunfury.blepFishing.Miscellaneous;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import com.kunfury.blepFishing.Tournament.Tournament;
import org.bukkit.conversations.ConversationFactory;

import com.kunfury.blepFishing.Setup;
import com.kunfury.blepFishing.Signs.FishSign;

import com.kunfury.blepFishing.Conversations.ConFactory;
import com.kunfury.blepFishing.Objects.AreaObject;
import com.kunfury.blepFishing.Objects.BaseFishObject;
import com.kunfury.blepFishing.Objects.FishObject;
import com.kunfury.blepFishing.Objects.RarityObject;
import com.kunfury.blepFishing.Objects.TournamentObject;
import com.kunfury.blepFishing.Tournament.SaveTournaments;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

public class Variables {

	//region Static Lists
	public static List<BaseFishObject> BaseFishList = new ArrayList<>(); //Available fish to catch
	public static List<RarityObject> RarityList = new ArrayList<>(); //Available rarities
	public static List<AreaObject> AreaList = new ArrayList<>(); //Available Areas

	public static List<FishObject> CaughtFish = new ArrayList<>(); //Fish that have been caught, needs to be depreciated
	public static List<TournamentObject> Tournaments = new ArrayList<>();

	public static List<String> AllowedWorlds = new ArrayList<>();
	//endregion

	//region Static Booleans
	public static boolean HighPriority = false;
	public static boolean TournamentOnly = false;
	public static boolean TournamentRunning;
	public static boolean WorldsWhitelist = false;
	public static boolean RequireAreaPerm = false;
	public static boolean AllowWanderingTraders = false;
	public static boolean LegendaryFishAnnounce = true;
	//endregion

	//region Unique Static
	public static HashMap<String, List<FishObject>> FishDict = new HashMap<>(); //Dictionary containing lists of all caught fish
	public static ConversationFactory ConFactory = new ConFactory().GetFactory();
	public static ResourceBundle Messages;
	public static double TraderMod = 1;
	//endregion

	//region Private Variables
	private static List<String> FishNameList = new ArrayList<>();

	//endregion

	public static int RarityTotalWeight;
	public static int FishTotalWeight;
	
	public static String Prefix;
	
	public static String CSym = "$"; //The global currency symbol


	/**
	 * @param f The fish to be saved
	 */
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
		    
		    output.writeObject(FishDict);
		    output.close();
		} catch (IOException ex) {
		    ex.printStackTrace();
		}

		new FishSign().UpdateSigns();
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
		
		if(fishFound || fishName.equals("ALL")) { //If the fish is found, get all caught
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
	
	public static void AddTournament(TournamentObject tourney) {
		if(tourney != null)
			Tournaments.add(tourney);
		new SaveTournaments();

		new Tournament().CheckActiveTournaments();
	}

	public static List<String> SerializeItemList(List<ItemStack> items){
		List<String> serializedItems = new ArrayList<>();
		items.forEach(item -> {
			try {
				ByteArrayOutputStream io = new ByteArrayOutputStream();
				BukkitObjectOutputStream os = new BukkitObjectOutputStream(io);
				os.writeObject(item);
				os.flush();

				byte[] serializeObject = io.toByteArray();
				serializedItems.add(Base64.getEncoder().encodeToString(serializeObject));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		return serializedItems;
	}

	public static List<ItemStack> DeserializeItemList(List<String> data){
		List<ItemStack> items = new ArrayList<>();

		data.forEach(d -> {
			byte[] serializedObject = Base64.getDecoder().decode(d);

			try {
				ByteArrayInputStream in = new ByteArrayInputStream(serializedObject);
				BukkitObjectInputStream is= new BukkitObjectInputStream(in);
				items.add((ItemStack) is.readObject());
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		});


		return items;
	}

	public static List<String> GetFishNames(){
		if(FishNameList.size() < BaseFishList.size()){
			FishNameList.clear();
			for(BaseFishObject f : BaseFishList){
				FishNameList.add(f.Name);
			}
		}


		return FishNameList;
	}

}
