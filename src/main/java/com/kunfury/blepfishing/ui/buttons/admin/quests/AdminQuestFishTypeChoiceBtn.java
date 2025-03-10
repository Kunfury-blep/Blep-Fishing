package com.kunfury.blepfishing.ui.buttons.admin.quests;

import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.objects.FishType;
import com.kunfury.blepfishing.objects.quests.QuestType;
import com.kunfury.blepfishing.ui.objects.buttons.AdminQuestMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.quests.AdminQuestFishTypesPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class AdminQuestFishTypeChoiceBtn extends AdminQuestMenuButton {

    FishType fishType;
    public AdminQuestFishTypeChoiceBtn(QuestType questType, FishType fishType){
        super(questType);
        this.fishType = fishType;
    }


    @Override
    public ItemStack buildItemStack(Player player) {
        Material mat = Material.RED_CONCRETE;
        boolean selected = false;
        int modelData = 0;
        if(questType.FishTypeIds.contains(fishType.Id)){
            mat = Material.SALMON;
            selected = true;
            modelData = fishType.ModelData;
        }
        ItemStack item = new ItemStack(mat);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        setButtonTitle(m, fishType.Name);

        m.setCustomModelData(modelData);

        ArrayList<String> lore = new ArrayList<>();

        if(selected)
            lore.add(ChatColor.GREEN + "Enabled");
        else
            lore.add(ChatColor.RED + "Disabled");

        lore.add("");

        m.setLore(lore);

        m = setButtonId(m, getId());

        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        dataContainer.set(ItemHandler.FishTypeId, PersistentDataType.STRING, fishType.Id);
        dataContainer.set(ItemHandler.QuestTypeId, PersistentDataType.STRING, questType.Id);

        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        FishType fishType = getFishType();
        questType = getQuestType();

        if(questType.FishTypeIds.contains(fishType.Id)){
            questType.FishTypeIds.remove(fishType.Id);
        }else{
            questType.FishTypeIds.add(fishType.Id);
        }

        questType.ResetCatchList();

        ConfigHandler.instance.questConfig.Save();
        new AdminQuestFishTypesPanel(questType).Show(player);
    }

}
