package com.kunfury.blepfishing.ui.buttons.admin.fishEdit;

import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.panels.admin.fish.AdminFishPanel;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AdminFishPanelBtn extends MenuButton {

    public AdminFishPanelBtn(int page){
        super();
    }

    @Override
    public ItemStack buildItemStack(Player player) {
        ItemStack item = new ItemStack(Material.SALMON);
        ItemMeta m = item.getItemMeta();

        m.setDisplayName(Formatting.GetLanguageString("UI.Admin.Buttons.Base.fish"));
        m = setButtonId(m, getId());
        item.setItemMeta(m);

        return item;
    }

    @Override
    protected void click_left() {
        new AdminFishPanel(1).Show(player);
    }
}
