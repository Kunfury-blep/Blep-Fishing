package com.kunfury.blepFishing.Interfaces.Player;

import com.kunfury.blepFishing.BlepFishing;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Quests.QuestHandler;
import com.kunfury.blepFishing.Quests.QuestObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class QuestPanel {

    public void ClickBase(Player p){
        if(QuestHandler.getActiveQuests().size() == 0) NoQuestsFound(p);
        else ShowQuests(p);
    }

    private void NoQuestsFound(Player p){
        if(BlepFishing.configBase.getEnableQuests())
            p.sendMessage(Formatting.getFormattedMesage("Quests.noneActive"));
        else
            p.sendMessage(Formatting.getFormattedMesage("Quests.disabled"));

        new PlayerPanel().Show(p);

    }

    private void ShowQuests(Player p){
        Inventory inv = Bukkit.createInventory(null, 27, Formatting.getMessage("Player Panel.quests"));

        for(var q : QuestHandler.getActiveQuests()){
            inv.addItem(q.getItemStack(p.hasPermission("bf.admin"), p.getUniqueId().toString()));
        }

        p.openInventory(inv);
    }

    public void Click(InventoryClickEvent e, Player p){

        if(e.getClick().equals(ClickType.SHIFT_RIGHT)  && p.hasPermission("bf.admin")){
            int slot = e.getSlot();

            if(QuestHandler.getActiveQuests().size() >= slot + 1){
                QuestObject q = QuestHandler.getActiveQuests().get(slot);

                if(!q.isCompleted()){
                    new QuestHandler().CancelQuest(QuestHandler.getActiveQuests().get(slot));
                }
                ClickBase(p);
            }

        }
    }

}
