package com.kunfury.blepFishing.Endgame;

import com.kunfury.blepFishing.Miscellaneous.Variables;
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
    PatronObject[] patrons = {
        new PatronObject(Patron.BOTTLE, "Captain Jake", "If there's one thing I've learned on these seas, ", "it's the only reason to look in your crews bowl", "is to make sure they have enough."),
        new PatronObject(Patron.BOTTLE, "Tito the Deck Swabbie", "I think I\'ve started to hallucinate...", "I swear these squids are glowing.", ""),
        new PatronObject(Patron.BOTTLE, "One-Eyed Red", "It\'s been days since I\'ve seen the sun,", "I\'m starving...", ""),
    };
    public static int CasketTotalWeight;

    public ItemStack Perform(Player p, Location loc){
        if(!EndgameVars.TreasureEnabled) return null;

        if(GetTreasureChance(p) <= EndgameVars.TreasureChance){
            switch(new Random().nextInt(3)){
                case 0: return new CompassHandler().GenerateCompassPiece(p, loc);
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

        if(hasParrot) rolledAmt -= Variables.ParrotBonus;
        if(inBoat) rolledAmt -= Variables.BoatBonus;
        return rolledAmt;
    }

    public ItemStack GetMessageBottle(){
        ItemStack bottle = new ItemStack(Material.GLASS_BOTTLE, 1);
        ItemMeta m = bottle.getItemMeta();
        m.setDisplayName(ChatColor.LIGHT_PURPLE + "Message in a Bottle");
        m.setCustomModelData(MessageBottleMD);

        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.AQUA + "Right-Click to " + ChatColor.YELLOW + ChatColor.ITALIC + "Open");
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
        m.setDisplayName(ChatColor.WHITE + "Crumpled Letter");
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
            }else p.sendMessage(Variables.Prefix + ChatColor.RED + "Unable to open Casket. Something is wrong with the config.");
        } else p.sendMessage(Variables.Prefix + ChatColor.RED + "Unable to open Casket. No space in inventory.");

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
}
