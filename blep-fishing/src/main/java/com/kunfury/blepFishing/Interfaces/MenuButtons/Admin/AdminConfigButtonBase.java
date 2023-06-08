package com.kunfury.blepFishing.Interfaces.MenuButtons.Admin;

import com.kunfury.blepFishing.BlepFishing;
import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Conversations.EditRarityConvo;
import com.kunfury.blepFishing.Interfaces.MenuButton;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Tournament.TournamentObject;
import org.bukkit.ChatColor;
import org.bukkit.conversations.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AdminConfigButtonBase extends MenuButton {

    protected static class ConvoPrefix implements ConversationPrefix {

        public @NotNull String getPrefix(ConversationContext context) {
            return Variables.getPrefix();
        }
    }
}
