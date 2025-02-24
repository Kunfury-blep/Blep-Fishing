package com.kunfury.blepfishing.ui.buttons.admin.fishEdit;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.ui.objects.buttons.AdminFishMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.fish.AdminFishEditPanel;
import com.kunfury.blepfishing.ui.panels.admin.fish.AdminFishPanel;
import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.objects.FishType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class AdminFishButton extends AdminFishMenuButton {
    private final int page;

    public AdminFishButton(FishType fishType, int page) {
        super(fishType);
        this.page = page;
    }

    @Override
    public ItemStack buildItemStack(Player player) {
        ItemStack item = new ItemStack(Material.SALMON);
        ItemMeta m = item.getItemMeta();

        m.setDisplayName(ChatColor.AQUA + fishType.Name);
        m = setButtonId(m, getId());
        m.setCustomModelData(fishType.ModelData);

        List<String> lore = new ArrayList<>();

        if(!fishType.Lore.isEmpty())
            lore.add(fishType.Lore);
        else
            lore.add(ChatColor.RED + "No Lore Set");

        lore.add("");

        lore.add(ChatColor.BLUE + "ID: " + ChatColor.WHITE + fishType.Id);
        lore.add(ChatColor.BLUE + "Model Data: " + ChatColor.WHITE + fishType.ModelData);
        lore.add(ChatColor.BLUE +"Base Price: " + ChatColor.WHITE + fishType.PriceBase);

        lore.add(ChatColor.BLUE +"Length: " + ChatColor.WHITE + fishType.LengthMin + ChatColor.BLUE + " - " + ChatColor.WHITE + fishType.LengthMax);

        if(fishType.HeightMin == -1000 && fishType.HeightMax == 1000){
            lore.add(ChatColor.BLUE +"Height: " + ChatColor.WHITE + "ANY");
        }else{
            lore.add(ChatColor.BLUE +"Height: " + ChatColor.WHITE + fishType.HeightMin + ChatColor.BLUE + " - " + ChatColor.WHITE + fishType.HeightMax);
        }

        StringBuilder areaNames = new StringBuilder();
        for(var a : fishType.AreaIds){
            if(!areaNames.toString().isEmpty()) areaNames.append(ChatColor.BLUE + ", ");
            areaNames.append(ChatColor.WHITE).append(a);
        }
        lore.add(ChatColor.BLUE +"Areas: " + areaNames);


        if(fishType.RequireRain)
            lore.add(ChatColor.BLUE +"Requires Rain");

        lore.add("");
        lore.add(ChatColor.YELLOW + "Left-Click to Edit");
        if(!fishType.ConfirmedDelete)
            lore.add( ChatColor.RED + "Shift Right-Click to Delete");
        else
            lore.add(ChatColor.RED + "Really Delete?");

        m.setLore(lore);

        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        dataContainer.set(ItemHandler.FishTypeId, PersistentDataType.STRING, fishType.Id);
        dataContainer.set(pageKey, PersistentDataType.INTEGER, page);

        item.setItemMeta(m);

        return item;
    }

    @Override
    protected void click_left() {
        FishType type = getFishType();
        new AdminFishEditPanel(type).Show(player);
    }

    @Override
    protected void click_right_shift() {
        FishType type = getFishType();

        if(!type.ConfirmedDelete){
            type.ConfirmedDelete = true;

            Bukkit.getScheduler ().runTaskLater (BlepFishing.getPlugin(), () ->{
                type.ConfirmedDelete = false;
            } , 300);
        }
        else{
            FishType.Delete(type);
            ConfigHandler.instance.fishConfig.Save();
        }

        new AdminFishPanel(getPage()).Show(player);
    }
}
