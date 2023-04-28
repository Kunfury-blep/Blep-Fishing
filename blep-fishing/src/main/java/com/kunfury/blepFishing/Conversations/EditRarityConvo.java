package com.kunfury.blepFishing.Conversations;

import com.gmail.nossr50.datatypes.treasure.Rarity;
import com.kunfury.blepFishing.BlepFishing;
import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Objects.RarityObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EditRarityConvo {

    public ConversationFactory getFactory(RarityObject rarity){

        ConversationFactory factory = new ConversationFactory(BlepFishing.getPlugin())
                                        .withFirstPrompt(new WhichParamPrompt())
                                        .withModality(true)
                                        .withEscapeSequence("/quit")
                                        .withPrefix(new RarityConversationPrefix())
                                        .withTimeout(60)
                                        .thatExcludesNonPlayersWithMessage("This Conversation Factory is Player Only");

        if(rarity == null) //Goes straight to name if rarity doesn't already exist
            factory.withFirstPrompt(new NamePrompt());

        return factory;
    }

    private static class RarityConversationPrefix implements ConversationPrefix {

        public @NotNull String getPrefix(ConversationContext context) {
            String rarity = (String)context.getSessionData("rarity-oldId");
            if(rarity == null) return ChatColor.GREEN + "New Rarity: " + ChatColor.WHITE;
            return Formatting.formatColor(rarity) + ": " + ChatColor.WHITE;
        }
    }


    private static class WhichParamPrompt extends FixedSetPrompt {
        public WhichParamPrompt() {
            super("Name", "Weight", "Prefix", "Price Mod", "Cancel");
        }

        public @NotNull String getPromptText(ConversationContext context) {
            return "Which parameter would you like to change? " + formatFixedSet();
        }

        @Override
        protected boolean isInputValid(ConversationContext context, String input) {
            return fixedSet.stream().anyMatch(str -> str.equalsIgnoreCase(input));
        }

        @Override
        protected Prompt acceptValidatedInput(ConversationContext context, String s) {
            if (s.equals("Cancel")) {
                return Prompt.END_OF_CONVERSATION;
            }
            String param = s.toUpperCase();

            context.setSessionData("param", param);

            return switch (param) {
                case "NAME" -> new NamePrompt();
                case "WEIGHT" -> new WeightPrompt();
                case "PREFIX" -> new PrefixPrompt();
                case "PRICE MOD" -> new PriceModPrompt();
                default -> Prompt.END_OF_CONVERSATION;
            };

        }
    }

    private static class NamePrompt extends StringPrompt{

        @NotNull
        @Override
        public String getPromptText(@NotNull ConversationContext context) {
            return "What should the name be set to?";
        }

        @Nullable
        @Override
        public Prompt acceptInput(@NotNull ConversationContext context, @Nullable String s) {
            context.setSessionData("rarity-newId", s);
            return CheckParameters(context);
        }
    }

    private static class WeightPrompt extends NumericPrompt{

        @NotNull
        @Override
        public String getPromptText(@NotNull ConversationContext context) {
            return "What should the weight be set to? The lower the number the more rare.";
        }

        @Nullable
        @Override
        protected Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull Number number) {
            context.setSessionData("rarity-weight", number);
            return CheckParameters(context);
        }
    }

    private static class PrefixPrompt extends StringPrompt{

        @NotNull
        @Override
        public String getPromptText(@NotNull ConversationContext context) {
            return "What should the prefix be set to?";
        }

        @Nullable
        @Override
        public Prompt acceptInput(@NotNull ConversationContext context, @Nullable String s) {
            context.setSessionData("rarity-prefix", s);
            return CheckParameters(context);
        }
    }

    private static class PriceModPrompt extends NumericPrompt{

        @NotNull
        @Override
        public String getPromptText(@NotNull ConversationContext context) {
            return "What should the Price Mod be set to? This affects how much the fish will be worth.";
        }

        @Nullable
        @Override
        protected Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull Number number) {
            context.setSessionData("rarity-priceMod", number);
            return CheckParameters(context);
        }
    }

    private static class EndChoicePrompt extends FixedSetPrompt {
        public EndChoicePrompt() {
            super("Save", "Edit", "Cancel");
        }

        public @NotNull String getPromptText(ConversationContext context) {
            String endText = "Rarity Updated";
            endText += ChatColor.AQUA + "\n     Name: " + ChatColor.WHITE + context.getSessionData("rarity-newId");
            endText += ChatColor.AQUA + "\n     Weight: " + ChatColor.WHITE + context.getSessionData("rarity-weight");
            endText += ChatColor.AQUA + "\n     Prefix: " + ChatColor.WHITE + context.getSessionData("rarity-prefix");
            endText += ChatColor.AQUA + "\n     Price Mod: " + ChatColor.WHITE + context.getSessionData("rarity-priceMod");
            endText += "\n     What would you like to do next? " + formatFixedSet();
            return endText;
        }

        @Override
        protected boolean isInputValid(ConversationContext context, String input) {
            return fixedSet.stream().anyMatch(str -> str.equalsIgnoreCase(input));
        }

        @Override
        protected Prompt acceptValidatedInput(ConversationContext context, String s) {
            if (s.equals("Cancel")) {
                return CancelPrompt();
            }
            String param = s.toUpperCase();

            context.setSessionData("param", param);

            switch (param) {
                case "SAVE" -> {
                    if(ValidateContext(context))
                        return new SavePrompt();
                    return new ErrorPrompt();
                }
                case "EDIT" -> {
                    return new WhichParamPrompt();
                }
                default -> {
                    return Prompt.END_OF_CONVERSATION;
                }
            }

        }
    }

    private static class SavePrompt extends MessagePrompt{

        @Nullable
        @Override
        protected Prompt getNextPrompt(@NotNull ConversationContext context) {
            return Prompt.END_OF_CONVERSATION;
        }

        @NotNull
        @Override
        public String getPromptText(@NotNull ConversationContext context) {

            if(context.getSessionData("rarity-newId") == null){
                return "No name provided";
            }


            String oldId = (String) context.getSessionData("rarity-oldId");
            String newId = (String) context.getSessionData("rarity-newId");
            int rareWeight = (int) context.getSessionData("rarity-weight");
            String rarePrefix = (String) context.getSessionData("rarity-prefix");
            double rarePriceMod = Formatting.round ((float) context.getSessionData("rarity-priceMod"), 2);

            RarityObject rarity = RarityObject.GetRarity(oldId);


            if(RarityObject.Update(rarity, newId, rareWeight, rarePrefix, rarePriceMod) != null){
                return "Rarity Updated Successfully!";
            }
            return "Error Updating Rarity";
        }
    }

    private static class ErrorPrompt extends MessagePrompt{

        @Nullable
        @Override
        protected Prompt getNextPrompt(@NotNull ConversationContext context) {
            return new WhichParamPrompt();
        }

        @NotNull
        @Override
        public String getPromptText(@NotNull ConversationContext context) {

            if(context.getSessionData("rarity-newId") == null){
                return "No Rarity Name provided";
            }

            if(context.getSessionData("rarity-weight") == null){
                return "No Rarity Weight provided";
            }

            if(context.getSessionData("rarity-prefix") == null){
                return "No Rarity Prefix provided";
            }

            if(context.getSessionData("rarity-priceMod") == null){
                return "No Rarity Price Mod provided";
            }

            return "Error Updating Rarity";
        }
    }

    private static boolean ValidateContext(ConversationContext context){
        return (context.getSessionData("rarity-newId") != null) && (context.getSessionData("rarity-weight") != null)
                && (context.getSessionData("rarity-prefix") != null) && (context.getSessionData("rarity-priceMod") != null);
    }

    private static Prompt CancelPrompt(){

        return Prompt.END_OF_CONVERSATION;
    }

    private static Prompt CheckParameters(@NotNull ConversationContext context){
        if(context.getSessionData("rarity-newId") == null)
            return new NamePrompt();
        if(context.getSessionData("rarity-weight") == null)
            return new WeightPrompt();
        if(context.getSessionData("rarity-prefix") == null)
            return new PrefixPrompt();
        if(context.getSessionData("rarity-priceMod") == null)
            return new PriceModPrompt();


        return new EndChoicePrompt();
    }

}
