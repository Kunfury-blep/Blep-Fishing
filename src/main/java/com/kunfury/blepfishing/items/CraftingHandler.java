package com.kunfury.blepfishing.items;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.objects.FishBag;
import com.kunfury.blepfishing.objects.FishingJournal;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.ArrayList;
import java.util.List;

public class CraftingHandler {

    public static NamespacedKey FishBagCraftKey, FishJournalCraftKey;
    public static List<ShapedRecipe> ShapedRecipes;
    public static Material[] UpgradeItems = new Material[]{
            Material.IRON_BLOCK,
            Material.DIAMOND_BLOCK,
            Material.NETHERITE_BLOCK
    };

    public void Initialize(){
        ShapedRecipes = new ArrayList<>();
        if(ConfigHandler.instance.baseConfig.getEnableFishBags())
            SetupFishBagRecipe();
            //new FishBagRecipe().SmallBag();
        SetupFishingJournal();
    }

    private void SetupFishBagRecipe(){
        //Recipe Creation
        FishBagCraftKey = new NamespacedKey(BlepFishing.getPlugin(), "FishBagCraft");
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

    private void SetupFishingJournal(){
        FishJournalCraftKey = new NamespacedKey(BlepFishing.getPlugin(), "FishJournalCraft");
        ShapelessRecipe recipe = new ShapelessRecipe(FishJournalCraftKey, FishingJournal.GetRecipeItem());

        recipe.addIngredient(Material.BOOK);
        recipe.addIngredient(ItemHandler.FishMat);

        Bukkit.addRecipe(recipe);
    }

    public static void CheckBagCraft(CraftItemEvent e, ItemStack item){
        if(!FishBag.IsBag(item) || FishBag.IsBag(e.getInventory().getResult())) return;
        e.setCancelled(true);
        e.getWhoClicked().sendMessage(Formatting.getPrefix() + ChatColor.RED + "You cannot use your bag for that."); //TODO: Add to messages.yml
    }

    public static void LearnRecipes(Player player){
        if(ConfigHandler.instance.baseConfig.getEnableFishBags())
            player.discoverRecipe(FishBagCraftKey);

        player.discoverRecipe(FishJournalCraftKey);
    }

}
