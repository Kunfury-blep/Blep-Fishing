package com.kunfury.blepfishing.ui.buttons.player.quests;

import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.panels.player.quests.PlayerQuestPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PlayerQuestPanelBtn extends MenuButton {
    @Override
    public ItemStack buildItemStack(Player player) {
        ItemStack item = new ItemStack(Material.AXOLOTL_BUCKET);
        ItemMeta m = item.getItemMeta();
        assert m != null;






        List<String> lore = new ArrayList<>();
        //TODO: Convert to quests
        var activeQuests = Database.Quests.GetActive();
        if(activeQuests.isEmpty()){
            lore.add(Formatting.GetLanguageString("UI.Player.Buttons.Base.Quests.empty"));
        }else{
            for(var q : activeQuests){
                lore.add(Formatting.GetLanguageString("UI.Player.Buttons.Base.Quests.quest")
                                .replace("{name}", q.getType().Name)
                                .replace("{duration}", Formatting.asTime(q.getTimeRemaining(), ChatColor.WHITE)));
            }
        }

        var name = Formatting.GetLanguageString("UI.Player.Buttons.Base.Quests.name")
                .replace("{amount}", String.valueOf(activeQuests.size()));
        setButtonTitle(m, name);

        m.setLore(lore);
        item.setItemMeta(m);

        return item;
    }

    @Override
    protected void click_left() {
        new PlayerQuestPanel().Show(player);
    }
}
