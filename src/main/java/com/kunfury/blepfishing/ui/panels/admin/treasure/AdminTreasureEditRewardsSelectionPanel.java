package com.kunfury.blepfishing.ui.panels.admin.treasure;

import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.objects.TreasureType;
import com.kunfury.blepfishing.ui.MenuHandler;
import com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit.TournamentEditRewardsBtn;
import com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit.TournamentEditRewardsCashBtn;
import com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit.TournamentEditRewardsSaveBtn;
import com.kunfury.blepfishing.ui.buttons.admin.treasure.*;
import com.kunfury.blepfishing.ui.objects.Panel;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTreasureRewardMenuButton;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class AdminTreasureEditRewardsSelectionPanel extends Panel {

    TreasureType type;
    TreasureType.TreasureReward reward;

    public AdminTreasureEditRewardsSelectionPanel(TreasureType type, TreasureType.TreasureReward reward){
        super(type.Name + " Reward", 18);
        this.type = type;
        this.reward = reward;

        FillInventory = false;
    }

    @Override
    public void BuildInventory(Player player) {
        AddButton(new TreasureEditRewardDropChanceBtn(type, reward));
        AddButton(new TreasureEditRewardAnnounceBtn(type, reward));

        ItemStack itemGuide = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);

        ItemMeta meta = itemGuide.getItemMeta();
        meta.setDisplayName(ChatColor.BLUE + "Item Reward");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.WHITE + "Place reward item in free slot");
        meta.setLore(lore);

        itemGuide.setItemMeta(meta);

        var backgroundItem = MenuHandler.getBackgroundItem().clone();
        backgroundItem.setType(Material.BLACK_STAINED_GLASS_PANE);

        for(int i = 0; i < InventorySize; i++) {
            if(inv.getItem(i) == null)
                inv.setItem(i, MenuHandler.getBackgroundItem());
        }

        inv.setItem(3, itemGuide);
        inv.setItem(4, reward.Item);
        inv.setItem(5, itemGuide);
        inv.setItem(13, itemGuide);

//        inv.setItem(InventorySize - 9, new TournamentEditRewardsCashBtn(type, placement).getItemStack());
        inv.setItem(InventorySize - 5, new TreasureEditRewardSaveBtn(type, reward).getItemStack());
        inv.setItem(InventorySize - 1, new TreasureEditRewardsBtn(type).getBackButton());
    }
}
