package com.terminal29.ushanka.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.terminal29.ushanka.ICameraExtension;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(Camera.class)
public abstract class MixinCamera implements ICameraExtension {

    @Shadow
    private boolean ready;

    @Shadow
    private BlockView area;

    @Shadow
    private Entity focusedEntity;

    @Shadow
    private float pitch;

    @Shadow
    private float yaw;

    @Shadow
    private boolean thirdPerson;

    @Shadow
    private boolean inverseView;

    @Shadow
    abstract void moveBy(double double_1, double double_2, double double_3);

    @Shadow
    abstract void setRotation(float float_1, float float_2);

    @Shadow
    abstract void setPos(Vec3d vec3d_1);

    @Shadow
    abstract void setPos(double double_1, double double_2, double double_3);

    @Shadow
    abstract void updateRotation();

    @Shadow
    abstract double method_19318(double double_1);

    @Shadow
    private float field_18721;

    @Shadow
    private float field_18722;

    public void updateIsometric(BlockView blockView_1, Entity entity_1, boolean boolean_1, boolean boolean_2, float float_1) {
        this.ready = true;
        this.area = blockView_1;
        this.focusedEntity = entity_1;

        this.thirdPerson = true;
        this.inverseView = true;

        this.setRotation(entity_1.getYaw(float_1), entity_1.getPitch(float_1));
        this.setPos(MathHelper.lerp((double) float_1, entity_1.prevX, entity_1.x), MathHelper.lerp((double) float_1, entity_1.prevY, entity_1.y) + (double) MathHelper.lerp(float_1, this.field_18722, this.field_18721), MathHelper.lerp((double) float_1, entity_1.prevZ, entity_1.z));
        if (boolean_1) {
            if (boolean_2) {
                this.yaw += 180.0F;
                this.pitch += -this.pitch * 2.0F;
               this.updateRotation();
            }

            this.moveBy(-this.method_19318(4.0D), 0.0D, 0.0D);
        } else if (entity_1 instanceof LivingEntity && ((LivingEntity) entity_1).isSleeping()) {
            Direction direction_1 = ((LivingEntity) entity_1).getSleepingDirection();
            this.setRotation(direction_1 != null ? direction_1.asRotation() - 180.0F : 0.0F, 0.0F);
            this.moveBy(0.0D, 0.3D, 0.0D);
        } else {
            this.moveBy(-0.05000000074505806D, 0.0D, 0.0D);
        }

        GlStateManager.rotatef(this.pitch, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotatef(this.yaw + 180.0F, 0.0F, 1.0F, 0.0F);
    }
}
