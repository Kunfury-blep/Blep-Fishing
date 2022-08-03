package com.kunfury.blepFishing.Crafting.Equipment.FishBag;

import com.kunfury.blepFishing.Setup;
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

import static com.kunfury.blepFishing.Crafting.CraftingManager.*;

public class FishBagRecipe {

    public void SmallBag(){
        ItemStack bag = new ItemStack(Material.HEART_OF_THE_SEA, 1);
        bag = NBTEditor.set(bag, 1, "blep", "item", "fishBagTier" ); //Sets the tier of the bag to 1
        bag = NBTEditor.set(bag, 0, "blep", "item", "fishBagAmount" ); //Sets the current amount of the bag to 0
        bag = NBTEditor.set(bag, true, "blep", "item", "fishBagAutoPickup" ); //Sets the auto-pickup to true

        bag = NBTEditor.set(bag, "null", "blep", "item", "fishBagId" ); //The UUID of the fishBag

        ItemMeta m = bag.getItemMeta();
        assert m != null;
        m.setDisplayName("Small Bag o' Fish");

        m.setLore(new UpdateBag().GenerateLore(bag));

        m.setCustomModelData(1);
        m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        bag.setItemMeta(m);

        bag.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        //Recipe Creation
        key = new NamespacedKey(Setup.getPlugin(), "FishBag");
        ShapedRecipe tokenRecipe = new ShapedRecipe(key, bag);
        tokenRecipe.shape("123","456","789");
        tokenRecipe.setIngredient('1', Material.AIR);
        tokenRecipe.setIngredient('2', Material.AIR);
        tokenRecipe.setIngredient('3', Material.AIR);
        tokenRecipe.setIngredient('4', Material.STRING);
        tokenRecipe.setIngredient('5', Material.SALMON);
        tokenRecipe.setIngredient('6', Material.STRING);
        tokenRecipe.setIngredient('7', Material.LEATHER);
        tokenRecipe.setIngredient('8', Material.STRING);
        tokenRecipe.setIngredient('9', Material.LEATHER);
        Bukkit.addRecipe(tokenRecipe);

        //This doesn't work for some reason
        Bukkit.getScheduler().scheduleSyncDelayedTask(Setup.getPlugin(), () -> {
            for(Player p : Setup.getPlugin().getServer().getOnlinePlayers()) {
                p.discoverRecipe(key);
            }
        }, 1L); // 600L (ticks) is equal to 30 seconds (20 ticks = 1 second)
    }

}
