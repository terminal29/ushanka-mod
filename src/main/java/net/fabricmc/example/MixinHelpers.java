package net.fabricmc.example;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.Matrix4f;

import java.nio.FloatBuffer;

public class MixinHelpers {

    public static float isoScale = 25;
    public static float isoDistance = 1;

    public static Matrix4f orthographic(float left, float right, float bottom, float top, float near, float far){
        Matrix4f matrix = new Matrix4f();

        float[] orthoMat = new float[16];

        orthoMat[0 + 0 * 4] = 2.0f / (right - left);
        orthoMat[1 + 1 * 4] = 2.0f / (top - bottom);
        orthoMat[2 + 2 * 4] = 2.0f / (near - far);

        orthoMat[0 + 3 * 4] = (left + right) / (left - right);
        orthoMat[1 + 3 * 4] = (bottom + top) / (bottom - top);
        orthoMat[2 + 3 * 4] = (far + near) / (far - near);

        orthoMat[3 + 3 * 4] = 1.0f;

        matrix.setFromBuffer(FloatBuffer.wrap(orthoMat));

        return matrix;
    }

    public static void setPlayerRotation(float yaw, float pitch, float roll){
        if(MinecraftClient.getInstance().player != null){
            MinecraftClient.getInstance().player.pitch = pitch;
            MinecraftClient.getInstance().player.yaw = yaw;
        }
    }
}
