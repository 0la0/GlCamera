package etc.a0la0.glcamera.opengl_graphics.geometry;


import android.opengl.GLES20;

import java.nio.FloatBuffer;

import etc.a0la0.glcamera.opengl_graphics.GlGraphicsRenderer;
import etc.a0la0.glcamera.util.BufferUtil;
import etc.a0la0.glcamera.util.GLUtil;

public class Triangle {

    private final FloatBuffer vertexBuffer;
    private final int glProgram;

    // number of coordinates per vertex in this array
    private static final int COORDS_PER_VERTEX = 3;
    private static float triangleCoords[] = {
            // in counterclockwise order:
             0.0f,  0.622008459f, 0.0f,   // top
            -0.5f, -0.311004243f, 0.0f,   // bottom left
             0.5f, -0.311004243f, 0.0f    // bottom right
    };
    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    private float color[] = { 0.8f, 0.2f, 0.2f, 0.0f };


    public Triangle(String vertexShader, String fragmentShader) {
        vertexBuffer = BufferUtil.getFloatBuffer(triangleCoords);
        glProgram = GLUtil.createProgram(vertexShader, fragmentShader);
    }

    public void draw(float[] mvpMatrix) {
        // Add program to OpenGL environment
        GLES20.glUseProgram(glProgram);

        // get handle to vertex shader's vPosition member
        int positionHandle = GLES20.glGetAttribLocation(glProgram, "position");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(positionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(
                positionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        int colorHandle = GLES20.glGetUniformLocation(glProgram, "color");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(colorHandle, 1, color, 0);

        // get handle to shape's transformation matrix
        int mvpMatrixHandle = GLES20.glGetUniformLocation(glProgram, "mvpMatrix");
        GlGraphicsRenderer.checkGlError("glGetUniformLocation");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);
        GlGraphicsRenderer.checkGlError("glUniformMatrix4fv");

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionHandle);
    }

}
