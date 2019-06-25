package com.terminal29.ushanka.dimension.generator;

import com.terminal29.ushanka.dimension.VillageIslandManager;
import com.terminal29.ushanka.dimension.generator.VillageChunkGeneratorConfig;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.Heightmap;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public class VillageChunkGenerator extends ChunkGenerator<VillageChunkGeneratorConfig> {

    public VillageChunkGenerator(IWorld iWorld_1, BiomeSource biomeSource_1, VillageChunkGeneratorConfig chunkGeneratorConfig_1) {
        super(iWorld_1, biomeSource_1, chunkGeneratorConfig_1);
        if(iWorld_1 instanceof ServerWorld)
            VillageIslandManager.INSTANCE.setWorld((ServerWorld)iWorld_1);
    }

    @Override
    public void buildSurface(Chunk chunk) {
    }

    @Override
    public int getSpawnHeight() {
        return 0;
    }

    @Override
    public void populateNoise(IWorld iWorld, Chunk chunk) {

    }

    @Override
    public int getHeightOnGround(int i, int i1, Heightmap.Type type) {
        return 0;
    }
}
