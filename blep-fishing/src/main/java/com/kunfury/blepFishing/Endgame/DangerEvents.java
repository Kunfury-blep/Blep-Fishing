package com.kunfury.blepFishing.Endgame;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;

import java.util.Random;

public class DangerEvents {

    private EntityType[] waterEntities = new EntityType[]{EntityType.DROWNED, EntityType.ELDER_GUARDIAN, EntityType.GUARDIAN, EntityType.DOLPHIN};

    public void Trigger(Player p, Location iLoc){
        if(!AllBlueInfo.InAllBlue(iLoc) || !SpawnCheck()) return;

        //TODO: Roll table for monster chance


        int rnd = new Random().nextInt(waterEntities.length);
        switch(waterEntities[rnd]){
            case ELDER_GUARDIAN -> SpawnMob(iLoc, waterEntities[rnd], p);
            case DROWNED -> spawnDrowned(iLoc, p);
        }
    }

    private void spawnDrowned(Location loc, Player p){

    }

    public void SpawnMob(Location loc, EntityType et, Player p){
        World w = loc.getWorld();

        Entity e = w.spawnEntity(loc, et); //TODO: Continue testing once minecraft servers return

        if(e instanceof Drowned){
            ((Drowned) e).setTarget(p);
        }
    }



    public boolean SpawnCheck(){
        int rolledAmt = new Random().nextInt(100);
        return rolledAmt <= EndgameVars.MobSpawnChance;
    }


}
