package com.kunfury.blepFishing.Tournament;

import 	java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.kunfury.blepFishing.Miscellaneous.Variables;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.kunfury.blepFishing.Setup;

public class SaveWinner {

	/**
	 * Saves the winner do a file
	 * @param playerName the playername to put into the file
	 * @param items the items the player gets
	 * @param cashReward the cash the player gets
	 */
	public SaveWinner(String playerName, List<ItemStack> items, int cashReward) {
		if(items.size() <= 0 && cashReward <= 0) return;

		ItemStack cash = new ItemStack(Material.COMMAND_BLOCK, cashReward);
		//TODO: Ensure the below is needed
		cash.setAmount(cashReward); //Possibly not needed
		items.add(cash);

		 try {
			 Files.createDirectories(Paths.get(Setup.dataFolder + "/Rewards"));
			 String fileName = Setup.dataFolder + "/Rewards/" + playerName + ".json";
			  File myObj = new File(fileName);
			  myObj.createNewFile();
			  
			  FileWriter myWriter = new FileWriter(fileName, true);

			 Variables.SerializeItemList(items).forEach(serializedItem ->{
				  String message = serializedItem + System.lineSeparator();
				  try {
					myWriter.write(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
			  });
			  myWriter.close();
			  
			} catch (IOException e) {
			  System.out.println("An error occurred.");
			  e.printStackTrace();
			}
	}
}
