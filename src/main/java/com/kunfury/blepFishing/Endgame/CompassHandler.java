package com.kunfury.blepFishing.Endgame;

import com.kunfury.blepFishing.Miscellaneous.Variables;
import com.kunfury.blepFishing.Objects.AreaObject;
import com.kunfury.blepFishing.Setup;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CompassHandler {

    public void CombinePieces(ItemStack main, ItemStack second, PrepareSmithingEvent e){
        ItemStack combination = main;
        List<String> AchieveAreas = new ArrayList<>();
        List<AreaObject> validAreas = new ArrayList<>();
        for(var area : Variables.AreaList){
            if(area.HasCompass){
                validAreas.add(area);
                boolean piece1 = NBTEditor.getBoolean(main, "blep", "item", "allBlueCompass_" + area.Name);
                boolean piece2 = NBTEditor.getBoolean(second, "blep", "item", "allBlueCompass_" + area.Name);

                if(piece1 || piece2){
                    combination = NBTEditor.set(combination, true,"blep", "item", "allBlueCompass_" + area.Name);
                    AchieveAreas.add(area.Name);
                }
            }

        }

        if(AchieveAreas.size() >= validAreas.size()){
            CompleteCompass(e);
            return;
        }

        ItemMeta m = combination.getItemMeta();
        //TODO: Rename pieces to dynamic name based on percantage completed
        if (m != null) {
            m.setDisplayName("Compass Pieces - " + AchieveAreas.size());
            ArrayList<String> lore = new ArrayList<>();
            lore.add("A piece of something great...");
            lore.add("");

            for(var area : AchieveAreas){
                lore.add(ChatColor.AQUA + area);
            }

            m.setLore(lore);
        }



        combination.setItemMeta(m);

        e.setResult(combination);
    }

    public static List<Player> ActivePlayers = new ArrayList<>();
    public void UseCompass(ItemStack comp, Player p){
        CompassMeta cMeta = (CompassMeta) comp.getItemMeta();
        assert cMeta != null : "Compass Meta is null.";
        Location cLoc = cMeta.getLodestone();
        Location pLoc = p.getLocation();
        cLoc.setY(pLoc.getY());

        double distance = pLoc.distance(cLoc);
        String distanceStr;

        if(Variables.DebugMode) p.sendMessage("Radius: "+ EndgameVars.AreaRadius);

        if(distance >= 10000) distanceStr = "You feel an incredible distance seperates you and your target.";
//        else if(distance >= 7500) distanceStr = "There is still a long journey in front of you.";
//        else if(distance >= 5000) distanceStr = "There is still a long journey in front of you.";
//        else if(distance >= 2500) distanceStr = "There is still a long journey in front of you.";
        else if(distance >= 1000) distanceStr = "You feel there is still a long journey in front of you.";
        else if(distance > EndgameVars.AreaRadius) distanceStr = "You feel you are very close.";
        else distanceStr = "You are in the " + EndgameVars.AreaName + ".";

        //if(Variables.DebugMode) p.teleport(cLoc);

        p.sendMessage(ChatColor.GRAY + distanceStr);

        if(ActivePlayers.contains(p) || distance < EndgameVars.AreaRadius) return;
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!p.getInventory().contains(Material.COMPASS)){
                    if(ActivePlayers.contains(p)) ActivePlayers.remove(p);
                    cancel();
                }
                if(!ActivePlayers.contains(p)) ActivePlayers.add(p);
                double dist = pLoc.distance(cLoc);
                if(dist <= EndgameVars.AreaRadius){
                    p.sendMessage(ChatColor.GRAY + "You have reached the " + EndgameVars.AreaName + "!");
                    if(ActivePlayers.contains(p)) ActivePlayers.remove(p);
                    cancel();
                }
            }

        }.runTaskTimer(Setup.getPlugin(), 0, 60);
    }


    public ItemStack GenerateCompassPiece(Player p, Location loc, boolean spawned){
        List<AreaObject> availAreas = AreaObject.GetAreas(loc);
        List<AreaObject> trimmedAreas = availAreas;

        if(!spawned && p.getInventory().contains(Material.PRISMARINE_CRYSTALS)){
            for (var slot : p.getInventory())
            {
                if (slot != null && slot.getType().equals(Material.PRISMARINE_CRYSTALS) && AllBlueInfo.IsCompass(slot) && availAreas != null && availAreas.size() > 0)
                {
                    for(int i = 0; i < availAreas.size(); i++){
                        if(!availAreas.get(i).HasCompass || NBTEditor.contains(slot,"blep", "item", "allBlueCompass_" + availAreas.get(i).Name)) //Makes sure the player doesn't get duplicate drops
                            trimmedAreas.remove(availAreas.get(i));
                    }
                }
            }
        }

        if(trimmedAreas == null || trimmedAreas.size() <= 0) return null;

        int compRoll = new Random().nextInt(trimmedAreas.size());
        String area = trimmedAreas.get(compRoll).Name;

        ItemStack compass = new ItemStack(Material.PRISMARINE_CRYSTALS, 1);
        compass = NBTEditor.set(compass, true, "blep", "item", "allBlueCompass_" + area);
        compass = NBTEditor.set(compass, false, "blep", "item", "allBlueCompassComplete");

        ItemMeta m = compass.getItemMeta();
        m.setDisplayName(area + " Compass Piece");
        m.setLore(GenerateLore(compass));
        m.setCustomModelData(Variables.AreaList.indexOf(area) + 1);
        compass.setItemMeta(m);


        return compass;
    }

    private ArrayList<String> GenerateLore(ItemStack compass) {
        DecimalFormat formatter = new DecimalFormat("#,###");


        //Keep track of how complete the compass is
        //Pull from size of available areas
        int completionPercent = 0;


        ArrayList<String> lore = new ArrayList<>();

        lore.add("A small piece of something great...");
        lore.add("");
        lore.add(ChatColor.AQUA + "Combine pieces at a " + ChatColor.YELLOW + ChatColor.ITALIC + "smithing table");

        return lore;
    }

    public void LocateNextPiece(ItemStack compass, Player p){
        AreaObject area = null;


        for(var a : Variables.AreaList){
            if(a.HasCompass && !NBTEditor.getBoolean(compass, "blep", "item", "allBlueCompass_" + a.Name)){
                area = a;
                break;
            }

        }

        if(area != null && area.CompassHint != null && !area.CompassHint.isEmpty()){
            p.sendMessage(ChatColor.GRAY + area.CompassHint);
        }else p.sendMessage(ChatColor.GRAY + "You could not gleam any further information from the pieces.");

    }

    private void CompleteCompass(PrepareSmithingEvent e){
        ItemStack compass = new ItemStack(Material.COMPASS, 1);
        compass = NBTEditor.set(compass, true,"blep", "item", "allBlueCompassComplete");

        ItemMeta m = compass.getItemMeta();
        m.setDisplayName(ChatColor.ITALIC + "" + ChatColor.AQUA + "Compass to the " + EndgameVars.AreaName);
        m.setCustomModelData(1);
        ArrayList<String> lore = new ArrayList<String>();
        lore.add("");
        lore.add("It really does exist...");

        m.setLore(lore);
        compass.setItemMeta(m);

        e.setResult(compass);
    }

}
