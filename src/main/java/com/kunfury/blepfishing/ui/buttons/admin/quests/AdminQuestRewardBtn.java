package com.kunfury.blepfishing.ui.buttons.admin.quests;

import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.objects.quests.QuestType;
import com.kunfury.blepfishing.ui.objects.buttons.AdminQuestMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.quests.AdminQuestRewardsPanel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class AdminQuestRewardBtn extends AdminQuestMenuButton {


    public AdminQuestRewardBtn(QuestType questType){
        super(questType);
    }


    @Override
    protected ItemStack buildItemStack(Player player) {

        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        setButtonTitle(m,"Rewards");

        ArrayList<String> lore = new ArrayList<>();
        List<String> rewardStrings = new ArrayList<>();

        if(questType.CashReward > 0)
            rewardStrings.add(ChatColor.WHITE + "$" + ChatColor.GREEN + questType.CashReward);

        if(!questType.ItemRewards.isEmpty()){
            for(var rItem : questType.ItemRewards){
                String itemName = rItem.getType().name();
                ItemMeta rMeta = rItem.getItemMeta();

                if(rMeta != null && !rMeta.getDisplayName().isEmpty()){
                    itemName = rMeta.getDisplayName();
                }

                rewardStrings.add(rItem.getAmount() + "x " + itemName);
            }
        }
        lore.add(Formatting.ToCommaList(rewardStrings, ChatColor.WHITE, ChatColor.BLUE));


        m.setLore(lore);

        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        new AdminQuestRewardsPanel(getQuestType()).Show(player);
    }

}
