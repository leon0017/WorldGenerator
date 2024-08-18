package me.leonrobi.worldgenerator;

import me.leonrobi.worldgenerator.lib.FastNoiseLite;

import java.util.HashMap;

public record WorldOptions(
        String world,
        String name,
        int index,
        float biomeSize,
        float generationAmplification,
        int generationCaveBelowSurfaceY,
        HashMap<String, BiomeOptions> biomes,
        float generationScaleUp,
        float generationScaleDown,
        float generationScaleTilt,
        Integer generationWaterAtY, // Note: Use Integer for nullable Int in Kotlin
        boolean generationGenerateLava,
        boolean generationGenerateCaves,
        boolean generationExposedCaves,
        boolean doDaylightCycle,
        long startingWorldTime,
        boolean doWeatherCycle,
        int startAtY,
        float terrainFreqMultiplier,
        float terrainFractalGainMultiplier,
        float detailFreqMultiplier,
        float detailFractalGainMultiplier,
        FastNoiseLite.FractalType terrainFractalType
) {
}