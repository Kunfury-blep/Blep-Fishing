package com.kunfury.blepFishing.Config;

import com.kunfury.blepFishing.Setup;

import java.io.File;

public class ConfigHandler {

    public static void Update(String key, String value){

    }

    public static void Update(String key, boolean value){

    }


    private void getConfig(String fileName){
        File configFile = new File(Setup.getPlugin().getDataFolder(), fileName);
        //TODO: Get current config. Copy from Reload
    }

    private void setConfig(){
    }
}
