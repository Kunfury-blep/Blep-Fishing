package com.kunfury.blepfishing.ui.buttons.player.fish;

import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.helpers.ItemHandler;
import com.kunfury.blepfishing.objects.FishType;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class PlayerFishMissingBtn extends MenuButton {

    private final FishType fishType;

    public PlayerFishMissingBtn(FishType fishType){
        this.fishType = fishType;
    }

    @Override
    protected ItemStack buildItemStack(Player player) {
        ItemStack item = new ItemStack(Material.DEAD_TUBE_CORAL);
        ItemMeta m = item.getItemMeta();


        m.setDisplayName(ChatColor.AQUA + fishType.Name);
        m.setCustomModelData(fishType.ModelData);

        List<String> lore = new ArrayList<>();

        lore.add(Formatting.GetLanguageString("UI.Player.Buttons.Base.Fish.noneCaught"));

        m.setLore(lore);

        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        dataContainer.set(ItemHandler.FishTypeId, PersistentDataType.STRING, fishType.Id);
        //dataContainer.set(pageKey, PersistentDataType.INTEGER, page);

        item.setItemMeta(m);

        return item;
    }
}
