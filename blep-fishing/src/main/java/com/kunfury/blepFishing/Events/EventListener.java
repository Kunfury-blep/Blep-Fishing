package com.kunfury.blepFishing.Events;

import com.kunfury.blepFishing.Config.ItemsConfig;
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
import com.kunfury.blepFishing.Interfaces.Player.QuestPanel;
import com.kunfury.blepFishing.Miscellaneous.FishEconomy;
import com.kunfury.blepFishing.Interfaces.Player.PlayerPanel;
import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Objects.MarketObject;
import com.kunfury.blepFishing.BlepFishing;
import com.kunfury.blepFishing.Signs.FishSign;
import com.kunfury.blepFishing.Tournament.TournamentHandler;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class EventListener implements Listener {
    @EventHandler
    public void PlayerInteract(@NotNull PlayerInteractEvent e){
        if(e.getHand() != EquipmentSlot.HAND || e.getAction() == Action.PHYSICAL) return;

        Player p = e.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();
        Action a = e.getAction();


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
            case WRITTEN_BOOK:
                if(NBTEditor.contains(item, "blep", "item", "JournalID")){
                    if(a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK){
                        new JournalHandler().OpenJournal(p, item, e);
                        break;
                    }
                }
        }

        if(item.getType().equals(ItemsConfig.BagMat) && BagInfo.IsBag(item) && !p.getOpenInventory().getType().equals(InventoryType.CHEST)){
            e.setCancelled(true);
            if(a == Action.LEFT_CLICK_AIR || a == Action.LEFT_CLICK_BLOCK){
                if(p.isSneaking()) new UseFishBag().FillBag(item, p);
                else new UseFishBag().TogglePickup(item, p);
            }else if(a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK){
                if(BlepFishing.hasEconomy() && a == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getState() instanceof Sign){
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
    }

    @EventHandler
    public void PlayerQuit(@NotNull PlayerQuitEvent e){
        Player p = e.getPlayer();
        if(CompassHandler.ActivePlayers.contains(p)) CompassHandler.ActivePlayers.remove(p);
    }

    @EventHandler
    public void PlayerKick(PlayerKickEvent e){
        Player p = e.getPlayer();
        if(CompassHandler.ActivePlayers.contains(p)) CompassHandler.ActivePlayers.remove(p);
    }

    @EventHandler
    public void EntityInteract(@NotNull PlayerInteractEntityEvent e) {
        Entity entity = e.getRightClicked();
        Player p = e.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();
        if(BlepFishing.getEconomy() != null){
            if(entity instanceof Villager) {
                Villager villager = (Villager)entity;
                if(villager.getProfession().equals(Villager.Profession.FISHERMAN)){
                    if(item.getType() == ItemsConfig.FishMat){
                        e.setCancelled(true);
                        FishEconomy.SellFish(p, 1);
                    }
                    if(BagInfo.IsBag(item)){
                        e.setCancelled(true);
                        FishEconomy.SellBag(p, item, 1);
                    }

                }

            }
            if(BlepFishing.configBase.getAllowWanderingTraders() && entity instanceof WanderingTrader){
                if(item.getType() == ItemsConfig.FishMat){
                    e.setCancelled(true);
                    FishEconomy.SellFish(p, BlepFishing.configBase.getTraderMod());
                }
                if(BagInfo.IsBag(item)){
                    e.setCancelled(true);
                    FishEconomy.SellBag(p, item, BlepFishing.configBase.getTraderMod());
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
            if(origItem.getType().equals(Material.PRISMARINE_CRYSTALS))
                new CompassHandler().CombinePieces(origItem, upItem, e);

            if(origItem.getType().equals(ItemsConfig.BagMat)){
                new SmithingTableHandler().UpgradeBag(origItem, upItem, e);
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
                    }

                    if(item.getType().equals(ItemsConfig.FishMat)){
                        if(NBTEditor.contains(item, "blep", "item", "fishBagTier")) {
                            new CollectionHandler().CraftedBag(p, BagInfo.getType(item));
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

                    if(item.getType().equals(ItemsConfig.FishMat)){
                        new UseFishBag().FishBagWithdraw(e.getClick(), item.getItemMeta().getDisplayName(), p, mainHand);
                        return;
                    }
                }

                if(e.getView().getTitle().equals(Formatting.getMessage("Player Panel.title"))){
                    e.setCancelled(true);
                    new PlayerPanel().Click(e, p);
                }

                if(e.getView().getTitle().equals(Formatting.getMessage("Player Panel.quests"))){
                    e.setCancelled(true);
                    new QuestPanel().Click(e, p);
                }

            }
            case PLAYER -> {
                if(BagInfo.IsOpen(p, e.getInventory())) e.setCancelled(true);
                if(item != null && item.getType() == ItemsConfig.FishMat && BagInfo.IsBag(mainHand) && NBTEditor.contains( item,"blep", "item", "fishValue" )){
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
        Player p = e.getPlayer();
        if(BlepFishing.configBase.getEnableFishBags()) p.discoverRecipe(CraftingManager.key);

        new TournamentHandler().ShowBars(p);

        if(p.hasPermission("bf.admin") && Variables.ErrorMessages.size() > 0){
            Variables.ErrorMessages.forEach(er -> p.sendMessage(Variables.getPrefix() + ChatColor.RED + er));
        }
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
                if(it.getType().equals(ItemsConfig.BagMat)){
                    CraftingManager.CheckBagCraft(e, it);
                }
            }
        }
    }

    @EventHandler
    public void onCustomFish(FishCaughtEvent e){
        new TournamentHandler().ShowBars(e.GetWhoCaught());
    }


    @EventHandler
    public void a(InventoryClickEvent e) {

        if (e.getClickedInventory() != null && e.getClickedInventory().getType() == InventoryType.GRINDSTONE && e.getSlotType() == InventoryType.SlotType.RESULT && BagInfo.IsBag(e.getCurrentItem())) {
            e.setCancelled(true);
        }
    }
}
