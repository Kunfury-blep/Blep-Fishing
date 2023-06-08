package com.kunfury.blepFishing.Interfaces.MenuButtons;

import com.kunfury.blepFishing.BlepFishing;
import com.kunfury.blepFishing.Interfaces.Admin.AdminTournamentMenu;
import com.kunfury.blepFishing.Interfaces.MenuButton;
import com.kunfury.blepFishing.Interfaces.Player.QuestPanel;
import com.kunfury.blepFishing.Interfaces.Player.TournamentPanel;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Quests.QuestHandler;
import com.kunfury.blepFishing.Tournament.TournamentHandler;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PlayerQuestMenuButton extends MenuButton {
    @Override
    public String getId() {
        return "playerQuestMenu";
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public ItemStack getItemStack(Object o) {
        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(Formatting.getMessage("Player Panel.quests"));

        List<String> lore = new ArrayList<>();

        if(QuestHandler.getActiveQuests() != null && QuestHandler.getActiveQuests().size() > 0){
            lore.add(ChatColor.BLUE + " ~ " + QuestHandler.getActiveCount() + " Active ~ "); //TODO: Add top Translations

            if(player != null){
                lore.add("");
                for(var q : QuestHandler.getActiveQuests()){
                    int caughtAmt = 0;
                    String uuid = player.getUniqueId().toString();

                    if(q.getCatchMap().containsKey(uuid)){
                        caughtAmt = q.getCatchMap().get(uuid);
                    }

                    String qDesc = Formatting.formatColor(q.getName()) + " - " + caughtAmt + "/" + q.getCatchAmount() + " " + q.getFishTypeName();


                    if(q.isCompleted()){

                        for (ChatColor color : ChatColor.values()) {
                            qDesc = qDesc.replace(color.toString(), color + String.valueOf(ChatColor.STRIKETHROUGH));
                        }
                    }

                    lore.add(qDesc);
                }
            }

        }else{
            lore.add("");
            lore.add(Formatting.getMessage("Quests.noneActive"));
        }



        m.setLore(lore);

        item.setItemMeta(m);

        item = NBTEditor.set(item, getId(),"blep", "item", "buttonId");

        return item;
    }

    @Override
    protected void click_left() {
        new QuestPanel().ClickBase(player);
    }

    @Override
    protected void click_right(){
        if(BlepFishing.configBase.getEnableTournaments()){
            click_left();
            return;
        }

        TournamentHandler.EnableTournaments(true, player);
        new AdminTournamentMenu().ShowMenu(player);
    }
}
