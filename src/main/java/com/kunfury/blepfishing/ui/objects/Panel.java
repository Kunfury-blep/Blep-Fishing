package com.kunfury.blepfishing.ui.objects;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.helpers.Utilities;
import com.kunfury.blepfishing.ui.MenuHandler;
import com.kunfury.blepfishing.ui.buttons.footer.InfoButton;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public abstract class Panel {

    protected Inventory inv;
    protected String Title;
    protected int InventorySize;
    protected boolean FillInventory = true;
    protected boolean Refresh = false; //If enabled, refreshes the panel every second

    public Panel(String title, int inventorySize){
        Title = Formatting.formatColor(title);
        InventorySize = Utilities.getInventorySize(inventorySize);
        MenuHandler.SetupPanel(this);
    }

    public String getId(){
        return getClass().getName();
    }

    public void Show(Player player) {
        inv = Bukkit.createInventory(player, InventorySize, Title);
        BuildInventory(player);

        if(FillInventory){
            for(int i = 0; i < InventorySize; i++) {
                if(inv.getItem(i) == null)
                    inv.setItem(i, MenuHandler.getBackgroundItem());
            }
        }
        player.openInventory(inv);

        if(Refresh){
            new BukkitRunnable() {
                @Override
                public void run() {
                    if(!player.getOpenInventory().getTitle().equals(Title)){
                        cancel();
                        return;
                    }

                    inv = Bukkit.createInventory(player, InventorySize, Title);
                    BuildInventory(player);
                    player.openInventory(inv);
                }

            }.runTaskTimer(BlepFishing.getPlugin(), 0, 20);
        }


    }

    public void Show(CommandSender sender){
        Show((Player) sender);
    }

    protected abstract void BuildInventory(Player player);

    protected int slot = 0;
    protected void AddButton(MenuButton button){
        inv.setItem(slot, button.getItemStack());
        slot++;
    }

    protected void AddFooter(MenuButton backButton, MenuButton createButton, MenuButton deleteButton){
        if(getInfo() != null)
            inv.setItem(InventorySize - 5, new InfoButton(getInfo()).getItemStack());

        if(createButton != null)
            inv.setItem(InventorySize - 9, createButton.getItemStack());

        if(deleteButton != null)
            inv.setItem(InventorySize - 6, deleteButton.getItemStack());

        inv.setItem(InventorySize - 1, backButton.getBackButton());
    }

    protected List<String> getInfo(){
        return null;
    }

}
