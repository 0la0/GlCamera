package etc.a0la0.glcamera.opengl_camera;


import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.support.v4.util.Pair;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import etc.a0la0.glcamera.MainActivity;
import etc.a0la0.glcamera.util.GLUtil;

public class CameraRenderer extends GLSurfaceView implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {

    private int[] textures;
    private FloatBuffer vertexBuffer;
    private FloatBuffer texCoordBuffer;
    private int glProgram;

    private Camera camera;
    private SurfaceTexture surfaceTexture;
    private boolean surfaceTextureIsUpdated = false;
    private GLSurfaceView glSurfaceView;
    private long lastRenderTimeStamp = System.currentTimeMillis();


    public CameraRenderer(GLSurfaceView glSurfaceView) {
        super(glSurfaceView.getContext());

        this.glSurfaceView = glSurfaceView;
        float[] vertexBufferData = { 1.0f, -1.0f, -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f };
        float[] texCoordBufferData = { 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f };
        vertexBuffer = ByteBuffer.allocateDirect(8*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexBuffer.put(vertexBufferData);
        vertexBuffer.position(0);
        texCoordBuffer = ByteBuffer.allocateDirect(8*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        texCoordBuffer.put(texCoordBufferData);
        texCoordBuffer.position(0);
    }

    public void close() {
        surfaceTextureIsUpdated = false;
        surfaceTexture.release();
        camera.stopPreview();
        camera.release();
        camera = null;
        deleteTex();
    }

    public void onSurfaceCreated (GL10 gl10, EGLConfig config ) {
        initTex();
        surfaceTexture = new SurfaceTexture(textures[0]);
        surfaceTexture.setOnFrameAvailableListener(this);

        camera = Camera.open();
        try {
            camera.setPreviewTexture(surfaceTexture);
        }
        catch ( IOException ioe ) {
            ioe.printStackTrace();
        }

        GLES20.glClearColor (1.0f, 1.0f, 0.0f, 1.0f);


        Pair<String, String> shaders = MainActivity.getShaderPair();
        glProgram = GLUtil.createProgram(shaders.first, shaders.second);
    }

    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        synchronized(this) {
            if (surfaceTextureIsUpdated) {
                surfaceTexture.updateTexImage();
                surfaceTextureIsUpdated = false;
            }
        }

        long elapsedTime = System.currentTimeMillis() - lastRenderTimeStamp;
        lastRenderTimeStamp = System.currentTimeMillis();

        GLES20.glUseProgram(glProgram);

        int vertexPositionHandler = GLES20.glGetAttribLocation(glProgram, "vertexPosition");
        int texCoordHandler = GLES20.glGetAttribLocation(glProgram, "vertexTexCoord");
        int surfaceTextureHandler = GLES20.glGetUniformLocation(glProgram, "sTexture");
        int inputVarHandler1 = GLES20.glGetUniformLocation(glProgram, "inputVariable1");
        int inputVarHandler2 = GLES20.glGetUniformLocation(glProgram, "inputVariable2");
        int inputVarHandler3 = GLES20.glGetUniformLocation(glProgram, "inputVariable3");



        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textures[0]);
        GLES20.glUniform1i(surfaceTextureHandler, 0);

        GLES20.glVertexAttribPointer(vertexPositionHandler, 2, GLES20.GL_FLOAT, false, 4 * 2, vertexBuffer);
        GLES20.glVertexAttribPointer(texCoordHandler, 2, GLES20.GL_FLOAT, false, 4 * 2, texCoordBuffer);

        double currentTime = System.currentTimeMillis() * 0.005;
        float osc1 = (float) (Math.sin(currentTime * 0.5) + 1) / 2.0f;
        float osc2 = (float) (Math.cos(currentTime * 0.1) + 1) / 2.0f;
        float osc3 = (float) (Math.sin(1 + currentTime * 0.25) + 1) / 2.0f;

        GLES20.glUniform1f(inputVarHandler1, osc1);
        GLES20.glUniform1f(inputVarHandler2, osc2);
        GLES20.glUniform1f(inputVarHandler3, osc3);


        GLES20.glEnableVertexAttribArray(vertexPositionHandler);
        GLES20.glEnableVertexAttribArray(texCoordHandler);

        //GLES20.glEnable(testVariableHandler);
        //.glUniform1f();

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        GLES20.glFlush();
    }

    public void onSurfaceChanged(GL10 gl10, int width, int height ) {
        GLES20.glViewport( 0, 0, width, height );
        Camera.Parameters param = camera.getParameters();
        List<Camera.Size> psize = param.getSupportedPreviewSizes();
        if ( psize.size() > 0 ) {
            int i;
            for ( i = 0; i < psize.size(); i++ ) {

                if ( psize.get(i).width < width || psize.get(i).height < height )
                    break;
            }
            if ( i > 0 )
                i--;
            param.setPreviewSize(psize.get(i).width, psize.get(i).height);
        }
        param.set("orientation", "landscape");
        camera.setParameters ( param );
        camera.startPreview();
    }

    private void initTex() {
        textures = new int[1];
        GLES20.glGenTextures ( 1, textures, 0 );
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textures[0]);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
    }

    private void deleteTex() {
        GLES20.glDeleteTextures ( 1, textures, 0 );
    }

    public synchronized void onFrameAvailable ( SurfaceTexture st ) {
        surfaceTextureIsUpdated = true;
        glSurfaceView.requestRender();
    }


}