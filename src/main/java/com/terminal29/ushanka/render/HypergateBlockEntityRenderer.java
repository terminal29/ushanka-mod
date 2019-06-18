package com.terminal29.ushanka.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.terminal29.ushanka.ModInfo;
import com.terminal29.ushanka.block.HypergateBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.EndPortalBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.GlAllocationUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.SystemUtil;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.nio.FloatBuffer;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

@Environment(EnvType.CLIENT)
public class HypergateBlockEntityRenderer extends BlockEntityRenderer<HypergateBlockEntity> {
    private static final Identifier PLAIN_WHITE = ModInfo.identifierFor("textures/environment/white.png");
    private static final Identifier VILLAGE_CLOUDS_TEX = ModInfo.identifierFor("textures/environment/village_clouds.png");
    private static final Random RANDOM = new Random(31100L);
    private static final FloatBuffer MODELVIEW_MATRIX = GlAllocationUtils.allocateFloatBuffer(16);
    private static final FloatBuffer PROJECTION_MATRIX = GlAllocationUtils.allocateFloatBuffer(16);
    private final FloatBuffer field_4403 = GlAllocationUtils.allocateFloatBuffer(16);

    public HypergateBlockEntityRenderer() {
    }

    @Override
    public void render(HypergateBlockEntity blockEntity_1, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.disableLighting();
        RANDOM.setSeed(31100L);
        GlStateManager.getMatrix(GL11.GL_MODELVIEW_MATRIX , MODELVIEW_MATRIX);
        GlStateManager.getMatrix(GL11.GL_PROJECTION_MATRIX , PROJECTION_MATRIX);
        double double_4 = x * x + y * y + z * z;
        int int_2 = this.method_3592(double_4);
        float float_2 = this.method_3594();
        boolean boolean_1 = false;
        GameRenderer gameRenderer_1 = MinecraftClient.getInstance().gameRenderer;

        //sky
        GlStateManager.matrixMode(GL_MODELVIEW);
        GlStateManager.pushMatrix();
        {
            this.bindTexture(PLAIN_WHITE);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);


            GlStateManager.texGenMode(GlStateManager.TexCoord.S, 9216);
            GlStateManager.texGenMode(GlStateManager.TexCoord.T, 9216);
            GlStateManager.texGenMode(GlStateManager.TexCoord.R, 9216);
            GlStateManager.texGenParam(GlStateManager.TexCoord.S, 9474, this.method_3593(1.0F, 0.0F, 0.0F, 0.0F));
            GlStateManager.texGenParam(GlStateManager.TexCoord.T, 9474, this.method_3593(0.0F, 1.0F, 0.0F, 0.0F));
            GlStateManager.texGenParam(GlStateManager.TexCoord.R, 9474, this.method_3593(0.0F, 0.0F, 1.0F, 0.0F));
            GlStateManager.enableTexGen(GlStateManager.TexCoord.S);
            GlStateManager.enableTexGen(GlStateManager.TexCoord.T);
            GlStateManager.enableTexGen(GlStateManager.TexCoord.R);
        }
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(GL_TEXTURE);
        GlStateManager.pushMatrix();
        {
            GlStateManager.loadIdentity();
            GlStateManager.translatef(0.5F, 0.5F, 0.0F);

            GlStateManager.scalef(
                    (float) MinecraftClient.getInstance().window.getFramebufferWidth() / (float) MinecraftClient.getInstance().window.getFramebufferHeight(),
                    1.0F,
                    1.0F);

            GlStateManager.scalef(5.0F, 5.0F, 1.0F);
            float float_4 = (float) (1);
            GlStateManager.translatef(-(5.0F + float_4) * ((float) (SystemUtil.getMeasuringTimeMs() % 800000L) / 800000.0F), 17.0F / float_4, 0.0F);
            //GlStateManager.rotatef((float_4 * float_4 * 4321.0F + float_4 * 9.0F) * 2.0F, 0.0F, 0.0F, 1.0F);
            ///GlStateManager.scalef(4.5F - float_4 / 4.0F, 4.5F - float_4 / 4.0F, 1.0F);
            GlStateManager.multMatrix(MODELVIEW_MATRIX);
            GlStateManager.multMatrix(PROJECTION_MATRIX);
            Tessellator tessellator_1 = Tessellator.getInstance();
            BufferBuilder bufferBuilder_1 = tessellator_1.getBufferBuilder();
            bufferBuilder_1.begin(7, VertexFormats.POSITION_COLOR);

            float float_3 = 0.15F;
            float float_5 = (RANDOM.nextFloat() * 0.5F + 0.1F) * float_3;
            float float_6 = (RANDOM.nextFloat() * 0.5F + 0.4F) * float_3;
            float float_7 = (RANDOM.nextFloat() * 0.5F + 0.5F) * float_3;

            double offset = 1.0D;
            double zoffset = 0.0005D;

            Vec3d targetIslandSkyColor = blockEntity_1.getTargetIsland() != null ? blockEntity_1.getTargetIsland().getSkyColor() : new Vec3d(0, 0, 0);
            float r = (float) targetIslandSkyColor.x;
            float g = (float) targetIslandSkyColor.y;
            float b = (float) targetIslandSkyColor.z;

            if (blockEntity_1.shouldDrawSide(blockEntity_1.getCachedState(), Direction.SOUTH)) {
                bufferBuilder_1.vertex(x - offset, y, z + offset + zoffset).color(r, g, b, 1.0F).next();
                bufferBuilder_1.vertex(x + 2 * offset, y, z + offset + zoffset).color(r, g, b, 1.0F).next();
                bufferBuilder_1.vertex(x + 2 * offset, y + offset, z + offset + zoffset).color(r, g, b, 1.0F).next();
                bufferBuilder_1.vertex(x - offset, y + offset, z + offset + zoffset).color(r, g, b, 1.0F).next();
            }

            if (blockEntity_1.shouldDrawSide(blockEntity_1.getCachedState(), Direction.NORTH)) {
                bufferBuilder_1.vertex(x - offset, y + offset, z - zoffset).color(r, g, b, 1.0F).next();
                bufferBuilder_1.vertex(x + 2 * offset, y + offset, z - zoffset).color(r, g, b, 1.0F).next();
                bufferBuilder_1.vertex(x + 2 * offset, y, z - zoffset).color(r, g, b, 1.0F).next();
                bufferBuilder_1.vertex(x - offset, y, z - zoffset).color(r, g, b, 1.0F).next();
            }

            if (blockEntity_1.shouldDrawSide(blockEntity_1.getCachedState(), Direction.EAST)) {
                bufferBuilder_1.vertex(x + offset + zoffset, y + offset, z - offset).color(r, g, b, 1.0F).next();
                bufferBuilder_1.vertex(x + offset + zoffset, y + offset, z + 2 * offset).color(r, g, b, 1.0F).next();
                bufferBuilder_1.vertex(x + offset + zoffset, y, z + 2 * offset).color(r, g, b, 1.0F).next();
                bufferBuilder_1.vertex(x + offset + zoffset, y, z - offset).color(r, g, b, 1.0F).next();
            }

            if (blockEntity_1.shouldDrawSide(blockEntity_1.getCachedState(), Direction.WEST)) {
                bufferBuilder_1.vertex(x - zoffset, y, z - offset).color(r, g, b, 1.0F).next();
                bufferBuilder_1.vertex(x - zoffset, y, z + 2 * offset).color(r, g, b, 1.0F).next();
                bufferBuilder_1.vertex(x - zoffset, y + offset, z + 2 * offset).color(r, g, b, 1.0F).next();
                bufferBuilder_1.vertex(x - zoffset, y + offset, z - offset).color(r, g, b, 1.0F).next();
            }
            tessellator_1.draw();
            GlStateManager.disableBlend();
            GlStateManager.disableTexGen(GlStateManager.TexCoord.S);
            GlStateManager.disableTexGen(GlStateManager.TexCoord.T);
            GlStateManager.disableTexGen(GlStateManager.TexCoord.R);
            GlStateManager.enableLighting();
            if (boolean_1) {
                gameRenderer_1.setFogBlack(false);
            }
        }
        GlStateManager.popMatrix();

