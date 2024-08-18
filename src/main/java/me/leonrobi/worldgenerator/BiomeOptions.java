package me.leonrobi.worldgenerator;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import org.bukkit.Material;

import java.util.List;

public record BiomeOptions(
        Material level1Block,
        Material level2Block,
        int level2Depth,
        int level2DepthRandom,
        Material level3Block,
        Material level4Block,
        int level4YStart,
        List<Material> underwaterBlocks,
        List<FeatureOptions> featurePopulators,
        int fogColor,
        int waterColor,
        int waterFogColor,
        int skyColor,
        int foliageColor,
        int grassColor,
        float temperature,
        Holder.Reference<SoundEvent> ambientLoopSound,
        AmbientParticleSettings ambientParticleSettings,
        List<Material> underwaterUntil
) {
}