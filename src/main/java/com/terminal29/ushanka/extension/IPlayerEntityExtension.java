package com.terminal29.ushanka.extension;

import com.terminal29.ushanka.utility.IsoCameraDirection;
import net.minecraft.world.dimension.DimensionType;

public interface IPlayerEntityExtension {

    void changeToDimension(DimensionType type);

    void onCameraIsoChanged(boolean state, boolean updateRemote);

    void onCameraDirectionChanged(IsoCameraDirection direction, boolean updateRemote);
}
