package com.kunfury.blepFishing.Miscellaneous;

import net.minecraft.core.BlockPosition;
import net.minecraft.core.Holder;
import net.minecraft.core.IRegistry;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.chunk.Chunk;
import net.minecraft.world.level.chunk.ChunkSection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
//import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
//import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.CraftServer;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.minecraft.core.IRegistryWritable;

import java.util.Objects;

public class BiomeHandler {

    public String getBiomeName(Location loc) {
        String biomeName = null;

        String version = Bukkit.getBukkitVersion();

        if(version.contains("1.19") && !version.contains("1.19.3"))
            biomeName = getKey(loc).toString();


        if(biomeName == null)
            biomeName = loc.getBlock().getBiome().toString();

        if(biomeName.startsWith("minecraft:")){
            biomeName = biomeName.replace("minecraft:", "");
        }

        return biomeName;
    }

    ///
    //NMS For v1.19
    ///
    private MinecraftKey getKey(Location loc){
        World world = loc.getWorld();
        DedicatedServer dedicatedServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer nmsWorld = ((CraftWorld) world).getHandle();

        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        BiomeBase biomeBase = nmsWorld.getNoiseBiome(x >> 2, y >> 2, z >> 2).a();

        IRegistry<BiomeBase> registry = dedicatedServer.aX().b(IRegistry.aR);

        return registry.b(biomeBase);
    }

    ///
    //NMS For v1.18
    ///
//    public MinecraftKey getKey(Location loc){
//        World world = loc.getWorld();
//        DedicatedServer dedicatedServer = ((CraftServer) Bukkit.getServer()).getServer();
//        WorldServer nmsWorld = ((CraftWorld) world).getHandle();
//
//        int x = loc.getBlockX();
//        int y = loc.getBlockY();
//        int z = loc.getBlockZ();
//
//        BiomeBase biomeBase = nmsWorld.getNoiseBiome(x >> 2, y >> 2, z >> 2).a();
//
//        IRegistry<BiomeBase> registry = dedicatedServer.aU().b(IRegistry.aP);
//
//        return registry.b(biomeBase);
//    }


}
