package com.kunfury.blepFishing.Interfaces.MenuButtons;

import com.kunfury.blepFishing.Config.ItemsConfig;
import com.kunfury.blepFishing.Interfaces.Admin.AdminQuestMenu;
import com.kunfury.blepFishing.Interfaces.MenuButton;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Quests.QuestHandler;
import com.kunfury.blepFishing.Quests.QuestObject;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class AdminQuestButton extends MenuButton {
    @Override
    public String getId() {
        return "adminQuestButton";
    }

    @Override
    public String getPermission() {
        return "bf.admin";
    }

    @Override
    public ItemStack getItemStack() {
        return null;
    }

    public ItemStack getItemStack(QuestObject q) {
        ItemStack item = new ItemStack(ItemsConfig.FishMat);
        ItemMeta m = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();

        m.setDisplayName(Formatting.formatColor(q.getName()));
        m.setCustomModelData(q.getModelData());

        lore.add("Duration: " + q.getDuration());
        lore.add("Fish: " + q.getFishTypeName());

        lore.add("");

        if(QuestHandler.getActiveQuests().contains(q)){
            lore.add(ChatColor.GREEN + "Running");
            lore.add("");
            lore.add("Right-Click to Cancel");
        }else{
            lore.add(ChatColor.RED + "Not Running");
            lore.add("");
            lore.add("Left-Click to Start");
        }

        m.setLore(lore);
        item.setItemMeta(m);

        item = NBTEditor.set(item, getId(),"blep", "item", "buttonId");
        item = NBTEditor.set(item, q.getName() ,"blep", "item", "questId");

        return item;
    }

    @Override
    protected void click_left() {
        QuestObject quest = getQuest();

        if(!QuestHandler.getActiveQuests().contains(quest)){
            new QuestHandler().Start(quest);
            new AdminQuestMenu().ShowMenu(player);
        }
    }

    @Override
    protected void click_right() {
        QuestObject quest = getQuest();

        if(QuestHandler.getActiveQuests().contains(quest)){
            new QuestHandler().CancelQuest(quest);
            new AdminQuestMenu().ShowMenu(player);
        }
    }

    private QuestObject getQuest(){

        return QuestHandler.FindQuest(NBTEditor.getString(ClickedItem, "blep", "item", "questId"));
    }

}
