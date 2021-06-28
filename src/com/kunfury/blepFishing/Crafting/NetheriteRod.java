package com.kunfury.blepFishing.Crafting;

import com.kunfury.blepFishing.Setup;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.SmithingRecipe;

public class NetheriteRod {

    public void CreateRecipe(){
        RecipeChoice choice = new RecipeChoice.ExactChoice(new ItemStack(Material.IRON_BOOTS));
        RecipeChoice choice1 = new RecipeChoice.ExactChoice(new ItemStack(Material.DIAMOND));
//RecipeChoice choice1 = new ItemStack(Material.DIAMOND);

        SmithingRecipe recipe = new SmithingRecipe(new NamespacedKey(Setup.getPlugin(),"diamond_leather"), CraftingManager.ironRod, choice, choice1);
        Bukkit.addRecipe(recipe);
    }

}
