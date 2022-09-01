package com.kunfury.blepFishing.Miscellaneous;

import net.minecraft.core.Registry;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_19_R1.CraftServer;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.minecraft.core.IRegistryWritable;

public class BiomeHandler {

    public String getBiomeName(Location loc) {
        return loc.getBlock().getBiome().name();
    }


    public MinecraftKey getBiomeKey(Location location) {
        DedicatedServer dServer = ((CraftServer) Bukkit.getServer()).getServer();

        MinecraftServer ms = dServer.server.getServer();



        return null;
    }


}
