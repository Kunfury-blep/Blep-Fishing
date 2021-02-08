package Conversations;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.ConversationPrefix;

import com.kunfury.blepFishing.Setup;

public class ConFactory {

	public ConversationFactory GetFactory() {
		ConversationFactory factory = 
				new ConversationFactory(Setup.getPlugin())
				.withTimeout(30)
				.withPrefix(new BlepFishingConPrefix())
				.thatExcludesNonPlayersWithMessage("Go away evil console!");
		
		return factory;
		
	}
	
	private class BlepFishingConPrefix implements ConversationPrefix {

		public String getPrefix(ConversationContext context) {
			return ChatColor.AQUA + "[BF] " + ChatColor.WHITE;
		}
    }
}
