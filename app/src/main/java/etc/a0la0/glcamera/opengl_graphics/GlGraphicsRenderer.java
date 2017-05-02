/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package etc.a0la0.glcamera.opengl_graphics;

import android.content.res.Resources;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import etc.a0la0.glcamera.R;
import etc.a0la0.glcamera.opengl_graphics.geometry.Square;
import etc.a0la0.glcamera.opengl_graphics.geometry.Triangle;
import etc.a0la0.glcamera.util.InputStreamUtil;


public class GlGraphicsRenderer implements GLSurfaceView.Renderer {

    private Triangle triangle;
    private Square square;

    // mvpMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mvpMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private final float[] rotationMatrix = new float[16];

    private float angle;



    private final Resources resources;


    public GlGraphicsRenderer(Resources resources) {
        this.resources = resources;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        // Set the background frame color
        GLES20.glClearColor(0.2f, 0.2f, 0.5f, 1.0f);

        GLES20.glClearDepthf(1.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDepthFunc(GLES20.GL_LEQUAL);

        InputStream vertexInputStream = resources.openRawResource(R.raw.graphics_vertex_shader);
        InputStream fragmentInputStream = resources.openRawResource(R.raw.graphics_fragment_shader);

        String vertexShader = InputStreamUtil.getStringFromSream(vertexInputStream);
        String fragmentShader = InputStreamUtil.getStringFromSream(fragmentInputStream);

        triangle = new Triangle(vertexShader, fragmentShader);
        square = new Square(vertexShader, fragmentShader);

    }

    @Override
    public void onDrawFrame(GL10 unused) {

        float[] scratch = new float[16];

        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(viewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

        // Draw square
        square.draw(mvpMatrix);

        Matrix.setRotateM(rotationMatrix, 0, angle, 0, 0, 1.0f);

        // Combine the rotation matrix with the projection and camera view
        // Note that the mvpMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(scratch, 0, mvpMatrix, 0, rotationMatrix, 0);

        // Draw triangle
        triangle.draw(scratch);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

        // modelView = projection x view
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

    }




    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("GlGraphicsRenderer", glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }

    /**
     * Returns the rotation angle of the triangle shape (triangle).
     *
     * @return - A float representing the rotation angle.
     */
    public float getAngle() {
        return angle;
    }

    /**
     * Sets the rotation angle of the triangle shape (triangle).
     */
    public void setAngle(float angle) {
        this.angle = angle;
    }




}