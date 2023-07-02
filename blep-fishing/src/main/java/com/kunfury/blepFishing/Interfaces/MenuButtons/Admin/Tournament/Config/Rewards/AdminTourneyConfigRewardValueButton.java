package com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.Tournament.Config.Rewards;

import com.kunfury.blepFishing.Interfaces.Admin.AdminTournamentMenu;
import com.kunfury.blepFishing.Interfaces.MenuButton;
import com.kunfury.blepFishing.Miscellaneous.NBTEditor;
import org.apache.commons.lang3.EnumUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static com.kunfury.blepFishing.Interfaces.MenuButtons.Admin.Tournament.Config.Rewards.AdminTourneyConfigRewardButton.*;

public class AdminTourneyConfigRewardValueButton extends MenuButton  {
    @Override
    public String getId() {
        return "adminTournamentRewardsValue";
    }

    @Override
    public String getPermission() {
        return "bf.admin";
    }

    @Override
    public ItemStack getItemStack(Object o) {
        if(!(o instanceof RewardValue r))
            return null;

        ItemStack item = new ItemStack(Material.DIAMOND);
        ItemMeta m = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();

        m.setDisplayName(r.Type.toString());

        m.setLore(lore);
        item.setItemMeta(m);

        item = NBTEditor.set(item, getId(),"blep", "item", "buttonId");
        item = NBTEditor.set(item, r.TourneyId ,"blep", "item", "tourneyId");

        return item;
    }

    @Override
    protected void click_left() {
        AdminTournamentMenu menu = new AdminTournamentMenu();
        menu.ShowMenu(player);
    }

    public static class RewardValue{
        public String RewardKey;
        public String Value;
        public String TourneyId;

        public ValueType Type;

        public RewardValue(String rewardKey, String value, String tourneyId){
            RewardKey = rewardKey;
            TourneyId = tourneyId;

            String valueTypeStr = value.substring(0, 6);
            Value = value.replace(valueTypeStr, "");
            valueTypeStr =  valueTypeStr.replace(": ", "");

            if(EnumUtils.isValidEnum(ValueType.class, valueTypeStr)){
                Type = ValueType.valueOf(valueTypeStr);
            }
        }

        enum ValueType{
            CASH,
            TEXT,
            BYTE,
            ITEM
        }
    }
}

