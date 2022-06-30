package com.kunfury.blepFishing.Events;

import com.kunfury.blepFishing.Endgame.AllBlueGeneration;
import com.kunfury.blepFishing.Endgame.AllBlueInfo;
import com.kunfury.blepFishing.Endgame.CompassHandler;
import com.kunfury.blepFishing.Endgame.TreasureHandler;
import com.kunfury.blepFishing.CollectionLog.CollectionHandler;
import com.kunfury.blepFishing.CollectionLog.JournalHandler;
import com.kunfury.blepFishing.Crafting.CraftingManager;
import com.kunfury.blepFishing.Crafting.Equipment.FishBag.BagInfo;
import com.kunfury.blepFishing.Crafting.Equipment.FishBag.FishBagWithdraw;
import com.kunfury.blepFishing.Crafting.Equipment.FishBag.ParseFish;
import com.kunfury.blepFishing.Crafting.Equipment.FishBag.UseFishBag;
import com.kunfury.blepFishing.Crafting.SmithingTableHandler;
import com.kunfury.blepFishing.FishSwitch;
import com.kunfury.blepFishing.Miscellaneous.FishEconomy;
import com.kunfury.blepFishing.Miscellaneous.Variables;
import com.kunfury.blepFishing.Objects.FishObject;
import com.kunfury.blepFishing.Plugins.McMMOListener;
import com.kunfury.blepFishing.Setup;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.List;

public class EventListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onFishNormal(PlayerFishEvent e) {
        if(!Variables.HighPriority && e.getState() == PlayerFishEvent.State.CAUGHT_FISH) MoveToSwap(e);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFishHigh(PlayerFishEvent e) {
        if(Variables.HighPriority && e.getState() == PlayerFishEvent.State.CAUGHT_FISH) MoveToSwap(e);
    }

    @EventHandler
    public void PlayerInteract(PlayerInteractEvent e){
        Player p = e.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();
        Action a = e.getAction();


        if(item != null && a != Action.PHYSICAL){
            switch(item.getType()){
                case COMPASS:
                    if(AllBlueInfo.IsCompassComplete(item)){
                        new CompassHandler().UseCompass(item, p);
                        break;
                    }
                case PRISMARINE_CRYSTALS:
                    if(p.getShoulderEntityLeft() != null) Bukkit.broadcastMessage("Left Shoulder Found: " + p.getShoulderEntityLeft().getType());
                    else Bukkit.broadcastMessage("No Left Shoulder");
                    if(p.getShoulderEntityRight() != null) Bukkit.broadcastMessage("Right Shoulder Found: " + p.getShoulderEntityRight().getType());
                    else Bukkit.broadcastMessage("No Right Shoulder");
                    if(AllBlueInfo.IsCompass(item)){
                        new CompassHandler().LocateNextPiece(item, p);
                        break;
                    }
                case GLASS_BOTTLE:
                    if(NBTEditor.contains(item, "blep", "item", "MessageBottle")){
                        e.setCancelled(true);
                        new TreasureHandler().OpenBottle(p, item);
                        break;
                    }
                case CHEST:
                    if(NBTEditor.contains(item, "blep", "item", "CasketType")){
                        e.setCancelled(true);
                        new TreasureHandler().OpenCasket(p, item);
                        break;
                    }
                case HEART_OF_THE_SEA:
                    if(NBTEditor.contains(item, "blep", "item", "fishBagId") && !p.getOpenInventory().getType().equals(InventoryType.CHEST)){
                        e.setCancelled(true);
                        if(a == Action.LEFT_CLICK_AIR || a == Action.LEFT_CLICK_BLOCK){
                            new UseFishBag().TogglePickup(item, p);
                        }else if(a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK){
                            new UseFishBag().UseBag(item, p);
                        }
                    }
                case WRITTEN_BOOK:
                    if(NBTEditor.contains(item, "blep", "item", "JournalID")){
                        if(a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK){
                            new JournalHandler().OpenJournal(p, item, e);
                            break;
                        }
                    }
            }
        }



    }

