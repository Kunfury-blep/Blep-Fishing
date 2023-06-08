package com.kunfury.blepFishing.Endgame;

import com.kunfury.blepFishing.Config.ConfigBase;
import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Objects.AreaObject;
import com.kunfury.blepFishing.BlepFishing;
import io.github.bananapuncher714.nbteditor.NBTEditor;
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
import java.util.Objects;
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

            String sizeColor = "" + ChatColor.YELLOW;

            double sizeMod = AchieveAreas.size() / validAreas.size();

            if(sizeMod > .25){
                sizeColor += ChatColor.GOLD;
            }

            if(sizeMod > .5){
                sizeColor += ChatColor.GREEN;
            }

            if(sizeMod >= 1){
                sizeColor += ChatColor.DARK_GREEN;
            }

            m.setDisplayName(Formatting.getMessage("Endgame.Compass.piece")
                    .replace("{amount}", sizeColor + AchieveAreas.size() + ChatColor.WHITE + "/" + ChatColor.DARK_GREEN + validAreas.size()));
            ArrayList<String> lore = new ArrayList<>();
            lore.add(Formatting.getMessage("Endgame.Compass.pieceLore"));
            lore.add("");
            lore.add(Formatting.getMessage("Endgame.Compass.hint"));
            lore.add("");

            for(var area : AchieveAreas){
                lore.add(ChatColor.DARK_AQUA + area);
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

        if(!Objects.equals(cLoc.getWorld(), pLoc.getWorld())){ //Checks to make sure the player is in the world
            p.sendMessage(Formatting.getMessage("Endgame.Compass.Distance.world"));
            return;
        }


        double distance = pLoc.distance(cLoc);
        String distanceStr;

        if(Variables.DebugMode) p.sendMessage("Radius: "+ BlepFishing.configBase.getEndgameRadius());

        if(distance >= 10000) distanceStr = Formatting.getMessage("Endgame.Compass.Distance.veryFar");
        else if(distance >= 1000) distanceStr = Formatting.getMessage("Endgame.Compass.Distance.far");
        else if(distance > BlepFishing.configBase.getEndgameRadius()) distanceStr = Formatting.getMessage("Endgame.Compass.Distance.near");
        else distanceStr = Formatting.getMessage("Endgame.Compass.Distance.inside");

        if(Variables.DebugMode) p.teleport(cLoc);

        p.sendMessage(ChatColor.GRAY + distanceStr);

        if(ActivePlayers.contains(p) || distance < BlepFishing.configBase.getEndgameRadius()) return;
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!p.getInventory().contains(Material.COMPASS)){
                    if(ActivePlayers.contains(p)) ActivePlayers.remove(p);
                    cancel();
                }
                if(!ActivePlayers.contains(p)) ActivePlayers.add(p);

                if(!Objects.equals(cLoc.getWorld(), pLoc.getWorld())){
                    double dist = pLoc.distance(cLoc);
                    if(dist <= BlepFishing.configBase.getEndgameRadius()){
                        p.sendMessage(ChatColor.GRAY + Formatting.getMessage("Endgame.Compass.Distance.inside"));
                        if(ActivePlayers.contains(p)) ActivePlayers.remove(p);
                        cancel();
                    }
                }


            }

        }.runTaskTimer(BlepFishing.getPlugin(), 0, 60);
    }


    public ItemStack GenerateCompassPiece(Player p, Location loc, boolean spawned){
        if(!BlepFishing.configBase.getEnableAllBlue()) return null;

        List<AreaObject> availAreas = AreaObject.GetAreas(loc);
        List<AreaObject> trimmedAreas = new ArrayList<>();

        for(var a : availAreas){
            if(a.HasCompass)
                trimmedAreas.add(a);
        }

        if(!spawned && p.getInventory().contains(Material.PRISMARINE_CRYSTALS)){
            for (var slot : p.getInventory())
            {
                if (slot != null && slot.getType().equals(Material.PRISMARINE_CRYSTALS) && AllBlueInfo.IsCompass(slot) && availAreas != null && availAreas.size() > 0)
                {
                    //Makes sure the player doesn't get duplicate drops
                    trimmedAreas.removeIf(availArea -> NBTEditor.contains(slot, "blep", "item", "allBlueCompass_" + availArea.Name));
                }
            }
        }

        if(trimmedAreas.size() == 0) return null;

        int compRoll = new Random().nextInt(trimmedAreas.size());
        String area = trimmedAreas.get(compRoll).Name;

        ItemStack compass = new ItemStack(Material.PRISMARINE_CRYSTALS, 1);
        compass = NBTEditor.set(compass, true, "blep", "item", "allBlueCompass_" + area);
        compass = NBTEditor.set(compass, false, "blep", "item", "allBlueCompassComplete");

        ItemMeta m = compass.getItemMeta();
        m.setDisplayName(ChatColor.DARK_AQUA + area + " Compass Piece");
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

        lore.add(Formatting.getMessage("Endgame.Compass.pieceLore"));
        lore.add("");
        lore.add(Formatting.getMessage("Endgame.Compass.combine"));
        lore.add(Formatting.getMessage("Endgame.Compass.hint"));
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
        }else p.sendMessage(Formatting.getMessage("Endgame.Compass.noPieceHint"));

    }

    private void CompleteCompass(PrepareSmithingEvent e){
        ItemStack compass = new ItemStack(Material.COMPASS, 1);
        compass = NBTEditor.set(compass, true,"blep", "item", "allBlueCompassComplete");

        ItemMeta m = compass.getItemMeta();
        m.setDisplayName(Formatting.getMessage("Endgame.Compass.name"));
        m.setCustomModelData(1);
        ArrayList<String> lore = new ArrayList<String>();
        lore.add("");
        lore.add(Formatting.getMessage("Endgame.Compass.lore"));

        m.setLore(lore);
        compass.setItemMeta(m);

        e.setResult(compass);
    }

}
