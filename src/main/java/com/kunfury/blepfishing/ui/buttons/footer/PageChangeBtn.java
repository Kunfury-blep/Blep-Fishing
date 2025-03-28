package com.kunfury.blepfishing.ui.buttons.footer;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.objects.panels.PaginationPanel;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class PageChangeBtn<T> extends MenuButton {

    private final boolean next;
    private final PaginationPanel<T> panel;

    protected static final NamespacedKey nextKey = new NamespacedKey(BlepFishing.getPlugin(), "blep.page.next");

    public PageChangeBtn(PaginationPanel<T> panel, boolean next){
        this.next = next;
        this.panel = panel;
    }

    @Override
    public ItemStack buildItemStack(Player player) {
        ItemStack item = new ItemStack(Material.ORANGE_CANDLE);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        if(next){
            item.setType(Material.GREEN_CANDLE);
            m.setDisplayName(Formatting.GetLanguageString("UI.System.Buttons.pageNext"));
        }else
            m.setDisplayName(Formatting.GetLanguageString("UI.System.Buttons.pageBack"));


        getDataContainer(m).set(pageKey, PersistentDataType.INTEGER, panel.Page);
        getDataContainer(m).set(panelKey, PersistentDataType.STRING, panel.getId());
        getDataContainer(m).set(nextKey, PersistentDataType.BOOLEAN, next);

        item.setItemMeta(m);
        return item;
    }

    @Override
    protected void click_left() {

        var panel = getPanel();
        if(!(panel instanceof PaginationPanel)){
            return;
        }

        PaginationPanel<T> paginationPanel = (PaginationPanel<T>) panel;

        boolean next = ItemHandler.getTagBool(ClickedItem, nextKey);

        int page = ItemHandler.getTagInt(ClickedItem, pageKey);;
        if(next)
            page += 1;
        else
            page -= 1;

        paginationPanel.ChangePage(page);
        paginationPanel.Show(player);

    }
}
