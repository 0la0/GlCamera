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

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;


public class GlGraphicsView extends GLSurfaceView {

    private final GlGraphicsRenderer renderer;

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float previousX;
    private float previousY;

    public GlGraphicsView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        renderer = new GlGraphicsRenderer(getResources());
        setRenderer(renderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }



    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:

                float dx = x - previousX;
                float dy = y - previousY;

                // reverse direction of rotation above the mid-line
                if (y > getHeight() / 2) {
                    dx = dx * -1 ;
                }

                // reverse direction of rotation to left of the mid-line
                if (x < getWidth() / 2) {
                    dy = dy * -1 ;
                }

                renderer.setAngle(
                        renderer.getAngle() +
                        ((dx + dy) * TOUCH_SCALE_FACTOR));  // = 180.0f / 320
                requestRender();
        }

        previousX = x;
        previousY = y;
        return true;
    }

}
