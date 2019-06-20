package com.terminal29.ushanka.extension;

import com.terminal29.ushanka.utility.IsoCameraDirection;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;

public interface IPlayerEntityExtension {

    void changeToDimension(DimensionType type);

    void changeToDimension(DimensionType type, BlockPos position);

    DimensionType getPreviousDimension();

    BlockPos getPreviousPosition();

    void onCameraIsoChanged(boolean state, boolean updateRemote);

    void onCameraDirectionChanged(IsoCameraDirection direction, boolean updateRemote);

    boolean isCameraIso();
}
