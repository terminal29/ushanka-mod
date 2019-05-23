package com.terminal29.ushanka.extension;

import static com.terminal29.ushanka.extension.IClientPlayerEntityExtension.CameraDirection;

public interface IServerPlayerEntityExtension {
    boolean isCameraIso();
    CameraDirection getCameraDirection();
    void onClientReady();
}
