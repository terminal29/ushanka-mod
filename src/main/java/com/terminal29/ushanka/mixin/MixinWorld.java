package com.terminal29.ushanka.mixin;

import com.terminal29.ushanka.dimension.DimensionVillage;
import com.terminal29.ushanka.dimension.VillageBiome;
import com.terminal29.ushanka.dimension.VillageIslandManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.util.math.Vec3d;

@Mixin(World.class)
public class MixinWorld {
    @Inject(method="getSkyColor", at=@At("HEAD"), cancellable=true)
    public void onGetSkyColor(BlockPos blockPos_1, float float_1, CallbackInfoReturnable<Vec3d> cbi){
        if(((World)(Object)this).getBiome(blockPos_1) instanceof VillageBiome){
            if(VillageIslandManager.INSTANCE.chunkToIsland(new ChunkPos(blockPos_1)) != null){
                cbi.setReturnValue(VillageIslandManager.INSTANCE.chunkToIsland(new ChunkPos(blockPos_1)).getSkyColor());
            }
        }
    }
}
