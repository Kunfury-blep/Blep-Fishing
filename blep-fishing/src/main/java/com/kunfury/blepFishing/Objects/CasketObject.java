package com.kunfury.blepFishing.Objects;

import com.kunfury.blepFishing.Endgame.TreasureHandler;
import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Setup;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CasketObject implements Comparable<CasketObject>{
    public String Name;
    public String Prefix;
    public Integer Weight;
    public int ModelData;
    private List<ItemStack> DropTable;

    public CasketObject(String _name, int _weight, String _prefix, int _modelData, List<ItemStack> _dropTable){
        Name = _name;
        Prefix = _prefix;
        Weight = _weight;
        ModelData = _modelData;
        DropTable = _dropTable;
    }

    public ItemStack GetItemStack(){
        ItemStack casket = new ItemStack(Material.CHEST, 1);
        ItemMeta m = casket.getItemMeta();
        m.setDisplayName(GetPrefix() + Name + "");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.AQUA + "Right-Click to " + ChatColor.YELLOW + ChatColor.ITALIC + "Open");
        m.setLore(lore);
        m.setCustomModelData(ModelData);
        casket.setItemMeta(m);

        casket = NBTEditor.set(casket, Name, "blep" , "item", "CasketType");

        return casket;
    }

    public String GetPrefix(){
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', '&' + Prefix);
    }

    @Override
    public int compareTo(CasketObject t) {
        return this.Weight.compareTo(t.Weight);
    }

    public static CasketObject GetTreasure(ItemStack casket){
        String _name = NBTEditor.getString(casket, "blep", "item", "CasketType");

        for(var t : TreasureHandler.CasketList){
            if(t.Name.equalsIgnoreCase(_name)) return t;
        }

        return null;
    }

    public void Open(Player p, ItemStack casket){
        int rolledAmt = new Random().nextInt(DropTable.size());
        ItemStack reward = DropTable.get(rolledAmt);
        String message = "";
        if(reward.getType().equals(Material.AIR)){
            if(Setup.hasEconomy()){
                Setup.getEconomy().depositPlayer(p, reward.getAmount());
                message = ChatColor.GREEN + Setup.getEconomy().format(reward.getAmount());
            } else {
                p.sendMessage(Variables.Prefix + ChatColor.RED + "Unable to open Casket. You would have found currency but no economy was found.");
                return;
            }
        } else{
            p.getInventory().addItem(reward);

            message += reward.getAmount() + "x ";

            String dName = reward.getItemMeta().getDisplayName();
            if(dName != null && !dName.isEmpty()) message += dName;
            else{
                message += reward.getType().name().replace("_", " ").toLowerCase();
            }
        }
        casket.setAmount(casket.getAmount() - 1);
        p.playSound(p.getLocation(), Sound.ITEM_BUNDLE_DROP_CONTENTS, .33f, 1f);

        p.sendMessage(Variables.Prefix + "You found " + message + ChatColor.WHITE + " inside the " + GetPrefix() + Name + '!');
    }
}
