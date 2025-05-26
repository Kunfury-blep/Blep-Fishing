package com.kunfury.blepfishing.ui.buttons.admin.treasure;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.objects.treasure.Casket;
import com.kunfury.blepfishing.ui.objects.buttons.AdminTreasureRewardMenuButton;
import com.kunfury.blepfishing.ui.panels.admin.treasure.AdminTreasureEditRewardsSelectionPanel;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class TreasureEditRewardDropChanceBtn extends AdminTreasureRewardMenuButton {
    public TreasureEditRewardDropChanceBtn(Casket casket, Casket.TreasureReward reward) {
        super(casket, reward);
    }

    @Override
    public ItemStack buildItemStack(Player player) {
        ItemStack item = new ItemStack(Material.ANVIL);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName("Drop Chance");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(Formatting.GetLanguageString("UI.Admin.Buttons.Treasure.Rewards.Drop Chance.current")
                .replace("{amount}", String.valueOf(Reward.DropChance)));
        lore.add("");
        lore.add(Formatting.GetLanguageString("UI.Admin.Buttons.Treasure.Rewards.Announce.lore"));
        m.setLore(lore);
        m = setButtonId(m, getId());

        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        player.closeInventory();
        getConversation(player, new DropChancePrompt()).begin();
    }

    private class DropChancePrompt extends NumericPrompt {

        @NotNull
        @Override
        public String getPromptText(@NotNull ConversationContext context) {
            return Formatting.GetFormattedMessage("UI.Admin.Buttons.Treasure.Rewards.Drop Chance.prompt")
                    .replace("{amount}", String.valueOf(getReward().DropChance));
        }

        @Override
        protected boolean isNumberValid(@NotNull ConversationContext context, @NotNull Number input) {
            return input.intValue() > 0;
        }

        @Nullable
        @Override
        protected Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull Number number) {
            Casket casket = (Casket) getTreasureType();
            var reward = getReward();
            reward.DropChance = number.doubleValue();

            ConfigHandler.instance.treasureConfig.Save();
            new AdminTreasureEditRewardsSelectionPanel(casket, reward).Show(player);

            return END_OF_CONVERSATION;
        }
    }


}
