package com.kunfury.blepFishing.Endgame;

import com.kunfury.blepFishing.BlepFishing;
import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Objects.Patron;
import com.kunfury.blepFishing.Objects.PatronObject;
import com.kunfury.blepFishing.Objects.CasketObject;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.*;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static com.kunfury.blepFishing.Endgame.EndgameVars.MessageBottleMD;

public class TreasureHandler {

    public static List<CasketObject> CasketList = new ArrayList<>();
    public static int CasketTotalWeight;

    public ItemStack Perform(Player p, Location loc){
        if(!BlepFishing.configBase.getEnableTreasure()) return null;

        if(GetTreasureChance(p) <= BlepFishing.configBase.getTreasureChance()){
            switch(new Random().nextInt(3)){
                case 0: return new CompassHandler().GenerateCompassPiece(p, loc, false);
                case 1: return GetMessageBottle();
                case 2: return GetTreasureCasket();
                default: throw new IllegalStateException();
            }
        } else return null;
    }

    private int GetTreasureChance(Player p){
        int rolledAmt = new Random().nextInt(100);

        Entity pL = p.getShoulderEntityLeft();
        Entity pR = p.getShoulderEntityRight();

        boolean hasParrot =  (pL != null && pL.getType().equals(EntityType.PARROT)) || (pR != null && pR.getType().equals(EntityType.PARROT));
        boolean inBoat = p.isInsideVehicle() && p.getVehicle() instanceof Boat;

        if(hasParrot) rolledAmt -= BlepFishing.configBase.getParrotBonus();
        if(inBoat) rolledAmt -= BlepFishing.configBase.getBoatBonus();
        return rolledAmt;
    }

    public ItemStack GetMessageBottle(){
        if(!BlepFishing.configBase.getEnablePatrons())
            return GetTreasureCasket();

        ItemStack bottle = new ItemStack(Material.GLASS_BOTTLE, 1);
        ItemMeta m = bottle.getItemMeta();
        m.setDisplayName(Formatting.getMessage("Treasure.bottle"));
        m.setCustomModelData(MessageBottleMD);

        ArrayList<String> lore = new ArrayList<>();
        lore.add(Formatting.getMessage("Treasure.open"));
        m.setLore(lore);
        bottle.setItemMeta(m);
        bottle = NBTEditor.set(bottle, true, "blep", "item", "MessageBottle");
        return bottle;
    }

    public void OpenBottle(Player p, ItemStack bottle){
        bottle.setAmount(bottle.getAmount() - 1);
        int rolledAmt = new Random().nextInt(patrons.length);
        PatronObject pObj = patrons[rolledAmt];

        ItemStack message = new ItemStack(Material.PAPER,1);
        ItemMeta m = message.getItemMeta();
        m.setDisplayName(Formatting.getMessage("Treasure.letter"));
        m.setLore(pObj.GetLore());
        message.setItemMeta(m);
        p.getWorld().dropItem(p.getLocation(), message);
    }

    public ItemStack GetTreasureCasket(){
        if(CasketList == null || CasketList.size() <= 0) return null;

        int randR = ThreadLocalRandom.current().nextInt(0, CasketTotalWeight);
        CasketObject cObj = CasketList.get(0);
        for(final CasketObject casket : CasketList) {
            if(randR <= casket.Weight) {
                cObj = casket;
                break;
            }else
                randR -= casket.Weight;
        }
        return cObj.GetItemStack();
    }

    public void OpenCasket(Player p, ItemStack casket){
        if(casket.getAmount() == 1 || getEmptySlots(p) > 0){
            CasketObject tObj = CasketObject.GetTreasure(casket);
            if(tObj != null){
                tObj.Open(p, casket);
            }else p.sendMessage(Variables.getPrefix() + ChatColor.RED + "Unable to open Casket. Something is wrong with the config."); //TODO: Add to messages.yml
        } else p.sendMessage(Formatting.getFormattedMesage("System.inventoryFull"));

    }

    public static int getEmptySlots(Player p) {
        PlayerInventory inventory = p.getInventory();
        ItemStack[] cont = inventory.getStorageContents();
        int i = 0;
        for (ItemStack item : cont)
            if (item != null && item.getType() != Material.AIR) {
                i++;
            }
        return 36 - i;
    }

    PatronObject[] patrons = {
            new PatronObject(Patron.BOTTLE, "Captain Jake", "If there's one thing I've learned on these seas, ", "it's the only reason to look in your crews bowl", "is to make sure they have enough."),
            new PatronObject(Patron.BOTTLE, "Tito the Deck Swabbie", "I think I\'ve started to hallucinate...", "I swear these squids are glowing.", ""),
            new PatronObject(Patron.BOTTLE, "One-Eyed Red", "It\'s been days since I\'ve seen the sun,", "I\'m starving...", ""),
            new PatronObject(Patron.BOTTLE, "Peke", "Where are all the rum?!?", "Why is all the rum gone?!", ""),
            new PatronObject(Patron.BOTTLE, "King Birb", "If you like her, if she makes you happy,", "and if you feel like you know her.", "Then don't let her go."),
            new PatronObject(Patron.BOTTLE, "Nibrock", "I stayed up all night", "trying to figure out where the sun went,", "Then it dawned on me."),
    };

}

