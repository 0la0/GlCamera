package etc.a0la0.glcamera.opengl_graphics;


import android.content.res.Resources;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Pair;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import etc.a0la0.glcamera.R;
import etc.a0la0.glcamera.opengl_graphics.geometry.Cube;
import etc.a0la0.glcamera.util.InputStreamUtil;

public class SwipeCubeRenderer implements GLSurfaceView.Renderer {

    private final float[] mvpMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];

    private Resources resources;

    private final int NUM_CUBES = 2;
    private List<Cube> cubeList;

    private float cubeRotation;
    private long lastUpdateMillis;
    private static final int REFRESH_RATE_FPS = 60;
    private final float FRAME_TIME_MILLIS = TimeUnit.SECONDS.toMillis(1) / REFRESH_RATE_FPS;
    private float elapsedTime = 0;

    private String vertexShader;
    private String fragmentShader;

    private List<Pair<Float, Float>> touchList;

    public SwipeCubeRenderer(Resources resources) {
        this.resources = resources;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Set the fixed camera position (View matrix).
        Matrix.setLookAtM(viewMatrix, 0, 0.0f, 0.0f, -4.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);

        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClearDepthf(1.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDepthFunc(GLES20.GL_LEQUAL);

        InputStream vertexInputStream = resources.openRawResource(R.raw.graphics_3d_vertex_shader);
        InputStream fragmentInputStream = resources.openRawResource(R.raw.graphics_3d_fragment_shader);

        vertexShader = InputStreamUtil.getStringFromSream(vertexInputStream);
        fragmentShader = InputStreamUtil.getStringFromSream(fragmentInputStream);

        cubeList = getCubes(NUM_CUBES, vertexShader, fragmentShader);
        touchList = new LinkedList<>();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        float ratio = (float) width / height;

        GLES20.glViewport(0, 0, width, height);
        // This projection matrix is applied to object coordinates in the onDrawFrame() method.
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1.0f, 1.0f, 3.0f, 7.0f);
        // modelView = projection x view
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        synchronized (this) {
            render();
        }
        synchronized (this) {
            update();
        }
//        render();
//        update();
    }

    private List<Cube> getCubes(int numberOfCubes, String vertexShader, String fragmentShader) {
        List<Cube> cubeList = new LinkedList<>();
        for (int i = 0; i < numberOfCubes; i++) {
            cubeList.add(new Cube(vertexShader, fragmentShader, (float) Math.random() * 2.0f, (float) Math.random() * 2.0f));
        }
        return cubeList;
    }

    public void onTouch(float x, float y) {
        synchronized(this) {
            touchList.add(new Pair<>(x, y));
        }
    }

    private void render() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        for(Cube cube : cubeList) {
            cube.draw(mvpMatrix);
        }
    }

    private void update() {
        if (lastUpdateMillis != 0) {
            elapsedTime = (SystemClock.elapsedRealtime() - lastUpdateMillis) / FRAME_TIME_MILLIS;
            //cubeRotation += CUBE_ROTATION_INCREMENT * factor;
        }
        lastUpdateMillis = SystemClock.elapsedRealtime();

        for (int i = cubeList.size() - 1; i > -1; i--) {
            Cube cube = cubeList.get(i);
            cube.update(elapsedTime);
            if (cube.isDead()) {
                cubeList.remove(i);
            }
        }

        if (touchList.size() > 0) {
            for (Pair pair : touchList) {
                cubeList.add(new Cube(vertexShader, fragmentShader, (float) pair.first, (float) pair.second));
            }
            touchList = new LinkedList<>();
        }


//        for (Cube cube : cubeList) {
//            cube.update(elapsedTime);
//        }

    }

}
