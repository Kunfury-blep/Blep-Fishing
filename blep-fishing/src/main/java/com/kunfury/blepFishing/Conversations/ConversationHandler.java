package com.kunfury.blepFishing.Conversations;

import com.kunfury.blepFishing.Objects.RarityObject;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;


//Using the Bukkit Conversation API: https://docs.google.com/document/d/1ofTMA6dv7Vk3v4kKhLTC1sUooHgTq71ti44AI09OsNM/edit
public class ConversationHandler {

    public static void StartRarityConversation(Player p, RarityObject rarity) {


        Conversation rarityConvo = new EditRarityConvo().getFactory(rarity).buildConversation(p);
        ConversationContext context = rarityConvo.getContext();

        if (rarity != null) {
            context.setSessionData("rarity-oldId", rarity.getId());
            context.setSessionData("rarity-newId", rarity.getId());
            context.setSessionData("rarity-weight", rarity.getWeight());
            context.setSessionData("rarity-prefix", rarity.getPrefix());
            context.setSessionData("rarity-priceMod", rarity.getPriceMod());
        }
        p.closeInventory();
        rarityConvo.begin();
    }
}
