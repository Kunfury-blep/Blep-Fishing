package com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.Tournament.Config;

import com.kunfury.blepFishing.BlepFishing;
import com.kunfury.blepFishing.Interfaces.Admin.AdminTournamentConfigMenu;
import com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.AdminConfigButtonBase;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Tournament.TournamentHandler;
import com.kunfury.blepFishing.Tournament.TournamentObject;
import com.kunfury.blepFishing.Miscellaneous.NBTEditor;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class TournamentCooldownButton extends AdminConfigButtonBase {
    @Override
    public String getId() {
        return "adminTournamentConfigCooldownButton";
    }

    @Override
    public String getPermission() {
        return "bf.admin";
    }

    @Override
    public ItemStack getItemStack(Object o) {
        if(!(o instanceof TournamentObject t))
            return null;

        ItemStack item = new ItemStack(Material.CLOCK);
        ItemMeta m = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();

        assert m != null;
        m.setDisplayName("Cooldown");

        lore.add(Formatting.asTime((long) (t.Cooldown * 60 * 60 * 1000)));

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
                .withFirstPrompt(new DurationPrompt())
                .withModality(true)
                .withTimeout(60)
                .thatExcludesNonPlayersWithMessage("This Conversation Factory is Player Only");
    }

    private class DurationPrompt extends NumericPrompt{

        @NotNull
        @Override
        public String getPromptText(@NotNull ConversationContext context) {
            return "How many hours should the tournament wait before running again? Current: " + getTournament().Cooldown;
        }

        @Nullable
        @Override
        protected Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull Number cooldown) {
            TournamentObject t =  getTournament();

            TournamentHandler.UpdateTournamentValue(t.getName() + ".Cooldown", cooldown);
            t.Cooldown = cooldown.doubleValue();

            AdminTournamentConfigMenu menu = new AdminTournamentConfigMenu();
            menu.ShowMenu(player, t);
            return END_OF_CONVERSATION;
        }
    }


}
