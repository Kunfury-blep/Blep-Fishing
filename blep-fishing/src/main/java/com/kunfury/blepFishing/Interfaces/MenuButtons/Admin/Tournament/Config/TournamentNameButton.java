package com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.Tournament.Config;

import com.kunfury.blepFishing.BlepFishing;
import com.kunfury.blepFishing.Conversations.EditRarityConvo;
import com.kunfury.blepFishing.Interfaces.Admin.AdminTournamentConfigMenu;
import com.kunfury.blepFishing.Interfaces.MenuButton;
import com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.AdminConfigButtonBase;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Tournament.TournamentHandler;
import com.kunfury.blepFishing.Tournament.TournamentObject;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class TournamentNameButton extends AdminConfigButtonBase {
    @Override
    public String getId() {
        return "adminTournamentConfigName";
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
        m.setDisplayName("Edit Name");

        lore.add(Formatting.formatColor(t.getName()));

        m.setLore(lore);
        item.setItemMeta(m);

        item = NBTEditor.set(item, getId(),"blep", "item", "buttonId");
        item = NBTEditor.set(item, t.getName() ,"blep", "item", "tourneyId");

        return item;
    }

    protected void click_left() {
        TournamentObject tourney = getTournament();

        Conversation convo = getFactory().buildConversation(player);

        player.closeInventory();
        convo.begin();
    }

    private ConversationFactory getFactory(){

        return new ConversationFactory(BlepFishing.getPlugin())
                .withFirstPrompt(new NamePrompt())
                .withModality(true)
                .withTimeout(60)
                .thatExcludesNonPlayersWithMessage("This Conversation Factory is Player Only");
    }

    private class NamePrompt extends StringPrompt {

        @NotNull
        @Override
        public String getPromptText(@NotNull ConversationContext context) {
            return "What should the name be changed to?";
        }

        @Nullable
        @Override
        public Prompt acceptInput(@NotNull ConversationContext context, @Nullable String name) {

            TournamentObject t =  getTournament();
            TournamentHandler.UpdateTournamentName(t.getName(), name);
            t.Name = name;

            AdminTournamentConfigMenu menu = new AdminTournamentConfigMenu();
            menu.ShowMenu(player, t);

            return END_OF_CONVERSATION;
        }
    }


}
