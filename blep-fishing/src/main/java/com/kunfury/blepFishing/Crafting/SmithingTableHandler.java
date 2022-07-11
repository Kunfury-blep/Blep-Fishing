package com.kunfury.blepFishing.Crafting;

import com.kunfury.blepFishing.Crafting.Equipment.FishBag.BagInfo;
import com.kunfury.blepFishing.Crafting.Equipment.FishBag.UpdateBag;
import com.kunfury.blepFishing.Setup;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.SmithingRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class SmithingTableHandler {

    public void UpgradeBag(ItemStack oldBag, ItemStack upgradeItem, PrepareSmithingEvent e){
        if(!BagInfo.IsFull(oldBag)){
            e.setResult(new ItemStack(Material.AIR, 0));
            return;
        }
        switch(upgradeItem.getType()){
            case IRON_BLOCK:
                e.setResult(BagSetup(oldBag, 2));
                break;
            case DIAMOND_BLOCK:
                e.setResult(BagSetup(oldBag, 3));
                break;
            case NETHERITE_BLOCK:
                e.setResult(BagSetup(oldBag, 4));
                break;
            default:
                break;
        }
    }

    public ItemStack BagSetup(ItemStack oldBag, int goalTier){

        ItemStack result = new ItemStack(oldBag);
        int tier = NBTEditor.getInt(result, "blep", "item", "fishBagTier" );
        if(tier != goalTier - 1 ) return new ItemStack(Material.AIR, 0);

        result = NBTEditor.set(result, tier + 1, "blep", "item", "fishBagTier" ); //Sets the tier of the bag to 2

        ItemMeta m = result.getItemMeta();

        m.setDisplayName(BagInfo.bagTypes[tier + 1] + " Bag o' Fish");
        m.setLore(new UpdateBag().GenerateLore(result));

        result.setItemMeta(m);

        return result;
    }



    public void InitializeSmithRecipes(){
        SmithingRecipe medBag = new SmithingRecipe(new NamespacedKey(Setup.getPlugin(), "FishBagMed"),
                new ItemStack(Material.AIR), // any material seems fine
                new RecipeChoice.MaterialChoice(Material.HEART_OF_THE_SEA),
                new RecipeChoice.MaterialChoice(Material.IRON_BLOCK)
        );
        Bukkit.addRecipe(medBag);

        SmithingRecipe largeBag = new SmithingRecipe(new NamespacedKey(Setup.getPlugin(), "FishBagLarge"),
                new ItemStack(Material.AIR), // any material seems fine
                new RecipeChoice.MaterialChoice(Material.HEART_OF_THE_SEA),
                new RecipeChoice.MaterialChoice(Material.DIAMOND_BLOCK)
        );
        Bukkit.addRecipe(largeBag);

        SmithingRecipe giantBag = new SmithingRecipe(new NamespacedKey(Setup.getPlugin(), "FishBagGiant"),
                new ItemStack(Material.AIR), // any material seems fine
                new RecipeChoice.MaterialChoice(Material.HEART_OF_THE_SEA),
                new RecipeChoice.MaterialChoice(Material.NETHERITE_BLOCK)
        );
        Bukkit.addRecipe(giantBag);

        SmithingRecipe allBlueCompass = new SmithingRecipe(new NamespacedKey(Setup.getPlugin(), "AllBlueCompass"),
                new ItemStack(Material.AIR), // any material seems fine
                new RecipeChoice.MaterialChoice(Material.PRISMARINE_CRYSTALS),
                new RecipeChoice.MaterialChoice(Material.PRISMARINE_CRYSTALS)
        );
        Bukkit.addRecipe(allBlueCompass);
    }

}
