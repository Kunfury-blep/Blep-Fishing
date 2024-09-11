package com.kunfury.blepfishing.ui.panels;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.objects.panels.PaginationPanel;
import com.kunfury.blepfishing.ui.buttons.equipment.FishBagFishButton;
import com.kunfury.blepfishing.objects.FishBag;
import com.kunfury.blepfishing.objects.FishObject;
import com.kunfury.blepfishing.objects.FishType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FishBagPanel extends PaginationPanel<FishType> {

    public static final List<FishBagPanel> BagPanels = new ArrayList<>();

    public final FishBag fishBag;
    public FishBagPanel(FishBag fishBag, int page){
        super("Fish Bag: " + fishBag.getAmount() + "/" + fishBag.getMax(),
                FishType.GetAll().size() + 9, page, null);
        this.fishBag = fishBag;
    }

    @Override
    public void Show(Player player) {
        super.Show(player);
        BagPanels.add(this);
    }

    @Override
    public void BuildInventory(Player player) {
        //Fills the bag async to avoid lagging the server when a lot of fish have been caught
        Bukkit.getScheduler().runTaskAsynchronously(BlepFishing.getPlugin(), () ->{
            FillFishBag(inv);
        });
    }

    @Override
    protected List<FishType> getContents() {
        return List.of();
    }

    @Override
    protected MenuButton getButton(FishType object) {
        return null;
    }

    private void FillFishBag(Inventory inv){
        final List<FishObject> parsedFish = BlepFishing.getDatabase().FishBags.GetAllFish(fishBag.Id);
        HashMap<FishType, List<FishObject>> fishMap = new HashMap<>();

        for(var f : parsedFish){
            FishType type = f.getType();
            if(!fishMap.containsKey(f.getType())){
                fishMap.put(type, new ArrayList<>());
            }
            fishMap.get(type).add(f);
        }
        List<ItemStack> bagItems = new ArrayList<>();

        for(var type : fishMap.keySet()){
            if(type == null)
                continue;

            bagItems.add(new FishBagFishButton(Page).buildItemStack(fishBag, type, fishMap.get(type)));
        }


        int i = 0;
        for(var fishItem : bagItems){
            if(fishItem == null)
                continue;

            inv.setItem(i, fishItem);
            i++;
        }
    }

    public static FishBagPanel GetPanelFromInventory(Inventory inv){
        for(var panel : BagPanels){
            if(panel.inv != inv)
                continue;
            return panel;
        }
        return null;
    }

    public static void TryClosePanel(Inventory inv){
        for(var panel : BagPanels){
            if(panel.inv != inv)
                continue;
            BagPanels.remove(panel);
            return;
        }
    }
}
