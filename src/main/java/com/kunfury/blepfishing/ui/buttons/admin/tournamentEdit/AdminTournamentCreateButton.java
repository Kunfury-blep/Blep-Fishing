package com.kunfury.blepfishing.ui.buttons.admin.tournamentEdit;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.objects.FishType;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.ui.buttons.admin.fishEdit.AdminFishCreateButton;
import com.kunfury.blepfishing.ui.objects.MenuButton;
import com.kunfury.blepfishing.ui.panels.admin.fish.AdminFishEditPanel;
import com.kunfury.blepfishing.ui.panels.admin.fish.AdminFishPanel;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentEditPanel;
import com.kunfury.blepfishing.ui.panels.admin.tournaments.AdminTournamentPanel;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class AdminTournamentCreateButton extends MenuButton {
    @Override
    public ItemStack buildItemStack(Player player) {
        Material mat = Material.TURTLE_SCUTE;

        ItemStack item = new ItemStack(mat);
        ItemMeta m = item.getItemMeta();
        assert m != null;

        m.setDisplayName("Create New Tournament");
        ArrayList<String> lore = new ArrayList<>();
        m.setLore(lore);
        m = setButtonId(m, getId());
        item.setItemMeta(m);

        return item;
    }

    protected void click_left() {
        player.closeInventory();
        getConversation(player, new NewTournamentPrompt()).begin();    }

    private class NewTournamentPrompt extends StringPrompt {

        @NotNull
        @Override
        public String getPromptText(@NotNull ConversationContext context) {
            return Formatting.GetMessagePrefix() +  "What Should The New Tournament Be Named?";
        }

        @Nullable
        @Override
        public Prompt acceptInput(@NotNull ConversationContext conversationContext, @Nullable String tournamentName) {
            if(tournamentName == null){
                new AdminTournamentPanel().Show(player);
                return END_OF_CONVERSATION;
            }

            String initialId = Formatting.GetIdFromNames(tournamentName);

            int i = 0;
            String tournamentId = initialId;
            while(TournamentType.IdExists(tournamentId)){
                conversationContext.getForWhom().sendRawMessage(Formatting.GetMessagePrefix() + "Tournament with ID of " + tournamentId + " already exists. Changing to " + initialId + i);
                tournamentId = initialId + i;
                i++;
            }

            TournamentType tournamentType = new TournamentType(tournamentId, tournamentName);
            TournamentType.AddNew(tournamentType);

            ConfigHandler.instance.tourneyConfig.Save();
            new AdminTournamentEditPanel(tournamentType).Show(player);

            return END_OF_CONVERSATION;
        }
    }


}
