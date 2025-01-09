package com.kunfury.blepfishing.ui.buttons.player.fish;

import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.panels.player.PlayerFishPanel;
import com.kunfury.blepfishing.ui.panels.player.PlayerTournamentPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PlayerFishPanelBtn extends MenuButton {

    public PlayerFishPanelBtn(Player player){
        this.player = player;
    }

    @Override
    public ItemStack buildItemStack(Player player) {
        ItemStack item = new ItemStack(Material.SALMON);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        List<String> lore = new ArrayList<>();
        lore.add(Formatting.GetLanguageString("UI.Player.Buttons.Base.Fish.total")
                .replace("{amount}", String.valueOf(Database.Fish.GetTotalCatchAmount(player.getUniqueId().toString()))));

        var name = Formatting.GetLanguageString("UI.Player.Buttons.Base.Fish.name");

        m.setLore(lore);
        m.setDisplayName(name);
        item.setItemMeta(m);

        return item;
    }

    @Override
    protected void click_left() {
        new PlayerFishPanel().Show(player);
    }
}
