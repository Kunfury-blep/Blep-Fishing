package com.kunfury.blepfishing.objects.equipment;

import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.items.ItemHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class FishingRod {
    public int Id = -1;
    public int FishCaught;
    public final UUID PlayerId;

    public FishingRod(Player player){
        PlayerId = player.getUniqueId();
    }

    public FishingRod(ResultSet rs, int _amount) throws SQLException {
        Id = rs.getInt("id");
        PlayerId = UUID.fromString(rs.getString("playerId"));
        FishCaught = _amount;
    }

    public void UpdateRodItem(ItemStack rodItem){
        ItemMeta m = rodItem.getItemMeta();
        assert m != null;
        m.setLore(GenerateLore());
        rodItem.setItemMeta(m);
    }

    private List<String> GenerateLore(){
        List<String> lore = new ArrayList<>();

        lore.add("");
        lore.add(Formatting.GetLanguageString("Equipment.Fishing Rod.caughtAmount")
                .replace("{amount}", String.valueOf(FishCaught)));

        return lore;
    }


    ///
    //Static Methods
    ///
    public static FishingRod InitialSetup(ItemStack item, Player player){
        FishingRod fishingRod = new FishingRod(player);
        fishingRod.Id = Database.Rods.Add(fishingRod);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.getPersistentDataContainer().set(ItemHandler.FishRodId, PersistentDataType.INTEGER, fishingRod.Id);
        item.setItemMeta(meta);

        return  fishingRod;
    }

    public static boolean IsRod(ItemStack item){
        if(item.getType() != Material.FISHING_ROD) return false;
        return ItemHandler.hasTag(item, ItemHandler.FishRodId);
    }
}
