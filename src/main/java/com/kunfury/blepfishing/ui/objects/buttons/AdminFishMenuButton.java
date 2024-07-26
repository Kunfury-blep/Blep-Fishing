package com.kunfury.blepfishing.ui.objects.buttons;

import com.kunfury.blepfishing.objects.FishType;
import com.kunfury.blepfishing.objects.TournamentType;
import com.kunfury.blepfishing.ui.objects.MenuButton;

public abstract class AdminFishMenuButton extends MenuButton {

    protected FishType fishType;
    public AdminFishMenuButton(FishType fishType){
        this.fishType = fishType;
    }


    @Override
    public String getPermission(){
        return "bf.admin";
    }

}
