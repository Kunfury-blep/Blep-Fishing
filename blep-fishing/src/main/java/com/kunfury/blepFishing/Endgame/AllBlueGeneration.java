package com.kunfury.blepFishing.Endgame;

import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Objects.AllBlueObject;
import com.kunfury.blepFishing.Setup;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Logger;

public class AllBlueGeneration {

    public void Generate(InventoryClickEvent e){
        if(!(e.getWhoClicked() instanceof Player)) return;
        Player player = (Player) e.getWhoClicked();
        e.setCancelled(true);

        ItemStack comp = CreateCompass(player);

        player.setItemOnCursor(comp);
        e.getClickedInventory().clear();
        player.updateInventory();
    }

    public ItemStack CreateCompass(Player player){
        ItemStack comp = new ItemStack(Material.COMPASS);

        Location allBlue;

        if(EndgameVars.Permanent && EndgameVars.AllBlueList != null && EndgameVars.AllBlueList.size() > 0) allBlue = EndgameVars.AllBlueList.get(0).getLocation();
        else allBlue = new BiomeLocator().FindViableOcean();

        if(allBlue == null){
            player.sendMessage(Variables.Prefix + ChatColor.RED + "No suitable ocean found. Please attempt again.");
            player.sendMessage(Variables.Prefix + ChatColor.RED + "If issue persists, let an admin know.");

            Logger log = Bukkit.getLogger();
            log.warning(Variables.Prefix + "No suitable ocean found for All Blue Generation. This should only happen if oceans do not exist within the world border./");
            return null;
        }

        SaveAllBlue(allBlue);

        Location lodeLoc = allBlue;
        lodeLoc.setY(0);
        lodeLoc.getBlock().setType(Material.LODESTONE);

        comp = NBTEditor.set(comp, true,"blep", "item", "allBlueCompassComplete");
        CompassMeta cMeta = (CompassMeta) comp.getItemMeta();
        cMeta.setLodestoneTracked(true);
        cMeta.setLodestone(lodeLoc);

        cMeta.setDisplayName(ChatColor.ITALIC + "" + ChatColor.AQUA + "Compass to the " + EndgameVars.AreaName);
        cMeta.setCustomModelData(1);
        ArrayList<String> lore = new ArrayList<String>();
        lore.add("");
        lore.add("It really does exist...");
        lore.add("");
        lore.add(ChatColor.AQUA + "Right-Click to " + ChatColor.YELLOW + ChatColor.ITALIC + "focus");
        cMeta.setLore(lore);
        comp.setItemMeta(cMeta);

        for(Player p : Bukkit.getServer().getOnlinePlayers()){
            if(!p.equals(player)){
                Location pLoc = p.getLocation();
                int finalX = pLoc.getBlockX() - allBlue.getBlockX();
                int finalZ = pLoc.getBlockZ() - allBlue.getBlockZ();

                String direction = " seas."; //Just in case the calc comes out to 0 for one of them

                if(finalX > 0 && finalZ > 0) direction = " west.";
                if(finalX < 0 && finalZ < 0) direction = " east.";
                if(finalX < 0 && finalZ > 0) direction = " north.";
                if(finalX > 0 && finalZ < 0) direction = " south.";
                p.sendMessage(ChatColor.GRAY + "You feel a warm breeze in the air and hear Seagulls in the distant" + direction);
            }
        }
        return comp;
    }

    private void SaveAllBlue(Location allBlue){
        EndgameVars.AllBlueList.add(new AllBlueObject(allBlue));

        try {
            String path = Setup.dataFolder + "/Data" + "/endgameArea.data";
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(path));

            output.writeObject(EndgameVars.AllBlueList);
            output.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}