package com.kunfury.blepFishing.Crafting;

import com.kunfury.blepFishing.BlepFishing;
import com.kunfury.blepFishing.Crafting.Equipment.FishBag.BagInfo;
import com.kunfury.blepFishing.Crafting.Equipment.FishBag.FishBagRecipe;
import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Crafting.Equipment.FishBag.UpdateBag;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Miscellaneous.NBTEditor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CraftingManager {

    public static NamespacedKey key;
    public static List<ShapedRecipe> ShapedRecipes;

    public void InitItems(){
        ShapedRecipes = new ArrayList<>();
        if(BlepFishing.configBase.getEnableFishBags()) new FishBagRecipe().SmallBag();
    }

    public static void CheckBagCraft(CraftItemEvent e, ItemStack item){
        if(!BagInfo.IsBag(item) || BagInfo.IsBag(e.getInventory().getResult())) return;
        e.setCancelled(true);
        e.getWhoClicked().sendMessage(Variables.getPrefix() + ChatColor.RED + "You cannot use your bag for that."); //Add to messages.yml
    }

    public static ItemStack BagSetup(ItemStack oldBag, int goalTier){

        ItemStack result = new ItemStack(oldBag);
        int tier = NBTEditor.getInt(result, "blep", "item", "fishBagTier" );
        if(tier != goalTier - 1 ) return new ItemStack(Material.AIR, 0);

        result = NBTEditor.set(result, tier + 1, "blep", "item", "fishBagTier" ); //Sets the tier of the bag to 2

        ItemMeta m = result.getItemMeta();

        String bagName = switch(tier){
            case 1 -> Formatting.getMessage("Equipment.Fish Bag.medBag");
            case 2 -> Formatting.getMessage("Equipment.Fish Bag.largeBag");
            case 3 -> Formatting.getMessage("Equipment.Fish Bag.giantBag");
            default -> Formatting.getMessage("Equipment.Fish Bag.baseName");
        };

        m.setDisplayName(bagName);
        m.setLore(new UpdateBag().GenerateLore(result));

        result.setItemMeta(m);

        return result;
    }

}
