package me.leonrobi.worldgenerator;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.bukkit.Material;

import java.util.List;

public record FeatureOptions(
        ResourceKey<ConfiguredFeature<?, ?>> feature,
        double patchChance,
        int patchAmountMin,
        int patchAmountMax,
        List<Material> placeOn,
        Material setBlockBelowFeature
) {
}
