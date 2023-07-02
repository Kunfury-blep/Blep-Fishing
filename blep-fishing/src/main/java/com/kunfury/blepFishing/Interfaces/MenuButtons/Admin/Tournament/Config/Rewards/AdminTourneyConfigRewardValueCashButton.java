package com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.Tournament.Config.Rewards;

import com.kunfury.blepFishing.BlepFishing;
import com.kunfury.blepFishing.Interfaces.Admin.AdminTournamentConfigMenu;
import com.kunfury.blepFishing.Interfaces.Admin.AdminTournamentRewardsMenu;
import com.kunfury.blepFishing.Interfaces.Admin.AdminTournamentRewardsValueMenu;
import com.kunfury.blepFishing.Interfaces.MenuButton;
import com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.Tournament.Config.TournamentDurationButton;
import com.kunfury.blepFishing.Tournament.TournamentHandler;
import com.kunfury.blepFishing.Tournament.TournamentObject;
import com.kunfury.blepFishing.Miscellaneous.NBTEditor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AdminTourneyConfigRewardValueCashButton extends MenuButton  {

    int CashAmount;
    String RewardKey;

    public AdminTourneyConfigRewardValueCashButton(int cashAmount, String rewardKey){
        CashAmount = cashAmount;
        RewardKey = rewardKey;
    }

    public AdminTourneyConfigRewardValueCashButton(){
        this(0, "");
    }

    @Override
    public String getId() {
        return "adminTournamentRewardValueCash";
    }

    @Override
    public String getPermission() {
        return "bf.admin";
    }

    @Override
    public ItemStack getItemStack(Object o) {
        if(!(o instanceof TournamentObject t))
            return null;


        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta m = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();

        m.setDisplayName(ChatColor.GOLD + "Edit Cash Reward");

        if(CashAmount > 0){
            lore.add(ChatColor.WHITE + "Current: " + ChatColor.GREEN + CashAmount);
        }else{
            lore.add(ChatColor.RED + "No Cash Reward Set");
        }

        m.setLore(lore);
        item.setItemMeta(m);

        item = NBTEditor.set(item, getId(),"blep", "item", "buttonId");
        item = NBTEditor.set(item, t.getName(),"blep", "item", "tourneyId");
        item = NBTEditor.set(item, CashAmount, "blep", "item", "tourneyCash");
        item = NBTEditor.set(item, RewardKey, "blep", "item", "rewardId");

        return item;
    }

    @Override
    protected void click_left() {
        TournamentObject t = getTournament();

        Conversation convo = getFactory().buildConversation(player);

        player.closeInventory();
        convo.begin();
    }

    private ConversationFactory getFactory(){

        return new ConversationFactory(BlepFishing.getPlugin())
                .withFirstPrompt(new CashPrompt())
                .withModality(true)
                .withTimeout(60)
                .thatExcludesNonPlayersWithMessage("This Conversation Factory is Player Only");
    }

    private class CashPrompt extends NumericPrompt {

        @NotNull
        @Override
        public String getPromptText(@NotNull ConversationContext context) {
            int cash = NBTEditor.getInt(ClickedItem, "blep", "item", "tourneyCash");
            return "How much currency should be rewarded? \nCurrent: " + cash;
        }

        @Nullable
        @Override
        protected Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull Number cashValue) {
            TournamentObject t =  getTournament();

            int oldCash = NBTEditor.getInt(ClickedItem, "blep", "item", "tourneyCash");
            String rewardKey = NBTEditor.getString(ClickedItem, "blep", "item", "rewardId");



            t.Rewards.get(rewardKey).remove("CASH: " + oldCash);
            t.Rewards.get(rewardKey).add("CASH: " + cashValue);


            var menu = new AdminTournamentRewardsValueMenu();
            menu.ShowMenu(player, t, rewardKey);
            return END_OF_CONVERSATION;
        }
    }


}

