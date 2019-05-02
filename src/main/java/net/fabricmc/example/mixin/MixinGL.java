package net.fabricmc.example.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.example.IPlayerEntityExtension;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;


@Mixin(GameRenderer.class)
public abstract class MixinGL{

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    private float viewDistance;

    @Shadow
    private double field_4005;

    @Shadow
    private double field_3988;

    @Shadow
    private double field_4004;

    @Shadow
    @Final
    private Camera camera;

    @Shadow
    private int ticks;

    @Shadow
    public abstract double getFov(Camera camera_1, float float_1, boolean boolean_1);

    @Shadow
    public abstract void bobViewWhenHurt(float float_1);

    @Shadow
    public abstract void bobView(float float_1);

    @Shadow
    @Final
    private BackgroundRenderer backgroundRenderer;

    @Shadow
    public abstract boolean shouldRenderBlockOutline();

    @Shadow
    public abstract void renderAboveClouds(Camera camera_1, WorldRenderer worldRenderer_1, float float_1, double double_1, double double_2, double double_3);

    @Shadow
    public abstract void disableLightmap();

    @Shadow
    public abstract void enableLightmap();

    @Shadow
    public abstract void renderWeather(float f);

    @Shadow
    public boolean renderHand;

    @Shadow
    public abstract void renderHand(Camera c, float f);

    @Shadow
    public int field_4021;



    //@Inject(method="renderCenter", at=@At("HEAD"), cancellable = true)
    @Overwrite
    private void renderCenter(float float_1, long long_1){// CallbackInfo cbi){

        float imageRatio = (float)this.client.window.getFramebufferWidth()/this.client.window.getFramebufferHeight();
        float isoScale = ((IPlayerEntityExtension)MinecraftClient.getInstance().player).getIsoScale();
        float isoDistance = ((IPlayerEntityExtension)MinecraftClient.getInstance().player).getIsoDistance();

        //cbi.cancel();
        WorldRenderer worldRenderer_1 = this.client.worldRenderer;
        ParticleManager particleManager_1 = this.client.particleManager;
        boolean boolean_1 = this.shouldRenderBlockOutline();
        GlStateManager.enableCull();
        this.client.getProfiler().swap("camera");
        {
            this.viewDistance = (float)(this.client.options.viewDistance * 16);
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            if (this.field_4005 != 1.0D) {
                //GlStateManager.translatef((float)this.field_3988, (float)(-this.field_4004), 0.0F);
                //GlStateManager.scaled(this.field_4005, this.field_4005, 1.0D);
            }
            GlStateManager.ortho(-isoScale * imageRatio * 0.5f, isoScale * imageRatio * 0.5f, -isoScale * 0.5f, isoScale * 0.5f, -isoDistance * this.viewDistance , isoDistance * this.viewDistance);
        }
        Camera camera_1 = this.camera;
        camera_1.update(this.client.world, (Entity)(this.client.getCameraEntity() == null ? this.client.player : this.client.getCameraEntity()), this.client.options.perspective > 0, this.client.options.perspective == 2, float_1);
        Frustum frustum_1 = GlMatrixFrustum.get();
        this.client.getProfiler().swap("clear");
        GlStateManager.viewport(0, 0, this.client.window.getFramebufferWidth(), this.client.window.getFramebufferHeight());
        this.backgroundRenderer.renderBackground(camera_1, float_1);
        GlStateManager.clear(16640, MinecraftClient.IS_SYSTEM_MAC);
        this.client.getProfiler().swap("culling");
        VisibleRegion visibleRegion_1 = new FrustumWithOrigin(frustum_1);
        double double_1 = camera_1.getPos().x;
        double double_2 = camera_1.getPos().y;
        double double_3 = camera_1.getPos().z;
        visibleRegion_1.setOrigin(double_1, double_2, double_3);
        /*if (this.client.options.viewDistance >= 4) {
            this.backgroundRenderer.applyFog(camera_1, -1);
            this.client.getProfiler().swap("sky");
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            GlStateManager.ortho(-MixinHelpers.isoScale*this.client.window.getFramebufferWidth(), MixinHelpers.isoScale*this.client.window.getFramebufferWidth(), -MixinHelpers.isoScale*this.client.window.getFramebufferHeight(), MixinHelpers.isoScale*this.client.window.getFramebufferHeight(), -MixinHelpers.isoDistance*this.viewDistance , MixinHelpers.isoDistance*this.viewDistance);
            GlStateManager.matrixMode(5888);
            worldRenderer_1.renderSky(float_1);
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            GlStateManager.ortho(-MixinHelpers.isoScale*this.client.window.getFramebufferWidth(), MixinHelpers.isoScale*this.client.window.getFramebufferWidth(), -MixinHelpers.isoScale*this.client.window.getFramebufferHeight(), MixinHelpers.isoScale*this.client.window.getFramebufferHeight(), -MixinHelpers.isoDistance*this.viewDistance , MixinHelpers.isoDistance*this.viewDistance);
            GlStateManager.matrixMode(5888);
        }*/

        this.backgroundRenderer.applyFog(camera_1, 0);
        GlStateManager.shadeModel(7425);
        if (camera_1.getPos().y < 128.0D) {
            //this.renderAboveClouds(camera_1, worldRenderer_1, float_1, double_1, double_2, double_3);
        }

        this.client.getProfiler().swap("prepareterrain");
        this.backgroundRenderer.applyFog(camera_1, 0);
        this.client.getTextureManager().bindTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX);
        GuiLighting.disable();
        this.client.getProfiler().swap("terrain_setup");
        this.client.world.method_2935().getLightingProvider().doLightUpdates(2147483647, true, true);
        worldRenderer_1.setUpTerrain(camera_1, visibleRegion_1, this.field_4021++, this.client.player.isSpectator());
        this.client.getProfiler().swap("updatechunks");
        this.client.worldRenderer.updateChunks(long_1);
        this.client.getProfiler().swap("terrain");


