package com.kunfury.blepFishing.Events;

import com.kunfury.blepFishing.Endgame.AllBlueGeneration;
import com.kunfury.blepFishing.Endgame.AllBlueInfo;
import com.kunfury.blepFishing.Endgame.CompassHandler;
import com.kunfury.blepFishing.Endgame.TreasureHandler;
import com.kunfury.blepFishing.CollectionLog.CollectionHandler;
import com.kunfury.blepFishing.CollectionLog.JournalHandler;
import com.kunfury.blepFishing.Crafting.CraftingManager;
import com.kunfury.blepFishing.Crafting.Equipment.FishBag.BagInfo;
import com.kunfury.blepFishing.Crafting.Equipment.FishBag.UseFishBag;
import com.kunfury.blepFishing.Crafting.SmithingTableHandler;
import com.kunfury.blepFishing.FishSwitch;
import com.kunfury.blepFishing.Miscellaneous.FishEconomy;
import com.kunfury.blepFishing.Interfaces.Player.PlayerPanel;
import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Objects.MarketObject;
import com.kunfury.blepFishing.Plugins.McMMOListener;
import com.kunfury.blepFishing.Setup;
import com.kunfury.blepFishing.Signs.FishSign;
import com.kunfury.blepFishing.Tournament.TournamentHandler;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

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
        if(e.getHand() != EquipmentSlot.HAND) return;

        Player p = e.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();
        Action a = e.getAction();


        if(a != Action.PHYSICAL){
            switch(item.getType()){
                case COMPASS:
                    if(AllBlueInfo.IsCompassComplete(item)){
                        new CompassHandler().UseCompass(item, p);
                        break;
                    }
                case PRISMARINE_CRYSTALS:
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
                    if(BagInfo.IsBag(item) && !p.getOpenInventory().getType().equals(InventoryType.CHEST)){
                        e.setCancelled(true);
                        if(a == Action.LEFT_CLICK_AIR || a == Action.LEFT_CLICK_BLOCK){
                            if(p.isSneaking()) new UseFishBag().FillBag(item, p);
                            else new UseFishBag().TogglePickup(item, p);
                        }else if(a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK){
                            if(Setup.hasEconomy() && a == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getState() instanceof Sign){
                                Sign sign = (Sign) e.getClickedBlock().getState();
                                for(MarketObject market : FishSign.marketSigns) {
                                    if(market.CheckBool(sign)){
                                        FishEconomy.SellBag(e.getPlayer(), item, 1);
                                        return;
                                    }
                                }
                            }

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
        Player p = e.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();
        if(Setup.getEconomy() != null){
            if(entity instanceof Villager) {
                Villager villager = (Villager)entity;
                if(villager.getProfession().equals(Villager.Profession.FISHERMAN)){
                    if(item.getType() == Material.SALMON){
                        e.setCancelled(true);
                        FishEconomy.SellFish(p, 1);
                    }
                    if(BagInfo.IsBag(item)){
                        e.setCancelled(true);
                        FishEconomy.SellBag(p, item, 1);
                    }

                }

            }
            if(Variables.AllowWanderingTraders && entity instanceof WanderingTrader){
                if(item.getType() == Material.SALMON){
                    e.setCancelled(true);
                    FishEconomy.SellFish(p, Variables.TraderMod);
                }
                if(BagInfo.IsBag(item)){
                    e.setCancelled(true);
                    FishEconomy.SellBag(p, item, Variables.TraderMod);
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


    @EventHandler
    public void inventoryClick(InventoryClickEvent e){
        if(e.getClickedInventory() == null) return;
        ItemStack item = e.getCurrentItem();
        Player p = (Player) e.getWhoClicked();
        ItemStack mainHand = p.getInventory().getItemInMainHand();

        if(e.getView().getTitle().equals(Formatting.getMessage("Tournament.panelName"))){
            e.setCancelled(true);
            return;
        }

        switch(e.getClickedInventory().getType()){
            case SMITHING -> {
                if(e.getSlot() == 2 && item != null){
                    switch(item.getType()){
                        case COMPASS:
                            if(NBTEditor.getBoolean(item, "blep", "item", "allBlueCompassComplete")) {
                                new AllBlueGeneration().Generate(e);
                                break;
                            }
                        case HEART_OF_THE_SEA:
                            if(NBTEditor.contains(item, "blep", "item", "fishBagTier")) {
                                new CollectionHandler().CraftedBag(p, BagInfo.getType(item));
                                break;
                            }
                    }
                }
            }
            case CHEST -> {
                boolean bagOpen = false;
                if(BagInfo.IsOpen(p, e.getInventory())){
                    e.setCancelled(true);
                    bagOpen = true;
                }

                if(bagOpen && item != null && BagInfo.IsBag(mainHand)){
                    e.setCancelled(true);

                    if(e.getSlot() == 53 && item.getType().equals(Material.WARPED_SIGN)){
                        new UseFishBag().ChangePage(true, mainHand, p);
                        return;
                    }

                    if(e.getSlot() == 45 && item.getType().equals(Material.CRIMSON_SIGN)){
                        new UseFishBag().ChangePage(false, mainHand, p);
                        return;
                    }

                    if(item.getType().equals(Material.SALMON)){
                        new UseFishBag().FishBagWithdraw(e.getClick(), item.getItemMeta().getDisplayName(), p, mainHand);
                        return;
                    }
                }

                if(e.getView().getTitle().equals(Formatting.getMessage("Player Panel.title"))){
                    e.setCancelled(true);
                    new PlayerPanel().Click(e, p);
                }

            }
            case PLAYER -> {
                if(BagInfo.IsOpen(p, e.getInventory())) e.setCancelled(true);
                if(item != null && item.getType() == Material.SALMON && BagInfo.IsBag(mainHand) && NBTEditor.contains( item,"blep", "item", "fishValue" )){
                    new UseFishBag().AddFish(mainHand, p, item, true);
                    return;
                }
                if (Formatting.getMessage("Player Panel.title").equals(e.getView().getTitle())) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        if(Variables.EnableFishBags) e.getPlayer().discoverRecipe(CraftingManager.key);

        new TournamentHandler().ShowBars(e.getPlayer());
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent e) {
        CraftingInventory inv = e.getInventory();

        ItemStack item = e.getCurrentItem();

        if(BagInfo.IsBag(item) && BagInfo.getId(item).equals("null"))
            e.setCurrentItem(NBTEditor.set(item, UUID.randomUUID().toString(), "blep", "item", "fishBagId"));


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

    @EventHandler
    public void onCustomFish(FishCaughtEvent e){
        new TournamentHandler().ShowBars(e.GetWhoCaught());
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
