package me.leonrobi.worldgenerator.chunkgen;

import me.leonrobi.worldgenerator.BiomeOptions;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

public record BiomeInstance(
        BiomeOptions biomeOptions,
        Holder<Biome> holder,
        WorldInstance worldInstance
) {

}
