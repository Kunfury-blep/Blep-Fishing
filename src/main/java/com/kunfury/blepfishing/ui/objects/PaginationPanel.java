package com.kunfury.blepfishing.ui.objects;

import com.kunfury.blepfishing.ui.buttons.footer.PageChangeBtn;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;

public abstract class PaginationPanel<T> extends Panel{

    public int Page;
    protected final List<T> Contents;
    protected MenuButton backBtn;

    public PaginationPanel(String title, int inventorySize, int page, MenuButton backBtn) {
        super(title, inventorySize);
        Contents = getContents();
        Page = page;
        this.backBtn = backBtn;
    }

    @Override
    protected void  BuildInventory(Player player) {
        inv.clear();
        slot = 0;

        var pageContents = Contents;
        if(Contents.size() > 45){
            pageContents = getPageContents();
            inv.setItem(InventorySize - 7, new PageChangeBtn<>(this, false).getItemStack());
            inv.setItem(InventorySize - 3, new PageChangeBtn<>(this, true).getItemStack());
        }

        for(var b : pageContents){
            AddButton(getButton(b));
        }

        inv.setItem(InventorySize - 1, backBtn.getBackButton());
    }

    protected List<T> getPageContents(){
        List<T> pageContents = new ArrayList<>();

        int startPos = 45 * (Page - 1);
        int endPos = Math.min(startPos + 45, Contents.size() - 1);

        for(int i = startPos; i < endPos; i++){
            pageContents.add(Contents.get(i));
        }

        return pageContents;
    }

    protected abstract List<T> getContents();

    protected abstract MenuButton getButton(T object);

    public void ChangePage(int page){
        int maxPage = (int) Math.ceil((double) Contents.size() / 45);
        if(page < 1){
            page = maxPage;
        } else if (page > maxPage) {
            page = 1;
        }

        Page = page;
    }
}
