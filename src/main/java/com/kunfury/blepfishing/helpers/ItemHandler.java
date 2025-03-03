package com.kunfury.blepfishing.helpers;

import com.kunfury.blepfishing.BlepFishing;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ItemHandler {

    public static NamespacedKey FishIdKey, ButtonIdKey, FishTypeId,
            FishBagId, FishRodId, FishAreaId, TourneyTypeId, TourneyId, RarityId, TreasureTypeId,
            CompassKey, FishJournalId, QuestId;

    public static Material FishMat = Material.SALMON;
    public static Material BagMat = Material.HEART_OF_THE_SEA;
    public static int BagModelData = 1;

    public static void Initialize(){
        FishIdKey = new NamespacedKey(BlepFishing.getPlugin(), "fishId");
        ButtonIdKey = new NamespacedKey(BlepFishing.getPlugin(), "buttonId");

        FishBagId = new NamespacedKey(BlepFishing.getPlugin(), "fishBagId");
        FishTypeId = new NamespacedKey(BlepFishing.getPlugin(), "fishTypeId");
        FishRodId = new NamespacedKey(BlepFishing.getPlugin(), "fishRodId");
        FishAreaId = new NamespacedKey(BlepFishing.getPlugin(), "fishAreaId");

        TourneyTypeId = new NamespacedKey(BlepFishing.getPlugin(), "tourneyTypeId");
        TourneyId = new NamespacedKey(BlepFishing.getPlugin(), "tourneyId");

        RarityId = new NamespacedKey(BlepFishing.getPlugin(), "rarityId");
        TreasureTypeId = new NamespacedKey(BlepFishing.getPlugin(), "treasureTypeId");

        CompassKey = new NamespacedKey(BlepFishing.getPlugin(), "compass");

        FishJournalId = new NamespacedKey(BlepFishing.getPlugin(), "fishJournalId");

        QuestId = new NamespacedKey(BlepFishing.getPlugin(), "questId");

        InitializeOldNamespaces();
    }

    public static boolean hasTag(ItemStack item, NamespacedKey key){
        if(item == null || !item.hasItemMeta()) return false;

        ItemMeta m = item.getItemMeta();
        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        if(dataContainer.isEmpty()) return false;


        return dataContainer.has(key, PersistentDataType.STRING) || dataContainer.has(key, PersistentDataType.INTEGER)
                || dataContainer.has(key, PersistentDataType.BOOLEAN);
    }

    public static String getTagString(ItemStack item, NamespacedKey key){
        ItemMeta m = item.getItemMeta();
        if(m == null) return "";
        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        if(dataContainer.isEmpty()) return "";

        if(!dataContainer.has(key, PersistentDataType.STRING)) return "";

        return dataContainer.get(key, PersistentDataType.STRING);
    }

    public static int getTagInt(ItemStack item, NamespacedKey key){
        ItemMeta m = item.getItemMeta();
        if(m == null) return 0;
        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        if(dataContainer.isEmpty()) return 0;

        if(!dataContainer.has(key, PersistentDataType.INTEGER)) return 0;

        return dataContainer.get(key, PersistentDataType.INTEGER);
    }

    public static boolean getTagBool(ItemStack item, NamespacedKey key){
        ItemMeta m = item.getItemMeta();
        if(m == null) return false;
        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        if(dataContainer.isEmpty()) return false;

        if(!dataContainer.has(key, PersistentDataType.BOOLEAN)) return false;

        return dataContainer.get(key, PersistentDataType.BOOLEAN);
    }



    ///
    //Handle Rewrite Keys
    //These methods are intended only for those migrating from the Blep Fishing Rewrite Development Builds to Prod
    ///

    private static NamespacedKey oldFishIdKey, oldFishTypeId,
            oldFishBagId, oldFishRodId, oldFishAreaId, oldTourneyTypeId, oldTourneyId, oldRarityId, oldTreasureTypeId,
            oldCompassKey, oldFishJournalId;

    private static List<NamespacedKey> oldKeys;

    private static String rewriteNamespace;
    private static void InitializeOldNamespaces(){
        rewriteNamespace = "blepfishingrewrite";

        oldFishIdKey = new NamespacedKey(rewriteNamespace,"blep.fishid");
        oldFishBagId = new NamespacedKey(rewriteNamespace, "blep.fishbagid");
        oldFishTypeId = new NamespacedKey(rewriteNamespace, "blep.fishtypeid");
        oldFishRodId = new NamespacedKey(rewriteNamespace, "blep.fishrodid");
        oldFishAreaId = new NamespacedKey(rewriteNamespace, "blep.fishareaid");

        oldTourneyTypeId = new NamespacedKey(rewriteNamespace, "blep.tourneytypeid");
        oldTourneyId = new NamespacedKey(rewriteNamespace, "blep.tourneyid");

        oldRarityId = new NamespacedKey(rewriteNamespace, "blep.rarityid");
        oldTreasureTypeId = new NamespacedKey(rewriteNamespace, "blep.treasuretypeid");

        oldCompassKey = new NamespacedKey(rewriteNamespace, "blep.compass");

        oldFishJournalId = new NamespacedKey(rewriteNamespace, "blep.fishjournalid");

//        oldKeys = Arrays.asList(oldFishIdKey, oldButtonIdKey, oldFishTypeId,
//                oldFishBagId, oldFishRodId, oldFishAreaId, oldTourneyTypeId, oldTourneyId, oldRarityId, oldTreasureTypeId, oldCompassKey, oldFishJournalId);
    }

    public static void UpdateOldKeys(PersistentDataContainer dataContainer){

        if(dataContainer.isEmpty())
            return;

        if(dataContainer.has(oldFishIdKey)){
            var oldVal = dataContainer.get(oldFishIdKey, PersistentDataType.INTEGER);
            dataContainer.set(FishIdKey, PersistentDataType.INTEGER, oldVal);
            dataContainer.remove(oldFishIdKey);
        }

//        if(dataContainer.has(oldButtonIdKey)){
//            var oldVal = dataContainer.get(oldButtonIdKey, PersistentDataType.STRING);
//            dataContainer.set(ButtonIdKey, PersistentDataType.STRING, oldVal);
//            dataContainer.remove(oldButtonIdKey);
//        }

        if(dataContainer.has(oldFishTypeId)){
            var oldVal = dataContainer.get(oldFishTypeId, PersistentDataType.STRING);
            dataContainer.set(FishTypeId, PersistentDataType.STRING, oldVal);
            dataContainer.remove(oldFishTypeId);
        }

        if(dataContainer.has(oldFishBagId)){
            var oldVal = dataContainer.get(oldFishBagId, PersistentDataType.INTEGER);
            dataContainer.set(FishBagId, PersistentDataType.INTEGER, oldVal);
            dataContainer.remove(oldFishBagId);
        }

        if(dataContainer.has(oldFishRodId)){
            var oldVal = dataContainer.get(oldFishRodId, PersistentDataType.INTEGER);
            dataContainer.set(FishRodId, PersistentDataType.INTEGER, oldVal);
            dataContainer.remove(oldFishRodId);
        }

        if(dataContainer.has(oldFishAreaId)){
            var oldVal = dataContainer.get(oldFishAreaId, PersistentDataType.STRING);
            dataContainer.set(FishAreaId, PersistentDataType.STRING, oldVal);
            dataContainer.remove(oldFishAreaId);
        }

        if(dataContainer.has(oldFishJournalId)){
            var oldVal = dataContainer.get(oldFishJournalId, PersistentDataType.INTEGER);
            dataContainer.set(FishJournalId, PersistentDataType.INTEGER, oldVal);
            dataContainer.remove(oldFishJournalId);
        }

        if(dataContainer.has(oldTourneyTypeId)){
            var oldVal = dataContainer.get(oldTourneyTypeId, PersistentDataType.STRING);
            dataContainer.set(TourneyTypeId, PersistentDataType.STRING, oldVal);
            dataContainer.remove(oldTourneyTypeId);
        }

        if(dataContainer.has(oldTourneyId)){
            var oldVal = dataContainer.get(oldTourneyId, PersistentDataType.INTEGER);
            dataContainer.set(TourneyId, PersistentDataType.INTEGER, oldVal);
            dataContainer.remove(oldTourneyId);
        }

        if(dataContainer.has(oldRarityId)){
            var oldVal = dataContainer.get(oldRarityId, PersistentDataType.STRING);
            dataContainer.set(RarityId, PersistentDataType.STRING, oldVal);
            dataContainer.remove(oldRarityId);
        }

        if(dataContainer.has(oldTreasureTypeId)){
            var oldVal = dataContainer.get(oldTreasureTypeId, PersistentDataType.STRING);
            dataContainer.set(TreasureTypeId, PersistentDataType.STRING, oldVal);
            dataContainer.remove(oldTreasureTypeId);
        }

        if(dataContainer.has(oldCompassKey)){
            var oldVal = dataContainer.get(oldCompassKey, PersistentDataType.INTEGER);
            dataContainer.set(CompassKey, PersistentDataType.INTEGER, oldVal);
            dataContainer.remove(oldCompassKey);
        }
    }

    public static boolean HasOldKeys(PersistentDataContainer dataContainer){
        return dataContainer.getKeys().stream().anyMatch(key -> key.getNamespace().equals(rewriteNamespace));
    }
}
