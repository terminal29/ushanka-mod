package com.terminal29.ushanka.extension;

import com.terminal29.ushanka.utility.IsoCameraDirection;

public interface IServerPlayerEntityExtension {
    boolean isCameraIso();
    IsoCameraDirection getCameraDirection();
    void onClientReady();
}
