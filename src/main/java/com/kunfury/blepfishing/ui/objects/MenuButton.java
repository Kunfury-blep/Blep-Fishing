package com.kunfury.blepfishing.ui.objects;

import com.kunfury.blepfishing.BlepFishing;
import com.kunfury.blepfishing.helpers.Formatting;
import com.kunfury.blepfishing.helpers.Utilities;
import com.kunfury.blepfishing.items.ItemHandler;
import com.kunfury.blepfishing.objects.FishType;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.ui.MenuHandler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class MenuButton {

    protected static final NamespacedKey dayKey = new NamespacedKey(BlepFishing.getPlugin(), "blep.tournamentEdit.day");
    protected static final NamespacedKey timeKey = new NamespacedKey(BlepFishing.getPlugin(), "blep.tournamentEdit.time");
    protected static final NamespacedKey panelKey = new NamespacedKey(BlepFishing.getPlugin(), "blep.panel");
    protected static final NamespacedKey pageKey = new NamespacedKey(BlepFishing.getPlugin(), "blep.page");

    protected ItemStack ClickedItem;
    protected Player player;
    private PersistentDataContainer dataContainer;
    public String getId(){
        return getClass().getName();
    }

    public MenuButton(){
        MenuHandler.SetupButton(this);
    }

    protected void setButtonTitle(ItemMeta m, String title){
        m.setDisplayName(ChatColor.AQUA + title);
    }


    public void perform(InventoryClickEvent e){
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

    public String getPermission(){
        return null;
    }

    public ItemStack getItemStack(){
        ItemStack item = buildItemStack();
        ItemMeta m = item.getItemMeta();
        assert m != null;
        m = setButtonId(m, getId());


        item.setItemMeta(m);

        return item;
    }

    protected abstract ItemStack buildItemStack();

    public ItemStack getCustomItemStack(String name, List<String> lore, Material material){
        ItemStack item = getItemStack();

        if(material != null)
            item.setType(material);

        ItemMeta meta = item.getItemMeta();
        assert meta != null;

        if(name != null)
            meta.setDisplayName(name);

        List<String> itemLore = new ArrayList<>();

        if(lore != null)
            itemLore = lore;

        if(Utilities.DebugMode){
            itemLore.add("");
            itemLore.add(getId());
        }



        meta.setLore(itemLore);


        item.setItemMeta(meta);

        return item;
    }

    public ItemStack getBackButton(){
        return getCustomItemStack(Formatting.GetLanguageString("UI.System.Buttons.goBack"), new ArrayList<>(), Material.REDSTONE);
    }

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

    protected ItemMeta setButtonId(ItemMeta buttonMeta, String buttonId){
        buttonMeta.getPersistentDataContainer().set(ItemHandler.ButtonIdKey, PersistentDataType.STRING, buttonId);
        return buttonMeta;
    }

    protected PersistentDataContainer getDataContainer(ItemMeta meta){
        if(dataContainer == null)
            dataContainer = meta.getPersistentDataContainer();
        return dataContainer;
    }

    protected FishType getFishType(){
        String typeId = ItemHandler.getTagString(ClickedItem, ItemHandler.FishTypeId);
        return FishType.FromId(typeId);
    }


    protected TournamentType getTournamentType(){
        String typeId = ItemHandler.getTagString(ClickedItem, ItemHandler.TourneyTypeId);
        return TournamentType.FromId(typeId);
    }

    protected int getPage(){
        return ItemHandler.getTagInt(ClickedItem, pageKey);
    }
    protected Panel getPanel(){
        String panelId = ItemHandler.getTagString(ClickedItem, panelKey);
        for(Panel panel : MenuHandler.Panels.values()){
            if(panel.getId().equals(panelId)){
                return panel;
            }
        }
        return null;
    }
}
