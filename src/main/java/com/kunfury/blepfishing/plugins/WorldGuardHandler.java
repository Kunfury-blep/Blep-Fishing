package com.kunfury.blepfishing.plugins;

import com.kunfury.blepfishing.objects.FishType;
import com.kunfury.blepfishing.objects.FishingArea;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.BooleanFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StringFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class WorldGuardHandler {
    public static Flag BF_AREA, BF_FISH, BF_ENDGAME, BF_FISHING;

    public void Load(){
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            // create a flag with the name "my-custom-flag", defaulting to true
            StringFlag areaFlag = new StringFlag("bf-area");
            registry.register(areaFlag);
            BF_AREA = areaFlag; // only set our field if there was no error

            StringFlag fishFlag = new StringFlag("bf-fish");
            registry.register(fishFlag);
            BF_FISH = fishFlag; // only set our field if there was no error

            BooleanFlag endgameFlag = new BooleanFlag("bf-endgame");
            registry.register(endgameFlag);
            BF_ENDGAME = endgameFlag;

            StateFlag fishingFlag = new StateFlag("bf-fishing", true);
            registry.register(fishingFlag);
            BF_FISHING = fishingFlag;

        } catch (FlagConflictException e) {
            // some other plugin registered a flag by the same name already.
            // you can use the existing flag, but this may cause conflicts - be sure to check type
            Flag<?> existing = registry.get("bf-area");
            if (existing instanceof StateFlag) { //TODO: FIX THIS!
                BF_AREA = existing;
            } else {
                Bukkit.getLogger().warning("Worldguard Blep Fishing not registered");
                // types don't match - this is bad news! some other plugin conflicts with you
                // hopefully this never actually happens
            }
        }
    }

    private WorldGuardPlugin getWorldGuard(){
        Plugin plugin = Bukkit.getPluginManager().getPlugin("WorldGuard");

        if(!(plugin instanceof WorldGuardPlugin))
            return null;

        return (WorldGuardPlugin) plugin;
    }

    private WorldEditPlugin getWorldEdit(){
        Plugin plugin = Bukkit.getPluginManager().getPlugin("WorldEdit");

        if(!(plugin instanceof WorldEditPlugin))
            return null;

        return (WorldEditPlugin) plugin;
    }

    private static Set<ProtectedRegion> getRegions(Location loc){
        var regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer().get(new BukkitWorld(loc.getWorld()));


        if(regionContainer != null){
            ApplicableRegionSet set = regionContainer.getApplicableRegions(BlockVector3.at(loc.getX(),loc.getY(),loc.getZ()));
            return set.getRegions();
        }

        return null;

    }

    public static List<FishType> GetFish(Location loc){
        var regions = getRegions(loc);

        List<FishType> fishTypes = new ArrayList<>();

        if(regions != null){
            for (var region : regions) {
                Map<Flag<?>, Object> flags = region.getFlags();

                flags.forEach((key, value) -> {
                    if(key.getName().equals("bf-fish")){
                        FishType fishType = FishType.FromId(String.valueOf(value));
                        if(fishType != null){
                            fishTypes.add(fishType);
                        }

                    }

                });
            }
        }
        return fishTypes;
    }

    public static List<FishingArea> GetAreas(Location loc){
        var regions = getRegions(loc);

        List<FishingArea> areaList = new ArrayList<>();

        if(regions != null){
            for (var region : regions) {
                Map<Flag<?>, Object> flags = region.getFlags();

                flags.forEach((key, value) -> {
                    if(key.getName().equals("bf-area")){
                        FishingArea area = FishingArea.FromId(String.valueOf(value));
                        if(area != null){
                            areaList.add(area);
                        }

                    }

                });
            }
        }

        return areaList;
    }

    public static boolean GetEndgame(Location loc){
        var regions = getRegions(loc);

        AtomicBoolean endgame = new AtomicBoolean(false);

        if(regions != null){
            for (var region : regions) {
                Map<Flag<?>, Object> flags = region.getFlags();

                flags.forEach((key, value) -> {
                    if(key.getName().equals("bf-endgane")){
                        if((boolean)value){
                            endgame.set(true);
                        }

                    }

                });
            }
        }

        return endgame.get();
    }

    public static boolean canFish(Location loc){
        World world = loc.getWorld();
        if(world == null) return true;

        com.sk89q.worldedit.util.Location wgLoc = new com.sk89q.worldedit.util.Location(BukkitAdapter.adapt(world), BukkitAdapter.asBlockVector(loc).toVector3());

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();

        return query.testState(wgLoc, null, (StateFlag) BF_FISHING);
    }
}
