package com.terminal29.ushanka;

import net.minecraft.client.util.math.Matrix4f;

import java.nio.FloatBuffer;

public class MathUtilities {

    // https://gist.github.com/shaunlebron/8832585
    public static float shortAngleDist(float a0, float a1) {
        float max = (float) 360;
        float da = (a1 - a0) % max;
        return 2 * da % max - da;
    }

    public static float angleLerp(float a0, float a1, float t) {
        return a0 + shortAngleDist(a0, a1) * t;
    }

    public static float lerp(float a0, float a1, float t) { return a0 + (a1 - a0) * t; }

    public static Matrix4f matrixLerp(Matrix4f a0, Matrix4f a1, float t){
        Matrix4f mat = new Matrix4f();
        float[] a0Elements = new float[16];
        a0.putIntoBuffer(FloatBuffer.wrap(a0Elements));
        float[] a1Elements = new float[16];
        a1.putIntoBuffer(FloatBuffer.wrap(a1Elements));
        float[] matElements = new float[16];
        for(int i = 0; i < 16; i++){
            matElements[i] = lerp(a0Elements[i], a1Elements[i], t);
        }
        mat.setFromBuffer(FloatBuffer.wrap(matElements));
        return mat;
    }

    // https://stackoverflow.com/questions/90238/whats-the-syntax-for-mod-in-java
    public static int mod(int x, int y)
    {
        int result = x % y;
        return result < 0? result + y : result;
    }

    public static boolean isInRangeInclusive(int num, int b1, int b2){
        int smaller = b1 < b2 ? b1 : b2;
        int bigger = b1 < b2 ? b2 : b1;
        return smaller <= num && num <= bigger;
    }
}
