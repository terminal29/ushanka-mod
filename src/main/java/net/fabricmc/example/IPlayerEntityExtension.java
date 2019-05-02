package net.fabricmc.example;

public interface IPlayerEntityExtension {
    enum CameraDirection{
        NORTH,
        SOUTH,
        EAST,
        WEST
    }

    void setCameraDirection(CameraDirection direction);

    CameraDirection getCameraDirection();

    void rotateCameraLeft();

    void rotateCameraRight();
}
