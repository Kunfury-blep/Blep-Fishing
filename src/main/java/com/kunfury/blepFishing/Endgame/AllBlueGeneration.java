package com.kunfury.blepFishing.Endgame;

import com.kunfury.blepFishing.Miscellaneous.Variables;
import com.kunfury.blepFishing.Objects.AllBlueObject;
import com.kunfury.blepFishing.Setup;
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
import java.util.logging.Logger;

public class AllBlueGeneration {

    public void Generate(InventoryClickEvent e){
        if(!(e.getWhoClicked() instanceof Player)) return;
        Player player = (Player) e.getWhoClicked();
        ItemStack comp = e.getCurrentItem();
        e.setCancelled(true);


        if(EndgameVars.Permanent && EndgameVars.AllBlueList != null && EndgameVars.AllBlueList.size() > 0){
            Location allBlue = EndgameVars.AllBlueList.get(0).getLocation();

            SetupCompass(allBlue, comp, player, e);
            return;
        }


        Location allBlue = new BiomeLocator().FindViableOcean(comp);
        if(allBlue == null){
            e.setCancelled(true);
            e.getWhoClicked().sendMessage(Variables.Prefix + ChatColor.RED + "No suitable ocean foundp. Please attempt again.");
            e.getWhoClicked().sendMessage(Variables.Prefix + ChatColor.RED + "If issue persists, let an admin know.");

            Logger log = Bukkit.getLogger();
            log.warning(Variables.Prefix + "No suitable ocean found for All Blue Generation. This should only happen if oceans do not exist within the world border./");
            return;
        }
        SaveAllBlue(allBlue);

        SetupCompass(allBlue, comp, player, e);

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

    private void SetupCompass(Location allBlue, ItemStack comp, Player player, InventoryClickEvent e){
        Location lodeLoc = allBlue;
        lodeLoc.setY(0);
        lodeLoc.getBlock().setType(Material.LODESTONE);

        CompassMeta cMeta = (CompassMeta) comp.getItemMeta();
        cMeta.setLodestoneTracked(true);
        cMeta.setLodestone(lodeLoc);
        comp.setItemMeta(cMeta);

        player.setItemOnCursor(comp);
        e.getClickedInventory().clear();
        player.updateInventory();
    }


}