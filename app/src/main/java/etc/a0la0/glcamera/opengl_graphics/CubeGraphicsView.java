package etc.a0la0.glcamera.opengl_graphics;


import android.content.Context;
import android.opengl.GLSurfaceView;

public class CubeGraphicsView extends GLSurfaceView {

    private final CubeRenderer renderer;

    public CubeGraphicsView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        renderer = new CubeRenderer(getResources());
        setRenderer(renderer);
    }

}
