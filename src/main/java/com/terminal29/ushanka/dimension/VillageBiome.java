package com.terminal29.ushanka.dimension;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;

public class VillageBiome extends Biome {
    public static VillageBiome Build(){
        Biome.Settings settings = new Biome.Settings()
            .configureSurfaceBuilder(SurfaceBuilder.NOPE, SurfaceBuilder.AIR_CONFIG)
            .precipitation(Precipitation.NONE)
            .category(Category.NONE)
            .depth(0.1F)
            .scale(0.2F)
            .temperature(0.6F)
            .downfall(0.0F)
            .waterColor(4159204)
            .waterFogColor(329011)
            .parent(null);


        return new VillageBiome(settings);
    }
    public VillageBiome(Biome.Settings biomeSettings) {
        super(biomeSettings);
    }
}
