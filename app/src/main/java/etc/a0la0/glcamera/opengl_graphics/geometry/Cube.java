package etc.a0la0.glcamera.opengl_graphics.geometry;


import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.SystemClock;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.concurrent.TimeUnit;

import etc.a0la0.glcamera.util.BufferUtil;
import etc.a0la0.glcamera.util.GLUtil;

public class Cube {

    private static final float VERTICES[] = {
            -0.5f, -0.5f, -0.5f,
             0.5f, -0.5f, -0.5f,
             0.5f,  0.5f, -0.5f,
            -0.5f,  0.5f, -0.5f,
            -0.5f, -0.5f,  0.5f,
             0.5f, -0.5f,  0.5f,
             0.5f,  0.5f,  0.5f,
            -0.5f,  0.5f,  0.5f
    };

    private static final float COLORS[] = {
            0.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,
            1.0f, 0.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            0.0f, 1.0f, 1.0f, 1.0f,
    };


    private static final byte INDICES[] = {
            0, 1, 3, 3, 1, 2, // Front face.
            0, 1, 4, 4, 5, 1, // Bottom face.
            1, 2, 5, 5, 6, 2, // Right face.
            2, 3, 6, 6, 7, 3, // Top face.
            3, 7, 4, 4, 3, 0, // Left face.
            4, 5, 7, 7, 6, 5, // Rear face.
    };

    private static final int COORDS_PER_VERTEX = 3;
    private static final int VALUES_PER_COLOR = 4;
    private final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;
    private final int COLOR_STRIDE = VALUES_PER_COLOR * 4;

    private final FloatBuffer vertexBuffer;
    private final FloatBuffer colorBuffer;
    private final ByteBuffer indexBuffer;
    private final int glProgram;
    private final int positionHandle;
    private final int colorHandle;
    private final int mvpMatrixHandle;


    private float cubeRotation;
    private long lastUpdateMillis;
    private static final int REFRESH_RATE_FPS = 60;

//    private final float CUBE_ROTATION_INCREMENT = ((float) Math.random() * 4.0f) - 2.0f;
    private final float CUBE_ROTATION_INCREMENT = (float) Math.random();
    private final float FRAME_TIME_MILLIS = TimeUnit.SECONDS.toMillis(1) / REFRESH_RATE_FPS;

    private final float[] rotationMatrix = new float[16];
    private final float[] mvpMatrix = new float[16];

//    private final float rotateX = (float) Math.random();
//    private final float rotateY = (float) Math.random();
//    private final float rotateZ = (float) Math.random();
    private final float rotateX = 0.0f;
    private final float rotateY = 1.0f;
    private final float rotateZ = 0.7f;

    private float[] rotate = new float[16];
    private float[] translate = new float[16];
    private float[] scale = new float[16];

    private float velocityX = (float) Math.random();
    private float velocityY = (float) Math.random();
    private float velocityZ = (float) Math.random();

    private float[] objectMatrix = new float[16];
    private float ttl = 200f;

    //TODO: rotate, translate, scale
    // http://stackoverflow.com/questions/26101130/scale-rotate-translate-w-matrices-in-opengl-es-2-0

    public Cube(String vertexShader, String fragmentShader, float x, float y) {
        vertexBuffer = BufferUtil.getFloatBuffer(VERTICES);
        colorBuffer = BufferUtil.getFloatBuffer(COLORS);
        indexBuffer = BufferUtil.getByteBuffer(INDICES);

        glProgram = GLUtil.createProgram(vertexShader, fragmentShader);

        positionHandle = GLES20.glGetAttribLocation(glProgram, "position");
        colorHandle = GLES20.glGetAttribLocation(glProgram, "color");
        mvpMatrixHandle = GLES20.glGetUniformLocation(glProgram, "mvpMatrix");

        Matrix.setIdentityM(rotate, 0);
        Matrix.setIdentityM(translate, 0);
        Matrix.setIdentityM(scale, 0);
        Matrix.setIdentityM(objectMatrix, 0);

        Matrix.translateM(translate, 0, x, y, 0);
    }


    public void draw(float[] mvpMatrixInput) {
        updateCubeRotation();
        // Apply the rotation.
        //Matrix.setRotateM(rotationMatrix, 0, cubeRotation, rotateX, rotateY, rotateZ);
        // Combine the rotation matrix with the projection and camera view
        Matrix.multiplyMM(mvpMatrix, 0, mvpMatrixInput, 0, objectMatrix, 0);

        // Add program to OpenGL environment.
        GLES20.glUseProgram(glProgram);

        // Prepare the cube coordinate data.
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(
                positionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                VERTEX_STRIDE, vertexBuffer
        );

        // Prepare the cube color data.
        GLES20.glEnableVertexAttribArray(colorHandle);
        GLES20.glVertexAttribPointer(
                colorHandle, VALUES_PER_COLOR,
                GLES20.GL_FLOAT, false,
                COLOR_STRIDE, colorBuffer
        );

        // Apply the projection and view transformation.
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);

        // Draw the cube.
        GLES20.glDrawElements(
                GLES20.GL_TRIANGLES, INDICES.length,
                GLES20.GL_UNSIGNED_BYTE, indexBuffer
        );

        // Disable vertex arrays.
        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(colorHandle);
    }

    public void update(float elapsedTime) {
        Matrix.translateM(
                translate, 0,
                velocityX * elapsedTime * 0.01f,
                velocityY * elapsedTime * 0.01f,
                velocityZ * elapsedTime * 0.01f
        );
//        Log.i("translate",
//            translate[0] + ", " + translate[1] + ", "  +translate[2] +  ", " + translate[3] + ", " +
//            translate[4] + ", " + translate[5] + ", " + translate[6] +  ", " + translate[7] + ", " +
//            translate[8] + ", " + translate[9] + ", " + translate[10] + ", " + translate[11]
//        );
        //Matrix.rotateM(rotate, 0, angle, x, y, z);
        //Matrix.scaleM(scale, 0, x, y, z);

        float[] temp = new float[16];
        Matrix.multiplyMM(temp, 0, translate, 0, rotate, 0);
        Matrix.multiplyMM(objectMatrix, 0, temp, 0, scale, 0);

        ttl -= elapsedTime;
    }


    private void updateCubeRotation() {
        if (lastUpdateMillis != 0) {
            float factor = (SystemClock.elapsedRealtime() - lastUpdateMillis) / FRAME_TIME_MILLIS;
            cubeRotation += CUBE_ROTATION_INCREMENT * factor;
        }
        lastUpdateMillis = SystemClock.elapsedRealtime();
    }

    public boolean isDead() {
        return ttl <= 0;
    }

}
