package etc.a0la0.glcamera.opengl_graphics;


import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class SwipeCubeGraphicsView extends GLSurfaceView {

//    private float previousX;
//    private float previousY;
    private final SwipeCubeRenderer renderer;

    public SwipeCubeGraphicsView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        renderer = new SwipeCubeRenderer(getResources());
        setRenderer(renderer);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:

//                float dx = x - previousX;
//                float dy = y - previousY;

                float normalX = -2.0f * x / ((float) getWidth());
                float normalY = -1.0f * y / ((float) getHeight());

                renderer.onTouch(normalX, normalY);
                requestRender();

        }

//        previousX = x;
//        previousY = y;
        return true;
    }

}
