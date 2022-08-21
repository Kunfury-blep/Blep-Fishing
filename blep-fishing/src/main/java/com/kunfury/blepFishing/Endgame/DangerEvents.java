package com.kunfury.blepFishing.Endgame;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;

import java.util.Random;

public class DangerEvents {

    private EntityType[] waterEntities = new EntityType[]{EntityType.DROWNED, EntityType.ELDER_GUARDIAN, EntityType.GUARDIAN, EntityType.DOLPHIN};

    public void Trigger(Player p, Location iLoc){
        if(!AllBlueInfo.InAllBlue(iLoc) || !SpawnCheck()) return;
        int rnd = new Random().nextInt(waterEntities.length);

        SpawnMob(iLoc, waterEntities[rnd], p);
    }

    public void SpawnMob(Location loc, EntityType et, Player p){
        World w = loc.getWorld();

        if(w == null) return;

        int amt = 1;
        boolean drowned = false;

        switch(et){
            case ELDER_GUARDIAN -> {
                amt = new Random().nextInt(2) + 1;
            }
            case DROWNED -> {
                drowned = true;
                amt = new Random().nextInt(5) + 1;
            }
            case DOLPHIN -> {
                amt = 1;
            }
            case GUARDIAN -> {
                amt = new Random().nextInt(4) + 1;
            }
        }

        for(int i = 0; i < amt; i++){
            Entity e = w.spawnEntity(loc, et);
            if(drowned){
                ((Drowned) e).setTarget(p);
            }

        }
    }



    public boolean SpawnCheck(){
        int rolledAmt = new Random().nextInt(100);
        return rolledAmt <= EndgameVars.MobSpawnChance;
    }


}
