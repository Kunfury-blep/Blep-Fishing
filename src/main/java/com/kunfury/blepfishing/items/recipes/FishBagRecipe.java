package com.kunfury.blepfishing.items.recipes;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.items.CraftingHandler;
import com.kunfury.blepfishing.objects.FishBag;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;

public class FishBagRecipe {


    public void SmallBag(){

        //Recipe Creation
        CraftingHandler.FishBagCraftKey = new NamespacedKey(BlepFishing.getPlugin(), "FishBagCraft");
        ShapedRecipe bagRecipe = new ShapedRecipe(CraftingHandler.FishBagCraftKey, FishBag.GetRecipeItem());
        bagRecipe.shape("123","456","789");
        //bagRecipe.setIngredient('1', Material.AIR);
        //bagRecipe.setIngredient('2', Material.AIR);
        //bagRecipe.setIngredient('3', Material.AIR);
        bagRecipe.setIngredient('4', Material.STRING);
        bagRecipe.setIngredient('5', Material.SALMON);
        bagRecipe.setIngredient('6', Material.STRING);
        bagRecipe.setIngredient('7', Material.LEATHER);
        bagRecipe.setIngredient('8', Material.STRING);
        bagRecipe.setIngredient('9', Material.LEATHER);
        Bukkit.addRecipe(bagRecipe);
        CraftingHandler.ShapedRecipes.add(bagRecipe);
    }

}
