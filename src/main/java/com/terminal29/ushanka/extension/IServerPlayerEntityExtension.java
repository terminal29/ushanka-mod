package com.terminal29.ushanka.extension;
import net.minecraft.nbt.CompoundTag;

import static com.terminal29.ushanka.extension.IClientPlayerEntityExtension.CameraDirection;

public interface IServerPlayerEntityExtension {
    boolean isCameraIso();
    CameraDirection getCameraDirection();
    CompoundTag serialize();
    void deserialize(CompoundTag tag);
    void onClientReady();
}
