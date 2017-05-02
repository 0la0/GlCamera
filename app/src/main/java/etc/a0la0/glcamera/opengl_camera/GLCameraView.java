package etc.a0la0.glcamera.opengl_camera;


import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.SurfaceHolder;

public class GLCameraView extends GLSurfaceView {

    CameraRenderer cameraRenderer;

    public GLCameraView(Context context) {
        super(context);
        cameraRenderer = new CameraRenderer(this);
        setEGLContextClientVersion(2);
        setRenderer(cameraRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public void surfaceCreated (SurfaceHolder holder) {
        super.surfaceCreated (holder);
    }

    public void surfaceDestroyed (SurfaceHolder holder) {
        cameraRenderer.close();
        super.surfaceDestroyed(holder);
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        super.surfaceChanged(holder, format, w, h );
    }

}
