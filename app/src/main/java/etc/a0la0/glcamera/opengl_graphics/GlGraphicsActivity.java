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

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;

public class GlGraphicsActivity extends Activity {

    public static final String GRAPHICS_MODE = "graphicsMode";
    public static final String GRAPHICS_2D = "graphics2d";
    public static final String GRAPHICS_3D = "graphics3d";
    public static final String GRAPHICS_SWIPE = "graphicsSwipe";

    private GLSurfaceView glSurfaceView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        String graphicsMode = bundle.getString(GRAPHICS_MODE);
        if (graphicsMode == null) {
            Log.i("-------", "graphics mode is null");
            return;
        }
        if (graphicsMode.equals(GRAPHICS_2D)) {
            glSurfaceView = new GlGraphicsView(this);
        }
        else if (graphicsMode.equals(GRAPHICS_3D)){
            glSurfaceView = new CubeGraphicsView(this);
        }
        else {
            glSurfaceView = new SwipeCubeGraphicsView(this);
        }
        setContentView(glSurfaceView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }
}