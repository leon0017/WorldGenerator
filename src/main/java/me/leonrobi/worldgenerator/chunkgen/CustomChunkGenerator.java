package me.leonrobi.worldgenerator.chunkgen;

import me.leonrobi.worldgenerator.WorldOptions;
import me.leonrobi.worldgenerator.lib.FastNoiseLite;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
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

    private final WorldOptions worldOptions;

    public CustomChunkGenerator(int seed, @NotNull WorldOptions worldOptions) {
        this.worldOptions = worldOptions;

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
    public List<BlockPopulator> getDefaultPopulators(@NotNull World world) {
        List<BlockPopulator> list = new ArrayList<>();

        for ()
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

}
