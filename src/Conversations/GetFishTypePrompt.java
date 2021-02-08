package Conversations;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;

import com.kunfury.blepFishing.Admin.AdminMenu;
import com.kunfury.blepFishing.Admin.TourneyAdmin;

import Miscellaneous.Variables;

public class GetFishTypePrompt {
	
	List<String> fishNames = new ArrayList<>();
	
	public GetFishTypePrompt(){
		Variables.BaseFishList.forEach(f -> {
			fishNames.add(f.Name);
		});
		fishNames.add("ALL");
	}
	
	public class InitialPrompt extends ValidatingPrompt  {
		
	    @Override
	    public String getPromptText(ConversationContext context) {
	        return "Which fish will the tournament be for? Enter \"ALL\" for all fish";
	    }

	    
		@Override
		protected Prompt acceptValidatedInput(ConversationContext context, String val) {
			//Conversation plugin = (Conversation) context.getPlugin();
			context.setSessionData("fish", val);
			return new FishFinishPrompt();
		}
		
		
		@Override
		protected String getFailedValidationText(ConversationContext context, String invalidInput) {
			
			return "That's not a fish type.";
		}


		@Override
		protected boolean isInputValid(ConversationContext context, String input) {
			boolean containsSearchStr = fishNames.stream().anyMatch(input::equalsIgnoreCase);
			return containsSearchStr;
		}
		
	};
	
	private class FishFinishPrompt extends MessagePrompt {

		@Override
		public String getPromptText(ConversationContext context) {
			Player p = (Player)context.getForWhom();
			String fish = (String)context.getSessionData("fish");
			fish = StringUtils.capitalize(fish.toLowerCase());
			new TourneyAdmin().UpdateTourney(p, null, fish, null, null);
			new AdminMenu().CreateTourneyGUI(p);
			
			return "Fish set to: " + fish;
		}

		@Override
		protected Prompt getNextPrompt(ConversationContext arg0) {
			return Prompt.END_OF_CONVERSATION;
		}
		
	}
	
	
}
