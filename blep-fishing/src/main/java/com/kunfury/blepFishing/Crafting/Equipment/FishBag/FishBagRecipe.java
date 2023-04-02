package com.kunfury.blepFishing.Crafting.Equipment.FishBag;

import com.kunfury.blepFishing.Config.ItemsConfig;
import com.kunfury.blepFishing.Crafting.CraftingManager;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.BlepFishing;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class FishBagRecipe {

    public void SmallBag(){
        ItemStack bag = new ItemStack(ItemsConfig.BagMat, 1);
        bag = NBTEditor.set(bag, 1, "blep", "item", "fishBagTier" ); //Sets the tier of the bag to 1
        bag = NBTEditor.set(bag, 0, "blep", "item", "fishBagAmount" ); //Sets the current amount of the bag to 0
        bag = NBTEditor.set(bag, true, "blep", "item", "fishBagAutoPickup" ); //Sets the auto-pickup to true

        bag = NBTEditor.set(bag, "null", "blep", "item", "fishBagId" ); //The UUID of the fishBag

        ItemMeta m = bag.getItemMeta();
        assert m != null;
        m.setDisplayName(Formatting.getMessage("Equipment.Fish Bag.smallBag"));

        m.setLore(new UpdateBag().GenerateLore(bag));

        m.setCustomModelData(ItemsConfig.BagModel);
        m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        bag.setItemMeta(m);

        bag.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        //Recipe Creation
        CraftingManager.key = new NamespacedKey(BlepFishing.getPlugin(), "FishBag");
        ShapedRecipe bagRecipe = new ShapedRecipe(CraftingManager.key, bag);
        bagRecipe.shape("123","456","789");
        bagRecipe.setIngredient('1', Material.AIR);
        bagRecipe.setIngredient('2', Material.AIR);
        bagRecipe.setIngredient('3', Material.AIR);
        bagRecipe.setIngredient('4', Material.STRING);
        bagRecipe.setIngredient('5', ItemsConfig.FishMat);
        bagRecipe.setIngredient('6', Material.STRING);
        bagRecipe.setIngredient('7', Material.LEATHER);
        bagRecipe.setIngredient('8', Material.STRING);
        bagRecipe.setIngredient('9', Material.LEATHER);
        Bukkit.addRecipe(bagRecipe);
        CraftingManager.ShapedRecipes.add(bagRecipe);

        //This doesn't work for some reason
        Bukkit.getScheduler().scheduleSyncDelayedTask(BlepFishing.getPlugin(), () -> {
            for(Player p : BlepFishing.getPlugin().getServer().getOnlinePlayers()) {
                p.discoverRecipe(CraftingManager.key);
            }
        }, 1L); // 600L (ticks) is equal to 30 seconds (20 ticks = 1 second)
    }

}
