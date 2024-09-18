package com.kunfury.blepfishing.ui.buttons.player.tournament;

import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.helpers.Utilities;
import com.kunfury.blepfishing.objects.FishObject;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PlayerTournamentFishBtn extends MenuButton {

    FishObject fish;
    int rank;
    public PlayerTournamentFishBtn(FishObject fish, int rank){
        this.fish = fish;
        this.rank = rank;
    }

    @Override
    protected ItemStack buildItemStack() {
        ItemStack item = new ItemStack(Material.SALMON);
        ItemMeta m = item.getItemMeta();

        m.setDisplayName(ChatColor.WHITE + "#" + rank + " " +  ChatColor.AQUA + fish.getType().Name);
        m.setCustomModelData(fish.getType().ModelData);


        List<String> lore = new ArrayList<>();
        lore.add(Formatting.GetLanguageString("Fish.length")
                .replace("{size}", Formatting.DoubleFormat(fish.Length)));
        lore.add(Formatting.GetLanguageString("Fish.caught")
                .replace("{player}", fish.getCatchingPlayer().getDisplayName())
                .replace("{date}", Formatting.dateToString(fish.DateCaught)));

        m.setLore(lore);

        item.setItemMeta(m);

        return item;
    }
}
