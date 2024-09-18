package com.kunfury.blepfishing.ui.objects.panels;

import com.kunfury.blepfishing.ui.buttons.footer.PageChangeBtn;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.objects.Panel;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;

public abstract class PaginationPanel<T> extends Panel {

    public int Page;
    protected MenuButton backBtn;

    public PaginationPanel(String title, int inventorySize, int page, MenuButton backBtn) {
        super(title, inventorySize);
        Page = page;
        this.backBtn = backBtn;
    }

    @Override
    protected void  BuildInventory(Player player) {
        inv.clear();
        slot = 0;

        var pageContents = getContents();
        if(getContents().size() > 45){
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
        int endPos = Math.min(startPos + 45, getContents().size() - 1);

        for(int i = startPos; i < endPos; i++){
            pageContents.add(getContents().get(i));
        }

        return pageContents;
    }


    private List<T> contents;
    private List<T> getContents(){
        if(contents == null || contents.isEmpty()){
            contents = loadContents();
        }
        return contents;
    }
    protected abstract List<T> loadContents();

    protected abstract MenuButton getButton(T object);

    public void ChangePage(int page){
        int maxPage = (int) Math.ceil((double) getContents().size() / 45);
        if(page < 1){
            page = maxPage;
        } else if (page > maxPage) {
            page = 1;
        }

        Page = page;
    }
}
