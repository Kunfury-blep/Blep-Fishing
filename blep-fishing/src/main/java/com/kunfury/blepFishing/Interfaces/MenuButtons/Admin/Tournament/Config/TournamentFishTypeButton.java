package com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.Tournament.Config;

import com.kunfury.blepFishing.BlepFishing;
import com.kunfury.blepFishing.Config.FishTime;
import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Interfaces.Admin.AdminTournamentConfigMenu;
import com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.AdminConfigButtonBase;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Tournament.TournamentHandler;
import com.kunfury.blepFishing.Tournament.TournamentObject;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.conversations.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class TournamentFishTypeButton extends AdminConfigButtonBase {
    @Override
    public String getId() {
        return "adminTournamentConfigFishTypeButton";
    }

    @Override
    public String getPermission() {
        return "bf.admin";
    }

    @Override
    public ItemStack getItemStack(Object o) {
        if(!(o instanceof TournamentObject t))
            return null;

        ItemStack item = new ItemStack(Material.SALMON);
        ItemMeta m = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();

        assert m != null;
        m.setDisplayName("Fish Type");

        lore.add(t.FishType);

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

    private class NamePrompt extends ValidatingPrompt {

        @NotNull
        @Override
        public String getPromptText(@NotNull ConversationContext context) {
            return "What should the name be changed to? \nOptions: ALL, RANDOM, <Fish Name>";
        }
        @Override
        protected boolean isInputValid(@NotNull ConversationContext conversationContext, @NotNull String fishName) {
            if(fishName.equalsIgnoreCase("ALL") || fishName.equalsIgnoreCase("RANDOM"))
                return true;

            for(var f : Variables.BaseFishList){
                if(fishName.equalsIgnoreCase(f.Name))
                    return true;
            }

            return false;
        }

        @Nullable
        @Override
        protected Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull String fishName) {
            TournamentObject t =  getTournament();

            TournamentHandler.UpdateTournamentValue(t.getName() + ".Fish Type", fishName);
            t.FishType = fishName;

            AdminTournamentConfigMenu menu = new AdminTournamentConfigMenu();
            menu.ShowMenu(player, t);
            return END_OF_CONVERSATION;
        }
    }


}
