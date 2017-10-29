package com.example.ahmed.opengl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Ahmed on 5/25/2016.
 */
public class MyRenderer implements GLSurfaceView.Renderer{

    //Loading libraries upon class loading
    static{
        System.loadLibrary("opencv_java3");
        System.loadLibrary("android_dlib");
        System.loadLibrary("grab-draw-module");

    }
    private Context context;
    public static float[] publicColor = {0.0f, 0.0f, 0.0f, 1.0f};

    //Constructor
    public MyRenderer(Context context){
        super();
        this.context = context;
    }


    public void loadGlassesModel(){
        String fileName = "test.obj";
        BufferedReader modelReader = null;
        try {
            modelReader = new BufferedReader(new InputStreamReader(context.getResources().getAssets().open(fileName)));
            MyRenderer.objects = ModelLoader.load(modelReader);
            Log.d("Loaded", MyRenderer.objects.size() + "");
            MyRenderer.loading = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Overriden functions from class Renderer
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        loadGlassesModel();
        loading = false;
    }

    float theata = 0.0f;
    public static Map<String, Triangle> objects;
    static boolean loading = true;

    @Override
    public void onDrawFrame(GL10 gl) {

        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glClearDepthf(-1000.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        theata += 1.0f;
        float[] identity = {1, 0, 0, 0,
                            0, 1, 0, 0,
                            0, 0, 1, 0,
                            0, 0, 0, 1};

        float[] matrix = new float[16];
        Matrix.setRotateM(matrix, 0, theata, 1, 1, 0);
        Log.d("not Still loadingggggg", theata + "");
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glDepthFunc(GLES20.GL_GREATER);

        GLES20.glColorMask(false, false, false, false);

        Triangle lens = objects.get("lens");
        Triangle frame = objects.get("frame");
        Triangle face = objects.get("face");


        face.setColor(new float[]{0.0f, 1.0f, 0.0f, 1.0f});
        face.draw(CameraView.viewMatrix, CameraView.projectionMatrix);

        GLES20.glColorMask(true, true, true, true);

        frame.setColor(publicColor);
        frame.draw(CameraView.viewMatrix, CameraView.projectionMatrix);

        lens.setColor(new float[]{0.0f, 0.0f, 0.0f, 1.0f});
        lens.draw(CameraView.viewMatrix, CameraView.projectionMatrix);

        CameraView.glCounter++;
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

}
