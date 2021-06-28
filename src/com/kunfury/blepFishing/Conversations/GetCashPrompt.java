package com.kunfury.blepFishing.Conversations;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

import com.kunfury.blepFishing.Admin.AdminMenu;
import com.kunfury.blepFishing.Admin.TourneyAdmin;

public class GetCashPrompt {

	public class InitialPrompt extends NumericPrompt {
		/**
		 * The initial prompt text
		 * @param context the conversation context
		 */
	    @Override
	    public String getPromptText(ConversationContext context) {
	        return "How much would you like to reward?";
	    }

		/**
		 * Checks if the number is valid
		 * @param context text context
		 * @param input the input the player made
		 * @return true if the number is valid
		 */
	    @Override
        protected boolean isNumberValid(ConversationContext context, Number input) {
            return input.intValue() > 0;
        }

	    
		@Override
		protected Prompt acceptValidatedInput(ConversationContext context, Number val) {
			//Conversation plugin = (Conversation) context.getPlugin();
			context.setSessionData("cash", val);
			return new CashFinishPrompt();
		}
		/**
		 * Just a string that tells the player that the number that got input is not valid
		 * @param context text received from player
		 * @param invalidInput a number that is invalid
		 * @return A fail text
		 */
		@Override
		protected String getInputNotNumericText(ConversationContext context, String invalidInput) {
			
			return "You need to input a proper number.";
		}

		/**
		 * Just a string that tells the player that the number that got input is not valid
		 * @param context text received from player
		 * @param invalidInput a number that is invalid
		 * @return A fail text
		 */
		@Override
		protected String getFailedValidationText(ConversationContext context, Number invalidInput) {
			
			return "Sorry, the number must be a positive number.";
		}
		
	};
	
	private class CashFinishPrompt extends MessagePrompt {
		/**
		 * Get's the prompt text and updates the cash reward
		 * @param context get's the text's context
		 * @return The updated tournament reward
		 */
		@Override
		public String getPromptText(ConversationContext context) {
			Player p = (Player)context.getForWhom();
			Integer cash = (Integer)context.getSessionData("cash");
			
			new TourneyAdmin().UpdateTourney(p, null, null, null, cash);
			new AdminMenu().CreateTourneyGUI(p);
			
			return "com.kunfury.blepFishing.Tournament cash reward updated to: " + cash;
		}

		/**
		 * Ends the conversation
		 * @param arg0 Conversation Context
		 * @return The end of the conversation
		 */
		@Override
		protected Prompt getNextPrompt(ConversationContext arg0) {
			return Prompt.END_OF_CONVERSATION;
		}
		
	}
	
	
}
