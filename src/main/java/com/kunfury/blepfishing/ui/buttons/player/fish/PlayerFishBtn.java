package com.kunfury.blepfishing.ui.buttons.player.fish;

import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.objects.FishObject;
import com.kunfury.blepfishing.objects.FishType;
import com.kunfury.blepfishing.objects.FishingArea;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class PlayerFishBtn extends MenuButton {

    private final FishType fishType;
    private final List<FishObject> caughtFish;

    public PlayerFishBtn(FishType fishType, List<FishObject> caughtFish){
        this.fishType = fishType;
        this.caughtFish = caughtFish;
    }

    @Override
    protected ItemStack buildItemStack() {
        ItemStack item = new ItemStack(Material.SALMON);
        ItemMeta m = item.getItemMeta();


        m.setDisplayName(ChatColor.AQUA + fishType.Name);
        m.setCustomModelData(fishType.ModelData);

        List<String> lore = new ArrayList<>();
        if(!fishType.Lore.isEmpty()){
            lore.add(fishType.Lore);
            lore.add("");
        }

        List<String> areaNames = new ArrayList<>();
        for(var id : fishType.AreaIds){
            FishingArea area = FishingArea.FromId(id);
            if(area == null)
                continue;

            areaNames.add(area.Name);
        }

        String areaLore = Formatting.GetLanguageString("UI.Player.Buttons.Base.Fish.area")
                        .replace("{areas}", Formatting.ToCommaList(areaNames, ChatColor.WHITE, ChatColor.BLUE));

        lore.addAll(Formatting.ToLoreList(areaLore));

        lore.add(Formatting.GetLanguageString("UI.Player.Buttons.Base.Fish.total")
                .replace("{amount}", String.valueOf(caughtFish.size())));




        m.setLore(lore);

        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        dataContainer.set(ItemHandler.FishTypeId, PersistentDataType.STRING, fishType.Id);
        //dataContainer.set(pageKey, PersistentDataType.INTEGER, page);

        item.setItemMeta(m);

        return item;
    }
}
