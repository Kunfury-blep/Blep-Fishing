package com.kunfury.blepfishing.objects.treasure;

import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.helpers.Utilities;
import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.objects.FishingArea;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.nio.channels.NotYetBoundException;
import java.time.LocalDateTime;
import java.util.*;

public class CompassPiece extends TreasureType{

    public static List<FishingArea> CompassAreas;

    public CompassPiece(String id, int weight, boolean announce) {
        super(id, weight, announce);

        CompassAreas = FishingArea.GetAll().stream()
                .filter(a -> a.HasCompassPiece)
                .toList();
    }

    @Override
    public ItemStack GetItem() {
        throw new NotYetBoundException();
    }

    @Override
    public boolean CanGenerate(Player player){
        return true;
    }

    @Override
    protected void Use(ItemStack item, Player player) {
        //TODO: Implement focusing towards the next piece
    }

    @Override
    public String getFormattedName() {
        return Formatting.GetLanguageString("Treasure.Compass Piece.name");
    }

    @Override
    public ItemStack GetItem(PlayerFishEvent e){

        var player = e.getPlayer();

        List<FishingArea> fishingAreas = FishingArea.GetAvailableAreas(e.getHook().getLocation()).stream()
                .filter(a -> a.HasCompassPiece)
                .filter(a -> !HasPiece(player, a))
                .toList();

        if(fishingAreas.isEmpty())
            return null;

        var area = fishingAreas.get(0);

        Database.TreasureDrops.Add(new TreasureDrop
                ("compassPiece." + area.Id, player.getUniqueId().toString(), LocalDateTime.now()));

        return GeneratePiece(new FishingArea[]{area});
    }


    //Checks if user has the compass piece already
    private boolean HasPiece(Player player, FishingArea area){
        if(!player.getInventory().contains(Material.PRISMARINE_SHARD))
            return false;

        for(var item : player.getInventory()){
            if(item == null || item.getType() != Material.PRISMARINE_SHARD || !ItemHandler.hasTag(item, ItemHandler.FishAreaId))
                continue;

            var areaIdArray = ItemHandler.getTagString(item, ItemHandler.FishAreaId);
            for(var areaId : areaIdArray.split(", ")){
                var a = FishingArea.FromId(areaId);
                if(area == null){
                    Utilities.Severe("Invalid Area ID found in compass piece");
                    continue;
                }
                if(area == a)
                    return true;
            }
        }

        return false;
    }

    public static ItemStack GeneratePiece(FishingArea[] areas){
        ItemStack item = new ItemStack(Material.PRISMARINE_SHARD);
        ItemMeta itemMeta = item.getItemMeta();

        assert itemMeta != null;

        StringBuilder areaIds = new StringBuilder();

        List<String> lore = new ArrayList<>();

        for(var a : areas){
            lore.add(ChatColor.YELLOW + a.Name);

            if(!areaIds.isEmpty())
                areaIds.append(", ");
            areaIds.append(a.Id);
        }

        String title;
        if(areas.length == 1)
            title = Formatting.GetLanguageString("Treasure.Compass Piece.name");
        else{
            title = Formatting.GetLanguageString("Treasure.Compass Piece.nameMulti")
                    .replace("{amount}", String.valueOf(areas.length))
                    .replace("{total}", String.valueOf(FishingArea.GetCompassAreas().size()));
        }

        itemMeta.getPersistentDataContainer().set(ItemHandler.TreasureTypeId, PersistentDataType.STRING, "compassPiece");
        itemMeta.getPersistentDataContainer().set(ItemHandler.FishAreaId, PersistentDataType.STRING, areaIds.toString());
        itemMeta.setDisplayName(title);

        itemMeta.setLore(lore);

        item.setItemMeta(itemMeta);

        return  item;
    }

    public static ItemStack GenerateCompass(){
        ItemStack compassItem = new ItemStack(Material.COMPASS);

        CompassMeta compassMeta = (CompassMeta) compassItem.getItemMeta();
        assert compassMeta != null;

        compassMeta.setDisplayName(Formatting.GetLanguageString("Treasure.Compass.name"));

        List<String> lore = new ArrayList<>();

        lore.add(Formatting.GetLanguageString("Treasure.Compass.lore"));
        lore.add("");
        lore.add(Formatting.GetLanguageString("Treasure.Compass.use"));

        compassMeta.setLore(lore);
        compassMeta.addEnchant(Enchantment.LUCK_OF_THE_SEA, 1, false);
        compassMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        compassMeta.getPersistentDataContainer().set(ItemHandler.CompassKey, PersistentDataType.INTEGER, -1);

        compassItem.setItemMeta(compassMeta);
        return compassItem;
    }

    public static void FocusCompass(ItemStack compassItem){
        CompassMeta compassMeta = (CompassMeta) compassItem.getItemMeta();
        assert compassMeta != null;

        int compassId = ItemHandler.getTagInt(compassItem, ItemHandler.CompassKey);

        Location allBlueLoc = Database.AllBlues.Get(compassId);

        if(allBlueLoc == null){
            Utilities.Severe("Tried to Focus Compass with invalid All Blue");
            return;
        }

        if(allBlueLoc.getBlock().getType() != Material.LODESTONE)
            allBlueLoc.getBlock().setType(Material.LODESTONE);

        compassMeta.setLodestone(allBlueLoc);

        compassItem.setItemMeta(compassMeta);
    }

    public static ItemStack Combine(ItemStack[] craftComponents){
        List<FishingArea> areas = new ArrayList<>();
        for(var item : craftComponents){
            if(item == null)
                continue;
            var areaIdArray = ItemHandler.getTagString(item, ItemHandler.FishAreaId);
            if(areaIdArray.isEmpty())
                return null;

            for(var areaId : areaIdArray.split(", ")){
                var area = FishingArea.FromId(areaId);
                if(area == null){
                    Utilities.Severe("Invalid Area ID found in compass piece");
                    return null;
                }
                if(!areas.contains(area))
                    areas.add(area);
            }
        }

        if(new HashSet<>(areas).containsAll(CompassAreas))
            return GenerateCompass();

        if(areas.size() <= 1) //Ensures pieces are actually being combined
            return null;


        return GeneratePiece(areas.toArray(new FishingArea[0]));
    }

    public static boolean IsPiece(ItemStack item){
        return item != null && item.getType() == Material.PRISMARINE_SHARD
                && ItemHandler.hasTag(item, ItemHandler.FishAreaId);
    }

    public static boolean isCompass(ItemStack item){
        return item != null && item.getType() == Material.COMPASS
                && ItemHandler.hasTag(item, ItemHandler.CompassKey);
    }
}
