package com.kunfury.blepFishing.CollectionLog;

import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Objects.CollectionLogObject;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.List;

public class JournalHandler {
    public void OpenJournal(Player p, ItemStack journal, PlayerInteractEvent e){
        e.setCancelled(true);
        BookMeta m = (BookMeta) journal.getItemMeta();


        m.setTitle(getTitle());
        CollectionLogObject log = new CollectionHandler().GetLog(p);

        List<String> pagesList = new ArrayList<>();
        pagesList.add(getFrontPage(log));

//        BaseComponent[] page = new ComponentBuilder("Click")
//                //.event(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://spigotmc.org"))
//                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/help"))
//                .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/help"))
//                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Thanks for hovering").create()))
//                .create();



        List<String> fishPages = new ArrayList<>();

        for(var f : log.getFishList()){
            //TODO: Sort the pages alphabetically by caught first
            if(f.IsCaught()) fishPages.add(f.getPage()); //TODO: Refactor this and below to only pull the list once

        }
        //pagesList.add(getGlossary(log));
        pagesList.addAll(fishPages);

        m.setPages(pagesList);
        //m.spigot().addPage(page);
        journal.setItemMeta(m);
        p.openBook(journal);
    }

    private String getTitle(){

        return "";
    }

    private String getFrontPage(CollectionLogObject log){
        String page = "";
        page += "Fish: "   +"0/" + Variables.BaseFishList.size();


        return page;
    }

    private String getGlossary(CollectionLogObject log){
        //TODO: Clickable links to the different sections of the book
        //TODO: Different sections technically seperate books and player is redirected to them
        String glossary = "";



//        for(var f : Variables.BaseFishList){ //Sort alphabetically
//            if(log.getFishList().stream().filter(fish -> f.Name.equals(fish.getName()) && fish.IsCaught()).findFirst().isPresent()){
//                glossary += "[" + f.Name + "]";
//            } else glossary += "[Unknown]";
//        }
        return glossary;
    }


}
