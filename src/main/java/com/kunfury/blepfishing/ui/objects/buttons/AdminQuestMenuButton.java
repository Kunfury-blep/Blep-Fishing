package com.kunfury.blepfishing.ui.objects.buttons;

import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.objects.quests.QuestType;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public abstract class AdminQuestMenuButton extends MenuButton {

    protected QuestType questType;
    public AdminQuestMenuButton(QuestType questType){
        this.questType = questType;
    }

    @Override
    public ItemStack getItemStack(Player player) {
        ItemStack item = buildItemStack(player);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        dataContainer.set(ItemHandler.QuestTypeId, PersistentDataType.STRING, questType.Id);

        m = setButtonId(m, getId());
        item.setItemMeta(m);
        return item;
    }

    @Override
    public String getPermission(){
        return "bf.admin";
    }

    protected QuestType getQuestType(){
        String typeId = ItemHandler.getTagString(ClickedItem, ItemHandler.QuestTypeId);
        return QuestType.FromId(typeId);
    }

}
