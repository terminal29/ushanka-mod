package com.terminal29.ushanka.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.terminal29.ushanka.block.HypergateBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.EndPortalBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.GlAllocationUtils;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.SystemUtil;
import net.minecraft.util.math.Direction;

import java.nio.FloatBuffer;
import java.util.Random;

@Environment(EnvType.CLIENT)
public class HypergateBlockEntityRenderer extends BlockEntityRenderer<HypergateBlockEntity> {
    private static final Identifier SKY_TEX = new Identifier("textures/environment/end_sky.png");
    private static final Identifier PORTAL_TEX = new Identifier("textures/entity/end_portal.png");
    private static final Random RANDOM = new Random(31100L);
    private static final FloatBuffer field_4408 = GlAllocationUtils.allocateFloatBuffer(16);
    private static final FloatBuffer field_4404 = GlAllocationUtils.allocateFloatBuffer(16);
    private final FloatBuffer field_4403 = GlAllocationUtils.allocateFloatBuffer(16);

    public HypergateBlockEntityRenderer() {
    }

    @Override
    public void render(HypergateBlockEntity blockEntity_1, double double_1, double double_2, double double_3, float float_1, int int_1) {
        GlStateManager.disableLighting();
        RANDOM.setSeed(31100L);
        GlStateManager.getMatrix(2982, field_4408);
        GlStateManager.getMatrix(2983, field_4404);
        double double_4 = double_1 * double_1 + double_2 * double_2 + double_3 * double_3;
        int int_2 = this.method_3592(double_4);
        float float_2 = this.method_3594();
        boolean boolean_1 = false;
        GameRenderer gameRenderer_1 = MinecraftClient.getInstance().gameRenderer;

        for(int int_3 = 0; int_3 < int_2; ++int_3) {
            GlStateManager.pushMatrix();
            float float_3 = 2.0F / (float)(18 - int_3);
            if (int_3 == 0) {
                this.bindTexture(SKY_TEX);
                float_3 = 0.15F;
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            }

            if (int_3 >= 1) {
                this.bindTexture(PORTAL_TEX);
                boolean_1 = true;
                gameRenderer_1.setFogBlack(true);
            }

            if (int_3 == 1) {
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
            }

            GlStateManager.texGenMode(GlStateManager.TexCoord.S, 9216);
            GlStateManager.texGenMode(GlStateManager.TexCoord.T, 9216);
            GlStateManager.texGenMode(GlStateManager.TexCoord.R, 9216);
            GlStateManager.texGenParam(GlStateManager.TexCoord.S, 9474, this.method_3593(1.0F, 0.0F, 0.0F, 0.0F));
            GlStateManager.texGenParam(GlStateManager.TexCoord.T, 9474, this.method_3593(0.0F, 1.0F, 0.0F, 0.0F));
            GlStateManager.texGenParam(GlStateManager.TexCoord.R, 9474, this.method_3593(0.0F, 0.0F, 1.0F, 0.0F));
            GlStateManager.enableTexGen(GlStateManager.TexCoord.S);
            GlStateManager.enableTexGen(GlStateManager.TexCoord.T);
            GlStateManager.enableTexGen(GlStateManager.TexCoord.R);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.loadIdentity();
            GlStateManager.translatef(0.5F, 0.5F, 0.0F);
            GlStateManager.scalef(0.5F, 0.5F, 1.0F);
            float float_4 = (float)(int_3 + 1);
            GlStateManager.translatef(17.0F / float_4, (2.0F + float_4 / 1.5F) * ((float)(SystemUtil.getMeasuringTimeMs() % 800000L) / 800000.0F), 0.0F);
            GlStateManager.rotatef((float_4 * float_4 * 4321.0F + float_4 * 9.0F) * 2.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.scalef(4.5F - float_4 / 4.0F, 4.5F - float_4 / 4.0F, 1.0F);
            GlStateManager.multMatrix(field_4404);
            GlStateManager.multMatrix(field_4408);
            Tessellator tessellator_1 = Tessellator.getInstance();
            BufferBuilder bufferBuilder_1 = tessellator_1.getBufferBuilder();
            bufferBuilder_1.begin(7, VertexFormats.POSITION_COLOR);
            float float_5 = (RANDOM.nextFloat() * 0.5F + 0.1F) * float_3;
            float float_6 = (RANDOM.nextFloat() * 0.5F + 0.4F) * float_3;
            float float_7 = (RANDOM.nextFloat() * 0.5F + 0.5F) * float_3;

            double offset = 1.0D;
            double zoffset = 0.005D;

            if (blockEntity_1.shouldDrawSide(blockEntity_1.getCachedState(), Direction.SOUTH)) {
                bufferBuilder_1.vertex(double_1 - offset, double_2, double_3 + offset + zoffset).color(float_5, float_6, float_7, 1.0F).next();
                bufferBuilder_1.vertex(double_1 + 2*offset, double_2, double_3 + offset + zoffset).color(float_5, float_6, float_7, 1.0F).next();
                bufferBuilder_1.vertex(double_1 + 2*offset, double_2 + offset, double_3 + offset + zoffset).color(float_5, float_6, float_7, 1.0F).next();
                bufferBuilder_1.vertex(double_1 - offset, double_2 + offset, double_3 + offset + zoffset).color(float_5, float_6, float_7, 1.0F).next();
            }

            if (blockEntity_1.shouldDrawSide(blockEntity_1.getCachedState(), Direction.NORTH)) {
                bufferBuilder_1.vertex(double_1 - offset, double_2 + offset, double_3 - zoffset).color(float_5, float_6, float_7, 1.0F).next();
                bufferBuilder_1.vertex(double_1 + 2*offset, double_2 + offset, double_3 - zoffset).color(float_5, float_6, float_7, 1.0F).next();
                bufferBuilder_1.vertex(double_1 + 2*offset, double_2, double_3 - zoffset).color(float_5, float_6, float_7, 1.0F).next();
                bufferBuilder_1.vertex(double_1 - offset, double_2, double_3 - zoffset).color(float_5, float_6, float_7, 1.0F).next();
            }

            if (blockEntity_1.shouldDrawSide(blockEntity_1.getCachedState(), Direction.EAST)) {
                bufferBuilder_1.vertex(double_1 + offset + zoffset, double_2 + offset, double_3 - offset).color(float_5, float_6, float_7, 1.0F).next();
                bufferBuilder_1.vertex(double_1 + offset + zoffset, double_2 + offset, double_3 + 2*offset).color(float_5, float_6, float_7, 1.0F).next();
                bufferBuilder_1.vertex(double_1 + offset + zoffset, double_2, double_3 + 2*offset).color(float_5, float_6, float_7, 1.0F).next();
                bufferBuilder_1.vertex(double_1 + offset + zoffset, double_2, double_3 - offset).color(float_5, float_6, float_7, 1.0F).next();
            }

            if (blockEntity_1.shouldDrawSide(blockEntity_1.getCachedState(), Direction.WEST)) {
                bufferBuilder_1.vertex(double_1 - zoffset, double_2, double_3 - offset).color(float_5, float_6, float_7, 1.0F).next();
                bufferBuilder_1.vertex(double_1 - zoffset, double_2, double_3 + 2*offset).color(float_5, float_6, float_7, 1.0F).next();
                bufferBuilder_1.vertex(double_1 - zoffset, double_2 + offset, double_3 + 2*offset).color(float_5, float_6, float_7, 1.0F).next();
                bufferBuilder_1.vertex(double_1 - zoffset, double_2 + offset, double_3 - offset).color(float_5, float_6, float_7, 1.0F).next();
            }

            tessellator_1.draw();
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
            this.bindTexture(SKY_TEX);
        }

        GlStateManager.disableBlend();
        GlStateManager.disableTexGen(GlStateManager.TexCoord.S);
        GlStateManager.disableTexGen(GlStateManager.TexCoord.T);
        GlStateManager.disableTexGen(GlStateManager.TexCoord.R);
        GlStateManager.enableLighting();
        if (boolean_1) {
            gameRenderer_1.setFogBlack(false);
        }
    }

        protected int method_3592(double double_1) {
            byte int_9;
            if (double_1 > 36864.0D) {
                int_9 = 1;
            } else if (double_1 > 25600.0D) {
                int_9 = 3;
            } else if (double_1 > 16384.0D) {
                int_9 = 5;
            } else if (double_1 > 9216.0D) {
                int_9 = 7;
            } else if (double_1 > 4096.0D) {
                int_9 = 9;
            } else if (double_1 > 1024.0D) {
                int_9 = 11;
            } else if (double_1 > 576.0D) {
                int_9 = 13;
            } else if (double_1 > 256.0D) {
                int_9 = 14;
            } else {
                int_9 = 15;
            }

            return int_9;
        }

        protected float method_3594() {
            return 0.75F;
        }

        private FloatBuffer method_3593(float float_1, float float_2, float float_3, float float_4) {
            this.field_4403.clear();
            this.field_4403.put(float_1).put(float_2).put(float_3).put(float_4);
            this.field_4403.flip();
            return this.field_4403;
        }

}
