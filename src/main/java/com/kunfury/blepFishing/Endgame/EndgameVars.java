package com.kunfury.blepFishing.Endgame;

import com.kunfury.blepFishing.Objects.AllBlueObject;
import com.kunfury.blepFishing.Objects.AreaObject;

import java.util.ArrayList;
import java.util.List;

public class EndgameVars {

    public static boolean TreasureEnabled = true;
    public static int TreasureChance = 50;

    public static int AreaRadius = 32; //The size of the 'All Blue'. Larger sizes may make it more difficult to find an appropriate location
    public static String AreaName = "All Blue"; //Name of the All Blue. Easy to change for those who don't like the reference
    public static int MobSpawnChance = 25; //Chance of fishing up a mob while in the All Blue
    public static boolean Permanent = true;
    public static boolean Enabled = true;
    public static int AvailableFish = 2000; //Amount of fish in All Blue if it is not permanent
    public static AreaObject EndgameArea; //The area that the endgame area generation will look for.

    //TODO: Add all these to the config
    public static int CompassPieceMD = 1;
    public static int CompassMD = 1;
    public static int MessageBottleMD = 1;
    public static double FishSizeMod = 1.10;

    public static List<AllBlueObject> AllBlueList = new ArrayList<>();
//    public static final List<String> oceanBiomes = Arrays.asList(
//            "DEEP_LUKEWARM_OCEAN", "OCEAN", "DEEP_OCEAN", "LUKEWARM_OCEAN", "WARM_OCEAN");

}
