package me.leonrobi.worldgenerator.chunkgen;

import com.mojang.datafixers.util.Pair;
import me.leonrobi.worldgenerator.BiomeOptions;
import me.leonrobi.worldgenerator.WorldOptions;
import me.leonrobi.worldgenerator.commands.ChunkGenLatencyCommand;
import me.leonrobi.worldgenerator.lib.FastNoiseLite;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.generator.CraftChunkData;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CustomChunkGenerator extends ChunkGenerator {

    private final FastNoiseLite terrainNoise;
    private final FastNoiseLite detailNoise;
    private final FastNoiseLite caveNoise;
    private final FastNoiseLite biomeNoise;
    private final Random random;

    public WorldInstance worldInstance;
    private final WorldOptions worldOptions;

    public World world;

    public CustomChunkGenerator(int seed, @NotNull WorldInstance worldInstance) {
        this.worldInstance = worldInstance;
        this.worldOptions = worldInstance.worldOptions();

        terrainNoise = new FastNoiseLite(seed);
        detailNoise = new FastNoiseLite(seed);
        caveNoise = new FastNoiseLite(seed);
        biomeNoise = new FastNoiseLite(seed);
        random = new Random(seed);

        terrainNoise.SetFractalOctaves(5);
        terrainNoise.SetNoiseType(FastNoiseLite.NoiseType.Perlin);

        detailNoise.SetNoiseType(FastNoiseLite.NoiseType.Perlin);

        caveNoise.SetFrequency(0.05F);
        caveNoise.SetFractalGain(0.2F);
        caveNoise.SetNoiseType(FastNoiseLite.NoiseType.Perlin);

        biomeNoise.SetNoiseType(FastNoiseLite.NoiseType.Value);

        setNoiseSettings();
    }

    public void setNoiseSettings() {
        terrainNoise.SetFractalPingPongStrength(2F + worldOptions.generationAmplification());
        biomeNoise.SetFrequency(worldOptions.biomeSize() / 16F);

        terrainNoise.SetFrequency(0.001F * worldOptions.terrainFreqMultiplier()); // Higher values create spiked-y terrain, lower values make terrain more flat.
        terrainNoise.SetFractalGain(0.7F * worldOptions.terrainFractalGainMultiplier()); // Higher values create many hills, lower values create fewer hills.
        detailNoise.SetFrequency(0.05F * worldOptions.detailFreqMultiplier());
        detailNoise.SetFractalGain(0.2F * worldOptions.detailFractalGainMultiplier());

        terrainNoise.SetFractalType(worldOptions.terrainFractalType());
    }

    @Override
    public @NotNull List<BlockPopulator> getDefaultPopulators(@NotNull World world) {
        List<BlockPopulator> list = new ArrayList<>();

        for (BiomeInstance biomeInstance : worldInstance.biomeInstances().values()) {
            biomeInstance.biomeOptions().featurePopulators().forEach(featureOptions -> {
                list.add(new FeaturePopulator(
                        biomeInstance.holder().unwrapKey().get(),
                        featureOptions
                ));
            });
        }

        return list;
    }

    private void placeBelowSmart(Material current, Material below, ChunkData chunkData, int x, int y, int z) {
        if (!chunkData.getBlockData(x, y - 1, z).getMaterial().equals(current)) {
            chunkData.setBlock(x, y - 1, z, below);
        }
    }

    private void placeBelowSmartUntil(Material current, Material below, List<Material> until, ChunkData chunkData, int x, int y, int z) {
        int vY = y - 1;
        if (!chunkData.getBlockData(x, vY, z).getMaterial().equals(current)) {
            while (vY > chunkData.getMinHeight()) {
                if (until.contains(chunkData.getBlockData(x, vY, z).getMaterial())) {
                    break;
                }
                chunkData.setBlock(x, vY, z, below);
                vY--;
            }
        }
    }

    private boolean placeIfAbove(Material ifAbove, Material thenBelow, ChunkData chunkData, int x, int y, int z) {
        return placeIfAbove(ifAbove, thenBelow, chunkData, x, y, z, 1);
    }

    private boolean placeIfAbove(Material ifAbove, Material thenBelow, ChunkData chunkData, int x, int y, int z, int layers) {
        if (chunkData.getBlockData(x, y + 1, z).getMaterial().equals(ifAbove)) {
            for (int loopY = y; loopY >= y - layers + 1; loopY--) {
                chunkData.setBlock(x, loopY, z, thenBelow);
            }
            return true;
        }
        return false;
    }

    private void placeStoneNoCave(ChunkData chunkData, int x, int y, int z, BiomeOptions biomeInfo) {
        int level2Depth = biomeInfo.level2DepthRandom().isEmpty()
                ? biomeInfo.level2Depth()
                : random.nextInt(biomeInfo.level2Depth(), biomeInfo.level2DepthRandom().get());

        int stoneY = y - (level2Depth + 1);
        chunkData.setBlock(x, stoneY, z, caveMaterial(biomeInfo, stoneY));
        placeIfAbove(Material.AIR, biomeInfo.level1Block(), chunkData, x, y, z);
        placeIfAbove(biomeInfo.level1Block(), biomeInfo.level2Block(), chunkData, x, y - 1, z, level2Depth);
    }

    private Material caveMaterial(BiomeOptions biomeInfo, int currentY) {
        if (currentY < biomeInfo.level4YStart()) {
            int diff = biomeInfo.level4YStart() - currentY;
            if (diff < 5) {
                return random.nextInt(3) == 0 ? biomeInfo.level4Block() : biomeInfo.level3Block();
            } else {
                return biomeInfo.level4Block();
            }
        } else {
            return biomeInfo.level3Block();
        }
    }

    private final double caveFunction = -0.3;

    @Override
    public void generateNoise(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        long start = System.currentTimeMillis();

        int worldX = chunkX * 16;
        int worldZ = chunkZ * 16;

        Pair<Double, BiomeInstance>[][] noiseAndBiomeLookup = new Pair[16][16];
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                float realX = (i + worldX);
                float realZ = (j + worldZ);
                double terrainNoiseValue = terrainNoise.GetNoise(realX, realZ);
                double detailNoiseValue = detailNoise.GetNoise(realX, realZ);
                BiomeInstance biome = getBiome((int) realX, (int) realZ);
                noiseAndBiomeLookup[i][j] = Pair.of(terrainNoiseValue * 2 + detailNoiseValue / 10, biome);
            }
        }

        int startY = worldInfo.getMinHeight() + 5;
        int y = startY;

        while (y < worldInfo.getMaxHeight()) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    Pair noiseAndBiome = noiseAndBiomeLookup[x][z];
                    double noise1 = (double) noiseAndBiome.getFirst();
                    BiomeOptions biomeInfo = ((BiomeInstance) noiseAndBiome.getSecond()).biomeOptions();

                    double currentY = noise1 > worldOptions.generationScaleTilt()
                            ? worldOptions.startAtY() + (noise1 * worldOptions.generationScaleUp())
                            : worldOptions.startAtY() + (noise1 * worldOptions.generationScaleDown());

                    if (y < currentY) {
                        if (!worldOptions.generationGenerateCaves()) {
                            placeStoneNoCave(chunkData, x, y, z, biomeInfo);
                        } else {
                            float distanceToSurface = Math.abs((float) (y - currentY));

                            if (worldOptions.generationExposedCaves()) {
                                double noise2 = caveNoise.GetNoise((x + worldX), y, (z + worldZ));

                                if (noise2 > caveFunction) {
                                    if (distanceToSurface < worldOptions.generationCaveBelowSurfaceY())
                                        placeStoneNoCave(chunkData, x, y, z, biomeInfo);
                                    else
                                        chunkData.setBlock(x, y, z, caveMaterial(biomeInfo, y));
                                } else if (worldOptions.generateWater() && currentY < worldOptions.generationWaterAtY() && distanceToSurface < worldInstance.worldOptions().generationCaveBelowSurfaceY()) {
                                    placeStoneNoCave(chunkData, x, y, z, biomeInfo);
                                }
                            } else {
                                if (distanceToSurface < worldOptions.generationCaveBelowSurfaceY()) {
                                    placeStoneNoCave(chunkData, x, y, z, biomeInfo);
                                } else {
                                    double noise2 = caveNoise.GetNoise((x + worldX), y, (z + worldZ));

                                    if (noise2 > caveFunction) {
                                        chunkData.setBlock(x, y, z, caveMaterial(biomeInfo, y));
                                    } else if (worldOptions.generateWater() && currentY < worldOptions.generationWaterAtY() && distanceToSurface < worldInstance.worldOptions().generationCaveBelowSurfaceY()) {
                                        placeStoneNoCave(chunkData, x, y, z, biomeInfo);
                                    }
                                }
                            }
                        }
                    } else if (worldOptions.generateWater() && y < worldOptions.generationWaterAtY()) {
                        Material liquidMat = worldOptions.generationWaterIsLava() ? Material.LAVA : Material.WATER;
                        chunkData.setBlock(x, y, z, liquidMat);
                        placeBelowSmartUntil(liquidMat,
                                biomeInfo.underwaterBlocks().get(random.nextInt(biomeInfo.underwaterBlocks().size())),
                                biomeInfo.underwaterUntil(), chunkData, x, y, z);
                    }
                }
            }
            y++;
        }

        ChunkAccess chunkAccess = ((CraftChunkData) chunkData).getHandle();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                BiomeInstance biomeLookup = noiseAndBiomeLookup[x][z].getSecond();
                BiomeOptions biomeInfo = biomeLookup.biomeOptions();

                for (int yLoop = worldInfo.getMinHeight(); yLoop < worldInfo.getMaxHeight(); yLoop++) {
                    chunkAccess.setBiome(x, yLoop, z, biomeLookup.holder());

                    if (yLoop < worldInfo.getMinHeight() + 5) {
                        if (worldOptions.generationGenerateCaves()) {
                            double caveNoiseValue = caveNoise.GetNoise((x + worldX), yLoop, (z + worldZ));
                            double distance = Math.abs((worldInfo.getMinHeight() + 5) - yLoop);
                            if (caveNoiseValue > -0.8 * (distance / 2))
                                chunkData.setBlock(x, yLoop, z, caveMaterial(biomeInfo, yLoop));
                        } else {
                            chunkData.setBlock(x, yLoop, z, caveMaterial(biomeInfo, yLoop));
                        }
                    }
                }

                chunkData.setBlock(x, chunkData.getMinHeight(), z, Material.BEDROCK);
            }
        }

        long end = System.currentTimeMillis();
        if (world != null) {
            ChunkGenLatencyCommand.latencyMap.put(world, end - start);
        }
    }

    public BiomeInstance getBiome(int worldX, int worldZ) {
        List<BiomeInstance> biomeInstances = new ArrayList<>(worldInstance.biomeInstances().values());
        int biomeInstancesSize = biomeInstances.size();
        int biomeIndex = biomeInstancesSize == 0 ? 0 : (int) (((biomeNoise.GetNoise((float) worldX, (float) worldZ) + 1) / 2.0F) * biomeInstancesSize);
        return biomeInstances.get(biomeIndex);
    }

    @Override
    public boolean shouldGenerateMobs() {
        return true;
    }

}