        // clouds
        GlStateManager.matrixMode(GL_MODELVIEW);
        GlStateManager.pushMatrix();
        {
            GlStateManager.disableLighting();
            this.bindTexture(VILLAGE_CLOUDS_TEX);

            float xPos = -(5.0F) * ((float) (SystemUtil.getMeasuringTimeMs() % 800000L) / 800000.0F) + blockEntity_1.getPos().getX()*0.1415926535f + blockEntity_1.getPos().getZ()*0.1345234563f + blockEntity_1.getPos().getY()*0.12323423f;
            double offset = 1.0D;
            double zoffset = 0.001D;
            Tessellator tessellator_1 = Tessellator.getInstance();
            BufferBuilder bufferBuilder_1 = tessellator_1.getBufferBuilder();
            bufferBuilder_1.begin(GL_QUADS, VertexFormats.POSITION_UV_COLOR);
            if (blockEntity_1.shouldDrawSide(blockEntity_1.getCachedState(), Direction.SOUTH)) {
                bufferBuilder_1.vertex(x - offset, y, z + offset + zoffset).texture(xPos,1).color(1, 1, 2, 1.0F).next();
                bufferBuilder_1.vertex(x + 2 * offset, y, z + offset + zoffset).texture(xPos + (48.0/256),1).color(1, 1, 2, 1.0F).next();
                bufferBuilder_1.vertex(x + 2 * offset, y + offset, z + offset + zoffset).texture(xPos + (48.0/256),0).color(1, 1, 2, 1.0F).next();
                bufferBuilder_1.vertex(x - offset, y + offset, z + offset + zoffset).texture(xPos,0).color(1, 1, 2, 1.0F).next();
            }

            if (blockEntity_1.shouldDrawSide(blockEntity_1.getCachedState(), Direction.NORTH)) {
                bufferBuilder_1.vertex(x - offset, y + offset, z - zoffset).texture(xPos + (48.0/256),0).color(1, 1, 1, 1.0F).next();
                bufferBuilder_1.vertex(x + 2 * offset, y + offset, z - zoffset).texture(xPos,0).color(1, 1, 1, 1.0F).next();
                bufferBuilder_1.vertex(x + 2 * offset, y, z - zoffset).texture(xPos,1).color(1, 1, 1, 1.0F).next();
                bufferBuilder_1.vertex(x - offset, y, z - zoffset).texture(xPos + (48.0/256),1).color(1, 1, 1, 1.0F).next();
            }

            if (blockEntity_1.shouldDrawSide(blockEntity_1.getCachedState(), Direction.EAST)) {
                bufferBuilder_1.vertex(x + offset + zoffset, y + offset, z - offset).texture(xPos + (48.0/256),0).color(1, 1, 1, 1.0F).next();
                bufferBuilder_1.vertex(x + offset + zoffset, y + offset, z + 2 * offset).texture(xPos,0).color(1, 1, 1, 1.0F).next();
                bufferBuilder_1.vertex(x + offset + zoffset, y, z + 2 * offset).texture(xPos,1).color(1, 1, 1, 1.0F).next();
                bufferBuilder_1.vertex(x + offset + zoffset, y, z - offset).texture(xPos + (48.0/256),1).color(1, 1, 1, 1.0F).next();
            }

            if (blockEntity_1.shouldDrawSide(blockEntity_1.getCachedState(), Direction.WEST)) {
                bufferBuilder_1.vertex(x - zoffset, y, z - offset).texture(xPos,1).color(1, 1, 1, 1.0F).next();
                bufferBuilder_1.vertex(x - zoffset, y, z + 2 * offset).texture(xPos + (48.0/256),1).color(1, 1, 1, 1.0F).next();
                bufferBuilder_1.vertex(x - zoffset, y + offset, z + 2 * offset).texture(xPos + (48.0/256),0).color(1, 1, 1, 1.0F).next();
                bufferBuilder_1.vertex(x - zoffset, y + offset, z - offset).texture(xPos,0).color(1, 1, 1, 1.0F).next();
            }
            tessellator_1.draw();
            GlStateManager.enableLighting();
        }
        GlStateManager.popMatrix();

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
