package com.terminal29.ushanka.extension;

public interface IPlayerEntityExtension {
    enum CameraDirection {
        NORTH, // 0
        SOUTH, // 180
        EAST, // 90
        WEST, // 270
        NONE // ~
    }

    float ISO_DEADZONE = 0.0001f;

    float YAW_DEADZONE = 0.1f;

    void setCameraDirection(CameraDirection direction);

    CameraDirection getCameraDirection();

    void rotateCameraLeft();

    void rotateCameraRight();

    boolean isChangingDirection();

    float getIsoScale();

    void setIsoScale(float isoScale);

    float getIsoDistance();

    void setIsoDistance(float isoDistance);

    float getIsoSlider();

    boolean isCameraIso();

    void setCameraIso(boolean state);

    void teleportToVillage();

    void teleportToOverworld();
}
