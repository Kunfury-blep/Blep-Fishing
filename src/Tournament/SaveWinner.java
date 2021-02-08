package Tournament;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.google.gson.Gson;
import com.kunfury.blepFishing.Setup;

public class SaveWinner {
	
	///
	//Used for saving rewards to 
	///
	public SaveWinner(String playerName, List<ItemStack> items, int cashReward) {
		Gson gson = new Gson();		
		if(cashReward > 0) {
			ItemStack cash = new ItemStack(Material.AIR, cashReward);
			items.add(cash);
		}		
		 try {
			 Files.createDirectories(Paths.get(Setup.dataFolder + "/Rewards"));
			 String fileName = Setup.dataFolder + "/Rewards/" + playerName + ".json";
			  File myObj = new File(fileName);
			  if (myObj.createNewFile()) {
			    System.out.println("File created: " + myObj.getName());
			  } else {
			    System.out.println("File already exists.");
			  }
			  
			  FileWriter myWriter = new FileWriter(fileName, true);
			  
			  items.forEach(item ->{
				  String message = gson.toJson(item) + System.lineSeparator();
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
