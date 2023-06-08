package com.kunfury.blepFishing.Interfaces;

import com.kunfury.blepFishing.BlepFishing;
import com.kunfury.blepFishing.Conversations.EditRarityConvo;
import com.kunfury.blepFishing.Objects.RarityObject;
import com.kunfury.blepFishing.Tournament.TournamentHandler;
import com.kunfury.blepFishing.Tournament.TournamentObject;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import net.minecraft.world.item.Items;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class MenuButton {

    protected ItemStack ClickedItem;
    protected Player player;
    public abstract String getId();

    public void perform(@NotNull InventoryClickEvent e){
        ClickedItem = e.getCurrentItem();
        player = (Player) e.getWhoClicked();
        ClickType clickType = e.getClick();

        if(clickType.isLeftClick()){
            if(clickType.isShiftClick()){
                click_left_shift();
            }else{
                click_left();
            }
        }
        if(clickType.isRightClick()){
            if(clickType.isShiftClick()){
                click_right_shift();
            }else{
                click_right();
            }
        }
    }

    public abstract String getPermission();

    public ItemStack getItemStack(){
        return getItemStack(null);
    }

    public abstract ItemStack getItemStack(Object o);

    protected void click_left(){

    }

    protected  void click_right(){
        click_left();
    }

    protected void click_left_shift(){
        click_left();
    }

    protected void click_right_shift(){
        click_right();
    }

    public TournamentObject getTournament(){
        return TournamentHandler.FindTournament(NBTEditor.getString(ClickedItem, "blep", "item", "tourneyId"));
    }
}
