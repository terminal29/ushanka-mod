package com.terminal29.ushanka.mixin;

import com.sun.istack.internal.Nullable;
import com.terminal29.ushanka.dimension.DimensionTypeVillage;
import com.terminal29.ushanka.dimension.UshankaDimensions;
import com.terminal29.ushanka.extension.IClientPlayerEntityExtension;
import com.terminal29.ushanka.extension.IPlayerEntityExtension;
import com.terminal29.ushanka.extension.IServerPlayerEntityExtension;
import com.terminal29.ushanka.extension.IServerWorldExtension;
import com.terminal29.ushanka.item.InventoryType;
import com.terminal29.ushanka.item.ItemUshanka;
import com.terminal29.ushanka.item.TickableItem;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity implements IPlayerEntityExtension {
    boolean hasUshankaEquipped = false;
    DimensionType previousDimension = null;
    BlockPos previousPosition = null;

    protected MixinPlayerEntity(EntityType<? extends LivingEntity> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    protected void onTick(CallbackInfo info) {
        PlayerEntity $this = (PlayerEntity)(Object)this;
        $this.inventory.main.forEach( itemStack -> {
            if(itemStack.getItem() instanceof TickableItem){
                ((TickableItem)(itemStack.getItem())).tick(itemStack, this, this.world, InventoryType.MAIN);
            }
        });
        $this.inventory.armor.forEach( itemStack -> {
            if(itemStack.getItem() instanceof TickableItem){
                ((TickableItem)(itemStack.getItem())).tick(itemStack, this, this.world, InventoryType.ARMOR);
            }
        });
        $this.inventory.offHand.forEach( itemStack -> {
            if(itemStack.getItem() instanceof TickableItem){
                ((TickableItem)(itemStack.getItem())).tick(itemStack, this, this.world, InventoryType.OFFHAND);
            }
        });

        if($this.getEquippedStack(EquipmentSlot.HEAD).getItem() instanceof ItemUshanka){
            hasUshankaEquipped = true;
        }else{
            hasUshankaEquipped = false;
        }

        if(hasUshankaEquipped){
            if($this instanceof ClientPlayerEntity){
                if(!this.isCameraIso() && !(this.dimension instanceof DimensionTypeVillage))
                    ((IClientPlayerEntityExtension)this).requestCameraIso(true);
            }
            if(this.isCameraIso() && !(this.dimension instanceof DimensionTypeVillage)){
                this.changeToDimension(UshankaDimensions.VILLAGE, this.getPreviousPosition());
            }
        }

        if(!hasUshankaEquipped && this.dimension instanceof DimensionTypeVillage){
            if($this instanceof ClientPlayerEntity){
                ((IClientPlayerEntityExtension)this).forceCameraIso(false);
            }
            this.changeToDimension(this.getPreviousDimension(), this.getPreviousPosition());

        }

    }

    // With help from https://github.com/StellarHorizons/Galacticraft-Rewoven/blob/master/src/main/java/com/hrznstudio/galacticraft/GalacticraftCommands.java
    @Override
    public void changeToDimension(DimensionType type) {
        this.changeToDimension(type, null);
    }

    @Override
    public void changeToDimension(DimensionType type, @Nullable BlockPos position) {
        if(this.getServer() != null) {
            ServerWorld world = this.getServer().getWorld(type);
            if (world != null) {
                ((IServerWorldExtension) world).addOnTickAction(() -> {

                    BlockPos spawnPos = world.getSpawnPos();
                    double x, y, z;
                    if (position == null) {
                        x = spawnPos.getX();
                        y = spawnPos.getY();
                        z = spawnPos.getZ();
                    } else {
                        x = position.getX();
                        y = position.getY();
                        z = position.getZ();
                    }

                    if ((Object)this instanceof ServerPlayerEntity) {
                        ServerPlayerEntity player = (ServerPlayerEntity)(Object)this;
                        player.stopRiding();
                        if (player.isSleeping()) {
                            player.wakeUp(true, true, false);
                        }

                        if (world == player.world) {
                            player.networkHandler.teleportRequest(x, y, z, 0, 0, Collections.emptySet());
                        } else {
                            previousDimension = this.dimension;
                            previousPosition = this.getBlockPos();
                            player.teleport(world, x, y, z, 0, 0);
                        }
                    } else {
                        if (world == this.world) {
                            this.setPosition(x, y, z);
                        } else {
                            previousPosition = this.getBlockPos();
                            previousDimension = this.dimension;
                            this.detach();
                            this.dimension = world.dimension.getType();
                            Entity entity_2 = this;
                            entity_2 = entity_2.getType().create(world);
                            if (entity_2 == null) {
                                return false;
                            }
                            entity_2.method_5878(entity_2);
                            entity_2.setPosition(x, y, z);
                            world.method_18769(entity_2);
                            entity_2.removed = true;
                        }
                    }

                    if (!(this instanceof LivingEntity) || !((LivingEntity) this).isFallFlying()) {
                        this.setVelocity(this.getVelocity().multiply(1.0D, 0.0D, 1.0D));
                        this.onGround = true;
                    }
                    return true;
                });
            }
        }
    }

    @Override
    public DimensionType getPreviousDimension() {
        return previousDimension;
    }

    @Override
    public BlockPos getPreviousPosition() {
        return previousPosition;
    }


}
