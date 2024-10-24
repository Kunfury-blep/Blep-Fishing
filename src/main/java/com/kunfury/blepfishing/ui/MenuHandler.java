package com.kunfury.blepfishing.ui;

import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.objects.Panel;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;

public class MenuHandler {
    private static ItemStack backgroundItem;

    public static HashMap<String, MenuButton> MenuButtons = new HashMap<>();
    public static HashMap<String, Panel> Panels = new HashMap<>();

    public static void SetupButton(MenuButton btn){
        var btnId = btn.getId();
        if(MenuButtons.containsKey(btnId))
            return;

        MenuButtons.put(btnId, btn);
    }

    public static void SetupPanel(Panel panel){
        var panelId = panel.getId();
        if(Panels.containsKey(panelId))
            return;

        Panels.put(panelId, panel);
    }

    public static ItemStack getBackgroundItem(){
        if(backgroundItem == null){
            backgroundItem = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
            ItemMeta meta = backgroundItem.getItemMeta();
            assert meta != null;
            meta.setDisplayName(" ");
            meta.getPersistentDataContainer().set(ItemHandler.ButtonIdKey, PersistentDataType.STRING, "_background");
            backgroundItem.setItemMeta(meta);
        }
        return backgroundItem;
    }


}
