package me.leonrobi.worldgenerator.chunkgen;

import me.leonrobi.worldgenerator.WorldOptions;

import java.util.HashMap;

public record WorldInstance(
        WorldOptions worldOptions,
        HashMap<String, BiomeInstance> biomeInstances
) {

}
