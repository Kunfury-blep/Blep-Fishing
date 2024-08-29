package com.kunfury.blepfishing.objects;

import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.helpers.Utilities;
import com.kunfury.blepfishing.items.ItemHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public class FishObject {

    public int Id = -1;
    public final UUID PlayerId;
    public final String TypeId;
    public final String RarityId;
    public final double Length;
    public final LocalDateTime DateCaught;
    public final double Score;
    public final Integer RodId;

    public Integer FishBagId;


    public FishObject(String rarity, String type,  double length, UUID playerId,
                        Integer rodId, Integer bagId){
        //Id = GenerateId();
        PlayerId = playerId;
        DateCaught = LocalDateTime.now();
        TypeId = type;
        RarityId = rarity;
        Length = length;
        Score = CalcScore();
        RodId = rodId;
        FishBagId = bagId;

        Id = Database.Fish.Add(this);
    }


    public FishObject(String rarity, String type,  double length, UUID playerId,
                      LocalDateTime dateCaught, double score){
        PlayerId = playerId;
        DateCaught = dateCaught;
        TypeId = type;
        RarityId = rarity;
        Length = length;
        Score = score;
        RodId = null;
    }

    public FishObject(ResultSet rs) throws SQLException {
        Id = rs.getInt("id");
        PlayerId = UUID.fromString(rs.getString("playerId"));
        RarityId = rs.getString("rarityId");
        TypeId = rs.getString("typeId");
        Length = rs.getDouble("length");
        Score = rs.getDouble("score");
        DateCaught = Utilities.TimeFromLong(rs.getLong("dateCaught"));
        FishBagId = (Integer) rs.getObject("fishBagId");
        RodId = (Integer) rs.getObject("rodId");
    }

    public void AssignId(int id){
        if(Id != -1){
            Bukkit.getLogger().severe("Tried to overwrite fish of id " + Id);
            return;
        }
        Id = id;
    }

    @Override
    public String toString() {
        return Id + " : " + TypeId + " : " + RarityId + " : " + Length + " : " + DateCaught + " : " + PlayerId;
    }

    public List<String> getItemLore(){
        List<String> lore = new ArrayList<>();

        lore.add(ChatColor.DARK_PURPLE + getType().Lore);
        lore.add(ChatColor.GRAY + "Length: " + Length);
        lore.add(ChatColor.GRAY + "Caught By: " + getCatchingPlayer().getName() + " on " + DateCaught.toLocalDate().toString());

        return lore;
    }

    public void setFishBagId(Integer bagId){
        FishBagId = bagId;
        Database.Fish.Update(Id, "fishBagId", bagId);
    }

    public Integer getFishBagId(){
        return FishBagId;
    }

    public ItemStack CreateItemStack(){
        ItemStack fishItem = new ItemStack(Material.SALMON);

        ItemMeta itemMeta = fishItem.getItemMeta();

        assert itemMeta != null;

        itemMeta.getPersistentDataContainer().set(ItemHandler.FishIdKey, PersistentDataType.INTEGER, Id);
        itemMeta.setDisplayName(Formatting.formatColor(getRarity().Prefix + getType().Name));

        itemMeta.setLore(getItemLore());

        itemMeta.setCustomModelData(getType().ModelData);
        fishItem.setItemMeta(itemMeta);

        return fishItem;
    }

    private Player player;
    public Player getCatchingPlayer(){
        if(player == null)
            player = Bukkit.getPlayer(PlayerId);

        return player;
    }

    private double CalcScore() {
        double adjWeight = getRarity().Weight;

        List<Rarity> rarityList = Rarity.GetAll().stream().toList();

        if(rarityList.get(0).Weight  != 1)
            adjWeight = adjWeight / rarityList.get(0).Weight;

        var score = ((Length / getType().LengthMax)/adjWeight) * 100;
        score = (double) Math.round(score * 100) / 100;
        return score;
    }

    public double getScore(){return Score;}

    private FishType fishType;
    public FishType getType(){
        if(fishType == null){
            fishType = FishType.FromId(TypeId);
        }
        return fishType;
    }

    private Rarity rarity;
    public Rarity getRarity(){
        if(rarity == null){
            rarity = Rarity.FromId(RarityId);
        }
        return rarity;
    }


    ///
    //STATIC METHODS
    ///

    public static FishObject GetCaughtFish(int fishId){
        return Database.Fish.Get(fishId);
    }

}


