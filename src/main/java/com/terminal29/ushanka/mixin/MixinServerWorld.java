package com.terminal29.ushanka.mixin;

import com.terminal29.ushanka.ModInfo;
import com.terminal29.ushanka.UshankaPersistentData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.WorldSaveHandler;
import net.minecraft.world.chunk.ChunkManager;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.Executor;
import java.util.function.BiFunction;

@Mixin(ServerWorld.class)
public abstract class MixinServerWorld extends World {


    protected MixinServerWorld(LevelProperties levelProperties_1, DimensionType dimensionType_1, BiFunction<World, Dimension, ChunkManager> biFunction_1, Profiler profiler_1, boolean boolean_1) {
        super(levelProperties_1, dimensionType_1, biFunction_1, profiler_1, boolean_1);
    }

    @Inject(method="<init>", at=@At("RETURN"))
    void init(MinecraftServer minecraftServer_1, Executor executor_1, WorldSaveHandler worldSaveHandler_1, LevelProperties levelProperties_1, DimensionType dimensionType_1, Profiler profiler_1, WorldGenerationProgressListener worldGenerationProgressListener_1, CallbackInfo cbi){
        ((ServerWorld)(Object)this).getPersistentStateManager().getOrCreate(UshankaPersistentData::new, ModInfo.DISPLAY_NAME);
    }

}
