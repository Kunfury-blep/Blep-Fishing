package com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.Tournament.Config;

import com.kunfury.blepFishing.BlepFishing;
import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Interfaces.Admin.AdminTournamentConfigMenu;
import com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.AdminConfigButtonBase;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Tournament.TournamentHandler;
import com.kunfury.blepFishing.Tournament.TournamentObject;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class TournamentBossbarPercentButton extends AdminConfigButtonBase {
    @Override
    public String getId() {
        return "adminTournamentConfigBossbarPercentButton";
    }

    @Override
    public String getPermission() {
        return "bf.admin";
    }

    @Override
    public ItemStack getItemStack(Object o) {
        if(!(o instanceof TournamentObject t))
            return null;

        ItemStack item = new ItemStack(Material.NAME_TAG);
        ItemMeta m = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();

        assert m != null;
        m.setDisplayName("Bossbar Perecent");

        lore.add(String.valueOf(t.BossbarPercent));

        m.setLore(lore);
        item.setItemMeta(m);

        item = NBTEditor.set(item, getId(),"blep", "item", "buttonId");
        item = NBTEditor.set(item, t.getName() ,"blep", "item", "tourneyId");

        return item;
    }

    protected void click_left() {
        Conversation convo = getFactory().buildConversation(player);

        player.closeInventory();
        convo.begin();
    }

    private ConversationFactory getFactory(){

        return new ConversationFactory(BlepFishing.getPlugin())
                .withFirstPrompt(new PercentPrompt())
                .withModality(true)
                .withTimeout(60)
                .thatExcludesNonPlayersWithMessage("This Conversation Factory is Player Only");
    }

    private class PercentPrompt extends ValidatingPrompt{

        @NotNull
        @Override
        public String getPromptText(@NotNull ConversationContext context) {
            return "What Completion Percent should the bossbar show? Current: " + getTournament().BossbarPercent; //TODO: Seems to sometimes have trailing values
        }

        @Override
        protected boolean isInputValid(@NotNull ConversationContext conversationContext, @NotNull String value) {
            if(!NumberUtils.isCreatable(value))
                return false;

            double percent = Double.parseDouble(value);

            return percent <= 100 && percent >= 0;
        }

        @Nullable
        @Override
        protected Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull String value) {
            TournamentObject t =  getTournament();

            double percent = Double.parseDouble(value);

            TournamentHandler.UpdateTournamentValue(t.getName() + ".Bossbar Percent", percent);
            t.BossbarPercent = percent;

            AdminTournamentConfigMenu menu = new AdminTournamentConfigMenu();
            menu.ShowMenu(player, t);
            return END_OF_CONVERSATION;
        }
    }


}
