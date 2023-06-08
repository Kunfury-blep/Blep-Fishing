package com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.Tournament.Config.Rewards;

import com.kunfury.blepFishing.BlepFishing;
import com.kunfury.blepFishing.Interfaces.Admin.AdminTournamentConfigMenu;
import com.kunfury.blepFishing.Interfaces.Admin.AdminTournamentRewardsMenu;
import com.kunfury.blepFishing.Interfaces.Admin.AdminTournamentRewardsValueMenu;
import com.kunfury.blepFishing.Interfaces.MenuButton;
import com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.Tournament.Config.TournamentNameButton;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Tournament.TournamentHandler;
import com.kunfury.blepFishing.Tournament.TournamentObject;
import com.patreon.resources.Reward;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AdminTourneyConfigRewardValueTextButton extends MenuButton  {

    String RewardText;
    String RewardKey;
    public AdminTourneyConfigRewardValueTextButton(String rewardText, String rewardKey){
        RewardText = rewardText;
        RewardKey = rewardKey;
    }

    public AdminTourneyConfigRewardValueTextButton(){
        this("", "");
    }
    @Override
    public String getId() {
        return "adminTournamentRewardValueText";
    }

    @Override
    public String getPermission() {
        return "bf.admin";
    }

    @Override
    public ItemStack getItemStack(Object o) {
        if(!(o instanceof TournamentObject t))
            return null;


        ItemStack item = new ItemStack(Material.OAK_SIGN);
        ItemMeta m = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();

        m.setDisplayName(ChatColor.YELLOW + "Edit Reward Message");

        if(RewardText.isEmpty()){
            lore.add(ChatColor.RED +  "No Text Reward Set");
        }else{
            lore.add(ChatColor.WHITE + Formatting.formatColor(RewardText));
        }

        m.setLore(lore);
        item.setItemMeta(m);

        item = NBTEditor.set(item, getId(),"blep", "item", "buttonId");
        item = NBTEditor.set(item, t.getName(),"blep", "item", "tourneyId");
        item = NBTEditor.set(item, RewardText, "blep", "item", "tourneyText");
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
                .withFirstPrompt(new TextPrompt())
                .withModality(true)
                .withTimeout(60)
                .thatExcludesNonPlayersWithMessage("This Conversation Factory is Player Only");
    }

    private class TextPrompt extends StringPrompt {

        @NotNull
        @Override
        public String getPromptText(@NotNull ConversationContext context) {
            return "What message should players receive for this rank?";
        }

        @Nullable
        @Override
        public Prompt acceptInput(@NotNull ConversationContext context, @Nullable String text) {
            TournamentObject t =  getTournament();

            String oldText = NBTEditor.getString(ClickedItem, "blep", "item", "tourneyText");

            String rewardKey = NBTEditor.getString(ClickedItem, "blep", "item", "rewardId");

            var reward = t.Rewards.get(rewardKey);

            if(oldText != null){
                reward.remove("TEXT: " + oldText);
            }

            reward.add("TEXT: " + text);

            var menu = new AdminTournamentRewardsValueMenu();
            menu.ShowMenu(player, t, rewardKey);
            return END_OF_CONVERSATION;
        }
    }


}

