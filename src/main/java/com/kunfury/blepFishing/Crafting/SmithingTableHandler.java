package com.kunfury.blepFishing.Crafting;

import com.kunfury.blepFishing.AllBlue.AllBlueGeneration;
import com.kunfury.blepFishing.AllBlue.CompassHandler;
import com.kunfury.blepFishing.Crafting.Equipment.FishBag.BagInfo;
import com.kunfury.blepFishing.Crafting.Equipment.FishBag.UpdateBag;
import com.kunfury.blepFishing.Crafting.Equipment.Update;
import com.kunfury.blepFishing.Setup;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.event.inventory.SmithItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.SmithingRecipe;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class SmithingTableHandler implements Listener {



    String[] bagNames = {"Small", "Medium", "Large", "Giant"};



    @EventHandler
    public void prepareSmithingEvent(PrepareSmithingEvent e){

        ItemStack result = e.getResult();

        ItemStack[] inv = e.getInventory().getStorageContents();

        ItemStack origItem = inv[0];
        ItemStack upItem = inv[1];

        if(origItem != null && upItem != null){
            switch(origItem.getType()){
                case HEART_OF_THE_SEA:
                    UpgradeBag(origItem, upItem, e);
                    break;
//                case FISHING_ROD:
//                    UpgradeRod(origItem, upItem, e);
//                    break;
                case PRISMARINE_CRYSTALS:
                    new CompassHandler().CombinePieces(origItem, upItem, e);
                    break;
                default:
                    break;
            }

            List<HumanEntity> viewers = e.getViewers();
            viewers.forEach(humanEntity -> ((Player)humanEntity).updateInventory());
        }

        //Bukkit.broadcastMessage("Smithing Inventory: " + Arrays.toString(e.getInventory().getStorageContents()));
    }

    @EventHandler
    public void smithingTableClick(InventoryClickEvent e){
        if(e.getClickedInventory() != null && e.getClickedInventory().getType() == InventoryType.SMITHING && e.getSlot() == 2){
            ItemStack item = e.getCurrentItem();
            if(item != null && item.getType().equals(Material.COMPASS) && NBTEditor.getBoolean(item, "blep", "item", "allBlueCompassComplete")) {
                new AllBlueGeneration().Generate(e);
            }

        }
    }

    private void UpgradeBag(ItemStack oldBag, ItemStack upgradeItem, PrepareSmithingEvent e){
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

    private ItemStack NetheriteRodSetup(ItemStack initialRod){

        ItemStack result = new ItemStack(initialRod);
        ItemMeta meta = result.getItemMeta();

        if (meta instanceof Damageable)
            ((Damageable) meta).setDamage(-999);

        meta.setDisplayName("Netherite Fishing Rod");
        result.setItemMeta(meta);
        return result;
    }

    private ItemStack BagSetup(ItemStack oldBag, int goalTier){

        ItemStack result = new ItemStack(oldBag);
        int tier = NBTEditor.getInt(result, "blep", "item", "fishBagTier" );
        if(tier != goalTier - 1 ) return new ItemStack(Material.AIR, 0);

        result = NBTEditor.set(result, tier + 1, "blep", "item", "fishBagTier" ); //Sets the tier of the bag to 2

        ItemMeta m = result.getItemMeta();

        m.setDisplayName(bagNames[tier] + " Bag o' Fish");
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
