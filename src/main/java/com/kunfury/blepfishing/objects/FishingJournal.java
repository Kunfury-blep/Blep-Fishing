package com.kunfury.blepfishing.objects;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.config.ConfigHandler;
import com.kunfury.blepfishing.database.Database;
import com.kunfury.blepfishing.helpers.Utilities;
import com.kunfury.blepfishing.items.ItemHandler;
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
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.ObjectInputFilter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class FishingJournal {

    public int Id = -1;
    public final UUID PlayerId;
    public LocalDateTime LastUpdate;

    public FishingJournal(UUID playerId){
        PlayerId = playerId;
        LastUpdate = LocalDateTime.now();
        Id = Database.FishingJournals.Add(this);
    }

    public FishingJournal(ResultSet resultSet) throws SQLException {
        Id = resultSet.getInt("id");
        PlayerId = UUID.fromString(resultSet.getString("playerId"));
        LastUpdate = Utilities.TimeFromLong(resultSet.getLong("lastUpdate"));
    }

    public static FishingJournal Get(ItemStack item) {
        if(!ItemHandler.hasTag(item, ItemHandler.FishJournalId))
            return null;

        int journalId = ItemHandler.getTagInt(item, ItemHandler.FishJournalId);

        return Database.FishingJournals.Get(journalId);
    }

    public void Update(){

    }



    public ItemStack GetItemStack(){
        ItemStack bookItem = new ItemStack(Material.WRITTEN_BOOK);

        BookMeta bookMeta = (BookMeta) bookItem.getItemMeta();
        assert bookMeta != null;

        Player player = Bukkit.getPlayer(PlayerId);
        assert player != null;

        PersistentDataContainer dataContainer = bookMeta.getPersistentDataContainer();
        dataContainer.set(ItemHandler.FishJournalId, PersistentDataType.INTEGER, Id);

        TextComponent playerPage = new TextComponent(player.getDisplayName() + "\n\n");
        playerPage.addExtra("Total Caught: " + Database.Fish.GetTotalCatchAmount(PlayerId.toString()));

        bookMeta.spigot().addPage(new BaseComponent[]{playerPage});

        if(ConfigHandler.instance.treasureConfig.getCompassEnabled()){
            TextComponent compassPage = new TextComponent(" -- Compass Pieces --\n");


            TextComponent compassPieceDetails = new TextComponent();

            int totalPieces = 0;
            int foundPieces = 0;
            int i = 1;

            for(var area : FishingArea.GetAll()){
                if(!area.HasCompassPiece)
                    continue;

                totalPieces++;
                String areaName = area.Name;

                //Hides the name of the compass piece if not yet found
                if(!Database.TreasureDrops.HasTreasure("compassPiece." + area.Id, PlayerId.toString()))
                    areaName = ChatColor.MAGIC + areaName;
                else
                    foundPieces++;

                TextComponent pieceComponent = new TextComponent(i + ". " + areaName + "\n");

                String hint;
                if(area.CompassHint.isEmpty())
                    hint = ChatColor.RED + "" + ChatColor.BOLD + "No Hint Set";
                else
                    hint = ChatColor.AQUA + area.CompassHint;

                pieceComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hint)));

                compassPieceDetails.addExtra(pieceComponent);

                i++;
            }

            compassPage.addExtra(foundPieces + "/" + totalPieces + " Found\n\n");
            compassPage.addExtra(compassPieceDetails);

            compassPage.addExtra("\n");
            compassPage.addExtra(ChatColor.ITALIC + "Hover Piece For Hint");

            bookMeta.spigot().addPage(new BaseComponent[]{compassPage});
        }
        
        var sortedFish = FishType.GetAll()
                .stream().sorted(Comparator.comparing(fishType -> fishType.Name)).toList();

        for(var fish : sortedFish){
            int catchAmt = Database.Fish.GetCatchAmount(fish.Id, PlayerId.toString());
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

    public static boolean IsJournal(ItemStack item){

        if(item == null || !item.hasItemMeta() || item.getType() != Material.WRITTEN_BOOK)
            return false;

        return ItemHandler.hasTag(item, ItemHandler.FishJournalId);
    }

    public static Integer GetId(ItemStack item){
        return ItemHandler.getTagInt(item, ItemHandler.FishJournalId);
    }


    public static ItemStack GetRecipeItem(){
        ItemStack journal = new ItemStack(Material.WRITTEN_BOOK, 1);

        BookMeta m = (BookMeta) journal.getItemMeta();
        assert m != null;

        PersistentDataContainer dataContainer = m.getPersistentDataContainer();
        dataContainer.set(ItemHandler.FishJournalId, PersistentDataType.INTEGER, -1);

        m.setDisplayName(ChatColor.AQUA + "Fishing Journal");

        //List<String> lore = new ArrayList<>();



        m.setCustomModelData(1);
        journal.setItemMeta(m);

        return journal;
    }

}