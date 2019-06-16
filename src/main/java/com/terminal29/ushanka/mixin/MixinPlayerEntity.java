package com.terminal29.ushanka.mixin;

import com.terminal29.ushanka.extension.IPlayerEntityExtension;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Collections;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity implements IPlayerEntityExtension {
    protected MixinPlayerEntity(EntityType<? extends LivingEntity> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }

    // With help from https://github.com/StellarHorizons/Galacticraft-Rewoven/blob/master/src/main/java/com/hrznstudio/galacticraft/GalacticraftCommands.java
    @Override
    public void changeToDimension(DimensionType type) {

        ServerWorld world = this.getServer().getWorld(type);
        Entity $this = this;
        BlockPos spawnPos = world.getSpawnPos();
        double x = spawnPos.getX();
        double y = spawnPos.getY();
        double z = spawnPos.getZ();

        if ($this instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) $this;
            player.stopRiding();
            if (player.isSleeping()) {
                player.wakeUp(true, true, false);
            }

            if (world == $this.world) {
                player.networkHandler.teleportRequest(x, y, z, 0, 0, Collections.emptySet());
            } else {
                player.teleport(world, x, y, z, 0, 0);
            }
        } else {
            if (world == $this.world) {
                $this.setPosition(x, y, z);
            } else {
                $this.detach();
                $this.dimension = world.dimension.getType();
                Entity entity_2 = $this;
                $this = $this.getType().create(world);
                if ($this == null) {
                    return;
                }

                $this.method_5878(entity_2);
                $this.setPosition(x, y, z);
                world.method_18769($this);
                entity_2.removed = true;
            }
        }

        if (!($this instanceof LivingEntity) || !((LivingEntity) $this).isFallFlying()) {
            $this.setVelocity($this.getVelocity().multiply(1.0D, 0.0D, 1.0D));
            $this.onGround = true;
        }
    }


}
