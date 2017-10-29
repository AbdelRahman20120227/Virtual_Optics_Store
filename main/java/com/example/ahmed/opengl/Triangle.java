package com.example.ahmed.opengl;


import android.opengl.GLES20;
import android.util.Log;

import com.threed.jpct.Loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.zip.ZipInputStream;

/**
 * Created by Ahmed on 5/25/2016.
 */
public class Triangle {

    final int program;
    private int mPositionHandle;
    private int mNormalHandle;
    private int mColorHandle;
    private int mVMatrixHandle;
    private int mPMatrixHandle;

    final String vertexShaderCode =
                    "uniform mat4 Vmatrix;" +
                    "uniform mat4 Pmatrix;" +
                    "attribute vec3 vPosition;" +
                    "attribute vec3 vNormal;" +
                    "varying vec3 normal;" +
                    "varying vec3 position;" +
                    "void main() {" +
                    "  normal = normalize(vNormal);" +
                    "  position = normalize(vPosition);"+
                    "  vec4 finalPosition = Pmatrix * Vmatrix * vec4(vPosition, 1.0);" +
                    "  gl_Position = vec4(finalPosition.xy, finalPosition.z * finalPosition.z / -1000.0, finalPosition.w);" +
                    "}";

    final String fragmentShaderCode =
                    "precision mediump float;"+
                    "uniform vec4 vColor;"+
                    "varying vec3 normal;" +
                    "varying vec3 position;" +
                    "void main() {" +
                        "  float diffuseFactor = max(dot( normalize(normal), normalize(vec3(4.0, 4.0, 4.0) - normalize(position)) ), 0.0);"+
                        "  gl_FragColor = vec4(diffuseFactor * vColor.rgb, vColor.a);" +
                    "}";

    private FloatBuffer vertexBuffer;
    private IntBuffer indicesBuffer;


    private float[] color;

    int buffersIDs[] = new int[2];

    float[] buffer1;
    int[] buffer2;


    public Triangle(ArrayList<Float> loadedVertices, ArrayList<Integer> loadedIndices){

        program = GLES20.glCreateProgram();

        int vertexShaderHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(vertexShaderHandle, vertexShaderCode);

        int fragmentShaderHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(fragmentShaderHandle, fragmentShaderCode);

        GLES20.glCompileShader(vertexShaderHandle);
        GLES20.glCompileShader(fragmentShaderHandle);

        GLES20.glAttachShader(program, vertexShaderHandle);
        GLES20.glAttachShader(program, fragmentShaderHandle);

        GLES20.glLinkProgram(program);

        buffer1 = new float[loadedVertices.size()];
        buffer2 = new int[loadedIndices.size()];

        for(int i = 0; i < buffer1.length; i++){
            buffer1[i] = loadedVertices.get(i);
        }

        for(int i = 0; i < buffer2.length; i++){
            buffer2[i] = loadedIndices.get(i);
        }

        ByteBuffer bb = ByteBuffer.allocateDirect(buffer1.length*4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(buffer1);
        vertexBuffer.position(0);

        ByteBuffer ib = ByteBuffer.allocateDirect(buffer2.length*4);
        ib.order(ByteOrder.nativeOrder());
        indicesBuffer = ib.asIntBuffer();
        indicesBuffer.put(buffer2);
        indicesBuffer.position(0);



        GLES20.glGenBuffers(2, buffersIDs, 0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffersIDs[0]);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, buffersIDs[1]);

        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, 4 * buffer1.length, vertexBuffer, GLES20.GL_STATIC_DRAW);
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, 4 * buffer2.length, indicesBuffer, GLES20.GL_STATIC_DRAW);

        Log.d("MY PROGRAM", program + " " + fragmentShaderHandle + " " + vertexShaderHandle + " " + buffersIDs[0] + " " + buffersIDs[1]);


    }

    public void setColor(float[] color){
        this.color = color;
    }

    public void draw(float[] viewMatrix, float[] projectionMatrix){
        GLES20.glUseProgram(program);

        mPositionHandle = GLES20.glGetAttribLocation(program,"vPosition");

        mNormalHandle = GLES20.glGetAttribLocation(program,"vNormal");

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffersIDs[0]);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, buffersIDs[1]);


        GLES20.glVertexAttribPointer(mPositionHandle ,3, GLES20.GL_FLOAT, false, 24, 0);
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        GLES20.glVertexAttribPointer(mNormalHandle ,3, GLES20.GL_FLOAT, false, 24, 12);
        GLES20.glEnableVertexAttribArray(mNormalHandle);


        mColorHandle = GLES20.glGetUniformLocation(program,"vColor");
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        mVMatrixHandle = GLES20.glGetUniformLocation(program,"Vmatrix");
        mPMatrixHandle = GLES20.glGetUniformLocation(program,"Pmatrix");

        GLES20.glUniformMatrix4fv(mVMatrixHandle,1,false,viewMatrix,0);

        GLES20.glUniformMatrix4fv(mPMatrixHandle,1,false,projectionMatrix,0);


        GLES20.glDrawElements(GLES20.GL_TRIANGLES, buffer2.length, GLES20.GL_UNSIGNED_INT, 0);
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mNormalHandle);
        Log.d("drawnnnnnnnnnnnnnnn", buffer1.length + " " + buffer2.length);
    }
}
