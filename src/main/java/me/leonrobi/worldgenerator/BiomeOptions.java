package me.leonrobi.worldgenerator;

import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.MiscOverworldFeatures;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class BiomeOptions {

    public static final Material DEFAULT_LEVEL_1_BLOCK = Material.GRASS_BLOCK;
    public static final Material DEFAULT_LEVEL_2_BLOCK = Material.DIRT;
    public static final int DEFAULT_LEVEL_2_DEPTH = 3;
    public static final Optional<Integer> DEFAULT_LEVEL_2_DEPTH_RANDOM = Optional.empty();
    public static final Material DEFAULT_LEVEL_3_BLOCK = Material.STONE;
    public static final Material DEFAULT_LEVEL_4_BLOCK = Material.DEEPSLATE;
    public static final int DEFAULT_LEVEL_4_Y_START = 0;
    public static final List<Material> DEFAULT_UNDERWATER_BLOCKS = List.of(Material.SAND, Material.GRAVEL, Material.CLAY);
    public static final List<FeatureOptions> DEFAULT_FEATURE_POPULATORS = List.of(new FeatureOptions(MiscOverworldFeatures.FOREST_ROCK,
            5,
            1,
            1,
            List.of(Material.GRASS_BLOCK),
            null
    ));
    public static final int DEFAULT_FOG_COLOR = 0x878787;
    public static final int DEFAULT_WATER_COLOR = 0x0000FF;
    public static final int DEFAULT_WATER_FOG_COLOR = 0x0000FF;
    public static final int DEFAULT_SKY_COLOR = 0x87CEEB;
    public static final int DEFAULT_FOLIAGE_COLOR = 0x00FF00;
    public static final int DEFAULT_GRASS_COLOR = 0x00FF00;
    public static final float DEFAULT_TEMPERATURE = 1.0F;
    public static final Holder.Reference<SoundEvent> DEFAULT_AMBIENT_LOOP_SOUND = null;
    public static final AmbientParticleSettings DEFAULT_AMBIENT_PARTICLE_SETTINGS = null;

    private String biomeName;
    private Material level1Block;
    private Material level2Block;
    private int level2Depth;
    private Optional<Integer> level2DepthRandom;
    private Material level3Block;
    private Material level4Block;
    private int level4YStart;
    private List<Material> underwaterBlocks;
    private List<FeatureOptions> featurePopulators;
    private int fogColor;
    private int waterColor;
    private int waterFogColor;
    private int skyColor;
    private int foliageColor;
    private int grassColor;
    private float temperature;
    private Holder.Reference<SoundEvent> ambientLoopSound;
    private AmbientParticleSettings ambientParticleSettings;
    private List<Material> underwaterUntil;

    // Constructor
    public BiomeOptions(String biomeName, Material level1Block, Material level2Block, int level2Depth, Optional<Integer> level2DepthRandom,
                        Material level3Block, Material level4Block, int level4YStart, List<Material> underwaterBlocks,
                        List<FeatureOptions> featurePopulators, int fogColor, int waterColor, int waterFogColor,
                        int skyColor, int foliageColor, int grassColor, float temperature,
                        Holder.Reference<SoundEvent> ambientLoopSound, AmbientParticleSettings ambientParticleSettings) {
        this.biomeName = biomeName;
        this.level1Block = level1Block;
        this.level2Block = level2Block;
        this.level2Depth = level2Depth;
        this.level2DepthRandom = level2DepthRandom;
        this.level3Block = level3Block;
        this.level4Block = level4Block;
        this.level4YStart = level4YStart;
        this.underwaterBlocks = underwaterBlocks;
        this.featurePopulators = featurePopulators;
        this.fogColor = fogColor;
        this.waterColor = waterColor;
        this.waterFogColor = waterFogColor;
        this.skyColor = skyColor;
        this.foliageColor = foliageColor;
        this.grassColor = grassColor;
        this.temperature = temperature;
        this.ambientLoopSound = ambientLoopSound;
        this.ambientParticleSettings = ambientParticleSettings;

        updateUnderwaterUntil();
    }

    public static @NotNull BiomeOptions cloneDefault(@NotNull String biomeName) {
        return new BiomeOptions(
                biomeName,
                DEFAULT_LEVEL_1_BLOCK,
                DEFAULT_LEVEL_2_BLOCK,
                DEFAULT_LEVEL_2_DEPTH,
                DEFAULT_LEVEL_2_DEPTH_RANDOM,
                DEFAULT_LEVEL_3_BLOCK,
                DEFAULT_LEVEL_4_BLOCK,
                DEFAULT_LEVEL_4_Y_START,
                DEFAULT_UNDERWATER_BLOCKS,
                DEFAULT_FEATURE_POPULATORS,
                DEFAULT_FOG_COLOR,
                DEFAULT_WATER_COLOR,
                DEFAULT_WATER_FOG_COLOR,
                DEFAULT_SKY_COLOR,
                DEFAULT_FOLIAGE_COLOR,
                DEFAULT_GRASS_COLOR,
                DEFAULT_TEMPERATURE,
                DEFAULT_AMBIENT_LOOP_SOUND,
                DEFAULT_AMBIENT_PARTICLE_SETTINGS
        );
    }

    // Getters
    public String biomeName() {
        return this.biomeName;
    }

    public Material level1Block() {
        return level1Block;
    }

    public Material level2Block() {
        return level2Block;
    }

    public int level2Depth() {
        return level2Depth;
    }

    public Optional<Integer> level2DepthRandom() {
        return level2DepthRandom;
    }

    public Material level3Block() {
        return level3Block;
    }

    public Material level4Block() {
        return level4Block;
    }

    public int level4YStart() {
        return level4YStart;
    }

    public List<Material> underwaterBlocks() {
        return underwaterBlocks;
    }

    public List<FeatureOptions> featurePopulators() {
        return featurePopulators;
    }

    public int fogColor() {
        return fogColor;
    }

    public int waterColor() {
        return waterColor;
    }

    public int waterFogColor() {
        return waterFogColor;
    }

    public int skyColor() {
        return skyColor;
    }

    public int foliageColor() {
        return foliageColor;
    }

    public int grassColor() {
        return grassColor;
    }

    public float temperature() {
        return temperature;
    }

    public Holder.Reference<SoundEvent> ambientLoopSound() {
        return ambientLoopSound;
    }

    public AmbientParticleSettings ambientParticleSettings() {
        return ambientParticleSettings;
    }

    public List<Material> underwaterUntil() {
        return underwaterUntil;
    }

    // Setters
    public void level1Block(Material level1Block) {
        this.level1Block = level1Block;
    }

    public void level2Block(Material level2Block) {
        this.level2Block = level2Block;
    }

    public void level2Depth(int level2Depth) {
        this.level2Depth = level2Depth;
    }

    public void level2DepthRandom(Optional<Integer> level2DepthRandom) {
        this.level2DepthRandom = level2DepthRandom;
    }

    public void level3Block(Material level3Block) {
        this.level3Block = level3Block;
        updateUnderwaterUntil();
    }

    public void level4Block(Material level4Block) {
        this.level4Block = level4Block;
    }

    public void level4YStart(int level4YStart) {
        this.level4YStart = level4YStart;
    }

    public void underwaterBlocks(List<Material> underwaterBlocks) {
        this.underwaterBlocks = underwaterBlocks;
    }

    public void featurePopulators(List<FeatureOptions> featurePopulators) {
        this.featurePopulators = featurePopulators;
    }

    public void fogColor(int fogColor) {
        this.fogColor = fogColor;
    }

    public void waterColor(int waterColor) {
        this.waterColor = waterColor;
    }

    public void waterFogColor(int waterFogColor) {
        this.waterFogColor = waterFogColor;
    }

    public void skyColor(int skyColor) {
        this.skyColor = skyColor;
    }

    public void foliageColor(int foliageColor) {
        this.foliageColor = foliageColor;
    }

    public void grassColor(int grassColor) {
        this.grassColor = grassColor;
    }

    public void temperature(float temperature) {
        this.temperature = temperature;
    }

    public void ambientLoopSound(Holder.Reference<SoundEvent> ambientLoopSound) {
        this.ambientLoopSound = ambientLoopSound;
    }

    public void ambientParticleSettings(AmbientParticleSettings ambientParticleSettings) {
        this.ambientParticleSettings = ambientParticleSettings;
    }

    private void updateUnderwaterUntil() {
        underwaterUntil = List.of(Material.AIR, this.level3Block);
    }
}