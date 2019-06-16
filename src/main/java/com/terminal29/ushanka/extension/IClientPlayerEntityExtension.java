package com.terminal29.ushanka.extension;

import com.terminal29.ushanka.utility.IsoCameraDirection;

public interface IClientPlayerEntityExtension {

    float ISO_DEADZONE = 0.0001f;

    float YAW_DEADZONE = 0.1f;

    void requestCameraDirection(IsoCameraDirection direction);

    void forceCameraDirection(IsoCameraDirection direction);

    IsoCameraDirection getCameraDirection();

    void rotateCameraLeft();

    void rotateCameraRight();

    boolean isChangingDirection();

    float getIsoScale();

    void setIsoScale(float isoScale);

    float getIsoDistance();

    void setIsoDistance(float isoDistance);

    float getIsoSlider();

    boolean isCameraIso();

    void requestCameraIso(boolean state);

    void forceCameraIso(boolean state);
}
