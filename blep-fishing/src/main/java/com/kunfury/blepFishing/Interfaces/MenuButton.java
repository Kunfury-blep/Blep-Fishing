package com.kunfury.blepFishing.Interfaces;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
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

    public abstract ItemStack getItemStack();

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
}
