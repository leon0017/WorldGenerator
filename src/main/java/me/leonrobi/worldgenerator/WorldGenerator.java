package me.leonrobi.worldgenerator;

import me.leonrobi.worldgenerator.chunkgen.BiomeInstance;
import me.leonrobi.worldgenerator.chunkgen.CustomChunkGenerator;
import me.leonrobi.worldgenerator.chunkgen.WorldInstance;
import me.leonrobi.worldgenerator.commands.ChunkGenLatencyCommand;
import me.leonrobi.worldgenerator.lib.BiomeRegister;
import me.leonrobi.worldgenerator.lib.NMSLib;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.HashMap;

public final class WorldGenerator extends JavaPlugin {

    public static final HashMap<String, WorldInstance> WORLD_INSTANCES = new HashMap<>();

    @Override
    public void onEnable() {
        getCommand("chunkgenlatency").setExecutor(new ChunkGenLatencyCommand());

        // Configurable world generation settings are in here
        WorldOptions myWorldOptions = WorldOptions.cloneDefault("my_world_type");

        // Make a new biome
        BiomeOptions myBiomeOptions = BiomeOptions.cloneDefault("my_biome_name");
        // The surface is now Magenta Wool instead of Grass in this biome:
        myBiomeOptions.level1Block(Material.MAGENTA_WOOL);
        // Sky and fog are yellow:
        myBiomeOptions.skyColor(Color.YELLOW.getRGB());
        myBiomeOptions.fogColor(Color.YELLOW.getRGB());
        // Water is red:
        myBiomeOptions.waterColor(Color.RED.getRGB());
        myBiomeOptions.waterFogColor(Color.RED.getRGB());
        // Our biome is now setup

        // Uncomment this to remove the default biome as well.
        //myWorldOptions.biomes().clear();

        // We can add the biome to our world now
        myWorldOptions.addBiome(myBiomeOptions);

        // Lets change some of the world generation options
        myWorldOptions.generationExposedCaves(true); // Cave entrances are on the surface
        myWorldOptions.startAtY(50); // The lower this value, the lower the y level of the surface

        // Register the world type
        registerCustomWorldType(myWorldOptions);
        // Use our world type "my_world_type" to load the minecraft world "world_test"
        loadCustomWorld("my_world_type", "world_test", 123);
    }

    public void loadCustomWorld(@NotNull String worldTypeName, @NotNull String worldName, int seed) throws IllegalArgumentException {
        WorldInstance worldInstance = WORLD_INSTANCES.get(worldTypeName);

        if (worldInstance == null) {
            throw new IllegalArgumentException("No such worldTypeName '" + worldTypeName + "' registered.");
        }

        CustomChunkGenerator customChunkGenerator = new CustomChunkGenerator(seed, worldInstance);
        customChunkGenerator.world =
                new WorldCreator(worldName).environment(World.Environment.NORMAL).seed(seed).generator(customChunkGenerator).createWorld();
    }

    public void registerCustomWorldType(@NotNull WorldOptions worldOptions) {
        HashMap<String, BiomeInstance> biomeInstances = new HashMap<>();

        WorldInstance worldInstance = new WorldInstance(worldOptions, biomeInstances);

        worldOptions.biomes().forEach((biomeName, biomeInfo) ->
                biomeInstances.put(biomeName, BiomeRegister.registerBiome(biomeName, biomeInfo, worldInstance)));

        for (World world : Bukkit.getWorlds()) {
            ChunkGenerator chunkGenerator = world.getGenerator();
            if (chunkGenerator instanceof CustomChunkGenerator customChunkGenerator) {
                if (customChunkGenerator.worldInstance.worldOptions().worldTypeName().equals(worldOptions.worldTypeName())) {
                    customChunkGenerator.worldInstance = worldInstance;
                    customChunkGenerator.setNoiseSettings();

                    NMSLib.setValue(
                            world, CraftWorld.class, "populators", customChunkGenerator.getDefaultPopulators(world)
                    );
                }
            }
        }

        WORLD_INSTANCES.put(worldOptions.worldTypeName(), worldInstance);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
