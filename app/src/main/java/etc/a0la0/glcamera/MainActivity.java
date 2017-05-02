package etc.a0la0.glcamera;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import etc.a0la0.glcamera.opengl_camera.GlCameraActivity;
import etc.a0la0.glcamera.opengl_graphics.GlGraphicsActivity;
import etc.a0la0.glcamera.util.InputStreamUtil;

public class MainActivity extends AppCompatActivity {

    public static final String FRAGMENT_KEY = "FRAGMENT_KEY";
    public static final String EDGE = "EDGE";
    public static final String BLUR = "BLUR";
    public static final String SHARPEN = "SHARPEN";
    public static final String TEST = "TEST";

    public static Map<String, Pair<String, String>> shaderMap;
    public static String activeSet = EDGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        shaderMap = buildShaderMap();

        findViewById(R.id.edgeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeSet = EDGE;
                startCameraActivity();
            }
        });

        findViewById(R.id.blurButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeSet = BLUR;
                startCameraActivity();
            }
        });

        findViewById(R.id.sharpenButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeSet = SHARPEN;
                startCameraActivity();
            }
        });

        findViewById(R.id.testButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeSet = TEST;
                startCameraActivity();
            }
        });

        findViewById(R.id.graphicsButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GlGraphicsActivity.class);
                intent.putExtra(GlGraphicsActivity.GRAPHICS_MODE, GlGraphicsActivity.GRAPHICS_2D);
                startActivity(intent);
            }
        });

        findViewById(R.id.graphics3dButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GlGraphicsActivity.class);
                intent.putExtra(GlGraphicsActivity.GRAPHICS_MODE, GlGraphicsActivity.GRAPHICS_3D);
                startActivity(intent);
            }
        });

        findViewById(R.id.graphicsSwipeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GlGraphicsActivity.class);
                intent.putExtra(GlGraphicsActivity.GRAPHICS_MODE, GlGraphicsActivity.GRAPHICS_SWIPE);
                startActivity(intent);
            }
        });

    }

    private void startCameraActivity() {
        Intent intent = new Intent(getApplicationContext(), GlCameraActivity.class);
        startActivity(intent);
    }

    private Map<String, Pair<String, String>> buildShaderMap() {
        InputStream vertexInputStream = getResources().openRawResource(R.raw.video_regular_background_vertex_shader);

        InputStream blurFragmentInputStream = getResources().openRawResource(R.raw.extreme_blur_fragment_shader);
        InputStream edgeFragmentInputStream = getResources().openRawResource(R.raw.edge_detection_fragment_shader);
        InputStream sharpenFragmentInputStream = getResources().openRawResource(R.raw.sharpen_fragment_shader);
        InputStream testFragmentInputStream = getResources().openRawResource(R.raw.variable_test_fragment_shader);

        String videoBackgroundVertexShader = InputStreamUtil.getStringFromSream(vertexInputStream);
        String blurFragmentShader = InputStreamUtil.getStringFromSream(blurFragmentInputStream);
        String edgeFragmentShader = InputStreamUtil.getStringFromSream(edgeFragmentInputStream);
        String sharpenFragmentShader = InputStreamUtil.getStringFromSream(sharpenFragmentInputStream);
        String testFragmentShader = InputStreamUtil.getStringFromSream(testFragmentInputStream);

        Map<String, Pair<String, String>> shaderMap = new HashMap<>();
        shaderMap.put(BLUR, new Pair<>(videoBackgroundVertexShader, blurFragmentShader));
        shaderMap.put(EDGE, new Pair<>(videoBackgroundVertexShader, edgeFragmentShader));
        shaderMap.put(SHARPEN, new Pair<>(videoBackgroundVertexShader, sharpenFragmentShader));
        shaderMap.put(TEST, new Pair<>(videoBackgroundVertexShader, testFragmentShader));
        return shaderMap;
    }

    public static Pair<String, String> getShaderPair() {
        return shaderMap.get(activeSet);
    }
}
