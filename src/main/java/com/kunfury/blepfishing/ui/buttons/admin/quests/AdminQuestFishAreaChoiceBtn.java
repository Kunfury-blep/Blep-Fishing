package com.kunfury.blepfishing.ui.buttons.admin.quests;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.objects.FishType;
import com.kunfury.blepfishing.objects.FishingArea;
import com.kunfury.blepfishing.objects.quests.QuestType;
import com.kunfury.blepfishing.ui.objects.buttons.AdminQuestMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.quests.AdminQuestFishAreasPanel;
import com.kunfury.blepfishing.ui.panels.admin.quests.AdminQuestFishTypesPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class AdminQuestFishAreaChoiceBtn extends AdminQuestMenuButton {

    FishingArea fishingArea;
    public AdminQuestFishAreaChoiceBtn(QuestType questType, FishingArea area){
        super(questType);
        fishingArea = area;
    }


    @Override
    public ItemStack buildItemStack(Player player) {
        Material mat = Material.RED_CONCRETE;
        boolean selected = false;
        int modelData = 0;
        if(questType.FishingAreaIds.contains(fishingArea.Id)){
            mat = Material.GREEN_CONCRETE;
            selected = true;
        }
        ItemStack item = new ItemStack(mat);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        setButtonTitle(m, fishingArea.Name);

        m.setCustomModelData(modelData);

        ArrayList<String> lore = new ArrayList<>();

        lore.add("");
        if(selected)
            lore.add(ChatColor.GREEN + "Enabled");
        else
            lore.add(ChatColor.RED + "Disabled");

        m.setLore(lore);

        m = setButtonId(m, getId());

        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        dataContainer.set(ItemHandler.FishAreaId, PersistentDataType.STRING, fishingArea.Id);

        item.setItemMeta(m);

        return item;
    }

    @Override
    protected void click_left() {
        fishingArea = getArea();
        questType = getQuestType();

        if(questType.FishingAreaIds.contains(fishingArea.Id)){
            questType.FishingAreaIds.remove(fishingArea.Id);
        }else{
            questType.FishingAreaIds.add(fishingArea.Id);
        }

        questType.ResetCatchList();

        ConfigHandler.instance.questConfig.Save();
        new AdminQuestFishAreasPanel(questType).Show(player);
    }

    private FishingArea getArea(){
        return FishingArea.FromId(ItemHandler.getTagString(ClickedItem, ItemHandler.FishAreaId));
    }

}