        GlStateManager.matrixMode(5888);
        GlStateManager.pushMatrix();
        GlStateManager.disableAlphaTest();
        worldRenderer_1.renderLayer(BlockRenderLayer.SOLID, camera_1);
        GlStateManager.enableAlphaTest();
        worldRenderer_1.renderLayer(BlockRenderLayer.MIPPED_CUTOUT, camera_1);
        this.client.getTextureManager().getTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX).pushFilter(false, false);
        worldRenderer_1.renderLayer(BlockRenderLayer.CUTOUT, camera_1);
        this.client.getTextureManager().getTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX).popFilter();
        GlStateManager.shadeModel(7424);
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();


        GlStateManager.pushMatrix();
        GuiLighting.enable();
        this.client.getProfiler().swap("entities");
        worldRenderer_1.renderEntities(camera_1, visibleRegion_1, float_1);
        GuiLighting.disable();
        this.disableLightmap();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        if (boolean_1 && this.client.hitResult != null) {
            GlStateManager.disableAlphaTest();
            this.client.getProfiler().swap("outline");
            worldRenderer_1.drawHighlightedBlockOutline(camera_1, this.client.hitResult, 0);
            GlStateManager.enableAlphaTest();
        }

        if (this.client.debugRenderer.shouldRender()) {
            this.client.debugRenderer.renderDebuggers(long_1);
        }

        this.client.getProfiler().swap("destroyProgress");
        GlStateManager.enableBlend();
        GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        this.client.getTextureManager().getTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX).pushFilter(false, false);
        worldRenderer_1.renderPartiallyBrokenBlocks(Tessellator.getInstance(), Tessellator.getInstance().getBufferBuilder(), camera_1);
        this.client.getTextureManager().getTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX).popFilter();
        GlStateManager.disableBlend();
        this.enableLightmap();
        this.backgroundRenderer.applyFog(camera_1, 0);
        this.client.getProfiler().swap("particles");
        particleManager_1.renderParticles(camera_1, float_1);
        this.disableLightmap();
        GlStateManager.depthMask(false);
        GlStateManager.enableCull();
        this.client.getProfiler().swap("weather");
        this.renderWeather(float_1);
        GlStateManager.depthMask(true);
        worldRenderer_1.renderWorldBorder(camera_1, float_1);
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.alphaFunc(516, 0.1F);
        this.backgroundRenderer.applyFog(camera_1, 0);
        GlStateManager.enableBlend();
        GlStateManager.depthMask(false);
        this.client.getTextureManager().bindTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX);
        GlStateManager.shadeModel(7425);
        this.client.getProfiler().swap("translucent");
        worldRenderer_1.renderLayer(BlockRenderLayer.TRANSLUCENT, camera_1);
        GlStateManager.shadeModel(7424);
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.disableFog();
        if (camera_1.getPos().y >= 128.0D) {
            //this.client.getProfiler().swap("aboveClouds");
            //this.renderAboveClouds(camera_1, worldRenderer_1, float_1, double_1, double_2, double_3);
        }

        this.client.getProfiler().swap("hand");
        if (this.renderHand) {
            GlStateManager.clear(256, MinecraftClient.IS_SYSTEM_MAC);
            this.renderHand(camera_1, float_1);
        }

    }
}
