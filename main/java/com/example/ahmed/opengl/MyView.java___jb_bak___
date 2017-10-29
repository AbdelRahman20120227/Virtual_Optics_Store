package com.example.ahmed.opengl;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;

/**
 * Created by Ahmed on 5/24/2016.
 */
public class MyView extends GLSurfaceView {
    GLSurfaceView.Renderer myRenderer;
    public MyView(Context context) {
        super(context);
        this.setEGLContextClientVersion(2);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        setZOrderOnTop(true);
        myRenderer = new MyRenderer(context);
        setRenderer(myRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}
