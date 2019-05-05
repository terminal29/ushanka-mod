package com.terminal29.ushanka;

public interface IPlayerEntityExtension {
    enum CameraDirection {
        NORTH,
        SOUTH,
        EAST,
        WEST,
        NONE
    }

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

    boolean isIso

    boolean isCameraIso();

    void setCameraIso(boolean state);
}
