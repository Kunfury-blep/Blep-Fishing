package com.kunfury.blepFishing.Objects;

import com.kunfury.blepFishing.CollectionLog.CollectionHandler;
import com.kunfury.blepFishing.Crafting.Equipment.FishBag.BagInfo;
import com.kunfury.blepFishing.Config.Variables;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;


import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CollectionLogObject implements Serializable {

    @Serial
    private static final long serialVersionUID = 5123249474181852150L;
    private HashMap<String, Boolean> FishMap = new HashMap<>();

    private List<FishCollectionObject> fishList = new ArrayList<>();

    private HashMap<String, Boolean> BagMap = new HashMap<>();
    public boolean EndgameCompass = false;
    private String PlayerId; //UUID of owning player

    public CollectionLogObject(Player p){
        PlayerId = p.getUniqueId().toString();

        for(var f : Variables.BaseFishList){
            fishList.add(new FishCollectionObject(f.Name, false));
            FishMap.put(f.Name, false);
        }

        for(var b: BagInfo.bagTypes){
            BagMap.put(b, false);
        }

        //p.getWorld().dropItem(p.getLocation(), GenerateJournal(p));
    }

    //Needed to update the old data systems to the new
    public void UpdateOldFormats(){
        if(FishMap == null) return;
        fishList = new ArrayList<>();

        FishMap.forEach((key, value) -> {
            fishList.add(new FishCollectionObject(key, value));
        });
        FishMap = null;
    }


    public void CaughtFish(FishObject fish){
        FishCollectionObject fishCol = FindFish(fish.Name);
        if(fishCol == null){
            fishCol = new FishCollectionObject(fish);
            fishList.add(fishCol);
        }
        else fishCol.Caught(fish);

        new CollectionHandler().SaveLog();
    }

    private FishCollectionObject FindFish(String fishName){
        return fishList.stream().filter(fish -> fishName.equals(fish.getName())).findFirst().orElse(null);
    }


    public void CraftedBag(String bagType){
        Boolean hasBag = BagMap.get(bagType);
        if(hasBag == null || !hasBag) {
            BagMap.put(bagType, true);
            new CollectionHandler().SaveLog();
        }
    }

    public List<FishCollectionObject> getFishList(){

        //TODO: Get this working with the new fish lists
//        List<FishCollectionObject> trimmedList = fishList;
//        for(var f : Variables.BaseFishList){
//            if(!FishMap.containsKey(f.Name)) trimmedList.add(new FishCollectionObject(f.Name, false)); //Checks to ensure the collection log contains current fish
//            //if(!FishMap.containsKey(f.Name)) FishMap.put(f.Name, false);
//        }

        return fishList;
    }

    public String getUUID(){
        return PlayerId;
    }

    public ItemStack GenerateJournal(Player p){
        ItemStack journal = new ItemStack(Material.WRITTEN_BOOK);
        journal = NBTEditor.set(journal, PlayerId, "blep", "item", "JournalID");

        BookMeta m = (BookMeta) journal.getItemMeta();
        m.setTitle("Tattered Fishing Journal");
        m.setAuthor(p.getDisplayName());
        m.setPages("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam ", "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat", "nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");

        journal.setItemMeta(m);
        return journal;
    }
}
