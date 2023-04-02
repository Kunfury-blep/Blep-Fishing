package com.kunfury.blepFishing.Conversations;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.ConversationPrefix;

import com.kunfury.blepFishing.BlepFishing;

public class ConFactory {

	public ConversationFactory GetFactory() {
		return new ConversationFactory(BlepFishing.getPlugin())
		.withTimeout(30)
		.withPrefix(new BlepFishingConPrefix())
		.thatExcludesNonPlayersWithMessage("Go away evil console!");
		
	}
	
	private static class BlepFishingConPrefix implements ConversationPrefix {
		public String getPrefix(ConversationContext context) {
			return ChatColor.AQUA + "[BF] " + ChatColor.WHITE;
		}
    }
}
