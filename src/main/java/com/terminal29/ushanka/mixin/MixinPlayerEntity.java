package com.terminal29.ushanka.mixin;

import com.terminal29.ushanka.MathUtilities;
import com.terminal29.ushanka.ModInfo;
import com.terminal29.ushanka.UshankaPersistentData;
import com.terminal29.ushanka.dimension.UshankaDimensions;
import com.terminal29.ushanka.extension.IGameRenderExtension;
import com.terminal29.ushanka.extension.IPlayerEntityExtension;
import io.netty.buffer.Unpooled;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.packet.CustomPayloadC2SPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity implements IPlayerEntityExtension {

    private long ticks = 0;
    private CameraDirection currentDirection = CameraDirection.NONE;
    private CameraDirection requestedDirection = CameraDirection.NONE;
    private boolean isChangingDirection = false;
    private float currentIsoScale = 15;
    private float requestedIsoScale = 15;
    private float isoSlider = 0;
    private float currentIsoDistance = 1;
    private float requestedIsoDistance = 1;
    private boolean isCameraIso = false;
    private boolean requestedCameraIso = false;
    private boolean isCameraAnimatingIsoChange = false;

    protected MixinPlayerEntity(EntityType<? extends LivingEntity> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }

    // Need to send packet to server to set this data, getServer is inaccessible from client side...
    // Need to send packet back to client on loading? maybe? not sure?

    private UshankaPersistentData getPersistentData(){
        return MinecraftClient.getInstance().getServer().getWorld(this.dimension).getPersistentStateManager().get(UshankaPersistentData::new, ModInfo.DISPLAY_NAME);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(CallbackInfo cbi) {
        isCameraIso = getPersistentData().isPlayerIso(this.uuid);
        System.out.println(world.isClient() + ":" + getPersistentData().isPlayerIso(this.uuid));

        IGameRenderExtension gameRenderExtension = (IGameRenderExtension) MinecraftClient.getInstance().gameRenderer;
        gameRenderExtension.addOnRenderEventHandler(e -> {
            if (!world.isClient())
                return;
            if (requestedCameraIso != isCameraIso) {
                isCameraAnimatingIsoChange = true;
                if (requestedCameraIso) {
                    isoSlider = MathUtilities.lerp(isoSlider, 1, 0.1f);
                    this.pitch = MathUtilities.lerp(this.pitch, 0, isoSlider);
                    if (Math.abs(1 - isoSlider) < ISO_DEADZONE) {
                        isoSlider = 1;
                        isCameraIso = true;
                        getPersistentData().setPlayerIso(this.uuid, isCameraIso);
                    }
                    if ((requestedDirection == CameraDirection.NONE)) {
                        requestedDirection = getClosestCameraDirection(this.yaw + 90); // not sure why i need this
                    }
                    rotateToDirection();
                } else {
                    isoSlider = MathUtilities.lerp(isoSlider, 0, 0.2f);
                    if (Math.abs(isoSlider) < ISO_DEADZONE) {
                        isoSlider = 0;
                        isCameraIso = false;
                        getPersistentData().setPlayerIso(this.uuid, isCameraIso);
                        requestedDirection = CameraDirection.NONE;
                        currentDirection = CameraDirection.NONE;
                    }
                }
            } else {
                isCameraAnimatingIsoChange = false;
            }

            if (isCameraIso) {
                currentIsoScale = requestedIsoScale;
                currentIsoDistance = requestedIsoDistance;
                rotateToDirection();
            }
        });
    }

    @Inject(method = "tick", at = @At("RETURN"))
    protected void onTick(CallbackInfo info) {
        ticks++;
        if (this.world.isClient()) {
            // We are client side
            if (isCameraIso && this.dimension != UshankaDimensions.VILLAGE) {
                PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                buf.writeBoolean(true);
                buf.writeUuid(this.getUuid());
                ((ClientPlayerEntity) (Object) this).networkHandler.sendPacket(new CustomPayloadC2SPacket(new Identifier(ModInfo.DISPLAY_NAME, ModInfo.Packets.CHANGE_DIMENSION), buf));
            }
            if (!isCameraIso && this.dimension == UshankaDimensions.VILLAGE) {
                PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                buf.writeBoolean(false);
                buf.writeUuid(this.getUuid());
                ((ClientPlayerEntity) (Object) this).networkHandler.sendPacket(new CustomPayloadC2SPacket(new Identifier(ModInfo.DISPLAY_NAME, ModInfo.Packets.CHANGE_DIMENSION), buf));
            }
        }
    }

    @Override
    public void teleportToVillage() {
        changeToDimension(UshankaDimensions.VILLAGE);
    }

    @Override
    public void teleportToOverworld() {
        changeToDimension(DimensionType.OVERWORLD);
    }

    // With help from https://github.com/StellarHorizons/Galacticraft-Rewoven/blob/master/src/main/java/com/hrznstudio/galacticraft/GalacticraftCommands.java
    private void changeToDimension(DimensionType type) {
        if (this.dimension == null || this.dimension == type) {
            System.out.println("Dimension to: " + this.dimension);
            return;
        }

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

    private float angleForDirection(CameraDirection direction) {
        if (direction == CameraDirection.NORTH)
            return 0;
        if (direction == CameraDirection.SOUTH)
            return 180;
        if (direction == CameraDirection.EAST)
            return 90;
        if (direction == CameraDirection.WEST)
            return 270;
        return Float.POSITIVE_INFINITY;
    }

    private void rotateToDirection() {
        if (requestedDirection == CameraDirection.NORTH && currentDirection != CameraDirection.NORTH) {
            isChangingDirection = true;
            this.yaw = MathUtilities.angleLerp(this.yaw, 0, 0.2f);
            if (Math.abs(MathUtilities.shortAngleDist(this.yaw, 0)) < YAW_DEADZONE) {
                this.yaw = 0;
                currentDirection = CameraDirection.NORTH;
                isChangingDirection = false;
            }
        }
        if (requestedDirection == CameraDirection.SOUTH && currentDirection != CameraDirection.SOUTH) {
            isChangingDirection = true;
            this.yaw = MathUtilities.angleLerp(this.yaw, 180, 0.2f);
            if (Math.abs(MathUtilities.shortAngleDist(this.yaw, 180)) < YAW_DEADZONE) {
                this.yaw = 180;
                currentDirection = CameraDirection.SOUTH;
                isChangingDirection = false;
            }
        }
        if (requestedDirection == CameraDirection.EAST && currentDirection != CameraDirection.EAST) {
            isChangingDirection = true;
            this.yaw = MathUtilities.angleLerp(this.yaw, 90, 0.2f);
            if (Math.abs(MathUtilities.shortAngleDist(this.yaw, 90)) < YAW_DEADZONE) {
                this.yaw = 90;
                currentDirection = CameraDirection.EAST;
                isChangingDirection = false;
            }
        }
        if (requestedDirection == CameraDirection.WEST && currentDirection != CameraDirection.WEST) {
            isChangingDirection = true;
            this.yaw = MathUtilities.angleLerp(this.yaw, 270, 0.2f);
            if (Math.abs(MathUtilities.shortAngleDist(this.yaw, 270)) < YAW_DEADZONE) {
                this.yaw = 270;
                currentDirection = CameraDirection.WEST;
                isChangingDirection = false;
            }
        }
    }

    private CameraDirection getClosestCameraDirection(float angle) {
        float northDistance = MathUtilities.shortAngleDist(angle, angleForDirection(CameraDirection.NORTH));
        float eastDistance = MathUtilities.shortAngleDist(angle, angleForDirection(CameraDirection.EAST));
        float westDistance = MathUtilities.shortAngleDist(angle, angleForDirection(CameraDirection.WEST));
        float southDistance = MathUtilities.shortAngleDist(angle, angleForDirection(CameraDirection.SOUTH));

        List<Pair<Float, CameraDirection>> directions = new ArrayList<>();
        directions.add(new Pair<>(northDistance, CameraDirection.NORTH));
        directions.add(new Pair<>(eastDistance, CameraDirection.EAST));
        directions.add(new Pair<>(westDistance, CameraDirection.WEST));
        directions.add(new Pair<>(southDistance, CameraDirection.SOUTH));

        directions.sort(Comparator.comparing(Pair::getLeft));

        return directions.get(0).getRight();
    }

    @Override
    public float getIsoScale() {
        return currentIsoScale;
    }

    @Override
    public void setIsoScale(float requestedIsoScale) {
        this.requestedIsoScale = requestedIsoScale;
    }

    @Override
    public float getIsoDistance() {
        return currentIsoDistance;
    }

    @Override
    public void setIsoDistance(float requestedIsoDistance) {
        this.requestedIsoDistance = requestedIsoDistance;
    }

    @Override
    public float getIsoSlider() {
        return this.isoSlider;
    }

    @Override
    public boolean isChangingDirection() {
        return isChangingDirection;
    }

    @Override
    public CameraDirection getCameraDirection() {
        return currentDirection;
    }

    @Override
    public void setCameraDirection(CameraDirection direction) {
        this.requestedDirection = direction;
    }

    @Override
    public void rotateCameraLeft() {
        if (getCameraDirection() == CameraDirection.NORTH) {
            setCameraDirection(CameraDirection.WEST);
        } else if (getCameraDirection() == CameraDirection.WEST) {
            setCameraDirection(CameraDirection.SOUTH);
        } else if (getCameraDirection() == CameraDirection.SOUTH) {
            setCameraDirection(CameraDirection.EAST);
        } else if (getCameraDirection() == CameraDirection.EAST) {
            setCameraDirection(CameraDirection.NORTH);
        }
        System.out.println(getCameraDirection().name());
    }

    @Override
    public void rotateCameraRight() {
        if (getCameraDirection() == CameraDirection.NORTH) {
            setCameraDirection(CameraDirection.EAST);
        } else if (getCameraDirection() == CameraDirection.EAST) {
            setCameraDirection(CameraDirection.SOUTH);
        } else if (getCameraDirection() == CameraDirection.SOUTH) {
            setCameraDirection(CameraDirection.WEST);
        } else if (getCameraDirection() == CameraDirection.WEST) {
            setCameraDirection(CameraDirection.NORTH);
        }
        System.out.println(getCameraDirection().name());
    }

    @Override
    public boolean isCameraIso() {
        return isCameraIso || isCameraAnimatingIsoChange;
    }

    @Override
    public void setCameraIso(boolean state) {
        if (!isCameraAnimatingIsoChange)
            requestedCameraIso = state;
    }
}