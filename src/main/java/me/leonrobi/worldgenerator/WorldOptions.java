package me.leonrobi.worldgenerator;

import me.leonrobi.worldgenerator.lib.FastNoiseLite;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class WorldOptions {

    public static final float DEFAULT_BIOME_SIZE = 0.2F;
    public static final float DEFAULT_GENERATION_AMPLIFICATION = 0.0F;
    public static final int DEFAULT_GENERATION_CAVE_BELOW_SURFACE_Y = 5;
    public static final HashMap<String, BiomeOptions> DEFAULT_BIOMES = new HashMap<>() {{
        put("default", BiomeOptions.cloneDefault("default"));
    }};
    public static final float DEFAULT_GENERATION_SCALE_UP = 32.0F;
    public static final float DEFAULT_GENERATION_SCALE_DOWN = 16.0F;
    public static final float DEFAULT_GENERATION_SCALE_TILT = 0.0F;
    public static final int DEFAULT_GENERATION_WATER_AT_Y = 90;
    public static final boolean DEFAULT_GENERATION_WATER_IS_LAVA = false;
    public static final boolean DEFAULT_GENERATION_GENERATE_CAVES = true;
    public static final boolean DEFAULT_GENERATION_EXPOSED_CAVES = false;
    public static final int DEFAULT_START_AT_Y = 65;
    public static final float DEFAULT_TERRAIN_FREQ_MULTIPLIER = 1.0F;
    public static final float DEFAULT_TERRAIN_FRACTAL_GAIN_MULTIPLIER = 1.0F;
    public static final float DEFAULT_DETAIL_FREQ_MULTIPLIER = 1.0F;
    public static final float DEFAULT_DETAIL_FRACTAL_GAIN_MULTIPLIER = 1.0F;
    public static final FastNoiseLite.FractalType DEFAULT_TERRAIN_FRACTAL_TYPE = FastNoiseLite.FractalType.Ridged;
    public static final boolean DEFAULT_GENERATE_WATER = true;

    private String worldTypeName;
    private float biomeSize;
    private float generationAmplification;
    private int generationCaveBelowSurfaceY;
    private HashMap<String, BiomeOptions> biomes;
    private float generationScaleUp;
    private float generationScaleDown;
    private float generationScaleTilt;
    private int generationWaterAtY;
    private boolean generationWaterIsLava;
    private boolean generationGenerateCaves;
    private boolean generationExposedCaves;
    private int startAtY;
    private float terrainFreqMultiplier;
    private float terrainFractalGainMultiplier;
    private float detailFreqMultiplier;
    private float detailFractalGainMultiplier;
    private FastNoiseLite.FractalType terrainFractalType;
    private boolean generateWater;

    public WorldOptions(String worldTypeName, float biomeSize, float generationAmplification, int generationCaveBelowSurfaceY,
                        HashMap<String, BiomeOptions> biomes, float generationScaleUp, float generationScaleDown,
                        float generationScaleTilt, int generationWaterAtY, boolean generationWaterIsLava,
                        boolean generationGenerateCaves, boolean generationExposedCaves, int startAtY,
                        float terrainFreqMultiplier, float terrainFractalGainMultiplier, float detailFreqMultiplier,
                        float detailFractalGainMultiplier, FastNoiseLite.FractalType terrainFractalType,
                        boolean generateWater) {
        this.worldTypeName = worldTypeName;
        this.biomeSize = biomeSize;
        this.generationAmplification = generationAmplification;
        this.generationCaveBelowSurfaceY = generationCaveBelowSurfaceY;
        this.biomes = biomes;
        this.generationScaleUp = generationScaleUp;
        this.generationScaleDown = generationScaleDown;
        this.generationScaleTilt = generationScaleTilt;
        this.generationWaterAtY = generationWaterAtY;
        this.generationWaterIsLava = generationWaterIsLava;
        this.generationGenerateCaves = generationGenerateCaves;
        this.generationExposedCaves = generationExposedCaves;
        this.startAtY = startAtY;
        this.terrainFreqMultiplier = terrainFreqMultiplier;
        this.terrainFractalGainMultiplier = terrainFractalGainMultiplier;
        this.detailFreqMultiplier = detailFreqMultiplier;
        this.detailFractalGainMultiplier = detailFractalGainMultiplier;
        this.terrainFractalType = terrainFractalType;
        this.generateWater = generateWater;
    }

    public static @NotNull WorldOptions cloneDefault(@NotNull String worldTypeName) {
        return new WorldOptions(
                worldTypeName,
                DEFAULT_BIOME_SIZE,
                DEFAULT_GENERATION_AMPLIFICATION,
                DEFAULT_GENERATION_CAVE_BELOW_SURFACE_Y,
                DEFAULT_BIOMES,
                DEFAULT_GENERATION_SCALE_UP,
                DEFAULT_GENERATION_SCALE_DOWN,
                DEFAULT_GENERATION_SCALE_TILT,
                DEFAULT_GENERATION_WATER_AT_Y,
                DEFAULT_GENERATION_WATER_IS_LAVA,
                DEFAULT_GENERATION_GENERATE_CAVES,
                DEFAULT_GENERATION_EXPOSED_CAVES,
                DEFAULT_START_AT_Y,
                DEFAULT_TERRAIN_FREQ_MULTIPLIER,
                DEFAULT_TERRAIN_FRACTAL_GAIN_MULTIPLIER,
                DEFAULT_DETAIL_FREQ_MULTIPLIER,
                DEFAULT_DETAIL_FRACTAL_GAIN_MULTIPLIER,
                DEFAULT_TERRAIN_FRACTAL_TYPE,
                DEFAULT_GENERATE_WATER
        );
    }

    public String worldTypeName() {
        return this.worldTypeName;
    }

    public float biomeSize() {
        return biomeSize;
    }

    public void biomeSize(float biomeSize) {
        this.biomeSize = biomeSize;
    }

    public float generationAmplification() {
        return generationAmplification;
    }

    public void generationAmplification(float generationAmplification) {
        this.generationAmplification = generationAmplification;
    }

    public int generationCaveBelowSurfaceY() {
        return generationCaveBelowSurfaceY;
    }

    public void generationCaveBelowSurfaceY(int generationCaveBelowSurfaceY) {
        this.generationCaveBelowSurfaceY = generationCaveBelowSurfaceY;
    }

    public HashMap<String, BiomeOptions> biomes() {
        return biomes;
    }

    public void addBiome(@NotNull BiomeOptions biomeOptions) {
        biomes.put(biomeOptions.biomeName(), biomeOptions);
    }

    public float generationScaleUp() {
        return generationScaleUp;
    }

    public void generationScaleUp(float generationScaleUp) {
        this.generationScaleUp = generationScaleUp;
    }

    public float generationScaleDown() {
        return generationScaleDown;
    }

    public void generationScaleDown(float generationScaleDown) {
        this.generationScaleDown = generationScaleDown;
    }

    public float generationScaleTilt() {
        return generationScaleTilt;
    }

    public void generationScaleTilt(float generationScaleTilt) {
        this.generationScaleTilt = generationScaleTilt;
    }

    public int generationWaterAtY() {
        return generationWaterAtY;
    }

    public void generationWaterAtY(int generationWaterAtY) {
        this.generationWaterAtY = generationWaterAtY;
    }

    public boolean generationWaterIsLava() {
        return generationWaterIsLava;
    }

    public void generationWaterIsLava(boolean generationWaterIsLava) {
        this.generationWaterIsLava = generationWaterIsLava;
    }

    public boolean generationGenerateCaves() {
        return generationGenerateCaves;
    }

    public void generationGenerateCaves(boolean generationGenerateCaves) {
        this.generationGenerateCaves = generationGenerateCaves;
    }

    public boolean generationExposedCaves() {
        return generationExposedCaves;
    }

    public void generationExposedCaves(boolean generationExposedCaves) {
        this.generationExposedCaves = generationExposedCaves;
    }

    public int startAtY() {
        return startAtY;
    }

    public void startAtY(int startAtY) {
        this.startAtY = startAtY;
    }

    public float terrainFreqMultiplier() {
        return terrainFreqMultiplier;
    }

    public void terrainFreqMultiplier(float terrainFreqMultiplier) {
        this.terrainFreqMultiplier = terrainFreqMultiplier;
    }

    public float terrainFractalGainMultiplier() {
        return terrainFractalGainMultiplier;
    }

    public void terrainFractalGainMultiplier(float terrainFractalGainMultiplier) {
        this.terrainFractalGainMultiplier = terrainFractalGainMultiplier;
    }

    public float detailFreqMultiplier() {
        return detailFreqMultiplier;
    }

    public void detailFreqMultiplier(float detailFreqMultiplier) {
        this.detailFreqMultiplier = detailFreqMultiplier;
    }

    public float detailFractalGainMultiplier() {
        return detailFractalGainMultiplier;
    }

    public void detailFractalGainMultiplier(float detailFractalGainMultiplier) {
        this.detailFractalGainMultiplier = detailFractalGainMultiplier;
    }

    public FastNoiseLite.FractalType terrainFractalType() {
        return terrainFractalType;
    }

    public void terrainFractalType(FastNoiseLite.FractalType terrainFractalType) {
        this.terrainFractalType = terrainFractalType;
    }

    public boolean generateWater() {
        return generateWater;
    }

    public void generateWater(boolean generateWater) {
        this.generateWater = generateWater;
    }
}