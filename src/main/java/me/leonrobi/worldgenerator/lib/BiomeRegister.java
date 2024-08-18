package me.leonrobi.worldgenerator.lib;

import com.mojang.serialization.Lifecycle;
import me.leonrobi.worldgenerator.BiomeOptions;
import me.leonrobi.worldgenerator.chunkgen.BiomeInstance;
import me.leonrobi.worldgenerator.chunkgen.WorldInstance;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.Musics;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Optional;

public class BiomeRegister {

    private static final WritableRegistry<Biome> BIOME_REGISTRY = (WritableRegistry<Biome>) MinecraftServer.getServer().registryAccess().registryOrThrow(Registries.BIOME);
    private static final HashMap<String, WorldInstance> WORLD_INSTANCES = new HashMap<>();
    private static final HashMap<String, Holder<Biome>> BIOME_MAP = new HashMap<>();
    private static final HashMap<String, ResourceKey<Biome>> BIOME_RESOURCE_KEYS = new HashMap<>();

    public static void freezeBiomeRegistry(boolean freeze) {
        // NMS: https://mappings.cephx.dev/1.20.4/net/minecraft/core/MappedRegistry.html frozen --> l
        NMSLib.setValue(BIOME_REGISTRY, MappedRegistry.class, "frozen", freeze);
    }

    private static void setBiomeValues(@NotNull Holder<Biome> holder, @NotNull BiomeOptions biomeOptions) {
        Biome biome = holder.value();
        BiomeSpecialEffects specialEffects = biome.getSpecialEffects();
        // NMS:
        NMSLib.setValue(specialEffects, BiomeSpecialEffects.class, "fogColor", biomeOptions.fogColor());
        NMSLib.setValue(specialEffects, BiomeSpecialEffects.class, "waterColor", biomeOptions.waterColor());
        NMSLib.setValue(specialEffects, BiomeSpecialEffects.class, "waterFogColor", biomeOptions.waterFogColor());
        NMSLib.setValue(specialEffects, BiomeSpecialEffects.class, "skyColor", biomeOptions.skyColor());
        NMSLib.setValue(
                specialEffects, BiomeSpecialEffects.class, "foliageColorOverride", Optional.of(biomeOptions.foliageColor())
        );
        NMSLib.setValue(
                specialEffects, BiomeSpecialEffects.class, "grassColorOverride", Optional.of(biomeOptions.grassColor())
        );
        if (biomeOptions.ambientLoopSound() != null) NMSLib.setValue(
                specialEffects, BiomeSpecialEffects.class, "ambientLoopSoundEvent", Optional.of(biomeOptions.ambientLoopSound())
        );
        if (biomeOptions.ambientParticleSettings() != null) NMSLib.setValue(
                specialEffects, BiomeSpecialEffects.class, "ambientParticleSettings", Optional.of(biomeOptions.ambientParticleSettings())
        );

        //val newMobSpawnSettings = createMobSpawnsBuilder(biomeInfo)
        // NMS:
        //NMSLib.setValue(biome, Biome.class, "mobSettings", newMobSpawnSettings.build())

        //val climateSettings = biome.climateSettings
        // NMS:
        //NMSLib.setValue(climateSettings, ClimateSettings.class, "temperature", biomeInfo.temperature) // doesn't work..
    }

    public static @NotNull BiomeInstance registerBiome(@NotNull String biomeName, @NotNull BiomeOptions biomeOptions,
                                                       @NotNull WorldInstance worldInstance) {
        if (BIOME_MAP.containsKey(biomeName)) {
            Holder<Biome> holder = BIOME_MAP.get(biomeName);
            setBiomeValues(holder, biomeOptions);
            return new BiomeInstance(
                    biomeOptions, holder, worldInstance
            );
        }
        freezeBiomeRegistry(false);

        ResourceKey<Biome> resourceKey = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath("worldgen", biomeName.toLowerCase()));

        BiomeSpecialEffects.Builder specialEffectsBuilder = new BiomeSpecialEffects.Builder()
                .fogColor(biomeOptions.fogColor())
                .waterColor(biomeOptions.waterColor())
                .waterFogColor(biomeOptions.waterFogColor())
                .skyColor(biomeOptions.skyColor())
                .foliageColorOverride(biomeOptions.foliageColor())
                .grassColorOverride(biomeOptions.grassColor())
                .backgroundMusic(Musics.GAME);
        if (biomeOptions.ambientLoopSound() != null) specialEffectsBuilder.ambientLoopSound(biomeOptions.ambientLoopSound());
        if (biomeOptions.ambientParticleSettings() != null)
            specialEffectsBuilder.ambientParticle(biomeOptions.ambientParticleSettings());

        Biome nmsBiome = new Biome.BiomeBuilder()
                .temperatureAdjustment(Biome.TemperatureModifier.NONE)
                .downfall(0F)
                .hasPrecipitation(false)
                .specialEffects(specialEffectsBuilder.build())
                .temperature(biomeOptions.temperature())
                .generationSettings(BiomeGenerationSettings.EMPTY)
                .mobSpawnSettings(MobSpawnSettings.EMPTY)
                .build();

        Holder<Biome> holder = BIOME_REGISTRY.register(resourceKey, nmsBiome, RegistrationInfo.BUILT_IN);

        // NMS: https://mappings.cephx.dev/1.20.4/net/minecraft/core/Holder$Reference.html bindValue --> b
        NMSLib.callFunc(holder, Holder.Reference.class, "bindValue", nmsBiome);

        BIOME_MAP.put(biomeName, holder);
        BIOME_RESOURCE_KEYS.put(biomeName, resourceKey);

        freezeBiomeRegistry(true);

        return new BiomeInstance(
                biomeOptions, BIOME_REGISTRY.getHolderOrThrow(resourceKey), worldInstance
        );
    }

}
