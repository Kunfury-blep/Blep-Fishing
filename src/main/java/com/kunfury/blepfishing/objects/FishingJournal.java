package com.kunfury.blepfishing.objects;

import com.kunfury.blepfishing.database.Database;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.Comparator;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class FishingJournal {

    private final UUID playerId;

    public FishingJournal(UUID playerId){
        this.playerId = playerId;
    }

    public void Update(){

    }



    public ItemStack GetItemStack(){
        ItemStack bookItem = new ItemStack(Material.WRITTEN_BOOK);

        BookMeta bookMeta = (BookMeta) bookItem.getItemMeta();
        assert bookMeta != null;

        Player player = Bukkit.getPlayer(playerId);
        assert player != null;

        TextComponent playerPage = new TextComponent(player.getDisplayName() + "\n\n");
        playerPage.addExtra("Total Caught: " + Database.Fish.GetTotalCatchAmount(playerId.toString()));

        bookMeta.spigot().addPage(new BaseComponent[]{playerPage});


        TextComponent compassPage = new TextComponent(" -- Compass Pieces --\n");
        compassPage.addExtra("4/7 Found\n\n");

        int i = 1;
        for(var area : FishingArea.GetAll()){

            String areaName = area.Name;
            if(ThreadLocalRandom.current().nextInt(0, 2) == 1)
                areaName = ChatColor.MAGIC + areaName;

            TextComponent pieceComponent = new TextComponent(i + ". " + areaName + "\n");
            pieceComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Compass Piece Hint for " + area.Name)));

            compassPage.addExtra(pieceComponent);

            i++;
        }

        bookMeta.spigot().addPage(new BaseComponent[]{compassPage});

        var sortedFish = FishType.GetAll()
                .stream().sorted(Comparator.comparing(fishType -> fishType.Name)).toList();

        for(var fish : sortedFish){
            int catchAmt = Database.Fish.GetCatchAmount(fish.Id, playerId.toString());
            if(catchAmt == 0)
                continue;

            TextComponent fishPage = new TextComponent(fish.Name + "\n\n");

            fishPage.addExtra("Total Caught: " + catchAmt + "\n");
            fishPage.addExtra("Largest: " + fish.LengthMax + "\n");
            fishPage.addExtra("Smallest: " + fish.LengthMin + "\n");

            bookMeta.spigot().addPage(new BaseComponent[]{fishPage});
        }

        bookMeta.setTitle("Fishing Journal");
        bookMeta.setAuthor(player.getDisplayName());

        bookItem.setItemMeta(bookMeta);
        return bookItem;
    }

}