    @EventHandler
    public void PlayerQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();
        if(CompassHandler.ActivePlayers.contains(p)) CompassHandler.ActivePlayers.remove(p);
    }

    @EventHandler
    public void PlayerKick(PlayerKickEvent e){
        Player p = e.getPlayer();
        if(CompassHandler.ActivePlayers.contains(p)) CompassHandler.ActivePlayers.remove(p);
    }

    @EventHandler
    public void EntityInteract(PlayerInteractEntityEvent e) {
        Entity entity = e.getRightClicked();
        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if(Setup.getEconomy() != null){
            if(entity instanceof Villager) {
                Villager villager = (Villager)entity;
                if(villager.getProfession().equals(Villager.Profession.FISHERMAN)
                        && item.getType() == Material.SALMON) {
                    FishEconomy.SellFish(player, 1);
                    e.setCancelled(true);
                }

            }
            if(Variables.AllowWanderingTraders && entity instanceof WanderingTrader){
                if(item.getType() == Material.SALMON){
                    FishEconomy.SellFish(player, Variables.TraderMod);
                    e.setCancelled(true);
                }
                //
                if(item.getType() == Material.HEART_OF_THE_SEA && NBTEditor.contains(item,"blep", "item", "fishBagId" )){

                }

            }
        }
    }

    @EventHandler
    public void prepareSmithingEvent(PrepareSmithingEvent e){
        ItemStack[] inv = e.getInventory().getStorageContents();

        ItemStack origItem = inv[0];
        ItemStack upItem = inv[1];

        if(origItem != null && upItem != null){
            switch(origItem.getType()){
                case HEART_OF_THE_SEA:
                    new SmithingTableHandler().UpgradeBag(origItem, upItem, e);
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
    }

    //TODO: Combine this event with the one below
    @EventHandler
    public void smithingTableClick(InventoryClickEvent e){
        if(e.getClickedInventory() != null && e.getClickedInventory().getType() == InventoryType.SMITHING && e.getSlot() == 2){
            Player p = (Player) e.getWhoClicked();
            ItemStack item = e.getCurrentItem();
            if(item != null)
                switch(item.getType()){
                    case COMPASS:
                        if(NBTEditor.getBoolean(item, "blep", "item", "allBlueCompassComplete")) {
                            new AllBlueGeneration().Generate(e);
                            break;
                        }
                    case HEART_OF_THE_SEA:
                        if(NBTEditor.contains(item, "blep", "item", "fishBagTier")) {
                            new CollectionHandler().CraftedBag(p, BagInfo.GetType(item));
                            break;
                        }
                }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        if(Variables.EnableFishBags) e.getPlayer().discoverRecipe(CraftingManager.key);
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent e) {
        CraftingInventory inv = e.getInventory();

        //Checks that custom items are not used in recipes
        for (ItemStack it : inv.getStorageContents()) {
            if (it != null && it.getType() != Material.AIR) {
                switch(it.getType()){
                    case HEART_OF_THE_SEA:
                        CraftingManager.CheckBagCraft(e, it);
                        break;
                    default:
                        break;
                }
            }
        }
    }


    //TODO: Combine this event with the one above
    @EventHandler
    public void inventoryClick(InventoryClickEvent e) {
        if(e.getClickedInventory() == null) return;

        ClickType a = e.getClick();
        ItemStack item = e.getCurrentItem();
        Player p = (Player) e.getWhoClicked();
        ItemStack bag = p.getInventory().getItemInMainHand();
        if(item != null && bag != null){
            ItemMeta meta = bag.getItemMeta();
            if(meta == null || !e.getView().getTitle().equals(meta.getDisplayName())) return;

            e.setCancelled(true);
            String fishName = item.getItemMeta().getDisplayName();
            String bagId = NBTEditor.getString(item,"blep", "item", "fishBagId" );


            final BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
            //Grabs the collection Asynchronously
            scheduler.runTaskAsynchronously(Setup.getPlugin(), () -> {
                final List<FishObject> tempFish = new ParseFish().RetrieveFish(bagId, fishName);
                scheduler.runTask(Setup.getPlugin(), () -> {
                    boolean largeChoice = true;
                    boolean singleChoice = true;
                    switch(a){
                        case LEFT:
                            largeChoice = false;
                            singleChoice = true;
                            break;
                        case SHIFT_LEFT:
                            largeChoice = false;
                            singleChoice = false;
                            break;
                        case RIGHT:
                            largeChoice = true;
                            singleChoice = true;
                            break;
                        case SHIFT_RIGHT:
                            largeChoice = true;
                            singleChoice = false;
                            break;
                        default:
                            break;
                    }
                    new FishBagWithdraw(tempFish, largeChoice, singleChoice, p, bag);
                });
            });

            //Inventory is the player inv
            if(e.getClickedInventory() == p.getInventory() && item.getType() == Material.SALMON && NBTEditor.contains( item,"blep", "item", "fishValue" )){
               new UseFishBag().AddFish(p.getInventory().getItemInMainHand(), p, item);
            }
        }
    }



    private void MoveToSwap(PlayerFishEvent e){
        //Delays the check by 1 tick to ensure the MCMMO event has run.
        Bukkit.getScheduler ().runTaskLater (Setup.getPlugin(), () ->{
            if(McMcMmoCanFish(e.getPlayer())) new FishSwitch().FishHandler(e);
        } , 1);
    }


    private boolean McMcMmoCanFish(Player player) {
        if(McMMOListener.canFishList.contains(player)){
            McMMOListener.canFishList.remove(player);
            return false;
        }else return true;
    }
}
