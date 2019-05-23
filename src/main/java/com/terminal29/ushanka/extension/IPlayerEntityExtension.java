package com.terminal29.ushanka.extension;

import net.minecraft.world.dimension.DimensionType;

public interface IPlayerEntityExtension {

    void changeToDimension(DimensionType type);

    void onCameraIsoChanged(boolean state, boolean updateRemote);

    void onCameraDirectionChanged(IClientPlayerEntityExtension.CameraDirection direction, boolean updateRemote);
}
