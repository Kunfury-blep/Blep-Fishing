package com.kunfury.blepFishing.Config;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Map.Entry;

import com.kunfury.blepFishing.Objects.CollectionLogObject;
import com.kunfury.blepFishing.Objects.*;
import com.kunfury.blepFishing.BlepFishing;
import com.kunfury.blepFishing.Signs.FishSign;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

public class Variables {

	//region Static Lists
	public static List<BaseFishObject> BaseFishList = new ArrayList<>(); //Available fish to catch
	public static List<RarityObject> RarityList = new ArrayList<>(); //Available rarities
	public static List<AreaObject> AreaList = new ArrayList<>(); //Available Areas

	//endregion

	public static boolean DebugMode = false;
	//endregion

	//region Unique Static
	public static HashMap<String, List<FishObject>> FishDict = new HashMap<>();
	public static List<CollectionLogObject> CollectionLogs = new ArrayList<>();
	public static LocalDateTime RecordedDay;
	//endregion


	//endregion


	//region Private Variables
	private static List<String> FishNameList = new ArrayList<>();

	//endregion

	public static int RarityTotalWeight;
	public static int FishTotalWeight;

	public static String Prefix;

	public static String CurrSym = "$"; //The global currency symbol


	/**
	 * @param f The fish to be saved
	 * Desc: Stores the fish in the Dict and then to file
	 * Key is the fish name and the data stored is a list of the fish
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
	}

	public static List<FishObject> getFishList(String fishName) {
		fishName = fishName.toUpperCase();
		boolean fishFound = false;

		List<FishObject> fishList = new ArrayList<>();

		if(fishName.equalsIgnoreCase("ALL")) fishFound = true;
		else{
			//Ensures the fish exists, else returns null
			for (BaseFishObject f : BaseFishList) {
				if(fishName.equals(f.Name.toUpperCase())) {
					fishFound = true;
					break;
				}
			}
		}

		if(fishFound) { //If the fish is found, get all caught
			if(!fishName.equalsIgnoreCase("ALL")) {
				fishList = FishDict.get(fishName);
			}else {
				for (Entry<String, List<FishObject>> entry : FishDict.entrySet()) {
					fishList.addAll(entry.getValue());
				}
			}
			if(fishList != null && fishList.size() > 0)
				fishList.sort(Collections.reverseOrder());
			else
				fishList = new ArrayList<>();
		}
		return fishList;

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

	public static List<String> ErrorMessages = new ArrayList<>();

	public static void AddError(String error){
		if(!ErrorMessages.contains(error)){
			ErrorMessages.add(error);
		}
	}


}

