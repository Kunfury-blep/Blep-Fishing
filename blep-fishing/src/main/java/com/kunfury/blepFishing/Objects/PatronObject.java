package com.kunfury.blepFishing.Objects;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class PatronObject {
    public Patron PatronType;
    public String Name;
    private List<String> Message;


    public PatronObject(Patron _patronType, String _name, String lore1, String lore2, String lore3){
        PatronType = _patronType;
        Name = _name;

        Message = new ArrayList<>();
        Message.add(lore1);
        if(lore2 != null && !lore2.isEmpty()) Message.add(lore2);
        if(lore3 != null && !lore3.isEmpty()) Message.add(lore3);

    }

    public List<String> GetLore(){
        List<String> lore = new ArrayList();
        lore.addAll(Message);
        lore.add(ChatColor.AQUA +  "    -" + Name);
        return lore;
    }


}

