package me.leonrobi.worldgenerator.chunkgen;

import me.leonrobi.worldgenerator.FeatureOptions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.bukkit.Material;
import org.bukkit.craftbukkit.generator.CraftLimitedRegion;
import org.bukkit.craftbukkit.util.RandomSourceWrapper;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class FeaturePopulator extends BlockPopulator {

    private final ResourceKey<Biome> biomeKey;
    private final FeatureOptions featureOptions;

    public FeaturePopulator(@NotNull ResourceKey<Biome> biomeKey, @NotNull FeatureOptions featureOptions) {
        this.biomeKey = biomeKey;
        this.featureOptions = featureOptions;
    }

    private void placeFeature(LimitedRegion limitedRegion, ResourceKey<ConfiguredFeature<?, ?>> feature, int x, int y, int z, Random random) {
        CraftLimitedRegion cLimitedRegion = (CraftLimitedRegion) limitedRegion;
        Holder<ConfiguredFeature<?, ?>> holder = cLimitedRegion.getHandle()
                .registryAccess()
                .registryOrThrow(Registries.CONFIGURED_FEATURE)
                .getHolder(feature)
                .orElse(null);

        if (holder != null) {
            holder.value().place(
                    cLimitedRegion.getHandle(),
                    cLimitedRegion.getHandle().getMinecraftWorld().getChunkSource().getGenerator(),
                    new RandomSourceWrapper(random),
                    new BlockPos(x, y, z)
            );
        }
    }

    @Override
    public void populate(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull LimitedRegion limitedRegion) {
        if (featureOptions.patchChance() > random.nextDouble()) {
            int worldX = chunkX << 4;
            int worldZ = chunkZ << 4;

            CraftLimitedRegion craftLimitedRegion = (CraftLimitedRegion) limitedRegion;
            if (!craftLimitedRegion.getHandle().getBiome(new BlockPos(worldX, 0, worldZ)).is(biomeKey)) {
                return;
            }

            for (int i = 0; i <= random.nextInt(featureOptions.patchAmountMin(), featureOptions.patchAmountMax() + 1); i++) {
                int y = worldInfo.getMaxHeight() - 1;

                while (limitedRegion.getType(worldX, y, worldZ) == Material.AIR) {
                    y--;
                }

                if (!featureOptions.placeOn().contains(limitedRegion.getType(worldX, y, worldZ))) {
                    continue;
                }

                if (featureOptions.setBlockBelowFeature() != null) {
                    limitedRegion.setType(worldX, y, worldZ, featureOptions.setBlockBelowFeature());
                }

                placeFeature(limitedRegion, featureOptions.feature(), worldX, y + 1, worldZ, random);
            }
        }
    }

}
