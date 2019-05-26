package com.terminal29.ushanka.extension;

import net.minecraft.nbt.CompoundTag;

public interface IClientPlayerEntityExtension {
    enum CameraDirection {
        NORTH, // 180
        SOUTH, // 0
        EAST, // 270
        WEST, // 90
        NONE; // ~

        public static CameraDirection fromName(String name){
            for(CameraDirection direction : CameraDirection.values()){
                if(direction.name().compareToIgnoreCase(name) == 0)
                    return direction;
            }
            return CameraDirection.NONE;
        }
    }

    float ISO_DEADZONE = 0.0001f;

    float YAW_DEADZONE = 0.1f;

    void requestCameraDirection(CameraDirection direction);

    void forceCameraDirection(CameraDirection direction);

    CameraDirection getCameraDirection();

    void rotateCameraLeft();

    void rotateCameraRight();

    boolean isChangingDirection();

    float getIsoScale();

    void setIsoScale(float isoScale);

    float getIsoSlider();

    boolean isCameraIso();

    void requestCameraIso(boolean state);

    void forceCameraIso(boolean state);
}
