package Conversations;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

import com.kunfury.blepFishing.Admin.AdminMenu;
import com.kunfury.blepFishing.Admin.TourneyAdmin;

public class GetTourneyTimePrompt {
	
	public class InitialPrompt extends NumericPrompt {
	    @Override
	    public String getPromptText(ConversationContext context) {
	        return "How long, in hours, will the tournament last?";
	    }

	    @Override
        protected boolean isNumberValid(ConversationContext context, Number input) {
            return input.intValue() > 0;
        }

	    
		@Override
		protected Prompt acceptValidatedInput(ConversationContext context, Number val) {
			//Conversation plugin = (Conversation) context.getPlugin();
			context.setSessionData("time", val);
			return new TourneyFinishPrompt();
		}
		
		@Override
		protected String getInputNotNumericText(ConversationContext context, String invalidInput) {
			
			return "You need to input a proper number.";
		}
		
		@Override
		protected String getFailedValidationText(ConversationContext context, Number invalidInput) {
			
			return "Sorry, the number must be a positive number.";
		}
		
	};
	
	private class TourneyFinishPrompt extends MessagePrompt {

		@Override
		public String getPromptText(ConversationContext context) {
			Player p = (Player)context.getForWhom();
			Integer time = (Integer)context.getSessionData("time");
			
			new TourneyAdmin().UpdateTourney(p, time, null, null, null);
			new AdminMenu().CreateTourneyGUI(p);
			
			return "Tournament time updated to: " + time;
		}

		@Override
		protected Prompt getNextPrompt(ConversationContext arg0) {
			return Prompt.END_OF_CONVERSATION;
		}
		
	}
	
	
}
