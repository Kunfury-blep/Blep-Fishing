package com.kunfury.blepFishing.Crafting;

import com.kunfury.blepFishing.Config.ItemsConfig;
import com.kunfury.blepFishing.Crafting.Equipment.FishBag.BagInfo;
import com.kunfury.blepFishing.Crafting.Equipment.FishBag.UpdateBag;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.BlepFishing;
import com.kunfury.blepFishing.Miscellaneous.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.SmithingRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static com.kunfury.blepFishing.Crafting.CraftingManager.BagSetup;

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



    public static List<SmithingRecipe> SmithingKeys;
    public void InitializeSmithRecipes(){
        SmithingKeys = new ArrayList<>();
        SmithingRecipe medBag = new SmithingRecipe(new NamespacedKey(BlepFishing.getPlugin(), "FishBagMed"),
                new ItemStack(Material.AIR), // any material seems fine
                new RecipeChoice.MaterialChoice(ItemsConfig.BagMat),
                new RecipeChoice.MaterialChoice(Material.IRON_BLOCK)
        );
        SmithingKeys.add(medBag);
        Bukkit.addRecipe(medBag);

        SmithingRecipe largeBag = new SmithingRecipe(new NamespacedKey(BlepFishing.getPlugin(), "FishBagLarge"),
                new ItemStack(Material.AIR), // any material seems fine
                new RecipeChoice.MaterialChoice(ItemsConfig.BagMat),
                new RecipeChoice.MaterialChoice(Material.DIAMOND_BLOCK)
        );
        SmithingKeys.add(largeBag);
        Bukkit.addRecipe(largeBag);

        SmithingRecipe giantBag = new SmithingRecipe(new NamespacedKey(BlepFishing.getPlugin(), "FishBagGiant"),
                new ItemStack(Material.AIR), // any material seems fine
                new RecipeChoice.MaterialChoice(ItemsConfig.BagMat),
                new RecipeChoice.MaterialChoice(Material.NETHERITE_BLOCK)
        );
        SmithingKeys.add(giantBag);
        Bukkit.addRecipe(giantBag);

        SmithingRecipe allBlueCompass = new SmithingRecipe(new NamespacedKey(BlepFishing.getPlugin(), "AllBlueCompass"),
                new ItemStack(Material.AIR), // any material seems fine
                new RecipeChoice.MaterialChoice(Material.PRISMARINE_CRYSTALS),
                new RecipeChoice.MaterialChoice(Material.PRISMARINE_CRYSTALS)
        );
        SmithingKeys.add(allBlueCompass);
        Bukkit.addRecipe(allBlueCompass);
    }

}
